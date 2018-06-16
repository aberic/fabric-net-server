package cn.aberic.fabric.module.service.impl;

import cn.aberic.fabric.module.bean.vo.TraceVO;
import cn.aberic.fabric.module.mapper.*;
import cn.aberic.fabric.module.service.TraceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/4 15:03】
 */
@Service("traceService")
public class TraceServiceImpl implements TraceService {

    @Resource
    private OrgMapper orgMapper;
    @Resource
    private OrdererMapper ordererMapper;
    @Resource
    private PeerMapper peerMapper;
    @Resource
    private ChannelMapper channelMapper;
    @Resource
    private ChainCodeMapper chainCodeMapper;

    @Override
    public String queryBlockByTransactionID(TraceVO trace) {
        return trace(trace, orgMapper, channelMapper, chainCodeMapper, ordererMapper, peerMapper, TraceIntent.TRANSACTION);
    }

    @Override
    public String queryBlockByHash(TraceVO trace) {
        return trace(trace, orgMapper, channelMapper, chainCodeMapper, ordererMapper, peerMapper, TraceIntent.HASH);
    }

    @Override
    public String queryBlockByNumber(TraceVO trace) {
        return trace(trace, orgMapper, channelMapper, chainCodeMapper, ordererMapper, peerMapper, TraceIntent.NUMBER);
    }

    @Override
    public String queryBlockChainInfo(TraceVO trace) {
        return trace(trace, orgMapper, channelMapper, chainCodeMapper, ordererMapper, peerMapper, TraceIntent.INFO);
    }

}
