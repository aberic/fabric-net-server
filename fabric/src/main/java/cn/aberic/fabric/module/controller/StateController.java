package cn.aberic.fabric.module.controller;

import cn.aberic.fabric.module.bean.vo.StateVO;
import cn.aberic.fabric.module.service.StateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/4 15:01】
 */
@Api(value = "State Controller", tags = {"区块链网络中数据库状态增删改查接口"})
@CrossOrigin
@RestController
@RequestMapping("state")
public class StateController {

    @Resource
    private StateService stateService;

    @ApiOperation(value = "执行智能合约")
    @PostMapping(value = "invoke")
    public String invoke(@RequestBody @ApiParam(name = "合约对象", value = "操作合约所需转接对象", required = true) StateVO chainCode) {
        return stateService.invoke(chainCode);
    }

    @ApiOperation(value = "查询智能合约")
    @PostMapping(value = "query")
    public String query(@RequestBody @ApiParam(name = "合约对象", value = "操作合约所需转接对象", required = true) StateVO chainCode) {
        return stateService.query(chainCode);
    }

}
