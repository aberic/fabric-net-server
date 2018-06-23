package cn.aberic.fabric.utils;

import cn.aberic.fabric.mapper.*;
import cn.aberic.thrift.chaincode.ChaincodeInfo;
import cn.aberic.thrift.channel.ChannelInfo;
import cn.aberic.thrift.orderer.OrdererInfo;
import cn.aberic.thrift.org.OrgInfo;
import cn.aberic.thrift.peer.PeerInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hyperledger.fabric.sdk.aberic.FabricManager;
import org.hyperledger.fabric.sdk.aberic.OrgManager;

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

    public void removeManager(List<PeerInfo> peers, ChannelMapper channelMapper, ChaincodeMapper chaincodeMapper) {
        for (PeerInfo peer : peers) {
            List<ChannelInfo> channels = channelMapper.list(peer.getId());
            for (ChannelInfo channel : channels) {
                List<ChaincodeInfo> chaincodes = chaincodeMapper.list(channel.getId());
                for (ChaincodeInfo chaincode : chaincodes) {
                    fabricManagerMap.remove(chaincode.getId());
                }
            }
        }
    }

    public void removeManager(List<ChannelInfo> channels, ChaincodeMapper chaincodeMapper) {
        for (ChannelInfo channel : channels) {
            List<ChaincodeInfo> chaincodes = chaincodeMapper.list(channel.getId());
            for (ChaincodeInfo chaincode : chaincodes) {
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
                ChaincodeInfo chaincode = chaincodeMapper.get(chaincodeId);
                logger.debug(String.format("chaincode = %s", chaincode.toString()));
                ChannelInfo channel = channelMapper.get(chaincode.getChannelId());
                logger.debug(String.format("channel = %s", channel.toString()));
                PeerInfo peer = peerMapper.get(channel.getPeerId());
                logger.debug(String.format("peer = %s", peer.toString()));
                int orgId = peer.getOrgId();
                List<PeerInfo> peers = peerMapper.list(orgId);
                List<OrdererInfo> orderers = ordererMapper.list(orgId);
                OrgInfo org = orgMapper.get(orgId);
                logger.debug(String.format("org = %s", org.toString()));
                if (orderers.size() != 0 && peers.size() != 0) {
                    fabricManager = createFabricManager(org, channel, chaincode, orderers, peers);
                    fabricManagerMap.put(chaincodeId, fabricManager);
                }
            }
        }
        return fabricManager;
    }


    private FabricManager createFabricManager(OrgInfo org, ChannelInfo channel, ChaincodeInfo chainCode, List<OrdererInfo> orderers, List<PeerInfo> peers) throws Exception {
        OrgManager orgManager = new OrgManager();
        orgManager
                .init(chainCodeId, org.isTls())
                .setUser(org.getUsername(), org.getCryptoConfigDir())
                .setPeers(org.getName(), org.getMspId(), org.getDomainName())
                .setOrderers(org.getOrdererDomainName())
                .setChannel(channel.getName())
                .setChainCode(chainCode.getName(), chainCode.getPath(), chainCode.getVersion(), chainCode.getProposalWaitTime(), chainCode.getInvokeWaitTime())
                .setBlockListener(map -> {
                    logger.debug(map.get("code"));
                    logger.debug(map.get("data"));
                });
        for (OrdererInfo orderer : orderers) {
            orgManager.addOrderer(orderer.getName(), orderer.getLocation());
        }
        for (PeerInfo peer : peers) {
            orgManager.addPeer(peer.getName(), peer.getEventHubName(), peer.getLocation(), peer.getEventHubLocation(), peer.isEventListener);
        }
        orgManager.add();
        return orgManager.use(chainCodeId);
    }

}
