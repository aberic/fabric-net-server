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

import cn.aberic.fabric.bean.Home;
import cn.aberic.fabric.dao.entity.User;
import cn.aberic.fabric.service.*;
import cn.aberic.fabric.utils.CacheUtil;
import cn.aberic.fabric.utils.DataUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
    private CAService caService;
    @Resource
    private ChannelService channelService;
    @Resource
    private ChaincodeService chaincodeService;
    @Resource
    private AppService appService;
    @Resource
    private BlockService blockService;
    @Resource
    private UserService userService;

    @GetMapping(value = "index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index");

        Home home = CacheUtil.getHome();
        if (null == home) {
            home = DataUtil.obtain().home(leagueService, orgService, ordererService,
                    peerService, caService, channelService, chaincodeService,
                    appService, blockService);
            CacheUtil.putHome(home);
        }

        modelAndView.addObject("leagueCount", home.getLeagueCount());
        modelAndView.addObject("orgCount", home.getOrgCount());
        modelAndView.addObject("ordererCount", home.getOrdererCount());
        modelAndView.addObject("peerCount", home.getPeerCount());
        modelAndView.addObject("caCount", home.getCaCount());
        modelAndView.addObject("channelCount", home.getChannelCount());
        modelAndView.addObject("chaincodeCount", home.getChaincodeCount());
        modelAndView.addObject("appCount", home.getAppCount());
        modelAndView.addObject("blocks", home.getBlocks());
        //中间统计模块开始
        modelAndView.addObject("channelPercents", new JSONArray(home.getChannelPercents()).toString());
        modelAndView.addObject("channelBlockLists", new JSONArray(home.getChannelBlockLists()).toString());
        modelAndView.addObject("blockDaos", home.getBlockDaos());
        modelAndView.addObject("dayStatistics", home.getDayStatistics());
        modelAndView.addObject("platform", home.getPlatform());
        modelAndView.addObject("dayBlocks", home.getDayBlocks());
        modelAndView.addObject("dayTxs", home.getDayTxs());
        modelAndView.addObject("dayRWs", home.getDayRWs());
        modelAndView.addObject("dayBlocksJson", new JSONObject(home.getDayBlocks()));
        modelAndView.addObject("dayTxsJson", new JSONObject(home.getDayTxs()));
        modelAndView.addObject("dayRWsJson", new JSONObject(home.getDayRWs()));

        //中间统计模块结束

        return modelAndView;
    }

    @PostMapping(value = "login")
    public ModelAndView submit(@ModelAttribute User user, HttpServletRequest request) {
        try {
            String token = userService.login(user);
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
        CacheUtil.removeUser((String) request.getSession().getAttribute("token"));
        request.getSession().invalidate();
        return new ModelAndView(new RedirectView("login"));
    }
}
