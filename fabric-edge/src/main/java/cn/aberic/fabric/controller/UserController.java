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

package cn.aberic.fabric.controller;

import cn.aberic.fabric.dao.Role;
import cn.aberic.fabric.dao.User;
import cn.aberic.fabric.service.UserService;
import cn.aberic.fabric.utils.SpringUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import java.util.List;

/**
 * 描述：
 *
 * @author : Aberic 【2018-08-14 10:00】
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping(value = "submit")
    public ModelAndView submit(@ModelAttribute User user,
                               @RequestParam("intent") String intent,
                               @RequestParam("roleIdTmp") int roleIdTmp) {
        switch (intent) {
            case "add":
                userService.create(user);
                break;
            case "password":
                if (roleIdTmp == Role.SUPER_ADMIN) {
                    user.setRoleId(roleIdTmp);
                }
                userService.updatePassword(user);
                break;
            case "role":
                if (roleIdTmp == Role.SUPER_ADMIN) {
                    user.setRoleId(roleIdTmp);
                }
                userService.updateRole(user);
                break;
        }
        return new ModelAndView(new RedirectView("list"));
    }

    @GetMapping(value = "list")
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("users");
        List<User> users = userService.listAll();
        modelAndView.addObject("users", users);
        return modelAndView;
    }

    @GetMapping(value = "add")
    public ModelAndView add() {
        ModelAndView modelAndView = new ModelAndView("userSubmit");
        modelAndView.addObject("intentLittle", SpringUtil.get("add"));
        modelAndView.addObject("submit", SpringUtil.get("submit"));
        modelAndView.addObject("intent", "add");
        modelAndView.addObject("roles", userService.listRole());
        modelAndView.addObject("user", new User());
        return modelAndView;
    }

    @GetMapping(value = "password")
    public ModelAndView password(@RequestParam("id") int id) {
        ModelAndView modelAndView = new ModelAndView("userPassSubmit");
        modelAndView.addObject("intentLittle", SpringUtil.get("edit"));
        modelAndView.addObject("submit", SpringUtil.get("modify"));
        modelAndView.addObject("intent", "password");
        User user = userService.get(id);
        user.setPassword("");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @GetMapping(value = "role")
    public ModelAndView role(@RequestParam("id") int id) {
        ModelAndView modelAndView = new ModelAndView("userRoleSubmit");
        modelAndView.addObject("intentLittle", SpringUtil.get("edit"));
        modelAndView.addObject("submit", SpringUtil.get("modify"));
        modelAndView.addObject("intent", "role");
        User user = userService.get(id);
        user.setPassword("");
        modelAndView.addObject("roles", userService.listRole());
        modelAndView.addObject("user", user);
        modelAndView.addObject("roleIdTmp", user.getRoleId());
        return modelAndView;
    }

    @GetMapping(value = "delete")
    public ModelAndView delete(@RequestParam("id") int id) {
        userService.delete(id);
        return new ModelAndView(new RedirectView("list"));
    }

}
