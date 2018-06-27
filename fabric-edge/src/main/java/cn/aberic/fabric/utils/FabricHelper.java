package cn.aberic.fabric.utils;

import cn.aberic.fabric.dao.*;
import cn.aberic.fabric.dao.mapper.*;
import cn.aberic.fabric.sdk.FabricManager;
import cn.aberic.fabric.sdk.OrgManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/4 10:46】
 */
public class FabricHelper {

    private Logger logger = LogManager.getLogger(FabricHelper.class);

    /** 当前正在运行的智能合约Id */
    private int chainCodeId;

    private static FabricHelper instance;

    private final Map<Integer, FabricManager> fabricManagerMap;

    public static FabricHelper obtain() {
        if (null == instance) {
            synchronized (FabricHelper.class) {
                if (null == instance) {
                    instance = new FabricHelper();
                }
            }
        }
        return instance;
    }

    private FabricHelper() {
        fabricManagerMap = new HashMap<>();
    }

    public void removeManager(List<Peer> peers, ChannelMapper channelMapper, ChaincodeMapper chaincodeMapper) {
        for (Peer peer : peers) {
            List<Channel> channels = channelMapper.list(peer.getId());
            for (Channel channel : channels) {
                List<Chaincode> chaincodes = chaincodeMapper.list(channel.getId());
                for (Chaincode chaincode : chaincodes) {
                    fabricManagerMap.remove(chaincode.getId());
                }
            }
        }
    }

    public void removeManager(List<Channel> channels, ChaincodeMapper chaincodeMapper) {
        for (Channel channel : channels) {
            List<Chaincode> chaincodes = chaincodeMapper.list(channel.getId());
            for (Chaincode chaincode : chaincodes) {
                fabricManagerMap.remove(chaincode.getId());
            }
        }
    }

    public void removeManager(int chainCodeId) {
        fabricManagerMap.remove(chainCodeId);
    }

    public FabricManager get(OrgMapper orgMapper, ChannelMapper channelMapper, ChaincodeMapper chaincodeMapper,
                             OrdererMapper ordererMapper, PeerMapper peerMapper) throws Exception {
        return get(orgMapper, channelMapper, chaincodeMapper, ordererMapper, peerMapper, -1);
    }

    public FabricManager get(OrgMapper orgMapper, ChannelMapper channelMapper, ChaincodeMapper chaincodeMapper,
                             OrdererMapper ordererMapper, PeerMapper peerMapper, int chaincodeId) throws Exception {
        if (chaincodeId == -1) {
            chaincodeId = this.chainCodeId;
        } else {
            this.chainCodeId = chaincodeId;
        }

        // 尝试从缓存中获取fabricManager
        FabricManager fabricManager = fabricManagerMap.get(chaincodeId);
        if (null == fabricManager) { // 如果不存在fabricManager则尝试新建一个并放入缓存
            synchronized (fabricManagerMap) {
                Chaincode chaincode = chaincodeMapper.get(chaincodeId);
                logger.debug(String.format("chaincode = %s", chaincode.toString()));
                Channel channel = channelMapper.get(chaincode.getChannelId());
                logger.debug(String.format("channel = %s", channel.toString()));
                Peer peer = peerMapper.get(channel.getPeerId());
                logger.debug(String.format("peer = %s", peer.toString()));
                int orgId = peer.getOrgId();
                List<Peer> peers = peerMapper.list(orgId);
                List<Orderer> orderers = ordererMapper.list(orgId);
                Org org = orgMapper.get(orgId);
                logger.debug(String.format("org = %s", org.toString()));
                if (orderers.size() != 0 && peers.size() != 0) {
                    fabricManager = createFabricManager(org, channel, chaincode, orderers, peers);
                    fabricManagerMap.put(chaincodeId, fabricManager);
                }
            }
        }
        return fabricManager;
    }


    private FabricManager createFabricManager(Org org, Channel channel, Chaincode chainCode, List<Orderer> orderers, List<Peer> peers) throws Exception {
        OrgManager orgManager = new OrgManager();
        orgManager
                .init(chainCodeId, org.isTls())
                .setUser(org.getUsername(), org.getCryptoConfigDir())
                .setPeers(org.getName(), org.getMspId(), org.getDomainName())
                .setOrderers(org.getOrdererDomainName())
                .setChannel(channel.getName())
                .setChainCode(chainCode.getName(), chainCode.getPath(), chainCode.getSource(), chainCode.getPolicy(), chainCode.getVersion(), chainCode.getProposalWaitTime(), chainCode.getInvokeWaitTime())
                .setBlockListener(map -> {
                    logger.debug(map.get("code"));
                    logger.debug(map.get("data"));
                });
        for (Orderer orderer : orderers) {
            orgManager.addOrderer(orderer.getName(), orderer.getLocation());
        }
        for (Peer peer : peers) {
            orgManager.addPeer(peer.getName(), peer.getEventHubName(), peer.getLocation(), peer.getEventHubLocation(), peer.isEventListener());
        }
        orgManager.add();
        return orgManager.use(chainCodeId);
    }

}
