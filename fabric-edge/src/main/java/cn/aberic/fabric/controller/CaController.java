/*
 * Copyright (c) 2018. Aberic - All Rights Reserved.
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

import cn.aberic.fabric.dao.CA;
import cn.aberic.fabric.dao.League;
import cn.aberic.fabric.dao.Org;
import cn.aberic.fabric.dao.Peer;
import cn.aberic.fabric.service.CAService;
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
 * 作者：Aberic on 2018/7/13 00:05
 * 邮箱：abericyang@gmail.com
 */
@CrossOrigin
@RestController
@RequestMapping("ca")
public class CaController {

    @Resource
    private CAService caService;
    @Resource
    private PeerService peerService;
    @Resource
    private OrgService orgService;
    @Resource
    private LeagueService leagueService;

    @PostMapping(value = "submit")
    public ModelAndView submit(@ModelAttribute CA ca,
                               @RequestParam("intent") String intent,
                               @RequestParam("skFile") MultipartFile skFile,
                               @RequestParam("certificateFile") MultipartFile certificateFile,
                               @RequestParam("id") int id) {
        switch (intent) {
            case "add":
                ca = resetCa(ca);
                caService.add(ca, skFile, certificateFile);
                break;
            case "edit":
                ca = resetCa(ca);
                ca.setId(id);
                caService.update(ca, skFile, certificateFile);
                break;
        }
        return new ModelAndView(new RedirectView("list"));
    }

    @GetMapping(value = "add")
    public ModelAndView add() {
        ModelAndView modelAndView = new ModelAndView("caSubmit");
        modelAndView.addObject("intentLittle", SpringUtil.get("enter"));
        modelAndView.addObject("submit", SpringUtil.get("submit"));
        modelAndView.addObject("intent", "add");
        modelAndView.addObject("ca", new CA());
        List<Peer> peers = peerService.listAll();
        for (Peer peer : peers) {
            Org org = orgService.get(peer.getOrgId());
            peer.setOrgName(org.getName());
            League league = leagueService.get(org.getLeagueId());
            peer.setLeagueName(league.getName());
        }
        modelAndView.addObject("peers", peers);
        return modelAndView;
    }

    @GetMapping(value = "edit")
    public ModelAndView edit(@RequestParam("id") int id) {
        ModelAndView modelAndView = new ModelAndView("caSubmit");
        modelAndView.addObject("intentLittle", SpringUtil.get("edit"));
        modelAndView.addObject("submit", SpringUtil.get("modify"));
        modelAndView.addObject("intent", "edit");
        CA ca = caService.get(id);
        Org org = orgService.get(peerService.get(ca.getPeerId()).getOrgId());
        List<Peer> peers = peerService.listById(org.getId());
        League league = leagueService.get(orgService.get(org.getId()).getLeagueId());
        for (Peer peer : peers) {
            peer.setLeagueName(league.getName());
            peer.setOrgName(org.getName());
        }
        modelAndView.addObject("ca", ca);
        modelAndView.addObject("peers", peers);
        return modelAndView;
    }

    @GetMapping(value = "list")
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("cas");
        List<CA> cas = caService.listAll();
        for (CA ca: cas) {
            Peer peer = peerService.get(ca.getPeerId());
            Org org = orgService.get(peer.getOrgId());
            ca.setPeerName(peer.getName());
            ca.setOrgName(org.getName());
            ca.setLeagueName(leagueService.get(org.getLeagueId()).getName());
        }
        modelAndView.addObject("cas", cas);
        return modelAndView;
    }

    private CA resetCa(CA ca) {
        Peer peer = peerService.get(ca.getPeerId());
        Org org = orgService.get(peer.getOrgId());
        League league = leagueService.get(org.getLeagueId());
        ca.setLeagueName(league.getName());
        ca.setOrgName(org.getName());
        ca.setPeerName(peer.getName());
        return ca;
    }

}
