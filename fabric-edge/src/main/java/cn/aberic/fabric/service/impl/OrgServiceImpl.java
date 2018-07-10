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

import cn.aberic.fabric.dao.*;
import cn.aberic.fabric.dao.mapper.*;
import cn.aberic.fabric.service.OrgService;
import cn.aberic.fabric.utils.DateUtil;
import cn.aberic.fabric.utils.DeleteUtil;
import cn.aberic.fabric.utils.FabricHelper;
import cn.aberic.fabric.utils.FileUtil;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service("orgService")
public class OrgServiceImpl implements OrgService {

    @Resource
    private OrgMapper orgMapper;
    @Resource
    private Environment env;
    @Resource
    private LeagueMapper leagueMapper;
    @Resource
    private PeerMapper peerMapper;
    @Resource
    private OrdererMapper ordererMapper;
    @Resource
    private ChannelMapper channelMapper;
    @Resource
    private ChaincodeMapper chaincodeMapper;
    @Resource
    private AppMapper appMapper;


    @Override
    public int add(Org org, MultipartFile file) {
        if (StringUtils.isEmpty(org.getName()) ||
                StringUtils.isEmpty(org.getMspId()) ||
                StringUtils.isEmpty(org.getDomainName()) ||
                StringUtils.isEmpty(org.getOrdererDomainName()) ||
                StringUtils.isEmpty(org.getUsername()) ||
                null == file) {
            return 0;
        }
        org.setDate(DateUtil.getCurrent("yyyy年MM月dd日"));
        String parentPath = String.format("%s%s%s%s%s",
                env.getProperty("config.dir"),
                File.separator,
                leagueMapper.get(org.getLeagueId()).getName(),
                File.separator,
                org.getName());
        String childrenPath = parentPath + File.separator + Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[0];
        org.setCryptoConfigDir(childrenPath);
        try {
            FileUtil.unZipAndSave(file, parentPath, childrenPath);
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        return orgMapper.add(org);
    }

    @Override
    public int update(Org org, MultipartFile file) {
        if (null != file) {
            String parentPath = String.format("%s%s%s%s%s",
                    env.getProperty("config.dir"),
                    File.separator,
                    leagueMapper.get(org.getLeagueId()).getName(),
                    File.separator,
                    org.getName());
            String childrenPath = parentPath + File.separator + Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[0];
            org.setCryptoConfigDir(childrenPath);
            try {
                FileUtil.unZipAndSave(file, parentPath, childrenPath);
            } catch (IOException e) {
                e.printStackTrace();
                return 0;
            }
        }
        FabricHelper.obtain().removeManager(peerMapper.list(org.getId()), channelMapper, chaincodeMapper);
        return orgMapper.update(org);
    }

    @Override
    public List<Org> listAll() {
        return orgMapper.listAll();
    }

    @Override
    public List<Org> listById(int id) {
        return orgMapper.list(id);
    }

    @Override
    public Org get(int id) {
        return orgMapper.get(id);
    }

    @Override
    public int countById(int id) {
        return orgMapper.count(id);
    }

    @Override
    public int count() {
        return orgMapper.countAll();
    }

    @Override
    public int delete(int id) {
        return DeleteUtil.obtain().deleteOrg(id, orgMapper, ordererMapper, peerMapper, channelMapper, chaincodeMapper, appMapper);
    }

}
