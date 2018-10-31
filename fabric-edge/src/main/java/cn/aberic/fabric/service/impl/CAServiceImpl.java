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

package cn.aberic.fabric.service.impl;

import cn.aberic.fabric.dao.entity.CA;
import cn.aberic.fabric.dao.entity.League;
import cn.aberic.fabric.dao.entity.Org;
import cn.aberic.fabric.dao.entity.Peer;
import cn.aberic.fabric.dao.mapper.*;
import cn.aberic.fabric.service.CAService;
import cn.aberic.fabric.utils.CacheUtil;
import cn.aberic.fabric.utils.DateUtil;
import cn.aberic.fabric.utils.FabricHelper;
import cn.aberic.fabric.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 作者：Aberic on 2018/7/12 21:11
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
@Service("caService")
public class CAServiceImpl implements CAService {

    @Resource
    private LeagueMapper leagueMapper;
    @Resource
    private OrgMapper orgMapper;
    @Resource
    private PeerMapper peerMapper;
    @Resource
    private CAMapper caMapper;
    @Resource
    private ChannelMapper channelMapper;
    @Resource
    private ChaincodeMapper chaincodeMapper;

    @Override
    public int add(CA ca, MultipartFile skFile, MultipartFile certificateFile) {
        if (null == skFile || null == certificateFile) {
            log.debug("ca cert is null");
            return 0;
        }
        if (null != caMapper.check(ca)) {
            log.debug("had the same ca in this peer");
            return 0;
        }
        ca = resetCa(ca);
        try {
            ca.setSk(new String(IOUtils.toByteArray(skFile.getInputStream()), Charset.forName("UTF-8")));
            ca.setCertificate(new String(IOUtils.toByteArray(certificateFile.getInputStream()), Charset.forName("UTF-8")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ca.setDate(DateUtil.getCurrent("yyyy-MM-dd"));
        CacheUtil.removeHome();
        return caMapper.add(ca);
    }

    @Override
    public int update(CA ca, MultipartFile skFile, MultipartFile certificateFile) {
        FabricHelper.obtain().removeChaincodeManager(channelMapper.list(ca.getPeerId()), chaincodeMapper);
        CacheUtil.removeHome();
        CacheUtil.removeFlagCA(ca.getFlag());
        ca = resetCa(ca);
        if (StringUtils.isEmpty(ca.getCertificate()) || StringUtils.isEmpty(ca.getSk())) {
            return caMapper.updateWithNoFile(ca);
        }
        try {
            ca.setSk(new String(IOUtils.toByteArray(skFile.getInputStream())));
            ca.setCertificate(new String(IOUtils.toByteArray(certificateFile.getInputStream()), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return caMapper.update(ca);
    }

    @Override
    public List<CA> listAll() {
        return caMapper.listAll();
    }

    @Override
    public List<CA> listById(int id) {
        return caMapper.list(id);
    }

    @Override
    public CA get(int id) {
        return caMapper.get(id);
    }

    @Override
    public CA getByFlag(String flag) {
        return caMapper.getByFlag(flag);
    }

    @Override
    public int countById(int id) {
        return caMapper.count(id);
    }

    @Override
    public int count() {
        return caMapper.countAll();
    }

    @Override
    public int delete(int id) {
        FabricHelper.obtain().removeChaincodeManager(channelMapper.list(caMapper.get(id).getPeerId()), chaincodeMapper);
        return caMapper.delete(id);
    }

    @Override
    public List<Peer> getFullPeers() {
        List<Peer> peers = peerMapper.listAll();
        for (Peer peer : peers) {
            Org org = orgMapper.get(peer.getOrgId());
            peer.setOrgName(org.getMspId());
            League league = leagueMapper.get(org.getLeagueId());
            peer.setLeagueName(league.getName());
        }
        return peers;
    }

    @Override
    public List<Peer> getPeersByCA(CA ca) {
        Org org = orgMapper.get(peerMapper.get(ca.getPeerId()).getOrgId());
        List<Peer> peers = peerMapper.list(org.getId());
        League league = leagueMapper.get(orgMapper.get(org.getId()).getLeagueId());
        for (Peer peer : peers) {
            peer.setLeagueName(league.getName());
            peer.setOrgName(org.getMspId());
        }
        return peers;
    }

    @Override
    public List<CA> listFullCA() {
        List<CA> cas = caMapper.listAll();
        for (CA ca: cas) {
            Peer peer = peerMapper.get(ca.getPeerId());
            Org org = orgMapper.get(peer.getOrgId());
            ca.setPeerName(peer.getName());
            ca.setOrgName(org.getMspId());
            ca.setLeagueName(leagueMapper.get(org.getLeagueId()).getName());
        }
        return cas;
    }

    private CA resetCa(CA ca) {
        Peer peer = peerMapper.get(ca.getPeerId());
        Org org = orgMapper.get(peer.getOrgId());
        League league = leagueMapper.get(org.getLeagueId());
        // ca.setName(String.format("%s-%s", ca.getName(), ca.getPeerId()));
        ca.setLeagueName(league.getName());
        ca.setOrgName(org.getMspId());
        ca.setPeerName(peer.getName());
        ca.setFlag(MD5Util.md516(league.getName() + org.getMspId() + peer.getName() + ca.getName()));
        return ca;
    }

}
