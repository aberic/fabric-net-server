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

import cn.aberic.fabric.dao.entity.League;
import cn.aberic.fabric.dao.entity.Org;
import cn.aberic.fabric.dao.entity.Peer;
import cn.aberic.fabric.dao.mapper.*;
import cn.aberic.fabric.service.PeerService;
import cn.aberic.fabric.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service("peerService")
public class PeerServiceImpl implements PeerService {

    @Resource
    private LeagueMapper leagueMapper;
    @Resource
    private OrgMapper orgMapper;
    @Resource
    private OrdererMapper ordererMapper;
    @Resource
    private PeerMapper peerMapper;
    @Resource
    private CAMapper caMapper;
    @Resource
    private ChannelMapper channelMapper;
    @Resource
    private ChaincodeMapper chaincodeMapper;
    @Resource
    private AppMapper appMapper;
    @Resource
    private BlockMapper blockMapper;
    @Resource
    private Environment env;

    @Override
    public int add(Peer peer, MultipartFile serverCrtFile, MultipartFile clientCertFile, MultipartFile clientKeyFile) {
        if (StringUtils.isEmpty(peer.getName()) ||
                StringUtils.isEmpty(peer.getLocation()) ||
                StringUtils.isEmpty(peer.getEventHubLocation())) {
            throw new RuntimeException("peer name and location and eventHubLocation can not be null");
        }
        if (StringUtils.isNotEmpty(serverCrtFile.getOriginalFilename()) &&
                StringUtils.isNotEmpty(clientCertFile.getOriginalFilename()) &&
                StringUtils.isNotEmpty(clientKeyFile.getOriginalFilename())) {
            if (saveFileFail(peer, serverCrtFile, clientCertFile, clientKeyFile)) {
                throw new RuntimeException("tls file save fail");
            }
        }
        peer.setDate(DateUtil.getCurrent("yyyy-MM-dd"));
        CacheUtil.removeHome();
        return peerMapper.add(peer);
    }

    @Override
    public int update(Peer peer, MultipartFile serverCrtFile, MultipartFile clientCertFile, MultipartFile clientKeyFile) {
        if (StringUtils.isEmpty(serverCrtFile.getOriginalFilename()) ||
                StringUtils.isEmpty(clientCertFile.getOriginalFilename()) ||
                StringUtils.isEmpty(clientKeyFile.getOriginalFilename())) {
            FabricHelper.obtain().removeChaincodeManager(channelMapper.list(peer.getId()), chaincodeMapper);
            CacheUtil.removeHome();
            CacheUtil.removeFlagCA(peer.getId(), caMapper);
            return peerMapper.updateWithNoFile(peer);
        }
        if (saveFileFail(peer, serverCrtFile, clientCertFile, clientKeyFile)) {
            throw new RuntimeException("tls file save fail");
        }
        FabricHelper.obtain().removeChaincodeManager(channelMapper.list(peer.getId()), chaincodeMapper);
        CacheUtil.removeHome();
        CacheUtil.removeFlagCA(peer.getId(), caMapper);
        return peerMapper.update(peer);
    }

    @Override
    public List<Peer> listAll() {
        List<Peer> peers = peerMapper.listAll();
        for (Peer peer : peers) {
            Org org = orgMapper.get(peer.getOrgId());
            League league = leagueMapper.get(org.getLeagueId());
            peer.setLeagueName(league.getName());
            peer.setOrgName(org.getMspId());
            peer.setChannelCount(channelMapper.count(peer.getId()));
        }
        return peers;
    }

    @Override
    public List<Peer> listById(int id) {
        return peerMapper.list(id);
    }

    @Override
    public Peer get(int id) {
        return peerMapper.get(id);
    }

    @Override
    public int countById(int id) {
        return peerMapper.count(id);
    }

    @Override
    public int count() {
        return peerMapper.countAll();
    }

    @Override
    public int delete(int id) {
        return DeleteUtil.obtain().deletePeer(id, peerMapper, caMapper, channelMapper, chaincodeMapper, appMapper, blockMapper);
    }

    @Override
    public List<Org> listOrgByOrgId(int orgId) {
        League league = leagueMapper.get(orgMapper.get(orgId).getLeagueId());
        List<Org> orgs = orgMapper.list(league.getId());
        for (Org org : orgs) {
            org.setLeagueName(league.getName());
        }
        return orgs;
    }

    @Override
    public List<Org> getForPeerAndOrderer() {
        List<Org> orgs = new ArrayList<>(orgMapper.listAll());
        for (Org org : orgs) {
            org.setOrdererCount(ordererMapper.count(org.getId()));
            org.setPeerCount(peerMapper.count(org.getId()));
            org.setLeagueName(leagueMapper.get(org.getLeagueId()).getName());
        }
        return orgs;
    }

    @Override
    public Peer resetPeer(Peer peer) {
        Org org = orgMapper.get(peer.getOrgId());
        League league = leagueMapper.get(org.getLeagueId());
        peer.setLeagueName(league.getName());
        peer.setOrgName(org.getMspId());
        return peer;
    }

    private boolean saveFileFail(Peer peer, MultipartFile serverCrtFile, MultipartFile clientCertFile, MultipartFile clientKeyFile) {
        String peerTlsPath = String.format("%s%s%s%s%s%s%s%s",
                env.getProperty("config.dir"),
                File.separator,
                peer.getLeagueName(),
                File.separator,
                peer.getOrgName(),
                File.separator,
                peer.getName(),
                File.separator);
        String serverCrtPath = String.format("%s%s", peerTlsPath, serverCrtFile.getOriginalFilename());
        String clientCertPath = String.format("%s%s", peerTlsPath, clientCertFile.getOriginalFilename());
        String clientKeyPath = String.format("%s%s", peerTlsPath, clientKeyFile.getOriginalFilename());
        peer.setServerCrtPath(serverCrtPath);
        peer.setClientCertPath(clientCertPath);
        peer.setClientKeyPath(clientKeyPath);
        try {
            FileUtil.save(serverCrtFile, clientCertFile, clientKeyFile, serverCrtPath, clientCertPath, clientKeyPath);
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }
}
