package cn.aberic.fabric.controller;

import cn.aberic.fabric.thrift.MultiServiceProvider;
import cn.aberic.thrift.peer.PeerInfo;
import org.apache.thrift.TException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
@RequestMapping("peer")
public class PeerController {

    @Resource
    private MultiServiceProvider multiService;

    @GetMapping(value = "list")
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("peers");
        try {
            List<PeerInfo> peers = multiService.getPeerService().listAll();
            for (PeerInfo peer : peers) {
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
