package cn.aberic.fabric.module.controller;

import cn.aberic.fabric.module.bean.dto.OrgDTO;
import cn.aberic.fabric.module.service.OrgService;
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
@Api(value = "Org Controller", tags = {"区块链网络中组织相关接口"})
@CrossOrigin
@RestController
@RequestMapping("org")
public class OrgController {

    @Resource
    private OrgService orgService;

    @ApiOperation(value = "新增组织对象")
    @PostMapping(value = "add")
    public String add(@RequestBody @ApiParam(name = "组织对象", value = "组织对象详情", required = true) OrgDTO org) {
        return orgService.add(org);
    }

    @ApiOperation(value = "更新组织对象")
    @PostMapping(value = "update")
    public String update(@RequestBody @ApiParam(name = "组织对象", value = "组织对象详情", required = true) OrgDTO org) {
        return orgService.update(org);
    }

    @ApiOperation(value = "根据联盟id获取组织对象集合")
    @GetMapping(value = "list/{id}")
    public String list(@ApiParam(name = "联盟id", value = "id值", required = true) @PathVariable("id") int id) {
        return orgService.list(id);
    }

    @ApiOperation(value = "根据组织对象id获取组织对象")
    @GetMapping(value = "get/{id}")
    public String get(@ApiParam(name = "组织对象id", value = "id值", required = true) @PathVariable("id") int id) {
        return orgService.get(id);
    }

}
