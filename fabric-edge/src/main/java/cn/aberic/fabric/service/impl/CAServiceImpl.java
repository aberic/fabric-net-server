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

import cn.aberic.fabric.dao.CA;
import cn.aberic.fabric.dao.League;
import cn.aberic.fabric.dao.Org;
import cn.aberic.fabric.dao.Peer;
import cn.aberic.fabric.dao.mapper.*;
import cn.aberic.fabric.service.CAService;
import cn.aberic.fabric.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
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
    @Resource
    private Environment env;

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
        if (saveFileFail(ca, skFile, certificateFile)) {
            return 0;
        }
        ca = resetCa(ca);
        ca.setDate(DateUtil.getCurrent("yyyy-MM-dd"));
        return caMapper.add(ca);
    }

    @Override
    public int update(CA ca, MultipartFile skFile, MultipartFile certificateFile) {
        FabricHelper.obtain().removeManager(channelMapper.list(ca.getPeerId()), chaincodeMapper);
        CacheUtil.removeFlagCA(ca.getFlag());
        ca = resetCa(ca);
        if (StringUtils.isEmpty(ca.getCertificatePath()) || StringUtils.isEmpty(ca.getSkPath())) {
            return caMapper.updateWithNoFile(ca);
        }
        if (saveFileFail(ca, skFile, certificateFile)) {
            return 0;
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
        FabricHelper.obtain().removeManager(channelMapper.list(caMapper.get(id).getPeerId()), chaincodeMapper);
        return caMapper.delete(id);
    }

    private boolean saveFileFail(CA ca, MultipartFile skFile, MultipartFile certificateFile) {
        String caPath = String.format("%s%s%s%s%s%s%s%s%s%s",
                env.getProperty("config.dir"),
                File.separator,
                ca.getLeagueName(),
                File.separator,
                ca.getOrgName(),
                File.separator,
                ca.getPeerName(),
                File.separator,
                ca.getName(),
                File.separator);
        String skPath = String.format("%s%s", caPath, skFile.getOriginalFilename());
        String certificatePath = String.format("%s%s", caPath, certificateFile.getOriginalFilename());
        ca.setSkPath(skPath);
        ca.setCertificatePath(certificatePath);
        try {
            FileUtil.save(skFile, certificateFile, skPath, certificatePath);
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

    private CA resetCa(CA ca) {
        Peer peer = peerMapper.get(ca.getPeerId());
        Org org = orgMapper.get(peer.getOrgId());
        League league = leagueMapper.get(org.getLeagueId());
        ca.setLeagueName(league.getName());
        ca.setOrgName(org.getName());
        ca.setPeerName(peer.getName());
        ca.setFlag(MD5Util.md516(league.getName() + org.getName() + peer.getName() + ca.getName()));
        return ca;
    }

}
