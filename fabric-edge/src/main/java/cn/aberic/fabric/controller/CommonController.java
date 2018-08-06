/*
 * Copyright (c) 2018. Aberic - aberic@qq.com - All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.aberic.fabric.controller;

import cn.aberic.fabric.bean.Trace;
import cn.aberic.fabric.bean.Transaction;
import cn.aberic.fabric.dao.Channel;
import cn.aberic.fabric.dao.User;
import cn.aberic.fabric.service.*;
import cn.aberic.fabric.utils.CacheUtil;
import cn.aberic.fabric.utils.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    private CommonService commonService;
    @Resource
    private LeagueService leagueService;
    @Resource
    private OrgService orgService;
    @Resource
    private OrdererService ordererService;
    @Resource
    private PeerService peerService;
    @Resource
    private CAService caService;
    @Resource
    private ChannelService channelService;
    @Resource
    private ChaincodeService chaincodeService;
    @Resource
    private AppService appService;
    @Resource
    private TraceService traceService;

    @GetMapping(value = "index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index");
        int leagueCount;
        int orgCount;
        int ordererCount;
        int peerCount;
        int caCount;
        int channelCount;
        int chaincodeCount;
        int appCount;
        List<Transaction> tmpTransactions = new ArrayList<>();
        List<Transaction> transactions = new ArrayList<>();
        leagueCount = leagueService.listAll().size();
        orgCount = orgService.count();
        ordererCount = ordererService.count();
        peerCount = peerService.count();
        caCount = caService.count();
        channelCount = channelService.count();
        chaincodeCount = chaincodeService.count();
        appCount = appService.count();

        List<Channel> channels = channelService.listAll();
        for (Channel channel : channels) {
            try {
                JSONObject blockInfo = JSON.parseObject(traceService.queryBlockChainInfoForIndex(channel.getId()));
                int height = blockInfo.containsKey("data") ? blockInfo.getJSONObject("data").getInteger("height") : 0;
                for (int num = 1; num <= 10; num++) {
                    int heightCount = height - num;
                    if (heightCount < 0) {
                        break;
                    }
                    Trace trace = new Trace();
                    trace.setChannelId(channel.getId());
                    trace.setTrace(String.valueOf(heightCount));
                    JSONObject blockMessage = JSON.parseObject(traceService.queryBlockByNumberForIndex(trace));
                    JSONArray envelopes = blockMessage.containsKey("data")? blockMessage.getJSONObject("data").getJSONArray("envelopes") : new JSONArray();
                    int txCount = 0;
                    int size = envelopes.size();
                    for (int i = 0; i < size; i++) {
                        JSONObject envelope = envelopes.getJSONObject(i);
                        txCount += envelope.containsKey("transactionEnvelopeInfo") ? envelope.getJSONObject("transactionEnvelopeInfo").getInteger("txCount") : 0;
                    }
                    Transaction transaction = new Transaction();
                    transaction.setNum(heightCount);
                    transaction.setTxCount(txCount);
                    transaction.setChannelName(channel.getName());
                    transaction.setCalculatedBlockHash(blockMessage.getJSONObject("data").getString("calculatedBlockHash"));
                    transaction.setDate(envelopes.getJSONObject(0).getString("timestamp"));
                    tmpTransactions.add(transaction);
                }
            } catch (Exception e) {
                e.printStackTrace();
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
        modelAndView.addObject("caCount", caCount);
        modelAndView.addObject("channelCount", channelCount);
        modelAndView.addObject("chaincodeCount", chaincodeCount);
        modelAndView.addObject("appCount", appCount);
        modelAndView.addObject("transactions", transactions);

        return modelAndView;
    }

    @PostMapping(value = "login")
    public ModelAndView submit(@ModelAttribute User user, HttpServletRequest request) {
        try {
            String token = commonService.login(user);
            if (null != token) {
                // 校验通过时，在session里放入一个标识
                // 后续通过session里是否存在该标识来判断用户是否登录
                request.getSession().setAttribute("username", user.getUsername());
                request.getSession().setAttribute("token", token);
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
        CacheUtil.removeString((String) request.getSession().getAttribute("username"));
        request.getSession().invalidate();
        return new ModelAndView(new RedirectView("login"));
    }
}
