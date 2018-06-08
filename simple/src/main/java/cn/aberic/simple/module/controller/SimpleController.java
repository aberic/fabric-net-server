package cn.aberic.simple.module.controller;

import cn.aberic.simple.module.service.SimpleService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/4 15:01】
 */
@Controller
@RequestMapping("/sdk")
public class SimpleController {

    @Resource
    private SimpleService simpleService;

    @RequestMapping(value = "/chaincode", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public String chaincode(@RequestBody Map<String, Object> map) {
        return simpleService.chainCode(new JSONObject(map));
    }

    @RequestMapping(value = "/trace", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public String trace(@RequestBody Map<String, Object> map) {
        return simpleService.trace(new JSONObject(map));
    }

    @RequestMapping(value = "/init", method = RequestMethod.GET)
    @ResponseBody
    public int init() {
        return simpleService.init();
    }

    @RequestMapping(value = "/org/set", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public int setOrg(@RequestBody Map<String, Object> map) {
        return simpleService.setOrg(new JSONObject(map));
    }

    @RequestMapping(value = "/orderer/set", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public int setOrderer(@RequestBody Map<String, Object> map) {
        return simpleService.setOrderer(new JSONObject(map));
    }

    @RequestMapping(value = "/peer/set", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public int setPeer(@RequestBody Map<String, Object> map) {
        return simpleService.setPeer(new JSONObject(map));
    }

}
