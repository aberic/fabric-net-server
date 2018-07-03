package cn.aberic.fabric.controller;

import cn.aberic.fabric.bean.Trace;
import cn.aberic.fabric.bean.Transaction;
import cn.aberic.fabric.dao.Chaincode;
import cn.aberic.fabric.dao.Channel;
import cn.aberic.fabric.dao.User;
import cn.aberic.fabric.dao.mapper.UserMapper;
import cn.aberic.fabric.service.*;
import cn.aberic.fabric.utils.CacheUtil;
import cn.aberic.fabric.utils.DateUtil;
import cn.aberic.fabric.utils.MD5Util;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/4 15:01】
 */
@RestController
@RequestMapping("")
public class CommonController {

    @Resource
    private UserMapper userMapper;

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
            for (Chaincode chaincode : chaincodes) {
                try {
                    JSONObject blockInfo = JSON.parseObject(traceService.queryBlockChainInfo(chaincode.getId()));
                    int height = blockInfo.getJSONObject("data").getInteger("height");
                    int entCount = height - 10;
                    for (int num = height - 1; num >= entCount; num--) {
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
                            transaction.setTxCount(envelope.containsKey("transactionEnvelopeInfo") ? envelope.getJSONObject("transactionEnvelopeInfo").getInteger("txCount") : 0);
                            transaction.setChannelName(envelope.getString("channelId"));
                            transaction.setCreateMSPID(envelope.getString("createMSPID"));
                            transaction.setDate(envelope.getString("timestamp"));
                            tmpTransactions.add(transaction);
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
        int size = tmpTransactions.size() > 10 ? 10 : tmpTransactions.size();
        for (int i = 10; i > 0; i--) {
            if (i > size) {
                modelAndView.addObject(String.format("transaction%s", 10 - i), 0);
            } else if (i <= size) {
                Transaction transaction = tmpTransactions.get(size - i);
                transaction.setIndex(size - i + 1);
                transactions.add(transaction);
                modelAndView.addObject(String.format("transaction%s", 10 - i), transaction.getTxCount());
            }
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

    @PostMapping(value = "login")
    public ModelAndView submit(@ModelAttribute User user, HttpServletRequest request) {
        try {
            if (MD5Util.verify(user.getPassword(), userMapper.get(user.getUsername()).getPassword())) {
                // 校验通过时，在session里放入一个标识
                // 后续通过session里是否存在该标识来判断用户是否登录
                request.getSession().setAttribute("username", user.getUsername());
                String token = UUID.randomUUID().toString();
                request.getSession().setAttribute("token", token);
                CacheUtil.put(user.getUsername(), token);
                return new ModelAndView(new RedirectView("index"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView(new RedirectView("login"));
    }

    @GetMapping(value = "login")
    public ModelAndView login() {
        User user = new User();
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @GetMapping(value = "logout")
    public ModelAndView logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return new ModelAndView(new RedirectView("login"));
    }
}
