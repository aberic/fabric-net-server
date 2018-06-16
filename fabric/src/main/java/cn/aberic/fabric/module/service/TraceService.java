package cn.aberic.fabric.module.service;

import cn.aberic.fabric.base.BaseService;
import cn.aberic.fabric.module.bean.vo.TraceVO;
import cn.aberic.fabric.module.mapper.*;
import cn.aberic.fabric.utils.FabricHelper;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Hex;
import org.hyperledger.fabric.sdk.aberic.FabricManager;

import java.util.Map;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/4 10:27】
 */
public interface TraceService extends BaseService {

    /**
     * 根据交易ID查询区块
     *
     * @param trace 溯源对象
     *
     * @return 请求返回
     */
    String queryBlockByTransactionID(TraceVO trace);

    /**
     * 根据交易hash查询区块
     *
     * @param trace 溯源对象
     *
     * @return 请求返回
     */
    String queryBlockByHash(TraceVO trace);

    /**
     * 根据交易区块高度查询区块
     *
     * @param trace 溯源对象
     *
     * @return 请求返回
     */
    String queryBlockByNumber(TraceVO trace);

    /**
     * 查询当前链信息
     *
     * @param trace 溯源对象
     *
     * @return 请求返回
     */
    String queryBlockChainInfo(TraceVO trace);

    enum TraceIntent {
        TRANSACTION, HASH, NUMBER, INFO
    }

    default String trace(TraceVO trace, OrgMapper orgMapper, ChannelMapper channelMapper, ChainCodeMapper chainCodeMapper,
                         OrdererMapper ordererMapper, PeerMapper peerMapper, TraceIntent intent) {
        Map<String, String> resultMap = null;
        try {
            FabricManager manager = FabricHelper.obtain().get(orgMapper, channelMapper, chainCodeMapper, ordererMapper, peerMapper,
                    trace.getChainCodeId());
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
