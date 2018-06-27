package cn.aberic.fabric.controller;

import cn.aberic.fabric.dao.League;
import cn.aberic.fabric.dao.Org;
import cn.aberic.fabric.dao.Peer;
import cn.aberic.fabric.service.ChannelService;
import cn.aberic.fabric.service.LeagueService;
import cn.aberic.fabric.service.OrgService;
import cn.aberic.fabric.service.PeerService;
import org.springframework.web.bind.annotation.*;
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
                               @RequestParam("id") int id) {
        switch (intent) {
            case "add":
                peerService.add(peer);
                break;
            case "edit":
                peer.setId(id);
                peerService.update(peer);
                break;
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
        modelAndView.addObject("peer", new Peer());
        modelAndView.addObject("orgs", getForPeerAndOrderer());
        return modelAndView;
    }

    @GetMapping(value = "edit")
    public ModelAndView edit(@RequestParam("id") int id) {
        ModelAndView modelAndView = new ModelAndView("peerSubmit");
        modelAndView.addObject("intentLarge", "编辑节点");
        modelAndView.addObject("intentLittle", "编辑");
        modelAndView.addObject("submit", "修改");
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

}
