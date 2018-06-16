package cn.aberic.fabric.module.service;

import cn.aberic.fabric.base.BaseService;
import cn.aberic.fabric.module.bean.vo.StateVO;
import cn.aberic.fabric.module.mapper.*;
import cn.aberic.fabric.utils.FabricHelper;
import org.hyperledger.fabric.sdk.aberic.FabricManager;

import java.util.List;
import java.util.Map;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/4 10:27】
 */
public interface StateService extends BaseService {

    /**
     * 执行智能合约
     *
     * @param chainCode 智能合约
     *
     * @return 请求返回
     */
    String invoke(StateVO chainCode);

    /**
     * 查询智能合约
     *
     * @param chainCode 智能合约
     *
     * @return 请求返回
     */
    String query(StateVO chainCode);

    enum ChainCodeIntent {
        INVOKE, QUERY
    }

    default String chainCode(StateVO chainCode, OrgMapper orgMapper, ChannelMapper channelMapper, ChainCodeMapper chainCodeMapper,
                             OrdererMapper ordererMapper, PeerMapper peerMapper, ChainCodeIntent intent) {
        List<String> array = chainCode.getArgs();
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
                    chainCode.getId());
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
