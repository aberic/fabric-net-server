package cn.aberic.fabric.controller;

import cn.aberic.fabric.thrift.MultiServiceProvider;
import cn.aberic.thrift.state.StateInfo;
import org.apache.thrift.TException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/4 15:01】
 */
@CrossOrigin
@RestController
@RequestMapping("state")
public class StateController {

    @Resource
    private MultiServiceProvider multiSService;

    @PostMapping(value = "invoke")
    public String invoke(@RequestBody StateInfo stateInfo) {
        try {
            return multiSService.getStateService().invoke(stateInfo);
        } catch (TException e) {
            e.printStackTrace();
        }
        return "";
    }

    @PostMapping(value = "query")
    public String query(@RequestBody StateInfo stateInfo) {
        try {
            return multiSService.getStateService().query(stateInfo);
        } catch (TException e) {
            e.printStackTrace();
        }
        return "";
    }

}
