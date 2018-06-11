package cn.aberic.simple.module.controller;

import cn.aberic.simple.module.service.SimpleService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/org/add", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public String addOrg(@RequestBody Map<String, Object> map) {
        return simpleService.addOrg(new JSONObject(map));
    }

    @RequestMapping(value = "/org/list", method = RequestMethod.GET)
    @ResponseBody
    public String orgList() {
        return simpleService.getOrgList();
    }

    @RequestMapping(value = "/orderer/add", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public String addOrderer(@RequestBody Map<String, Object> map) {
        return simpleService.addOrderer(new JSONObject(map));
    }

    @RequestMapping(value = "/orderer/list/{hash}", method = RequestMethod.GET)
    @ResponseBody
    public String ordererList(@PathVariable("hash") String hash) {
        return simpleService.getOrdererListByOrgHash(hash);
    }

    @RequestMapping(value = "/peer/add", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public String addPeer(@RequestBody Map<String, Object> map) {
        return simpleService.addPeer(new JSONObject(map));
    }

    @RequestMapping(value = "/peer/list/{hash}", method = RequestMethod.GET)
    @ResponseBody
    public String peerList(@PathVariable("hash") String hash) {
        return simpleService.getPeerListByOrgHash(hash);
    }

}
