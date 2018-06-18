package cn.aberic.fabric.module.controller;

import cn.aberic.fabric.module.bean.dto.ChainCodeDTO;
import cn.aberic.fabric.module.service.ChainCodeService;
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
@Api(value = "ChainCode Controller", tags = {"区块链网络中链码服务相关接口"})
@CrossOrigin
@RestController
@RequestMapping("chaincode")
public class ChainCodeController {

    @Resource
    private ChainCodeService chainCodeService;

    @ApiOperation(value = "新增链码对象")
    @PostMapping(value = "add")
    public String add(@RequestBody @ApiParam(name = "链码对象", value = "链码对象详情", required = true) ChainCodeDTO chainCode) {
        return chainCodeService.add(chainCode);
    }

    @ApiOperation(value = "更新链码对象")
    @PostMapping(value = "update")
    public String update(@RequestBody @ApiParam(name = "链码对象", value = "链码对象详情", required = true) ChainCodeDTO chainCode) {
        return chainCodeService.update(chainCode);
    }

    @ApiOperation(value = "获取链码对象集合")
    @GetMapping(value = "list/{id}")
    public String list(@ApiParam(name = "链码所属通道id", value = "id值", required = true) @PathVariable("id") int id) {
        return chainCodeService.list(id);
    }


}
