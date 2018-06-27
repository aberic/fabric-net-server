package cn.aberic.fabric.service.impl;

import cn.aberic.fabric.base.BaseService;
import cn.aberic.fabric.bean.State;
import cn.aberic.fabric.dao.mapper.*;
import cn.aberic.fabric.sdk.FabricManager;
import cn.aberic.fabric.service.StateService;
import cn.aberic.fabric.utils.FabricHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/4 15:03】
 */
@Service("stateService")
public class StateServiceImpl implements StateService, BaseService {

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
    public String invoke(State state) {
        return chainCode(state, orgMapper, channelMapper, chaincodeMapper, ordererMapper, peerMapper, ChainCodeIntent.INVOKE);
    }

    @Override
    public String query(State state) {
        return chainCode(state, orgMapper, channelMapper, chaincodeMapper, ordererMapper, peerMapper, ChainCodeIntent.QUERY);
    }


    enum ChainCodeIntent {
        INVOKE, QUERY
    }

    private String chainCode(State state, OrgMapper orgMapper, ChannelMapper channelMapper, ChaincodeMapper chainCodeMapper,
                             OrdererMapper ordererMapper, PeerMapper peerMapper, ChainCodeIntent intent) {
        List<String> array = state.getStrArray();
        int length = array.size();
        String fcn = null;
        String[] argArray = new String[length - 1];
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                fcn = array.get(i);
            } else {
                argArray[i - 1] = array.get(i);
            }
        }
        Map<String, String> resultMap = null;
        try {
            FabricManager manager = FabricHelper.obtain().get(orgMapper, channelMapper, chainCodeMapper, ordererMapper, peerMapper,
                    state.getId());
            switch (intent) {
                case INVOKE:
                    resultMap = manager.invoke(fcn, argArray);
                    break;
                case QUERY:
                    resultMap = manager.query(fcn, argArray);
                    break;
            }
            if (resultMap.get("code").equals("error")) {
                return responseFail(resultMap.get("data"));
            } else {
                return responseSuccess(resultMap.get("data"), resultMap.get("txid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return responseFail(String.format("Request failed： %s", e.getMessage()));
        }
    }

}
