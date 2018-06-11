package cn.aberic.simple.module.manager;

import cn.aberic.simple.module.dto.OrdererDTO;
import cn.aberic.simple.module.dto.OrgDTO;
import cn.aberic.simple.module.dto.PeerDTO;
import cn.aberic.simple.module.mapper.SimpleMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hyperledger.fabric.sdk.aberic.FabricManager;
import org.hyperledger.fabric.sdk.aberic.OrgManager;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/4 10:46】
 */
public class SimpleManager {

    private Logger logger = LogManager.getLogger(SimpleManager.class);

    /** 当前正在运行的组织hash */
    private String hash;

    private static SimpleManager instance;

    private final Map<String, FabricManager> fabricManagerMap;

    public static SimpleManager obtain() {
        if (null == instance) {
            synchronized (SimpleManager.class) {
                if (null == instance) {
                    instance = new SimpleManager();
                }
            }
        }
        return instance;
    }

    private SimpleManager() {
        fabricManagerMap = new HashMap<>();
    }

    public void init(String hash) {
        this.hash = hash;
    }

    public FabricManager get(SimpleMapper simpleMapper, String hash) throws Exception {
        if (StringUtils.isEmpty(hash)) {
            hash = this.hash;
        }
        // 尝试从缓存中获取fabricManager
        FabricManager fabricManager = fabricManagerMap.get(hash);
        if (null == fabricManager) { // 如果不存在fabricManager则尝试新建一个并放入缓存
            synchronized (fabricManagerMap) {
                OrgDTO org = simpleMapper.getOrgByHash(hash);
                if (null != org) {
                    fabricManager = createFabricManager(org, simpleMapper.getOrdererListByOrgHash(hash), simpleMapper.getPeerListByOrgHash(hash));
                    fabricManagerMap.put(hash, fabricManager);
                }
            }
        }
        return fabricManager;
    }


    private FabricManager createFabricManager(OrgDTO org, List<OrdererDTO> orderers, List<PeerDTO> peers) throws Exception {
        OrgManager orgManager = new OrgManager();
        orgManager
                .init(org.getHash(), org.isTls() == 1, org.isCaTls() == 1)
                .setUser(org.getUsername(), org.getCryptoConfigDir(), org.getChannelArtifactsDir())
                .setCA(org.getCaName(), org.getCaLocation())
                .setPeers(org.getOrgName(), org.getOrgMSPID(), org.getOrgDomainName())
                .setOrderers(org.getOrdererDomainName())
                .setChannel(org.getChannelName())
                .setChainCode(org.getChaincodeName(), org.getChaincodeSource(), org.getChaincodePath(), org.getChaincodeVersion(), org.getProposalWaitTime(), org.getInvokeWaitTime())
                .setBlockListener(map -> {
                    logger.debug(map.get("code"));
                    logger.debug(map.get("data"));
                });
        for (OrdererDTO orderer : orderers) {
            orgManager.addOrderer(orderer.getName(), orderer.getLocation());
        }
        for (PeerDTO peer : peers) {
            orgManager.addPeer(peer.getPeerName(), peer.getPeerEventHubName(), peer.getPeerLocation(), peer.getPeerEventHubLocation(), peer.isEventListener() == 1);
        }
        orgManager.add();
        return orgManager.use(org.getHash());
    }

}
