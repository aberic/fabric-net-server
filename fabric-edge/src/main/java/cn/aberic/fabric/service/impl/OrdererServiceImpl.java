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

import cn.aberic.fabric.dao.Orderer;
import cn.aberic.fabric.dao.mapper.ChaincodeMapper;
import cn.aberic.fabric.dao.mapper.ChannelMapper;
import cn.aberic.fabric.dao.mapper.OrdererMapper;
import cn.aberic.fabric.dao.mapper.PeerMapper;
import cn.aberic.fabric.service.OrdererService;
import cn.aberic.fabric.utils.DateUtil;
import cn.aberic.fabric.utils.FabricHelper;
import cn.aberic.fabric.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service("ordererService")
public class OrdererServiceImpl implements OrdererService {

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
    public int add(Orderer orderer, MultipartFile serverCrtFile) {
        if (StringUtils.isEmpty(orderer.getName()) ||
                StringUtils.isEmpty(orderer.getLocation())) {
            return 0;
        }
        if (StringUtils.isNotEmpty(serverCrtFile.getOriginalFilename())) {
            if (saveFileFail(orderer, serverCrtFile)) {
                return 0;
            }
        }
        orderer.setDate(DateUtil.getCurrent("yyyy-MM-dd"));
        return ordererMapper.add(orderer);
    }

    @Override
    public int update(Orderer orderer, MultipartFile serverCrtFile) {
        FabricHelper.obtain().removeChaincodeManager(peerMapper.list(orderer.getOrgId()), channelMapper, chaincodeMapper);
        if (null == serverCrtFile) {
            return ordererMapper.updateWithNoFile(orderer);
        }
        if (saveFileFail(orderer, serverCrtFile)) {
            return 0;
        }
        return ordererMapper.update(orderer);
    }

    @Override
    public List<Orderer> listAll() {
        return ordererMapper.listAll();
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
        return ordererMapper.delete(id);
    }

    private boolean saveFileFail(Orderer orderer, MultipartFile serverCrtFile) {
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
        orderer.setServerCrtPath(serverCrtPath);
        try {
            FileUtil.save(serverCrtFile, serverCrtPath);
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

}
