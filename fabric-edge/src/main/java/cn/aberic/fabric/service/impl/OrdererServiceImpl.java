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
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

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

    @Override
    public int add(Orderer orderer) {
        if (StringUtils.isEmpty(orderer.getName()) ||
                StringUtils.isEmpty(orderer.getLocation())) {
            return 0;
        }
        orderer.setDate(DateUtil.getCurrent("yyyy年MM月dd日"));
        return ordererMapper.add(orderer);
    }

    @Override
    public int update(Orderer orderer) {
        FabricHelper.obtain().removeManager(peerMapper.list(orderer.getOrgId()), channelMapper, chaincodeMapper);
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
}
