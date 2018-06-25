package cn.aberic.fabric.controller;

import cn.aberic.fabric.bean.Transaction;
import cn.aberic.fabric.thrift.MultiServiceProvider;
import cn.aberic.thrift.chaincode.ChaincodeInfo;
import cn.aberic.thrift.channel.ChannelInfo;
import cn.aberic.thrift.common.SystemInfo;
import cn.aberic.thrift.trace.TraceInfo;
import cn.aberic.thrift.utils.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/4 15:01】
 */
@RestController
@RequestMapping("")
public class CommonController {

    @Resource
    private MultiServiceProvider multiService;

    @GetMapping(value = "index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index");
        int leagueCount = 0;
        int orgCount = 0;
        int ordererCount = 0;
        int peerCount = 0;
        int channelCount = 0;
        int chaincodeCount = 0;
        List<Transaction> tmpTransactions = new ArrayList<>();
        List<Transaction> transactions = new ArrayList<>();
        SystemInfo systemInfo;
        try {
            if (!multiService.getSystemService().isInit()) {
                multiService.getSystemService().init();
            }
            leagueCount = multiService.getLeagueService().listAll().size();
            orgCount = multiService.getOrgService().count();
            ordererCount = multiService.getOrdererService().count();
            peerCount = multiService.getPeerService().count();
            channelCount = multiService.getChannelService().count();
            chaincodeCount = multiService.getChaincodeService().count();
            systemInfo = multiService.getSystemService().get();
            systemInfo.setCpu((double) Math.round(systemInfo.getCpu() * 100) / 100);
            systemInfo.setMemory((double) Math.round(systemInfo.getMemory() * 100) / 100);
            systemInfo.setSwap((double) Math.round(systemInfo.getSwap() * 100) / 100);

            List<ChannelInfo> channels = multiService.getChannelService().listAll();
            for (ChannelInfo channel : channels) {
                List<ChaincodeInfo> chaincodes = multiService.getChaincodeService().listById(channel.getId());
                for (ChaincodeInfo chaincode: chaincodes) {
                    try {
                        JSONObject blockInfo = JSON.parseObject(multiService.getTraceService().queryBlockChainInfo(chaincode.getId()));
                        int height = blockInfo.getJSONObject("data").getInteger("height");
                        for (int num = height - 1; num >= 0; num--) {
                            TraceInfo trace = new TraceInfo();
                            trace.setId(chaincode.getId());
                            trace.setTrace(String.valueOf(num));
                            JSONObject blockMessage = JSON.parseObject(multiService.getTraceService().queryBlockByNumber(trace));
                            JSONArray envelopes = blockMessage.getJSONObject("data").getJSONArray("envelopes");
                            int size = envelopes.size();
                            for (int i = 0; i < size; i++) {
                                Transaction transaction = new Transaction();
                                transaction.setNum(num);
                                JSONObject envelope = envelopes.getJSONObject(i);
                                transaction.setTxCount(envelope.getJSONObject("transactionEnvelopeInfo").getInteger("txCount"));
                                transaction.setChannelName(envelope.getString("channelId"));
                                transaction.setCreateMSPID(envelope.getString("createMSPID"));
                                transaction.setDate(envelope.getString("timestamp"));
                                tmpTransactions.add(transaction);
                            }
                            if ((height - num) > 6) {
                                break;
                            }
                        }
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            // transactions.sort(Comparator.comparing(Transaction::getDate));
            tmpTransactions.sort((t1, t2) -> {
                try {
                    long td1 = DateUtil.str2Date(t1.getDate(), "yyyy/MM/dd HH:mm:ss").getTime();
                    long td2 = DateUtil.str2Date(t2.getDate(), "yyyy/MM/dd HH:mm:ss").getTime();
                    return Math.toIntExact(td2 - td1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            });
            for (int i = 0; i < 7; i++) {
                Transaction transaction = tmpTransactions.get(i);
                transaction.setIndex(i + 1);
                transactions.add(transaction);
            }
        } catch (Exception e) {
            systemInfo = new SystemInfo();
            e.printStackTrace();
        }
        modelAndView.addObject("leagueCount", leagueCount);
        modelAndView.addObject("orgCount", orgCount);
        modelAndView.addObject("ordererCount", ordererCount);
        modelAndView.addObject("peerCount", peerCount);
        modelAndView.addObject("channelCount", channelCount);
        modelAndView.addObject("chaincodeCount", chaincodeCount);
        modelAndView.addObject("systemInfo", systemInfo);
        modelAndView.addObject("transactions", transactions);

        return modelAndView;
    }
}
