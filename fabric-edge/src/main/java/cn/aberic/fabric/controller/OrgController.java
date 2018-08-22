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

package cn.aberic.fabric.controller;

import cn.aberic.fabric.dao.entity.Org;
import cn.aberic.fabric.service.OrgService;
import cn.aberic.fabric.utils.SpringUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/4 15:01】
 */
@CrossOrigin
@RestController
@RequestMapping("org")
public class OrgController {

    @Resource
    private OrgService orgService;

    @PostMapping(value = "submit")
    public ModelAndView submit(@ModelAttribute Org org,
                               @RequestParam("intent") String intent,
                               @RequestParam("id") int id) {
        switch (intent) {
            case "add":
                orgService.add(org);
                break;
            case "edit":
                org.setId(id);
                orgService.update(org);
                break;
        }
        return new ModelAndView(new RedirectView("list"));
    }

    @GetMapping(value = "add")
    public ModelAndView add() {
        ModelAndView modelAndView = new ModelAndView("orgSubmit");
        modelAndView.addObject("intentLittle", SpringUtil.get("enter"));
        modelAndView.addObject("submit", SpringUtil.get("submit"));
        modelAndView.addObject("intent", "add");
        modelAndView.addObject("orgVO", new Org());
        modelAndView.addObject("leagues", orgService.listAllLeague());
        return modelAndView;
    }

    @GetMapping(value = "edit")
    public ModelAndView edit(@RequestParam("id") int id) {
        ModelAndView modelAndView = new ModelAndView("orgSubmit");
        modelAndView.addObject("intentLittle", SpringUtil.get("edit"));
        modelAndView.addObject("submit", SpringUtil.get("modify"));
        modelAndView.addObject("intent", "edit");
        modelAndView.addObject("orgVO", orgService.get(id));
        modelAndView.addObject("leagues", orgService.listAllLeague());
        return modelAndView;
    }

    @GetMapping(value = "delete")
    public ModelAndView delete(@RequestParam("id") int id) {
        orgService.delete(id);
        return new ModelAndView(new RedirectView("list"));
    }

    @GetMapping(value = "list")
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("orgs");
        modelAndView.addObject("orgs", orgService.listAll());
        return modelAndView;
    }

}
