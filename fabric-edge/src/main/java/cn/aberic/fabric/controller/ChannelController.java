package cn.aberic.fabric.controller;

import cn.aberic.fabric.thrift.MultiServiceProvider;
import cn.aberic.thrift.channel.ChannelInfo;
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
@RequestMapping("channel")
public class ChannelController {

    @Resource
    private MultiServiceProvider multiService;

    @GetMapping(value = "list")
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("channels");
        try {
            List<ChannelInfo> channels = multiService.getChannelService().listAll();
            for (ChannelInfo channel : channels) {
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
