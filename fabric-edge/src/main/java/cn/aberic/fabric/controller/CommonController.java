package cn.aberic.fabric.controller;

import cn.aberic.fabric.thrift.MultiServiceProvider;
import org.apache.thrift.TException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/4 15:01】
 */
@RestController
@RequestMapping("")
public class CommonController {

    @Resource
    private MultiServiceProvider multiService;

    @GetMapping(value = "index")
    public ModelAndView leagueAdd() {
        ModelAndView modelAndView = new ModelAndView("index");
        int leagueCount = 0;
        int orgCount = 0;
        int ordererCount = 0;
        int peerCount = 0;
        int channelCount = 0;
        int chaincodeCount = 0;
        try {
            leagueCount = multiService.getLeagueService().listAll().size();
            orgCount = multiService.getOrgService().count();
            ordererCount = multiService.getOrdererService().count();
            peerCount = multiService.getPeerService().count();
            channelCount = multiService.getChannelService().count();
            chaincodeCount = multiService.getChaincodeService().count();
        } catch (TException e) {
            e.printStackTrace();
        }
        modelAndView.addObject("leagueCount", leagueCount);
        modelAndView.addObject("orgCount", orgCount);
        modelAndView.addObject("ordererCount", ordererCount);
        modelAndView.addObject("peerCount", peerCount);
        modelAndView.addObject("channelCount", channelCount);
        modelAndView.addObject("chaincodeCount", chaincodeCount);
        return modelAndView;
    }
}
