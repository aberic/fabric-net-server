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

import cn.aberic.fabric.bean.ChannelBlockList;
import cn.aberic.fabric.bean.ChannelPercent;
import cn.aberic.fabric.bean.Trace;
import cn.aberic.fabric.bean.Block;
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
    @Resource
    private BlockService blockService;

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
        List<Block> blocks = new ArrayList<>();
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

                Trace trace = new Trace();
                trace.setChannelId(channel.getId());
                trace.setTrace(String.valueOf(height - 1));
                JSONObject blockMessage = JSON.parseObject(traceService.queryBlockByNumberForIndex(trace));
                JSONArray envelopes = blockMessage.containsKey("data") ? blockMessage.getJSONObject("data").getJSONArray("envelopes") : new JSONArray();

                Block block = new Block();
                block.setNum(height);
                block.setPeerName(peerService.get(channel.getPeerId()).getName());
                block.setChannelName(channel.getName());
                block.setCalculatedBlockHash(blockMessage.getJSONObject("data").getString("calculatedBlockHash"));
                block.setDate(envelopes.getJSONObject(0).getString("timestamp"));
                blocks.add(block);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        blocks.sort((t1, t2) -> {
            try {
                long td1 = DateUtil.str2Date(t1.getDate(), "yyyy/MM/dd HH:mm:ss").getTime();
                long td2 = DateUtil.str2Date(t2.getDate(), "yyyy/MM/dd HH:mm:ss").getTime();
                return Math.toIntExact(td2 - td1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        });
        for (int i = 0; i < blocks.size(); i++) {
            blocks.get(i).setIndex(i + 1);
        }

        modelAndView.addObject("leagueCount", leagueCount);
        modelAndView.addObject("orgCount", orgCount);
        modelAndView.addObject("ordererCount", ordererCount);
        modelAndView.addObject("peerCount", peerCount);
        modelAndView.addObject("caCount", caCount);
        modelAndView.addObject("channelCount", channelCount);
        modelAndView.addObject("chaincodeCount", chaincodeCount);
        modelAndView.addObject("appCount", appCount);
        modelAndView.addObject("blocks", blocks);
        //中间统计模块开始

        List channelBlocklists = blockService.getChannelBlockLists(channels);
        List channelPercents = blockService.getChannelPercents(channels);

        JSONArray channelBlockLists = new JSONArray(channelBlocklists);
        JSONArray channelPercentLists = new JSONArray(channelPercents);

        modelAndView.addObject("channelPercents", channelPercentLists.toJSONString());
        modelAndView.addObject("channelBlockLists", channelBlockLists.toJSONString());
        modelAndView.addObject("dayStatistics", blockService.getDayStatistics());
        modelAndView.addObject("platform", blockService.getPlatform());
        //中间统计模块结束

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
