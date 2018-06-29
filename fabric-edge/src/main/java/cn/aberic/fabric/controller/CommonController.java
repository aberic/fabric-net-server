package cn.aberic.fabric.controller;

import cn.aberic.fabric.bean.Trace;
import cn.aberic.fabric.bean.Transaction;
import cn.aberic.fabric.dao.Chaincode;
import cn.aberic.fabric.dao.Channel;
import cn.aberic.fabric.service.*;
import cn.aberic.fabric.utils.DateUtil;
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
    private LeagueService leagueService;
    @Resource
    private OrgService orgService;
    @Resource
    private OrdererService ordererService;
    @Resource
    private PeerService peerService;
    @Resource
    private ChannelService channelService;
    @Resource
    private ChaincodeService chaincodeService;
    @Resource
    private TraceService traceService;

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
        leagueCount = leagueService.listAll().size();
        orgCount = orgService.count();
        ordererCount = ordererService.count();
        peerCount = peerService.count();
        channelCount = channelService.count();
        chaincodeCount = chaincodeService.count();

        List<Channel> channels = channelService.listAll();
        for (Channel channel : channels) {
            List<Chaincode> chaincodes = chaincodeService.listById(channel.getId());
            for (Chaincode chaincode: chaincodes) {
                try {
                    JSONObject blockInfo = JSON.parseObject(traceService.queryBlockChainInfo(chaincode.getId()));
                    int height = blockInfo.getJSONObject("data").getInteger("height");
                    for (int num = height - 1; num >= 0; num--) {
                        Trace trace = new Trace();
                        trace.setId(chaincode.getId());
                        trace.setTrace(String.valueOf(num));
                        JSONObject blockMessage = JSON.parseObject(traceService.queryBlockByNumber(trace));
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
                        if ((height - num) > 8) {
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
        int size = tmpTransactions.size() > 9 ? 9 : tmpTransactions.size();
        for (int i = 0; i < size; i++) {
            Transaction transaction = tmpTransactions.get(i);
            transaction.setIndex(i + 1);
            transactions.add(transaction);
        }
        modelAndView.addObject("leagueCount", leagueCount);
        modelAndView.addObject("orgCount", orgCount);
        modelAndView.addObject("ordererCount", ordererCount);
        modelAndView.addObject("peerCount", peerCount);
        modelAndView.addObject("channelCount", channelCount);
        modelAndView.addObject("chaincodeCount", chaincodeCount);
        modelAndView.addObject("transactions", transactions);

        return modelAndView;
    }
}
