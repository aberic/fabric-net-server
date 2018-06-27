package cn.aberic.fabric.controller;

import cn.aberic.fabric.thrift.MultiServiceProvider;
import cn.aberic.thrift.league.LeagueInfo;
import cn.aberic.thrift.org.OrgInfo;
import cn.aberic.thrift.peer.PeerInfo;
import org.apache.thrift.TException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    private MultiServiceProvider multiService;

    @PostMapping(value = "submit")
    public ModelAndView submit(@ModelAttribute PeerInfo peer,
                               @RequestParam("intent") String intent,
                               @RequestParam("id") int id) {
        try {
            switch (intent) {
                case "add":
                    multiService.getPeerService().add(peer);
                    break;
                case "edit":
                    peer.setId(id);
                    multiService.getPeerService().update(peer);
                    break;
            }
        } catch (TException e) {
            e.printStackTrace();
        }
        return new ModelAndView(new RedirectView("list"));
    }

    @GetMapping(value = "add")
    public ModelAndView add() {
        ModelAndView modelAndView = new ModelAndView("peerSubmit");
        modelAndView.addObject("intentLarge", "新建节点");
        modelAndView.addObject("intentLittle", "新建");
        modelAndView.addObject("submit", "新增");
        modelAndView.addObject("intent", "add");
        modelAndView.addObject("peer", new PeerInfo());
        List<OrgInfo> orgs = multiService.getForPeerAndOrderer();
        modelAndView.addObject("orgs", orgs);
        return modelAndView;
    }

    @GetMapping(value = "edit")
    public ModelAndView edit(@RequestParam("id") int id) {
        ModelAndView modelAndView = new ModelAndView("peerSubmit");
        modelAndView.addObject("intentLarge", "编辑节点");
        modelAndView.addObject("intentLittle", "编辑");
        modelAndView.addObject("submit", "修改");
        modelAndView.addObject("intent", "edit");
        PeerInfo peer;
        List<OrgInfo> orgs;
        try {
            peer = multiService.getPeerService().get(id);
            LeagueInfo league = multiService.getLeagueService().get(multiService.getOrgService().get(peer.getOrgId()).getLeagueId());
            orgs = multiService.getOrgService().listById(league.getId());
            for (OrgInfo org : orgs) {
                org.setLeagueName(league.getName());
            }
        } catch (TException e) {
            peer = new PeerInfo();
            orgs = new ArrayList<>();
            e.printStackTrace();
        }
        modelAndView.addObject("peer", peer);
        modelAndView.addObject("orgs", orgs);
        return modelAndView;
    }

    @GetMapping(value = "list")
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("peers");
        try {
            List<PeerInfo> peers = multiService.getPeerService().listAll();
            for (PeerInfo peer : peers) {
                peer.setOrgName(multiService.getOrgService().get(peer.getOrgId()).getName());
                peer.setChannelCount(multiService.getChannelService().countById(peer.getId()));
            }
            modelAndView.addObject("peers", peers);
        } catch (TException e) {
            modelAndView.addObject("peers", new ArrayList<>());
            e.printStackTrace();
        }
        return modelAndView;
    }

}
