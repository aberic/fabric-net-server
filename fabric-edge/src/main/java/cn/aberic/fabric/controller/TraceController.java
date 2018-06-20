package cn.aberic.fabric.controller;

import cn.aberic.fabric.thrift.MultiServiceProvider;
import cn.aberic.thrift.trace.TraceInfo;
import org.apache.thrift.TException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/4 15:01】
 */
@CrossOrigin
@RestController
@RequestMapping("trace")
public class TraceController {

    @Resource
    private MultiServiceProvider multiService;

    @PostMapping(value = "txid")
    public String queryBlockByTransactionID(@RequestBody TraceInfo trace) {
        try {
            return multiService.getTraceService().queryBlockByTransactionID(trace);
        } catch (TException e) {
            e.printStackTrace();
        }
        return "";
    }

    @PostMapping(value = "hash")
    public String queryBlockByHash(@RequestBody TraceInfo trace) {
        try {
            return multiService.getTraceService().queryBlockByHash(trace);
        } catch (TException e) {
            e.printStackTrace();
        }
        return "";
    }

    @PostMapping(value = "number")
    public String queryBlockByNumber(@RequestBody TraceInfo trace) {
        try {
            return multiService.getTraceService().queryBlockByNumber(trace);
        } catch (TException e) {
            e.printStackTrace();
        }
        return "";
    }

    @GetMapping(value = "info/{id}")
    public String queryBlockChainInfo(@PathVariable("id") int id) {
        try {
            return multiService.getTraceService().queryBlockChainInfo(id);
        } catch (TException e) {
            e.printStackTrace();
        }
        return "";
    }

}
