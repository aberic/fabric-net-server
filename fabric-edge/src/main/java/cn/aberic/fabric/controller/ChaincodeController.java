package cn.aberic.fabric.controller;

import cn.aberic.fabric.thrift.MultiServiceProvider;
import cn.aberic.thrift.chaincode.ChaincodeInfo;
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
@RequestMapping("chaincode")
public class ChaincodeController {

    @Resource
    private MultiServiceProvider multiService;

    @GetMapping(value = "listAll")
    public List<ChaincodeInfo> listAll() {
        try {
            return multiService.getChaincodeService().listAll();
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping(value = "list")
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("chaincodes");
        try {
            List<ChaincodeInfo> chaincodes = multiService.getChaincodeService().listAll();
            modelAndView.addObject("chaincodes", chaincodes);
        } catch (TException e) {
            modelAndView.addObject("chaincodes", new ArrayList<>());
            e.printStackTrace();
        }
        return modelAndView;
    }

}
