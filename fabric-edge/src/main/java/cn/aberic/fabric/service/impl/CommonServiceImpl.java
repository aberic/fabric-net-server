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

import cn.aberic.fabric.dao.User;
import cn.aberic.fabric.dao.mapper.UserMapper;
import cn.aberic.fabric.service.CommonService;
import cn.aberic.fabric.utils.CacheUtil;
import cn.aberic.fabric.utils.MD5Util;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * 描述：
 *
 * @author : Aberic 【2018-07-05 15:38】
 */
@Service("commonService")
public class CommonServiceImpl implements CommonService {

    @Resource
    private UserMapper userMapper;

    @Override
    public String login(User user) {
        try {
            if (MD5Util.verify(user.getPassword(), userMapper.get(user.getUsername()).getPassword())) {
                String token = UUID.randomUUID().toString();
                CacheUtil.putString(user.getUsername(), token);
                return token;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
