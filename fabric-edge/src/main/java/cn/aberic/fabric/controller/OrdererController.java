package cn.aberic.fabric.controller;

import cn.aberic.fabric.thrift.MultiServiceProvider;
import cn.aberic.thrift.orderer.OrdererInfo;
import org.apache.thrift.TException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/4 15:01】
 */
@CrossOrigin
@RestController
@RequestMapping("orderer")
public class OrdererController {

    @Resource
    private MultiServiceProvider multiService;

    @PostMapping(value = "add")
    public String add(@RequestBody OrdererInfo orderer) {
        try {
            if (multiService.getOrdererService().add(orderer) > 0) {
                return "success";
            }
        } catch (TException e) {
            e.printStackTrace();
        }
        return "fail";
    }

    @PostMapping(value = "update")
    public String update(@RequestBody OrdererInfo orderer) {
        try {
            if (multiService.getOrdererService().update(orderer) > 0) {
                return "success";
            }
        } catch (TException e) {
            e.printStackTrace();
        }
        return "fail";
    }

    @GetMapping(value = "list")
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("orderers");
        try {
            modelAndView.addObject("orderers", multiService.getOrdererService().listAll());
        } catch (TException e) {
            modelAndView.addObject("orderers", new ArrayList<>());
            e.printStackTrace();
        }
        return modelAndView;
    }

}
