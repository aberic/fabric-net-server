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

    public static DeleteUtil obtain(){
        if (null == instance) {
            synchronized (DeleteUtil.class) {
                if (null == instance) {
                    instance = new DeleteUtil();
                }
            }
        }
        return instance;
    }

    public int deleteLeague(int leagueId, LeagueMapper leagueMapper, OrgMapper orgMapper, OrdererMapper ordererMapper, PeerMapper peerMapper, ChannelMapper channelMapper, ChaincodeMapper chaincodeMapper) {
        List<Org> orgs = orgMapper.list(leagueId);
        for (Org org: orgs) {
            if (deleteOrg(org.getId(), orgMapper, ordererMapper, peerMapper, channelMapper, chaincodeMapper) <= 0) {
                return 0;
            }
        }
        return leagueMapper.delete(leagueId);
    }

    public int deleteOrg(int orgId, OrgMapper orgMapper, OrdererMapper ordererMapper, PeerMapper peerMapper, ChannelMapper channelMapper, ChaincodeMapper chaincodeMapper) {
        List<Peer> peers = peerMapper.list(orgId);
        for (Peer peer: peers) {
            if (deletePeer(peer.getId(), peerMapper, channelMapper, chaincodeMapper) <= 0) {
                return 0;
            }
        }
        if (ordererMapper.deleteAll(orgId) <= 0) {
            return 0;
        }
        return orgMapper.delete(orgId);
    }

    public int deletePeer(int peerId, PeerMapper peerMapper, ChannelMapper channelMapper, ChaincodeMapper chaincodeMapper) {
        List<Channel> channels = channelMapper.list(peerId);
        for (Channel channel: channels) {
            if(deleteChannel(channel.getId(), channelMapper, chaincodeMapper) <= 0){
                return 0;
            }
        }
        return peerMapper.delete(peerId);
    }

    public int deleteChannel(int channelId, ChannelMapper channelMapper, ChaincodeMapper chaincodeMapper) {
        if (chaincodeMapper.deleteAll(channelId) <= 0) {
            return 0;
        }
        return channelMapper.delete(channelId);
    }

}
