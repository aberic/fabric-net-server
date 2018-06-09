package cn.aberic.simple.module.service.impl;

import cn.aberic.simple.module.dto.OrdererDTO;
import cn.aberic.simple.module.dto.OrgDTO;
import cn.aberic.simple.module.dto.PeerDTO;
import cn.aberic.simple.module.manager.SimpleManager;
import cn.aberic.simple.module.mapper.SimpleMapper;
import cn.aberic.simple.module.service.SimpleService;
import cn.aberic.simple.utils.MD5Helper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.codec.binary.Hex;
import org.hyperledger.fabric.sdk.aberic.FabricManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/4 15:03】
 */
@Service("simpleService")
public class SimpleServiceImpl implements SimpleService {

    @Resource
    private SimpleMapper simpleMapper;

    @Override
    public String chainCode(JSONObject json) {
        String intent = json.getString("intent");
        JSONArray arrayJson = json.getJSONArray("array");
        int length = arrayJson.size();
        String fcn = null;
        String[] argArray = new String[length - 1];
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                fcn = arrayJson.getString(i);
            } else {
                argArray[i - 1] = arrayJson.getString(i);
            }
        }
        Map<String, String> resultMap;
        try {
            FabricManager manager = SimpleManager.obtain().get(simpleMapper, json.getString("orgHash"));
            switch (intent) {
                case "invoke":
                    resultMap = manager.invoke(fcn, argArray);
                    break;
                case "query":
                    resultMap = manager.query(fcn, argArray);
                    break;
                default:
                    throw new RuntimeException(String.format("no type was found with name %s", intent));
            }
            if (resultMap.get("code").equals("error")) {
                return responseFail(resultMap.get("data"));
            } else {
                return responseSuccess(resultMap.get("data"), resultMap.get("txid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return responseFail(String.format("请求失败： %s", e.getMessage()));
        }
    }

    @Override
    public String trace(JSONObject json) {
        String intent = json.getString("intent");
        String traceId = json.getString("traceId");
        Map<String, String> resultMap;
        try {
            FabricManager manager = SimpleManager.obtain().get(simpleMapper, json.getString("orgHash"));
            switch (intent) {
                case "queryBlockByTransactionID":
                    resultMap = manager.queryBlockByTransactionID(traceId);
                    break;
                case "queryBlockByHash":
                    resultMap = manager.queryBlockByHash(Hex.decodeHex(traceId.toCharArray()));
                    break;
                case "queryBlockByNumber":
                    resultMap = manager.queryBlockByNumber(Long.valueOf(traceId));
                    break;
                case "queryBlockchainInfo":
                    resultMap = manager.getBlockchainInfo();
                    break;
                default:
                    return responseFail("No func found, please check and try again.");
            }
            return responseSuccess(JSONObject.parseObject(resultMap.get("data")));
        } catch (Exception e) {
            e.printStackTrace();
            return responseFail(String.format("请求失败： %s", e.getMessage()));
        }
    }

    @Override
    public String addOrg(JSONObject json) {
        OrgDTO org = JSON.parseObject(json.toJSONString(), new TypeReference<OrgDTO>() {});
        String hash = MD5Helper.obtain().md532(org.getOrgName() + org.getChaincodeName());
        org.setHash(hash);
        if (simpleMapper.addOrg(org) > 0) {
            return responseSuccess(org.toString());
        }
        return responseFail("新增排序服务失败");
    }

    @Override
    public String addOrderer(JSONObject json) {
        OrdererDTO orderer = JSON.parseObject(json.toJSONString(), new TypeReference<OrdererDTO>() {});
        if (simpleMapper.addOrderer(orderer) > 0) {
            return responseSuccess(orderer.toString());
        }
        return responseFail("新增排序服务失败");
    }

    @Override
    public String addPeer(JSONObject json) {
        PeerDTO peer = JSON.parseObject(json.toJSONString(), new TypeReference<PeerDTO>() {});
        if (simpleMapper.addPeer(peer) > 0) {
            return responseSuccess(peer.toString());
        }
        return responseFail("新增节点服务失败");
    }

    @Override
    public String getOrgList() {
        return responseSuccess(JSONArray.parseArray(JSON.toJSONString(simpleMapper.getOrgList())));
    }

    @Override
    public String getOrdererListByOrgHash(String hash) {
        return responseSuccess(JSONArray.parseArray(JSON.toJSONString(simpleMapper.getOrdererListByOrgHash(hash))));
    }

    @Override
    public String getPeerListByOrgHash(String hash) {
        return responseSuccess(JSONArray.parseArray(JSON.toJSONString(simpleMapper.getPeerListByOrgHash(hash))));
    }
}
