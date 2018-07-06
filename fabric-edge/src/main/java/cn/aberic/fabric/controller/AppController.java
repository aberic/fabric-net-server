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

import cn.aberic.fabric.bean.App;
import cn.aberic.fabric.service.AppService;
import cn.aberic.fabric.utils.SpringUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/4 15:01】
 */
@CrossOrigin
@RestController
@RequestMapping("app")
public class AppController {

    @Resource
    private AppService appService;

    @PostMapping(value = "submit")
    public ModelAndView submit(@ModelAttribute App app,
                               @RequestParam("intent") String intent,
                               @RequestParam("chaincodeId") int chaincodeId) {
        switch (intent) {
            case "add":
                appService.add(app, chaincodeId);
                break;
            case "edit":
                appService.update(app);
                break;
        }
        Map map = new HashMap();
        map.put("id", chaincodeId);
        return new ModelAndView(new RedirectView("list"), map);
    }

    @GetMapping(value = "add")
    public ModelAndView add(@RequestParam("id") int chaincodeId) {
        ModelAndView modelAndView = new ModelAndView("appSubmit");
        modelAndView.addObject("intentLittle", SpringUtil.get("new_app"));
        modelAndView.addObject("submit", SpringUtil.get("submit"));
        modelAndView.addObject("intent", "add");
        modelAndView.addObject("app", new App());
        modelAndView.addObject("chaincodeId", chaincodeId);
        return modelAndView;
    }

    @GetMapping(value = "edit")
    public ModelAndView edit(@RequestParam("id") int id) {
        ModelAndView modelAndView = new ModelAndView("appSubmit");
        modelAndView.addObject("intentLittle", SpringUtil.get("edit"));
        modelAndView.addObject("submit", SpringUtil.get("modify"));
        modelAndView.addObject("intent", "edit");
        App app = appService.get(id);
        modelAndView.addObject("app", app);
        modelAndView.addObject("chaincodeId", app.getChaincodeId());
        return modelAndView;
    }

    @GetMapping(value = "delete")
    public ModelAndView delete(@RequestParam("id") int id, @RequestParam("chaincodeId") int chaincodeId) {
        appService.delete(id);
        Map map = new HashMap();
        map.put("id", chaincodeId);
        return new ModelAndView(new RedirectView("list"), map);
    }

    @GetMapping(value = "refresh")
    public ModelAndView refresh(@RequestParam("id") int id, @RequestParam("chaincodeId") int chaincodeId) {
        appService.updateKey(id);
        Map map = new HashMap();
        map.put("id", chaincodeId);
        return new ModelAndView(new RedirectView("list"), map);
    }

    @GetMapping(value = "list")
    public ModelAndView list(@RequestParam("id") int chaincodeId) {
        ModelAndView modelAndView = new ModelAndView("apps");
        List<App> apps = appService.list(chaincodeId);
        modelAndView.addObject("apps", apps);
        modelAndView.addObject("chaincodeId", chaincodeId);
        return modelAndView;
    }

}
