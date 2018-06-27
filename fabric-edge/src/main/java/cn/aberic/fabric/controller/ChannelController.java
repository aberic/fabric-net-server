package cn.aberic.fabric.controller;

import cn.aberic.fabric.thrift.MultiServiceProvider;
import cn.aberic.thrift.channel.ChannelInfo;
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
@RequestMapping("channel")
public class ChannelController {

    @Resource
    private MultiServiceProvider multiService;

    @PostMapping(value = "submit")
    public ModelAndView submit(@ModelAttribute ChannelInfo channel,
                               @RequestParam("intent") String intent,
                               @RequestParam("id") int id) {
        try {
            switch (intent) {
                case "add":
                    multiService.getChannelService().add(channel);
                    break;
                case "edit":
                    channel.setId(id);
                    multiService.getChannelService().update(channel);
                    break;
            }
        } catch (TException e) {
            e.printStackTrace();
        }
        return new ModelAndView(new RedirectView("list"));
    }

    @GetMapping(value = "add")
    public ModelAndView add() {
        ModelAndView modelAndView = new ModelAndView("channelSubmit");
        modelAndView.addObject("intentLarge", "新建通道");
        modelAndView.addObject("intentLittle", "新建");
        modelAndView.addObject("submit", "新增");
        modelAndView.addObject("intent", "add");
        ChannelInfo channel = new ChannelInfo();
        List<PeerInfo> peers;
        try {
            peers = multiService.getPeerService().listAll();
            for (PeerInfo peer : peers) {
                channel.setPeerName(peer.getName());
                OrgInfo org = multiService.getOrgService().get(peer.getOrgId());
                channel.setOrgName(org.getName());
                LeagueInfo league = multiService.getLeagueService().get(org.getLeagueId());
                channel.setLeagueName(league.getName());
            }
        } catch (TException e) {
            peers = new ArrayList<>();
            e.printStackTrace();
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
        ChannelInfo channel;
        List<PeerInfo> peers;
        OrgInfo org;
        try {
            channel = multiService.getChannelService().get(id);
            org = multiService.getOrgService().get(multiService.getPeerService().get(channel.getPeerId()).getOrgId());
            channel.setOrgName(org.getName());
            peers = multiService.getPeerService().listById(org.getId());
            LeagueInfo league = multiService.getLeagueService().get(multiService.getOrgService().get(org.getId()).getLeagueId());
            channel.setLeagueName(league.getName());
            org.setLeagueName(league.getName());
            for (PeerInfo peer : peers) {
                peer.setOrgName(org.getName());
            }
        } catch (TException e) {
            channel = new ChannelInfo();
            peers = new ArrayList<>();
            e.printStackTrace();
        }
        modelAndView.addObject("channel", channel);
        modelAndView.addObject("peers", peers);
        return modelAndView;
    }

    @GetMapping(value = "list")
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("channels");
        try {
            List<ChannelInfo> channels = multiService.getChannelService().listAll();
            for (ChannelInfo channel : channels) {
                channel.setPeerName(multiService.getPeerService().get(channel.getPeerId()).getName());
                channel.setChaincodeCount(multiService.getChaincodeService().countById(channel.getId()));
            }
            modelAndView.addObject("channels", channels);
        } catch (TException e) {
            modelAndView.addObject("channels", new ArrayList<>());
            e.printStackTrace();
        }
        return modelAndView;
    }

}
