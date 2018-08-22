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

package cn.aberic.fabric.runner;

import cn.aberic.fabric.dao.entity.Role;
import cn.aberic.fabric.dao.entity.User;
import cn.aberic.fabric.service.UserService;
import cn.aberic.fabric.utils.MD5Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Aberic on 2018/6/22 21:36
 * 邮箱：abericyang@gmail.com
 */
@Component
public class FabricEdgeRunner implements ApplicationRunner {

    @Resource
    private UserService userService;

    @Value("${username}")
    private String username;
    @Value("${password}")
    private String password;

    @Override
    public void run(ApplicationArguments args) {
        initRola();
        addUser();
        System.out.println();
        System.out.println(" _____   _   _   ____    ");
        System.out.println("| ____| | \\ | | |  _ \\   ");
        System.out.println("|  _|   |  \\| | | | | |  ");
        System.out.println("| |___  | |\\  | | |_| |  ");
        System.out.println("|_____| |_| \\_| |____/   ");
        System.out.println();
        System.out.println("===================== please make your fabric net work ===================== ");
        System.out.println();
        System.out.println("================================= read logs ================================ ");
    }

    private void initRola() {
        Role roleSuperAdmin = new Role(1, "超级管理员");
        Role roleAdmin = new Role(2, "管理员");
        Role roleMember = new Role(8, "普通会员");
        List<Role> roles = new ArrayList<>();
        roles.add(roleSuperAdmin);
        roles.add(roleAdmin);
        roles.add(roleMember);
        userService.addRoleList(roles);
    }

    private void addUser() {
        User user = new User();
        user.setUsername(username);
        user.setPassword(MD5Util.md5(password));
        user.setRoleId(Role.SUPER_ADMIN);
        userService.init(user);
    }

}
