/*
 * Copyright (c) 2018. Aberic - All Rights Reserved.
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

import cn.aberic.fabric.dao.Chaincode;
import cn.aberic.fabric.dao.Channel;
import cn.aberic.fabric.dao.Org;
import cn.aberic.fabric.dao.Peer;
import cn.aberic.fabric.dao.mapper.*;

import java.util.List;

/**
 * 作者：Aberic on 2018/7/7 18:18
 * 邮箱：abericyang@gmail.com
 */
public class DeleteUtil {

    private static DeleteUtil instance;

    public static DeleteUtil obtain() {
        if (null == instance) {
            synchronized (DeleteUtil.class) {
                if (null == instance) {
                    instance = new DeleteUtil();
                }
            }
        }
        return instance;
    }

    public int deleteLeague(int leagueId, LeagueMapper leagueMapper, OrgMapper orgMapper,
                            OrdererMapper ordererMapper, PeerMapper peerMapper, CAMapper caMapper,
                            ChannelMapper channelMapper, ChaincodeMapper chaincodeMapper, AppMapper appMapper, BlockMapper blockMapper) {
        List<Org> orgs = orgMapper.list(leagueId);
        if (orgs.size() == 0) {
            CacheUtil.removeHome();
        } else {
            for (Org org : orgs) {
                deleteOrg(org.getId(), orgMapper, ordererMapper, peerMapper, caMapper, channelMapper, chaincodeMapper, appMapper, blockMapper);
            }
        }
        return leagueMapper.delete(leagueId);
    }

    public int deleteOrg(int orgId, OrgMapper orgMapper, OrdererMapper ordererMapper,
                         PeerMapper peerMapper, CAMapper caMapper, ChannelMapper channelMapper,
                         ChaincodeMapper chaincodeMapper, AppMapper appMapper, BlockMapper blockMapper) {
        List<Peer> peers = peerMapper.list(orgId);
        if (peers.size() == 0) {
            CacheUtil.removeHome();
        } else {
            for (Peer peer : peers) {
                deletePeer(peer.getId(), peerMapper, caMapper, channelMapper, chaincodeMapper, appMapper, blockMapper);
            }
        }
        ordererMapper.deleteAll(orgId);
        return orgMapper.delete(orgId);
    }

    public int deletePeer(int peerId, PeerMapper peerMapper, CAMapper caMapper, ChannelMapper channelMapper,
                          ChaincodeMapper chaincodeMapper, AppMapper appMapper, BlockMapper blockMapper) {
        List<Channel> channels = channelMapper.list(peerId);
        if (channels.size() == 0) {
            CacheUtil.removeHome();
        } else {
            for (Channel channel : channels) {
                deleteChannel(channel.getId(), channelMapper, chaincodeMapper, appMapper, blockMapper);
            }
        }
        caMapper.deleteAll(peerId);
        return peerMapper.delete(peerId);
    }

    public int deleteChannel(int channelId, ChannelMapper channelMapper, ChaincodeMapper chaincodeMapper, AppMapper appMapper, BlockMapper blockMapper) {
        blockMapper.deleteByChannelId(channelId);
        List<Chaincode> chaincodes = chaincodeMapper.list(channelId);
        if (chaincodes.size() == 0) {
            FabricHelper.obtain().removeChannelManager(channelId);
            CacheUtil.removeHome();
        } else {
            for (Chaincode chaincode : chaincodes) {
                deleteChaincode(chaincode.getId(), chaincodeMapper, appMapper);
            }
        }
        return channelMapper.delete(channelId);
    }

    public int deleteChaincode(int chaincodeId, ChaincodeMapper chaincodeMapper, AppMapper appMapper) {
        appMapper.deleteAll(chaincodeId);
        FabricHelper.obtain().removeChaincodeManager(chaincodeMapper.get(chaincodeId).getCc());
        CacheUtil.removeHome();
        return chaincodeMapper.delete(chaincodeId);
    }

}
