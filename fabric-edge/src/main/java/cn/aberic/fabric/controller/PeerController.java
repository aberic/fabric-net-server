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

import cn.aberic.fabric.dao.League;
import cn.aberic.fabric.dao.Org;
import cn.aberic.fabric.dao.Peer;
import cn.aberic.fabric.service.ChannelService;
import cn.aberic.fabric.service.LeagueService;
import cn.aberic.fabric.service.OrgService;
import cn.aberic.fabric.service.PeerService;
import cn.aberic.fabric.utils.SpringUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import java.util.List;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/4 15:01】
 */
@CrossOrigin
@RestController
@RequestMapping("peer")
public class PeerController {

    @Resource
    private PeerService peerService;
    @Resource
    private OrgService orgService;
    @Resource
    private LeagueService leagueService;
    @Resource
    private ChannelService channelService;

    @PostMapping(value = "submit")
    public ModelAndView submit(@ModelAttribute Peer peer,
                               @RequestParam("intent") String intent,
                               @RequestParam("serverCrtFile") MultipartFile serverCrtFile,
                               @RequestParam("id") int id) {
        switch (intent) {
            case "add":
                peer = resetPeer(peer);
                peerService.add(peer, serverCrtFile);
                break;
            case "edit":
                peer = resetPeer(peer);
                peer.setId(id);
                peerService.update(peer, serverCrtFile);
                break;
        }
        return new ModelAndView(new RedirectView("list"));
    }

    @GetMapping(value = "add")
    public ModelAndView add() {
        ModelAndView modelAndView = new ModelAndView("peerSubmit");
        modelAndView.addObject("intentLittle", SpringUtil.get("enter"));
        modelAndView.addObject("submit", SpringUtil.get("submit"));
        modelAndView.addObject("intent", "add");
        modelAndView.addObject("peer", new Peer());
        modelAndView.addObject("orgs", getForPeerAndOrderer());
        return modelAndView;
    }

    @GetMapping(value = "edit")
    public ModelAndView edit(@RequestParam("id") int id) {
        ModelAndView modelAndView = new ModelAndView("peerSubmit");
        modelAndView.addObject("intentLittle", SpringUtil.get("edit"));
        modelAndView.addObject("submit", SpringUtil.get("modify"));
        modelAndView.addObject("intent", "edit");
        Peer peer = peerService.get(id);
        League league = leagueService.get(orgService.get(peer.getOrgId()).getLeagueId());
        List<Org> orgs = orgService.listById(league.getId());
        for (Org org : orgs) {
            org.setLeagueName(league.getName());
        }
        modelAndView.addObject("peer", peer);
        modelAndView.addObject("orgs", orgs);
        return modelAndView;
    }

    @GetMapping(value = "delete")
    public ModelAndView delete(@RequestParam("id") int id) {
        peerService.delete(id);
        return new ModelAndView(new RedirectView("list"));
    }

    @GetMapping(value = "list")
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("peers");
        List<Peer> peers = peerService.listAll();
        for (Peer peer : peers) {
            peer.setOrgName(orgService.get(peer.getOrgId()).getName());
            peer.setChannelCount(channelService.countById(peer.getId()));
        }
        modelAndView.addObject("peers", peers);
        return modelAndView;
    }

    private List<Org> getForPeerAndOrderer() {
        List<Org> orgs = orgService.listAll();
        for (Org org : orgs) {
            org.setLeagueName(leagueService.get(org.getLeagueId()).getName());
        }
        return orgs;
    }

    private Peer resetPeer(Peer peer) {
        Org org = orgService.get(peer.getOrgId());
        League league = leagueService.get(org.getLeagueId());
        peer.setLeagueName(league.getName());
        peer.setOrgName(org.getName());
        return peer;
    }

}
