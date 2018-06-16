package cn.aberic.fabric.module.controller;

import cn.aberic.fabric.module.bean.dto.ChannelDTO;
import cn.aberic.fabric.module.service.ChannelService;
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
@Api(value = "Channel Controller", tags = {"区块链网络中通道服务相关接口"})
@RestController
@RequestMapping("channel")
public class ChannelController {

    @Resource
    private ChannelService channelService;

    @ApiOperation(value = "新增通道对象")
    @PostMapping(value = "add")
    public String add(@RequestBody @ApiParam(name = "通道对象", value = "通道对象详情", required = true) ChannelDTO channel) {
        return channelService.add(channel);
    }

    @ApiOperation(value = "更新通道对象")
    @PostMapping(value = "update")
    public String update(@RequestBody @ApiParam(name = "通道对象", value = "通道对象详情", required = true) ChannelDTO channel) {
        return channelService.update(channel);
    }

    @ApiOperation(value = "获取通道对象集合")
    @GetMapping(value = "list/{id}")
    public String list(@ApiParam(name = "通道所属节点id", value = "id值", required = true) @PathVariable("id") int id) {
        return channelService.list(id);
    }

}
