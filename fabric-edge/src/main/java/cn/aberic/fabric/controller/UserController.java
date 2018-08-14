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

import cn.aberic.fabric.dao.League;
import cn.aberic.fabric.dao.User;
import cn.aberic.fabric.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping(value = "list")
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("users");
        List<User> users = userService.listAll();
        modelAndView.addObject("users", users);
        return modelAndView;
    }

    @GetMapping(value = "delete")
    public ModelAndView delete(@RequestParam("id") int id) {
        userService.delete(id);
        return new ModelAndView(new RedirectView("list"));
    }

}
