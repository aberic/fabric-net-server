package cn.aberic.simple.module.service.impl;

import cn.aberic.simple.module.dto.*;
import cn.aberic.simple.module.manager.SimpleManager;
import cn.aberic.simple.module.mapper.SimpleMapper;
import cn.aberic.simple.module.service.SimpleService;
import cn.aberic.simple.utils.MD5Helper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
    public String invoke(ChainCodeDTO chainCode) {
        return chainCode(chainCode, simpleMapper, ChainCodeIntent.INVOKE);
    }

    @Override
    public String query(ChainCodeDTO chainCode) {
        return chainCode(chainCode, simpleMapper, ChainCodeIntent.QUERY);
    }

    @Override
    public String queryBlockByTransactionID(TraceDTO trace) {
        return trace(trace, simpleMapper, TraceIntent.TRANSACTION);
    }

    @Override
    public String queryBlockByHash(TraceDTO trace) {
        return trace(trace, simpleMapper, TraceIntent.HASH);
    }

    @Override
    public String queryBlockByNumber(TraceDTO trace) {
        return trace(trace, simpleMapper, TraceIntent.NUMBER);
    }

    @Override
    public String queryBlockchainInfo(String hash) {
        return queryBlockchainInfo(simpleMapper, hash);
    }

    @Override
    public int init() {
        OrgDTO org = new OrgDTO();
        org.setOrgName(System.getenv("ORG_NAME"));
        org.setTls(System.getenv("ORG_TLS").equals("true") ? 1 : 0);
        org.setUsername(System.getenv("ORG_USERNAME"));
        org.setCryptoConfigDir(System.getenv("ORG_CRYPTO_CONFIG_DIR"));
        org.setOrgMSPID(System.getenv("ORG_MSP_ID"));
        org.setOrgDomainName(System.getenv("ORG_DOMAIN_NAME"));
        org.setOrdererDomainName(System.getenv("ORG_ORDERER_DOMAIN_NAME"));
        org.setChannelName(System.getenv("ORG_CHANNEL_NAME"));
        org.setChaincodeName(System.getenv("ORG_CHAINCODE_NAME"));
        org.setChaincodePath(System.getenv("ORG_CHAINCODE_PATH"));
        org.setChaincodeVersion(System.getenv("ORG_CHAINCODE_VERSION"));
        org.setProposalWaitTime(Integer.valueOf(System.getenv("ORG_PROPOSAL_WAIT_TIME")));
        org.setInvokeWaitTime(Integer.valueOf(System.getenv("ORG_INVOKE_WAIT_TIME")));

        String hash = MD5Helper.obtain().md532(org.getOrgName() + org.getChaincodeName());
        org.setHash(hash);

        simpleMapper.addOrg(org);

        OrdererDTO orderer = new OrdererDTO();
        orderer.setHash(hash);
        orderer.setName(System.getenv("ORDERER_NAME"));
        orderer.setLocation(System.getenv("ORDERER_LOCATION"));
        simpleMapper.addOrderer(orderer);

        PeerDTO peer = new PeerDTO();
        peer.setHash(hash);
        peer.setPeerName(System.getenv("PEER_NAME"));
        peer.setPeerEventHubName(System.getenv("PEER_EVENT_HUB_NAME"));
        peer.setPeerLocation(System.getenv("PEER_LOCATION"));
        peer.setPeerEventHubLocation(System.getenv("PEER_EVENT_HUB_LOCATION"));
        peer.setEventListener(System.getenv("PEER_IS_EVENT_LISTENER").equals("true") ? 1 : 0);
        simpleMapper.addPeer(peer);

        SimpleManager.obtain().init(hash);
        return 0;
    }

    @Override
    public String addOrg(OrgDTO org) {
        String hash = MD5Helper.obtain().md532(org.getOrgName() + org.getChaincodeName());
        if (null != simpleMapper.getOrgByHash(hash)) {
            return responseFail(String.format("Org already existed with hash %s, you can try updateOrgByHash.", hash));
        }
        org.setHash(hash);
        if (simpleMapper.addOrg(org) > 0) {
            return responseSuccess(org.toString());
        }
        return responseFail("add org fail");
    }

    @Override
    public String addOrderer(OrdererDTO orderer) {
        String hash = MD5Helper.obtain().md532(orderer.toString());
        if (null != simpleMapper.getOrdererByHash(hash)) {
            return responseFail(String.format("Orderer already existed with hash %s, you can try updateOrdererByHash.", hash));
        }
        orderer.setHash(hash);
        if (simpleMapper.addOrderer(orderer) > 0) {
            return responseSuccess(orderer.toString());
        }
        return responseFail("add orderer fail");
    }

    @Override
    public String addPeer(PeerDTO peer) {
        String hash = MD5Helper.obtain().md532(peer.toString());
        if (null != simpleMapper.getPeerByHash(hash)) {
            return responseFail(String.format("Peer already existed with hash %s, you can try updatePeerByHash.", hash));
        }
        peer.setHash(hash);
        if (simpleMapper.addPeer(peer) > 0) {
            return responseSuccess(peer.toString());
        }
        return responseFail("add peer fail");
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

    @Override
    public String updateOrg(OrgDTO org) {
        if (simpleMapper.updateOrgByHash(org) > 0) {
            return responseSuccess(org.toString());
        }
        return responseFail("update org fail");
    }

    @Override
    public String updateOrderer(OrdererDTO orderer) {
        if (simpleMapper.updateOrdererByHash(orderer) > 0) {
            return responseSuccess(orderer.toString());
        }
        return responseFail("update orderer fail");
    }

    @Override
    public String updatePeer(PeerDTO peer) {
        if (simpleMapper.updatePeerByHash(peer) > 0) {
            return responseSuccess(peer.toString());
        }
        return responseFail("update peer fail");
    }
}
