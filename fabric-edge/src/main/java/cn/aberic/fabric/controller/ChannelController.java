package cn.aberic.fabric.controller;

import cn.aberic.fabric.dao.Channel;
import cn.aberic.fabric.dao.League;
import cn.aberic.fabric.dao.Org;
import cn.aberic.fabric.dao.Peer;
import cn.aberic.fabric.service.*;
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
@RequestMapping("channel")
public class ChannelController {

    @Resource
    private ChannelService channelService;
    @Resource
    private PeerService peerService;
    @Resource
    private OrgService orgService;
    @Resource
    private LeagueService leagueService;
    @Resource
    private ChaincodeService chaincodeService;

    @PostMapping(value = "submit")
    public ModelAndView submit(@ModelAttribute Channel channel,
                               @RequestParam("intent") String intent,
                               @RequestParam("id") int id) {
        switch (intent) {
            case "add":
                channelService.add(channel);
                break;
            case "edit":
                channel.setId(id);
                channelService.update(channel);
                break;
        }
        return new ModelAndView(new RedirectView("list"));
    }

    @GetMapping(value = "add")
    public ModelAndView add() {
        ModelAndView modelAndView = new ModelAndView("channelSubmit");
        modelAndView.addObject("intentLarge", "录入通道");
        modelAndView.addObject("intentLittle", "录入");
        modelAndView.addObject("submit", "录入");
        modelAndView.addObject("intent", "add");
        Channel channel = new Channel();
        List<Peer> peers = peerService.listAll();
        for (Peer peer : peers) {
            channel.setPeerName(peer.getName());
            Org org = orgService.get(peer.getOrgId());
            channel.setOrgName(org.getName());
            League league = leagueService.get(org.getLeagueId());
            channel.setLeagueName(league.getName());
        }
        modelAndView.addObject("channel", channel);
        modelAndView.addObject("peers", peers);
        return modelAndView;
    }

    @GetMapping(value = "edit")
    public ModelAndView edit(@RequestParam("id") int id) {
        ModelAndView modelAndView = new ModelAndView("channelSubmit");
        modelAndView.addObject("intentLarge", "编辑通道");
        modelAndView.addObject("intentLittle", "编辑");
        modelAndView.addObject("submit", "修改");
        modelAndView.addObject("intent", "edit");
        Channel channel = channelService.get(id);
        Org org = orgService.get(peerService.get(channel.getPeerId()).getOrgId());
        channel.setOrgName(org.getName());
        List<Peer> peers = peerService.listById(org.getId());
        League league = leagueService.get(orgService.get(org.getId()).getLeagueId());
        channel.setLeagueName(league.getName());
        org.setLeagueName(league.getName());
        for (Peer peer : peers) {
            peer.setOrgName(org.getName());
        }
        modelAndView.addObject("channel", channel);
        modelAndView.addObject("peers", peers);
        return modelAndView;
    }

    @GetMapping(value = "list")
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("channels");
        List<Channel> channels = channelService.listAll();
        for (Channel channel : channels) {
            channel.setPeerName(peerService.get(channel.getPeerId()).getName());
            channel.setChaincodeCount(chaincodeService.countById(channel.getId()));
        }
        modelAndView.addObject("channels", channels);
        return modelAndView;
    }

}
