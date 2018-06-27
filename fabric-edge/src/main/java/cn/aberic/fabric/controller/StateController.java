package cn.aberic.fabric.controller;

import cn.aberic.fabric.bean.State;
import cn.aberic.fabric.service.StateService;
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
    private StateService stateService;

    @PostMapping(value = "invoke")
    public String invoke(@RequestBody State state) {
        return stateService.invoke(state);
    }

    @PostMapping(value = "query")
    public String query(@RequestBody State state) {
        return stateService.query(state);
    }

}
