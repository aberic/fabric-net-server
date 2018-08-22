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

package cn.aberic.fabric.service.impl;

import cn.aberic.fabric.dao.entity.Channel;
import cn.aberic.fabric.dao.entity.Org;
import cn.aberic.fabric.dao.entity.Peer;
import cn.aberic.fabric.dao.mapper.*;
import cn.aberic.fabric.service.ChannelService;
import cn.aberic.fabric.utils.CacheUtil;
import cn.aberic.fabric.utils.DateUtil;
import cn.aberic.fabric.utils.DeleteUtil;
import cn.aberic.fabric.utils.FabricHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service("channelService")
public class ChannelServiceImpl implements ChannelService {

    @Resource
    private LeagueMapper leagueMapper;
    @Resource
    private OrgMapper orgMapper;
    @Resource
    private PeerMapper peerMapper;
    @Resource
    private ChannelMapper channelMapper;
    @Resource
    private ChaincodeMapper chaincodeMapper;
    @Resource
    private AppMapper appMapper;
    @Resource
    private BlockMapper blockMapper;

    @Override
    public int add(Channel channel) {
        if (StringUtils.isEmpty(channel.getName())) {
            log.debug("channel name is empty");
            return 0;
        }
        if (null != channelMapper.check(channel)) {
            log.debug("had the same channel in this peer");
            return 0;
        }
        if (!channel.isBlockListener()) {
            channel.setCallbackLocation("");
        }
        channel.setDate(DateUtil.getCurrent("yyyy-MM-dd"));
        CacheUtil.removeHome();
        return channelMapper.add(channel);
    }

    @Override
    public int update(Channel channel) {
        FabricHelper.obtain().removeChaincodeManager(chaincodeMapper.list(channel.getId()));
        CacheUtil.removeHome();
        blockMapper.deleteByChannelId(channel.getId());
        if (!channel.isBlockListener()) {
            channel.setCallbackLocation("");
        }
        return channelMapper.update(channel);
    }

    @Override
    public int updateHeight(int id, int height) {
        return channelMapper.updateHeight(id, height);
    }

    @Override
    public List<Channel> listAll() {
        List<Channel> channels = channelMapper.listAll();
        for (Channel channel : channels) {
            Peer peer = peerMapper.get(channel.getPeerId());
            Org org = orgMapper.get(peer.getOrgId());
            channel.setLeagueName(leagueMapper.get(org.getLeagueId()).getName());
            channel.setOrgName(org.getMspId());
            channel.setPeerName(peer.getName());
            channel.setChaincodeCount(chaincodeMapper.count(channel.getId()));
        }
        return channels;
    }

    @Override
    public List<Channel> listById(int id) {
        return channelMapper.list(id);
    }

    @Override
    public Channel get(int id) {
        return channelMapper.get(id);
    }

    @Override
    public int countById(int id) {
        return channelMapper.count(id);
    }

    @Override
    public int count() {
        return channelMapper.countAll();
    }

    @Override
    public int delete(int id) {
        return DeleteUtil.obtain().deleteChannel(id, channelMapper, chaincodeMapper, appMapper, blockMapper);
    }
}
