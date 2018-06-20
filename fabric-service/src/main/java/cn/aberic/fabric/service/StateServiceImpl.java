package cn.aberic.fabric.service;

import cn.aberic.fabric.base.BaseService;
import cn.aberic.fabric.mapper.*;
import cn.aberic.fabric.utils.FabricHelper;
import cn.aberic.thrift.state.StateInfo;
import cn.aberic.thrift.state.StateService;
import org.apache.thrift.TException;
import org.hyperledger.fabric.sdk.aberic.FabricManager;
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
public class StateServiceImpl implements StateService.Iface, BaseService {

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
    public String invoke(StateInfo stateInfo) throws TException {
        return chainCode(stateInfo, orgMapper, channelMapper, chaincodeMapper, ordererMapper, peerMapper, ChainCodeIntent.INVOKE);
    }

    @Override
    public String query(StateInfo stateInfo) throws TException {
        return chainCode(stateInfo, orgMapper, channelMapper, chaincodeMapper, ordererMapper, peerMapper, ChainCodeIntent.QUERY);
    }


    enum ChainCodeIntent {
        INVOKE, QUERY
    }

    private String chainCode(StateInfo stateInfo, OrgMapper orgMapper, ChannelMapper channelMapper, ChaincodeMapper chainCodeMapper,
                             OrdererMapper ordererMapper, PeerMapper peerMapper, ChainCodeIntent intent) {
        List<String> array = stateInfo.getStrArray();
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
                    stateInfo.getId());
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
