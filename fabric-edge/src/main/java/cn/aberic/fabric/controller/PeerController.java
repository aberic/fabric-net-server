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

import cn.aberic.fabric.dao.Peer;
import cn.aberic.fabric.service.PeerService;
import cn.aberic.fabric.utils.SpringUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;

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

    @PostMapping(value = "submit")
    public ModelAndView submit(@ModelAttribute Peer peer,
                               @RequestParam("intent") String intent,
                               @RequestParam("serverCrtFile") MultipartFile serverCrtFile,
                               @RequestParam("clientCertFile") MultipartFile clientCertFile,
                               @RequestParam("clientKeyFile") MultipartFile clientKeyFile,
                               @RequestParam("id") int id) {
        switch (intent) {
            case "add":
                peer = peerService.resetPeer(peer);
                peerService.add(peer, serverCrtFile, clientCertFile, clientKeyFile);
                break;
            case "edit":
                peer = peerService.resetPeer(peer);
                peer.setId(id);
                peerService.update(peer, serverCrtFile, clientCertFile, clientKeyFile);
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
        modelAndView.addObject("orgs", peerService.getForPeerAndOrderer());
        return modelAndView;
    }

    @GetMapping(value = "edit")
    public ModelAndView edit(@RequestParam("id") int id) {
        ModelAndView modelAndView = new ModelAndView("peerSubmit");
        modelAndView.addObject("intentLittle", SpringUtil.get("edit"));
        modelAndView.addObject("submit", SpringUtil.get("modify"));
        modelAndView.addObject("intent", "edit");
        Peer peer = peerService.get(id);
        modelAndView.addObject("peer", peer);
        modelAndView.addObject("orgs", peerService.listOrgByOrgId(peer.getOrgId()));
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
        modelAndView.addObject("peers", peerService.listAll());
        return modelAndView;
    }

}
