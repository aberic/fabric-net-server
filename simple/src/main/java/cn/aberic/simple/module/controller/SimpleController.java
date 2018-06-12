package cn.aberic.simple.module.controller;

import cn.aberic.simple.module.dto.*;
import cn.aberic.simple.module.service.SimpleService;
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
@Api(value = "Fabric SDK 入口 API", tags = {"fabric-sdk-api"})
@RestController
@RequestMapping("sdk")
public class SimpleController {

    @Resource
    private SimpleService simpleService;

    @ApiOperation(value = "执行智能合约")
    @PostMapping(value = "chaincode/invoke")
    public String invoke(@RequestBody @ApiParam(name = "合约对象", value = "操作合约所需转接对象", required = true) ChainCodeDTO chainCode) {
        return simpleService.invoke(chainCode);
    }

    @ApiOperation(value = "查询智能合约")
    @PostMapping(value = "chaincode/query")
    public String query(@RequestBody @ApiParam(name = "合约对象", value = "操作合约所需转接对象", required = true) ChainCodeDTO chainCode) {
        return simpleService.query(chainCode);
    }

    @ApiOperation(value = "根据交易ID查询区块")
    @PostMapping(value = "trace/txid")
    public String queryBlockByTransactionID(@RequestBody @ApiParam(name = "溯源对象", value = "溯源操作所需转接对象", required = true) TraceDTO trace) {
        return simpleService.queryBlockByTransactionID(trace);
    }

    @ApiOperation(value = "根据交易hash查询区块")
    @PostMapping(value = "trace/hash")
    public String queryBlockByHash(@RequestBody @ApiParam(name = "溯源对象", value = "溯源操作所需转接对象", required = true) TraceDTO trace) {
        return simpleService.queryBlockByHash(trace);
    }

    @ApiOperation(value = "根据交易区块高度查询区块")
    @PostMapping(value = "trace/number")
    public String queryBlockByNumber(@RequestBody @ApiParam(name = "溯源对象", value = "溯源操作所需转接对象", required = true) TraceDTO trace) {
        return simpleService.queryBlockByNumber(trace);
    }

    @ApiOperation(value = "查询当前链信息")
    @GetMapping(value = "trace/info/{hash}")
    public String queryBlockchainInfo(@ApiParam(name = "当前组织Hash", value = "Hash值", required = true) @PathVariable("hash") String hash) {
        return simpleService.queryBlockchainInfo(hash);
    }

    @ApiOperation(value = "初始化环境变量数据，一般不允许被调用")
    @GetMapping(value = "init")
    public int init() {
        return simpleService.init();
    }

    @ApiOperation(value = "新增组织对象")
    @PostMapping(value = "org/add")
    public String addOrg(@RequestBody @ApiParam(name = "组织对象", value = "组织对象详情", required = true) OrgDTO org) {
        return simpleService.addOrg(org);
    }

    @ApiOperation(value = "更新组织对象")
    @PostMapping(value = "org/update")
    public String updateOrg(@RequestBody @ApiParam(name = "组织对象", value = "组织对象详情", required = true) OrgDTO org) {
        return simpleService.updateOrg(org);
    }

    @ApiOperation(value = "获取组织对象集合")
    @GetMapping(value = "org/list")
    public String orgList() {
        return simpleService.getOrgList();
    }

    @ApiOperation(value = "新增排序服务对象")
    @PostMapping(value = "orderer/add")
    public String addOrderer(@RequestBody @ApiParam(name = "排序服务对象", value = "排序服务对象信息", required = true) OrdererDTO orderer) {
        return simpleService.addOrderer(orderer);
    }

    @ApiOperation(value = "更新排序服务对象")
    @PostMapping(value = "orderer/update")
    public String updateOrderer(@RequestBody @ApiParam(name = "排序服务对象", value = "排序服务对象信息", required = true) OrdererDTO orderer) {
        return simpleService.updateOrderer(orderer);
    }

    @ApiOperation(value = "获取排序服务对象集合")
    @GetMapping(value = "orderer/list/{hash}")
    public String ordererList(@ApiParam(name = "当前组织Hash", value = "Hash值", required = true) @PathVariable("hash") String hash) {
        return simpleService.getOrdererListByOrgHash(hash);
    }

    @ApiOperation(value = "新增节点服务对象")
    @PostMapping(value = "peer/add")
    public String addPeer(@RequestBody @ApiParam(name = "节点服务对象", value = "节点服务对象信息", required = true) PeerDTO peer) {
        return simpleService.addPeer(peer);
    }

    @ApiOperation(value = "更新节点服务对象")
    @PostMapping(value = "peer/update")
    public String updatePeer(@RequestBody @ApiParam(name = "节点服务对象", value = "节点服务对象信息", required = true) PeerDTO peer) {
        return simpleService.updatePeer(peer);
    }

    @ApiOperation(value = "获取节点服务对象集合")
    @GetMapping(value = "peer/list/{hash}")
    public String peerList(@ApiParam(name = "当前组织Hash", value = "Hash值", required = true) @PathVariable("hash") String hash) {
        return simpleService.getPeerListByOrgHash(hash);
    }

}
