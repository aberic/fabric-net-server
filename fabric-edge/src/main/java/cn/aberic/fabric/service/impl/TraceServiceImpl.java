package cn.aberic.fabric.service.impl;

import cn.aberic.fabric.base.BaseService;
import cn.aberic.fabric.bean.Trace;
import cn.aberic.fabric.dao.mapper.*;
import cn.aberic.fabric.sdk.FabricManager;
import cn.aberic.fabric.service.TraceService;
import cn.aberic.fabric.utils.FabricHelper;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/4 15:03】
 */
@Service("traceService")
public class TraceServiceImpl implements TraceService, BaseService {

    @Resource
    private OrgMapper orgMapper;
    @Resource
    private OrdererMapper ordererMapper;
    @Resource
    private PeerMapper peerMapper;
    @Resource
    private ChannelMapper channelMapper;
    @Resource
    private ChaincodeMapper chaincodeMapper;

    @Override
    public String queryBlockByTransactionID(Trace trace) {
        return trace(trace, orgMapper, channelMapper, chaincodeMapper, ordererMapper, peerMapper, TraceIntent.TRANSACTION);
    }

    @Override
    public String queryBlockByHash(Trace trace) {
        return trace(trace, orgMapper, channelMapper, chaincodeMapper, ordererMapper, peerMapper, TraceIntent.HASH);
    }

    @Override
    public String queryBlockByNumber(Trace trace) {
        return trace(trace, orgMapper, channelMapper, chaincodeMapper, ordererMapper, peerMapper, TraceIntent.NUMBER);
    }

    @Override
    public String queryBlockChainInfo(int id) {
        Trace trace = new Trace();
        trace.setId(id);
        return trace(trace, orgMapper, channelMapper, chaincodeMapper, ordererMapper, peerMapper, TraceIntent.INFO);
    }

    enum TraceIntent {
        TRANSACTION, HASH, NUMBER, INFO
    }

    private String trace(Trace trace, OrgMapper orgMapper, ChannelMapper channelMapper, ChaincodeMapper chaincodeMapper,
                         OrdererMapper ordererMapper, PeerMapper peerMapper, TraceIntent intent) {
        Map<String, String> resultMap = null;
        try {
            FabricManager manager = FabricHelper.obtain().get(orgMapper, channelMapper, chaincodeMapper, ordererMapper, peerMapper,
                    trace.getId());
            switch (intent) {
                case TRANSACTION:
                    resultMap = manager.queryBlockByTransactionID(trace.getTrace());
                    break;
                case HASH:
                    resultMap = manager.queryBlockByHash(Hex.decodeHex(trace.getTrace().toCharArray()));
                    break;
                case NUMBER:
                    resultMap = manager.queryBlockByNumber(Long.valueOf(trace.getTrace()));
                    break;
                case INFO:
                    resultMap = manager.getBlockchainInfo();
                    break;
            }
            return responseSuccess(JSONObject.parseObject(resultMap.get("data")));
        } catch (Exception e) {
            e.printStackTrace();
            return responseFail(String.format("Request failed： %s", e.getMessage()));
        }
    }
}
