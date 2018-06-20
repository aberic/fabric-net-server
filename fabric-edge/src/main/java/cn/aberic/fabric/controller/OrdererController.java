package cn.aberic.fabric.controller;

import cn.aberic.fabric.thrift.MultiServiceProvider;
import cn.aberic.thrift.orderer.OrdererInfo;
import org.apache.thrift.TException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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
    private MultiServiceProvider multiSService;

    @PostMapping(value = "add")
    public String add(@RequestBody OrdererInfo orderer) {
        try {
            if (multiSService.getOrdererService().add(orderer) > 0) {
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
            if (multiSService.getOrdererService().update(orderer) > 0) {
                return "success";
            }
        } catch (TException e) {
            e.printStackTrace();
        }
        return "fail";
    }

    @GetMapping(value = "list/{id}")
    public List<OrdererInfo> list(@PathVariable("id") int id) {
        try {
            return multiSService.getOrdererService().listById(id);
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping(value = "listAll")
    public List<OrdererInfo> listAll() {
        try {
            return multiSService.getOrdererService().listAll();
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

}
