package cn.aberic.simple.module.manager;

import cn.aberic.simple.module.dto.OrdererDTO;
import cn.aberic.simple.module.dto.OrgDTO;
import cn.aberic.simple.module.dto.PeerDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hyperledger.fabric.sdk.aberic.FabricManager;
import org.hyperledger.fabric.sdk.aberic.OrgManager;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/4 10:46】
 */
public class SimpleManager {

    private Logger logger = LogManager.getLogger(SimpleManager.class);

    private static SimpleManager instance;

    private OrgDTO org;
    private OrdererDTO orderer;
    private PeerDTO peer;
    private FabricManager fabricManager;

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

    public void setOrg(OrgDTO org) {
        this.org = org;
        fabricManager = null;
    }

    public void setOrderer(OrdererDTO orderer) {
        this.orderer = orderer;
        fabricManager = null;
    }

    public void setPeer(PeerDTO peer) {
        this.peer = peer;
        fabricManager = null;
    }

    private SimpleManager() {
    }

    public FabricManager get() throws Exception {
        if (null == fabricManager) {
            synchronized (SimpleManager.class) {
                fabricManager = createFabricManager(org, orderer, peer);
            }
        }
        return fabricManager;
    }

    private FabricManager createFabricManager(OrgDTO org, OrdererDTO orderer, PeerDTO peer) throws Exception {
        OrgManager orgManager = new OrgManager();
        orgManager
                .init(org.getId(), org.isTls(), org.isCaTls())
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
        orgManager.addOrderer(orderer.getName(), orderer.getLocation());
        orgManager.addPeer(peer.getPeerName(), peer.getPeerEventHubName(), peer.getPeerLocation(), peer.getPeerEventHubLocation(), peer.isEventListener());
        orgManager.add();
        return orgManager.use(org.getId());
    }

}
