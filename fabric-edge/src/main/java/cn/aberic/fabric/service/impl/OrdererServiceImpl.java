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
import cn.aberic.fabric.dao.Orderer;
import cn.aberic.fabric.dao.Org;
import cn.aberic.fabric.dao.mapper.*;
import cn.aberic.fabric.service.OrdererService;
import cn.aberic.fabric.utils.CacheUtil;
import cn.aberic.fabric.utils.DateUtil;
import cn.aberic.fabric.utils.FabricHelper;
import cn.aberic.fabric.utils.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service("ordererService")
public class OrdererServiceImpl implements OrdererService {

    @Resource
    private LeagueMapper leagueMapper;
    @Resource
    private OrgMapper orgMapper;
    @Resource
    private OrdererMapper ordererMapper;
    @Resource
    private PeerMapper peerMapper;
    @Resource
    private ChannelMapper channelMapper;
    @Resource
    private ChaincodeMapper chaincodeMapper;
    @Resource
    private Environment env;

    @Override
    public int add(Orderer orderer, MultipartFile serverCrtFile, MultipartFile clientCertFile, MultipartFile clientKeyFile) {
        if (StringUtils.isEmpty(orderer.getName()) ||
                StringUtils.isEmpty(orderer.getLocation())) {
            return 0;
        }
        if (StringUtils.isNotEmpty(serverCrtFile.getOriginalFilename()) &&
                StringUtils.isNotEmpty(clientCertFile.getOriginalFilename()) &&
                StringUtils.isNotEmpty(clientKeyFile.getOriginalFilename())) {
            if (saveFileFail(orderer, serverCrtFile, clientCertFile, clientKeyFile)) {
                return 0;
            }
        }
        orderer.setDate(DateUtil.getCurrent("yyyy-MM-dd"));
        CacheUtil.removeHome();
        return ordererMapper.add(orderer);
    }

    @Override
    public int update(Orderer orderer, MultipartFile serverCrtFile, MultipartFile clientCertFile, MultipartFile clientKeyFile) {
        FabricHelper.obtain().removeChaincodeManager(peerMapper.list(orderer.getOrgId()), channelMapper, chaincodeMapper);
        CacheUtil.removeHome();
        if (null == serverCrtFile || null == clientCertFile || null == clientKeyFile) {
            return ordererMapper.updateWithNoFile(orderer);
        }
        if (saveFileFail(orderer, serverCrtFile, clientCertFile, clientKeyFile)) {
            return 0;
        }
        return ordererMapper.update(orderer);
    }

    @Override
    public List<Orderer> listAll() {
        List<Orderer> orderers = ordererMapper.listAll();
        for (Orderer orderer : orderers) {
            Org org = orgMapper.get(orderer.getOrgId());
            orderer.setLeagueName(leagueMapper.get(org.getLeagueId()).getName());
            orderer.setOrgName(org.getMspId());
        }
        return orderers;
    }

    @Override
    public List<Orderer> listById(int id) {
        return ordererMapper.list(id);
    }

    @Override
    public Orderer get(int id) {
        return ordererMapper.get(id);
    }

    @Override
    public int countById(int id) {
        return ordererMapper.count(id);
    }

    @Override
    public int count() {
        return ordererMapper.countAll();
    }

    @Override
    public int delete(int id) {
        CacheUtil.removeHome();
        return ordererMapper.delete(id);
    }

    @Override
    public List<Org> listOrgById(int orgId) {
        League league = leagueMapper.get(orgMapper.get(orgId).getLeagueId());
        List<Org> orgs = orgMapper.list(league.getId());
        for (Org org : orgs) {
            org.setLeagueName(league.getName());
        }
        return orgs;
    }

    @Override
    public List<Org> listAllOrg() {
        List<Org> orgs = new ArrayList<>(orgMapper.listAll());
        for (Org org : orgs) {
            org.setLeagueName(leagueMapper.get(org.getLeagueId()).getName());
        }
        return orgs;
    }

    @Override
    public Orderer resetOrderer(Orderer orderer) {
        Org org = orgMapper.get(orderer.getOrgId());
        League league = leagueMapper.get(org.getLeagueId());
        orderer.setLeagueName(league.getName());
        orderer.setOrgName(org.getMspId());
        return orderer;
    }

    private boolean saveFileFail(Orderer orderer, MultipartFile serverCrtFile, MultipartFile clientCertFile, MultipartFile clientKeyFile) {
        String ordererTlsPath = String.format("%s%s%s%s%s%s%s%s",
                env.getProperty("config.dir"),
                File.separator,
                orderer.getLeagueName(),
                File.separator,
                orderer.getOrgName(),
                File.separator,
                orderer.getName(),
                File.separator);
        String serverCrtPath = String.format("%s%s", ordererTlsPath, serverCrtFile.getOriginalFilename());
        String clientCertPath = String.format("%s%s", ordererTlsPath, clientCertFile.getOriginalFilename());
        String clientKeyPath = String.format("%s%s", ordererTlsPath, clientKeyFile.getOriginalFilename());
        orderer.setServerCrtPath(serverCrtPath);
        orderer.setClientCertPath(clientCertPath);
        orderer.setClientKeyPath(clientKeyPath);
        try {
            FileUtil.save(serverCrtFile, clientCertFile, clientKeyFile, serverCrtPath, clientCertPath, clientKeyPath);
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

}
