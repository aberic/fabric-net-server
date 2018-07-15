/*
 * Copyright (c) 2018. Aberic - aberic@qq.com - All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.aberic.fabric.utils;

import cn.aberic.fabric.dao.*;
import cn.aberic.fabric.dao.mapper.*;
import cn.aberic.fabric.sdk.FabricManager;
import cn.aberic.fabric.sdk.OrgManager;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/4 10:46】
 */
@Slf4j
public class FabricHelper {

    private static FabricHelper instance;

    private final Map<Integer, FabricManager> channelManagerMap;
    private final Map<String, FabricManager> chaincodeManagerMap;

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
        channelManagerMap = new LinkedHashMap<>();
        chaincodeManagerMap = new LinkedHashMap<>();
    }

    public void removeManager(List<Peer> peers, ChannelMapper channelMapper, ChaincodeMapper chaincodeMapper) {
        for (Peer peer : peers) {
            removeManager(channelMapper.list(peer.getId()), chaincodeMapper);
        }
    }

    public void removeManager(List<Channel> channels, ChaincodeMapper chaincodeMapper) {
        for (Channel channel : channels) {
            removeManager(chaincodeMapper.list(channel.getId()));
        }
    }

    public void removeManager(List<Chaincode> chaincodes) {
        for (Chaincode chaincode : chaincodes) {
            chaincodeManagerMap.remove(chaincode.getCc());
        }
    }

    public void removeManager(String cc) {
        chaincodeManagerMap.remove(cc);
    }

    public FabricManager get(OrgMapper orgMapper, ChannelMapper channelMapper, ChaincodeMapper chaincodeMapper,
                             OrdererMapper ordererMapper, PeerMapper peerMapper, CA ca, String cc) throws Exception {
        // 尝试从缓存中获取fabricManager
        FabricManager fabricManager = chaincodeManagerMap.get(MD5Util.md516(cc + (null != ca ? ca.getFlag() : "")));
        if (null == fabricManager) { // 如果不存在fabricManager则尝试新建一个并放入缓存
            synchronized (chaincodeManagerMap) {
                Chaincode chaincode = chaincodeMapper.getByCC(cc);
                log.debug(String.format("chaincode = %s", chaincode.toString()));
                Channel channel = channelMapper.get(chaincode.getChannelId());
                log.debug(String.format("channel = %s", channel.toString()));
                Peer peer = peerMapper.get(channel.getPeerId());
                log.debug(String.format("peer = %s", peer.toString()));
                int orgId = peer.getOrgId();
                List<Peer> peers = peerMapper.list(orgId);
                List<Orderer> orderers = ordererMapper.list(orgId);
                Org org = orgMapper.get(orgId);
                log.debug(String.format("org = %s", org.toString()));
                if (orderers.size() != 0 && peers.size() != 0) {
                    fabricManager = createFabricManager(org, channel, chaincode, orderers, peers, ca, cc);
                    chaincodeManagerMap.put(MD5Util.md516(cc + ca.getFlag()), fabricManager);
                }
            }
        }
        return fabricManager;
    }

    public FabricManager get(OrgMapper orgMapper, ChannelMapper channelMapper,
                             OrdererMapper ordererMapper, PeerMapper peerMapper, CA ca, int channelId) throws Exception {
        // 尝试从缓存中获取fabricManager
        FabricManager fabricManager = channelManagerMap.get(channelId);
        Channel channel = channelMapper.get(channelId);
        log.debug(String.format("channel = %s", channel.toString()));
        Peer peer = peerMapper.get(channel.getPeerId());
        log.debug(String.format("peer = %s", peer.toString()));
        int orgId = peer.getOrgId();
        List<Peer> peers = peerMapper.list(orgId);
        List<Orderer> orderers = ordererMapper.list(orgId);
        Org org = orgMapper.get(orgId);
        if (null == fabricManager) { // 如果不存在fabricManager则尝试新建一个并放入缓存
            synchronized (channelManagerMap) {
                if (orderers.size() != 0 && peers.size() != 0) {
                    fabricManager = createFabricManager(org, channel, null, orderers, peers, ca, String.valueOf(channelId));
                    channelManagerMap.put(channelId, fabricManager);
                }
            }
        }
        return fabricManager;
    }


    private FabricManager createFabricManager(Org org, Channel channel, Chaincode chaincode, List<Orderer> orderers, List<Peer> peers, CA ca, String cacheName) throws Exception {
        OrgManager orgManager = new OrgManager();
        orgManager
                .init(cacheName, org.isTls())
                .setPeers(org.getName(), org.getMspId(), org.getDomainName())
                .setUser(ca.getName(), ca.getSkPath(), ca.getCertificatePath())
                .setOrderers(org.getOrdererDomainName())
                .setChannel(channel.getName())
                .setChainCode(null == chaincode ? "" : chaincode.getName(),
                        null == chaincode ? "" : chaincode.getPath(),
                        null == chaincode ? "" : chaincode.getSource(),
                        null == chaincode ? "" : chaincode.getPolicy(),
                        null == chaincode ? "" : chaincode.getVersion(),
                        null == chaincode ? 0 : chaincode.getProposalWaitTime())
                .setBlockListener(map -> {
                    log.debug(map.get("code"));
                    log.debug(map.get("data"));
                });
        for (Orderer orderer : orderers) {
            orgManager.addOrderer(orderer.getName(), orderer.getLocation(), orderer.getServerCrtPath());
        }
        for (Peer peer : peers) {
            orgManager.addPeer(peer.getName(), peer.getEventHubName(), peer.getLocation(), peer.getEventHubLocation(), peer.isEventListener(), peer.getServerCrtPath());
        }
        orgManager.add();
        return orgManager.use(cacheName, ca.getName());
    }

}
