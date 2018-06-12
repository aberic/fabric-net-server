package cn.aberic.simple.module.service;

import cn.aberic.simple.base.BaseService;
import cn.aberic.simple.module.dto.*;
import cn.aberic.simple.module.manager.SimpleManager;
import cn.aberic.simple.module.mapper.SimpleMapper;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Hex;
import org.hyperledger.fabric.sdk.aberic.FabricManager;

import java.util.List;
import java.util.Map;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/4 10:27】
 */
public interface SimpleService extends BaseService {

    /**
     * 执行智能合约
     *
     * @param chainCode 智能合约
     * @return 请求返回
     */
    String invoke(ChainCodeDTO chainCode);

    /**
     * 查询智能合约
     *
     * @param chainCode 智能合约
     * @return 请求返回
     */
    String query(ChainCodeDTO chainCode);

    /**
     * 根据交易ID查询区块
     *
     * @param trace 溯源对象
     * @return 请求返回
     */
    String queryBlockByTransactionID(TraceDTO trace);

    /**
     * 根据交易hash查询区块
     *
     * @param trace 溯源对象
     * @return 请求返回
     */
    String queryBlockByHash(TraceDTO trace);

    /**
     * 根据交易区块高度查询区块
     *
     * @param trace 溯源对象
     * @return 请求返回
     */
    String queryBlockByNumber(TraceDTO trace);

    /**
     * 查询当前链信息
     *
     * @return 请求返回
     */
    String queryBlockchainInfo(String hash);

    /**
     * 通过环境变量来初始化SDK
     *
     * @return 请求返回
     */
    int init();

    /**
     * 新增组织
     *
     * @param org org
     * @return 请求返回
     */
    String addOrg(OrgDTO org);

    /**
     * 新增排序服务
     *
     * @param orderer orderer
     * @return 请求返回
     */
    String addOrderer(OrdererDTO orderer);

    /**
     * 新增节点服务
     *
     * @param peer peer
     * @return 请求返回
     */
    String addPeer(PeerDTO peer);

    /**
     * 获取所有组织列表
     *
     * @return 组织列表
     */
    String getOrgList();

    /**
     * 根据组织hash获取该组织下排序服务列表
     *
     * @param hash 组织hash
     * @return 排序服务列表
     */
    String getOrdererListByOrgHash(String hash);

    /**
     * 根据组织hash获取该组织下节点服务列表
     *
     * @param hash 组织hash
     * @return 节点服务列表
     */
    String getPeerListByOrgHash(String hash);

    /**
     * 根据hash更新org对象
     *
     * @param org org对象
     * @return 请求返回
     */
    String updateOrg(OrgDTO org);

    /**
     * 根据hash更新orderer对象
     *
     * @param orderer orderer对象
     * @return 请求返回
     */
    String updateOrderer(OrdererDTO orderer);

    /**
     * 根据hash更新peer对象
     *
     * @param peer peer对象
     * @return 请求返回
     */
    String updatePeer(PeerDTO peer);

    enum ChainCodeIntent {
        INVOKE, QUERY
    }

    default String chainCode(ChainCodeDTO chainCode, SimpleMapper simpleMapper, ChainCodeIntent intent) {
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
            FabricManager manager = SimpleManager.obtain().get(simpleMapper, chainCode.getHash());
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

    enum TraceIntent {
        TRANSACTION, HASH, NUMBER
    }

    default String trace(TraceDTO trace, SimpleMapper simpleMapper, TraceIntent intent) {
        Map<String, String> resultMap = null;
        try {
            FabricManager manager = SimpleManager.obtain().get(simpleMapper, trace.getHash());
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
            }
            return responseSuccess(JSONObject.parseObject(resultMap.get("data")));
        } catch (Exception e) {
            e.printStackTrace();
            return responseFail(String.format("Request failed： %s", e.getMessage()));
        }
    }

    default String queryBlockchainInfo(SimpleMapper simpleMapper, String hash) {
        try {
            FabricManager manager = SimpleManager.obtain().get(simpleMapper, hash);
            Map<String, String> resultMap = manager.getBlockchainInfo();
            return responseSuccess(JSONObject.parseObject(resultMap.get("data")));
        } catch (Exception e) {
            e.printStackTrace();
            return responseFail(String.format("Request failed： %s", e.getMessage()));
        }
    }

}
