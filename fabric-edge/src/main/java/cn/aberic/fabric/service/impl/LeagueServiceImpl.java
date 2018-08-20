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

import cn.aberic.fabric.dao.League;
import cn.aberic.fabric.dao.mapper.*;
import cn.aberic.fabric.service.LeagueService;
import cn.aberic.fabric.utils.CacheUtil;
import cn.aberic.fabric.utils.DateUtil;
import cn.aberic.fabric.utils.DeleteUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service("leagueService")
public class LeagueServiceImpl implements LeagueService {

    @Resource
    private LeagueMapper leagueMapper;
    @Resource
    private OrgMapper orgMapper;
    @Resource
    private PeerMapper peerMapper;
    @Resource
    private CAMapper caMapper;
    @Resource
    private OrdererMapper ordererMapper;
    @Resource
    private ChannelMapper channelMapper;
    @Resource
    private ChaincodeMapper chaincodeMapper;
    @Resource
    private AppMapper appMapper;
    @Resource
    private BlockMapper blockMapper;

    @Override
    public int add(League leagueInfo) {
        if (StringUtils.isEmpty(leagueInfo.getName())) {
            return 0;
        }
        leagueInfo.setDate(DateUtil.getCurrent("yyyy-MM-dd"));
        CacheUtil.removeHome();
        return leagueMapper.add(leagueInfo);
    }

    @Override
    public int update(League leagueInfo) {
        CacheUtil.removeFlagCA(leagueInfo.getId(), peerMapper, caMapper);
        CacheUtil.removeHome();
        return leagueMapper.update(leagueInfo);
    }

    @Override
    public List<League> listAll() {
        return leagueMapper.listAll();
    }

    @Override
    public League get(int id) {
        return leagueMapper.get(id);
    }

    @Override
    public int delete(int id) {
        return DeleteUtil.obtain().deleteLeague(id, leagueMapper, orgMapper, ordererMapper,
                peerMapper, caMapper, channelMapper, chaincodeMapper, appMapper, blockMapper);
    }

}
