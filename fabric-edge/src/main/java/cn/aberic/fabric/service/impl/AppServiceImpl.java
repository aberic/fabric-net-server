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

import cn.aberic.fabric.bean.App;
import cn.aberic.fabric.bean.Key;
import cn.aberic.fabric.dao.mapper.AppMapper;
import cn.aberic.fabric.service.AppService;
import cn.aberic.fabric.utils.CacheUtil;
import cn.aberic.fabric.utils.DateUtil;
import cn.aberic.fabric.utils.MathUtil;
import cn.aberic.fabric.utils.encryption.Utils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 描述：
 *
 * @author : Aberic 【2018-07-05 17:11】
 */
@Service("appService")
public class AppServiceImpl implements AppService {

    @Resource
    private AppMapper appMapper;

    @Override
    public int add(App app, int chaincodeId) {
        if (null != appMapper.check(app)) {
            return 0;
        }
        Key key = Utils.obtain().createECCDSAKeyPair();
        if (null == key) {
            return 0;
        }
        app.setPublicKey(key.getPublicKey());
        app.setPrivateKey(key.getPrivateKey());
        app.setChaincodeId(chaincodeId);
        app.setKey(MathUtil.getRandom8());
        app.setCreateDate(DateUtil.getCurrent("yyyy-MM-dd HH:mm:ss"));
        app.setModifyDate(DateUtil.getCurrent("yyyy-MM-dd HH:mm:ss"));
        if (app.isActive()) {
            CacheUtil.putKeyChaincodeId(app.getKey(), app.getChaincodeId());
        }
        return appMapper.add(app);
    }

    @Override
    public int update(App app) {
        app.setModifyDate(DateUtil.getCurrent("yyyy-MM-dd HH:mm:ss"));
        if (app.isActive()) {
            CacheUtil.putKeyChaincodeId(app.getKey(), app.getChaincodeId());
        } else {
            CacheUtil.removeString(app.getKey());
        }
        return appMapper.update(app);
    }

    @Override
    public int updateKey(int id) {
        App app = new App();
        app.setId(id);
        CacheUtil.removeKeyChaincodeId(appMapper.get(id).getKey());
        app.setKey(MathUtil.getRandom8());
        if (app.isActive()) {
            CacheUtil.putKeyChaincodeId(app.getKey(), app.getChaincodeId());
        } else {
            CacheUtil.removeString(app.getKey());
        }
        Key key = Utils.obtain().createECCDSAKeyPair();
        if (null == key) {
            return 0;
        }
        app.setPublicKey(key.getPublicKey());
        app.setPrivateKey(key.getPrivateKey());
        return appMapper.updateKey(app);
    }

    @Override
    public List<App> list(int id) {
        return appMapper.list(id);
    }

    @Override
    public App get(int id) {
        return appMapper.get(id);
    }

    @Override
    public int delete(int id) {
        return appMapper.delete(id);
    }

    @Override
    public int deleteAll(int id) {
        return appMapper.deleteAll(id);
    }

    @Override
    public int count(int id) {
        return appMapper.count(id);
    }
}
