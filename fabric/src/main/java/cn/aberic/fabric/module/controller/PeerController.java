package cn.aberic.fabric.module.controller;

import cn.aberic.fabric.module.bean.dto.PeerDTO;
import cn.aberic.fabric.module.service.PeerService;
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
@Api(value = "Peer Controller", tags = {"区块链网络中节点服务相关接口"})
@RestController
@RequestMapping("peer")
public class PeerController {

    @Resource
    private PeerService peerService;

    @ApiOperation(value = "新增节点服务对象")
    @PostMapping(value = "add")
    public String add(@RequestBody @ApiParam(name = "节点服务对象", value = "节点服务对象信息", required = true) PeerDTO peer) {
        return peerService.add(peer);
    }

    @ApiOperation(value = "更新节点服务对象")
    @PostMapping(value = "update")
    public String update(@RequestBody @ApiParam(name = "节点服务对象", value = "节点服务对象信息", required = true) PeerDTO peer) {
        return peerService.update(peer);
    }

    @ApiOperation(value = "获取节点服务对象集合")
    @GetMapping(value = "list/{id}")
    public String list(@ApiParam(name = "当前组织id", value = "id值", required = true) @PathVariable("id") int id) {
        return peerService.list(id);
    }

}
