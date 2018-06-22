package cn.aberic.fabric.controller;

import cn.aberic.fabric.thrift.MultiServiceProvider;
import cn.aberic.thrift.chaincode.ChaincodeInfo;
import cn.aberic.thrift.channel.ChannelInfo;
import cn.aberic.thrift.league.LeagueInfo;
import cn.aberic.thrift.org.OrgInfo;
import cn.aberic.thrift.peer.PeerInfo;
import org.apache.thrift.TException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
@RequestMapping("chaincode")
public class ChaincodeController {

    @Resource
    private MultiServiceProvider multiService;

    @PostMapping(value = "submit")
    public ModelAndView submit(@ModelAttribute ChaincodeInfo chaincode,
                               @RequestParam("intent") String intent,
                               @RequestParam("id") int id) {
        try {
            switch (intent) {
                case "add":
                    multiService.getChaincodeService().add(chaincode);
                    break;
                case "edit":
                    chaincode.setId(id);
                    multiService.getChaincodeService().update(chaincode);
                    break;
            }
        } catch (TException e) {
            e.printStackTrace();
        }
        return list();
    }

    @GetMapping(value = "add")
    public ModelAndView add() {
        ModelAndView modelAndView = new ModelAndView("chaincodeSubmit");
        modelAndView.addObject("intentLarge", "新建合约");
        modelAndView.addObject("intentLittle", "新建");
        modelAndView.addObject("submit", "新增");
        modelAndView.addObject("intent", "add");
        ChaincodeInfo chaincode = new ChaincodeInfo();
        List<ChannelInfo> channels;
        try {
            channels = multiService.getChannelService().listAll();
            for (ChannelInfo channel : channels) {
                chaincode.setChannelName(channel.getName());
                PeerInfo peer = multiService.getPeerService().get(channel.getPeerId());
                chaincode.setPeerName(peer.getName());
                OrgInfo org = multiService.getOrgService().get(peer.getId());
                chaincode.setOrgName(org.getName());
                LeagueInfo league = multiService.getLeagueService().get(org.getLeagueId());
                chaincode.setLeagueName(league.getName());
            }
        } catch (TException e) {
            channels = new ArrayList<>();
            e.printStackTrace();
        }
        modelAndView.addObject("chaincode", chaincode);
        modelAndView.addObject("channels", channels);
        return modelAndView;
    }

    @GetMapping(value = "edit")
    public ModelAndView edit(@RequestParam("id") int id) {
        ModelAndView modelAndView = new ModelAndView("chaincodeSubmit");
        modelAndView.addObject("intentLarge", "编辑合约");
        modelAndView.addObject("intentLittle", "编辑");
        modelAndView.addObject("submit", "修改");
        modelAndView.addObject("intent", "edit");
        ChaincodeInfo chaincode;
        List<ChannelInfo> channels;
        try {
            chaincode = multiService.getChaincodeService().get(id);
            PeerInfo peer = multiService.getPeerService().get(multiService.getChannelService().get(chaincode.getChannelId()).getPeerId());
            OrgInfo org = multiService.getOrgService().get(peer.getId());
            LeagueInfo league = multiService.getLeagueService().get(org.getLeagueId());
            chaincode.setPeerName(peer.getName());
            chaincode.setOrgName(org.getName());
            chaincode.setLeagueName(league.getName());
            channels = multiService.getChannelService().listById(peer.getId());
            for (ChannelInfo channel : channels) {
                chaincode.setChannelName(channel.getName());
            }
        } catch (TException e) {
            chaincode = new ChaincodeInfo();
            channels = new ArrayList<>();
            e.printStackTrace();
        }
        modelAndView.addObject("chaincode", chaincode);
        modelAndView.addObject("channels", channels);
        return modelAndView;
    }

    @GetMapping(value = "list")
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("chaincodes");
        try {
            List<ChaincodeInfo> chaincodes = multiService.getChaincodeService().listAll();
            for (ChaincodeInfo chaincode : chaincodes) {
                chaincode.setChannelName(multiService.getPeerService().get(chaincode.getChannelId()).getName());
            }
            modelAndView.addObject("chaincodes", chaincodes);
        } catch (TException e) {
            modelAndView.addObject("chaincodes", new ArrayList<>());
            e.printStackTrace();
        }
        return modelAndView;
    }

}
