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

import cn.aberic.fabric.dao.User;
import cn.aberic.fabric.dao.mapper.UserMapper;
import cn.aberic.fabric.service.UserService;
import cn.aberic.fabric.utils.CacheUtil;
import cn.aberic.fabric.utils.DateUtil;
import cn.aberic.fabric.utils.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public int init(User user) {
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            return 0;
        }
        List<User> users = listAll();
        for (User user1 : users) {
            if (StringUtils.equals(user.getUsername(), user1.getUsername())) {
                return update(user);
            }
        }
        return add(user);
    }

    @Override
    public int add(User user) {
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            return 0;
        }
        return userMapper.add(user);
    }

    @Override
    public int delete(int id) {
        return userMapper.delete(id);
    }

    @Override
    public int update(User user) {
        return userMapper.update(user);
    }

    @Override
    public List<User> listAll() {
        List<User> users = userMapper.listAll();
        for (User user: users) {
            try {
                user.setDate(DateUtil.strDateFormat(user.getDate(), "yyyyMMddHHmm", "yyyy/MM/dd HH:mm"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    @Override
    public User get(String username) {
        return userMapper.get(username);
    }

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
