package cn.aberic.fabric.module.service.impl;

import cn.aberic.fabric.module.bean.vo.StateVO;
import cn.aberic.fabric.module.mapper.*;
import cn.aberic.fabric.module.service.StateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/4 15:03】
 */
@Service("stateService")
public class StateServiceImpl implements StateService {

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
    public String invoke(StateVO chainCode) {
        return chainCode(chainCode, orgMapper, channelMapper, chainCodeMapper, ordererMapper, peerMapper, ChainCodeIntent.INVOKE);
    }

    @Override
    public String query(StateVO chainCode) {
        return chainCode(chainCode, orgMapper, channelMapper, chainCodeMapper, ordererMapper, peerMapper, ChainCodeIntent.QUERY);
    }

}
