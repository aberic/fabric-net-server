package cn.aberic.fabric.module.controller;

import cn.aberic.fabric.module.bean.vo.TraceVO;
import cn.aberic.fabric.module.service.TraceService;
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
@Api(value = "Trace Controller", tags = {"区块链网络中数据追溯接口"})
@CrossOrigin
@RestController
@RequestMapping("trace")
public class TraceController {

    @Resource
    private TraceService traceService;

    @ApiOperation(value = "根据交易ID查询区块")
    @PostMapping(value = "txid")
    public String queryBlockByTransactionID(@RequestBody @ApiParam(name = "溯源对象", value = "溯源操作所需转接对象", required = true) TraceVO trace) {
        return traceService.queryBlockByTransactionID(trace);
    }

    @ApiOperation(value = "根据交易hash查询区块")
    @PostMapping(value = "hash")
    public String queryBlockByHash(@RequestBody @ApiParam(name = "溯源对象", value = "溯源操作所需转接对象", required = true) TraceVO trace) {
        return traceService.queryBlockByHash(trace);
    }

    @ApiOperation(value = "根据交易区块高度查询区块")
    @PostMapping(value = "number")
    public String queryBlockByNumber(@RequestBody @ApiParam(name = "溯源对象", value = "溯源操作所需转接对象", required = true) TraceVO trace) {
        return traceService.queryBlockByNumber(trace);
    }

    @ApiOperation(value = "查询当前智能合约上的链信息")
    @GetMapping(value = "info/{id}")
    public String queryBlockChainInfo(@ApiParam(name = "溯源智能合约id", value = "溯源智能合约id", required = true) @PathVariable("id") int id) {
        TraceVO trace = new TraceVO();
        trace.setChainCodeId(id);
        return traceService.queryBlockChainInfo(trace);
    }

}
