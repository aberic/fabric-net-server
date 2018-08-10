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

import cn.aberic.fabric.bean.Block;
import cn.aberic.fabric.dao.Channel;
import cn.aberic.fabric.dao.User;
import cn.aberic.fabric.service.*;
import cn.aberic.fabric.utils.CacheUtil;
import cn.aberic.fabric.utils.DataUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
        leagueCount = leagueService.listAll().size();
        orgCount = orgService.count();
        ordererCount = ordererService.count();
        peerCount = peerService.count();
        caCount = caService.count();
        channelCount = channelService.count();
        chaincodeCount = chaincodeService.count();
        appCount = appService.count();

        List<Channel> channels = channelService.listAll();

        List<Block> blocks = CacheUtil.getHome();
        if (null == blocks) {
            blocks = DataUtil.obtain().home(channels, peerService, traceService);
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
        modelAndView.addObject("channelPercents", blockService.getChannelPercents(channels));
        modelAndView.addObject("channelBlockList", blockService.getChannelBlockLists(channels));
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
