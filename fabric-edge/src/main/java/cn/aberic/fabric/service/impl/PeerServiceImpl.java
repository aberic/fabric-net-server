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

import cn.aberic.fabric.dao.Peer;
import cn.aberic.fabric.dao.mapper.AppMapper;
import cn.aberic.fabric.dao.mapper.ChaincodeMapper;
import cn.aberic.fabric.dao.mapper.ChannelMapper;
import cn.aberic.fabric.dao.mapper.PeerMapper;
import cn.aberic.fabric.service.PeerService;
import cn.aberic.fabric.utils.DateUtil;
import cn.aberic.fabric.utils.DeleteUtil;
import cn.aberic.fabric.utils.FabricHelper;
import cn.aberic.fabric.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service("peerService")
public class PeerServiceImpl implements PeerService {

    @Resource
    private PeerMapper peerMapper;
    @Resource
    private ChannelMapper channelMapper;
    @Resource
    private ChaincodeMapper chaincodeMapper;
    @Resource
    private AppMapper appMapper;
    @Resource
    private Environment env;

    @Override
    public int add(Peer peer, MultipartFile serverCrtFile) {
        if (StringUtils.isEmpty(peer.getName()) ||
                StringUtils.isEmpty(peer.getLocation()) ||
                StringUtils.isEmpty(peer.getEventHubName()) ||
                StringUtils.isEmpty(peer.getEventHubLocation())) {
            return 0;
        }
        if (null == serverCrtFile) {
            log.debug("peer tls server.crt is null");
            return 0;
        }
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
        peer.setServerCrtPath(serverCrtPath);
        try {
            FileUtil.save(serverCrtFile, serverCrtPath);
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        peer.setDate(DateUtil.getCurrent("yyyy-MM-dd"));
        return peerMapper.add(peer);
    }

    @Override
    public int update(Peer peer, MultipartFile serverCrtFile) {
        FabricHelper.obtain().removeManager(channelMapper.list(peer.getId()), chaincodeMapper);
        if (null == serverCrtFile) {
            return peerMapper.update(peer);
        }
        return peerMapper.updateWithNoFile(peer);
    }

    @Override
    public List<Peer> listAll() {
        return peerMapper.listAll();
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
        return DeleteUtil.obtain().deletePeer(id, peerMapper, channelMapper, chaincodeMapper, appMapper);
    }
}
