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
@RequestMapping("/simple")
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

    @RequestMapping(value = "/org/add", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public int addOrg(@RequestBody Map<String, Object> map) {
        return simpleService.addOrg(new JSONObject(map));
    }

    @RequestMapping(value = "/orderer/add", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public int addOrderer(@RequestBody Map<String, Object> map) {
        return simpleService.addOrderer(new JSONObject(map));
    }

    @RequestMapping(value = "/peer/add", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public int addPeer(@RequestBody Map<String, Object> map) {
        return simpleService.addPeer(new JSONObject(map));
    }

}
