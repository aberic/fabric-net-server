package cn.aberic.fabric.module.controller;

import cn.aberic.fabric.module.bean.dto.LeagueDTO;
import cn.aberic.fabric.module.service.LeagueService;
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
@Api(value = "League Controller", tags = {"区块链网络中联盟相关接口"})
@RestController
@RequestMapping("league")
public class LeagueController {

    @Resource
    private LeagueService leagueService;

    @ApiOperation(value = "新增联盟对象")
    @PostMapping(value = "add")
    public String add(@RequestBody @ApiParam(name = "联盟对象", value = "联盟对象详情", required = true) LeagueDTO league) {
        return leagueService.add(league);
    }

    @ApiOperation(value = "更新联盟对象")
    @PostMapping(value = "update")
    public String update(@RequestBody @ApiParam(name = "组织对象", value = "组织对象详情", required = true) LeagueDTO league) {
        return leagueService.update(league);
    }

    @ApiOperation(value = "获取联盟对象集合")
    @GetMapping(value = "list")
    public String list() {
        return leagueService.list(0);
    }

}
