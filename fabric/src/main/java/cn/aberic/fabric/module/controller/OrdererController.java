package cn.aberic.fabric.module.controller;

import cn.aberic.fabric.module.bean.dto.OrdererDTO;
import cn.aberic.fabric.module.service.OrdererService;
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
@Api(value = "Orderer Controller", tags = {"区块链网络中排序服务相关接口"})
@RestController
@RequestMapping("orderer")
public class OrdererController {

    @Resource
    private OrdererService ordererService;

    @ApiOperation(value = "新增排序服务对象")
    @PostMapping(value = "add")
    public String add(@RequestBody @ApiParam(name = "排序服务对象", value = "排序服务对象信息", required = true) OrdererDTO orderer) {
        return ordererService.add(orderer);
    }

    @ApiOperation(value = "更新排序服务对象")
    @PostMapping(value = "update")
    public String update(@RequestBody @ApiParam(name = "排序服务对象", value = "排序服务对象信息", required = true) OrdererDTO orderer) {
        return ordererService.update(orderer);
    }

    @ApiOperation(value = "获取排序服务对象集合")
    @GetMapping(value = "list/{id}")
    public String list(@ApiParam(name = "当前组织id", value = "id值", required = true) @PathVariable("id") int id) {
        return ordererService.list(id);
    }

    @ApiOperation(value = "获取排序服务对象")
    @GetMapping(value = "get/{id}")
    public String get(@ApiParam(name = "排序服务对象id", value = "id值", required = true) @PathVariable("id") int id) {
        return ordererService.get(id);
    }

}
