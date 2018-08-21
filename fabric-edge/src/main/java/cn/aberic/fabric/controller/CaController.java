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

import cn.aberic.fabric.dao.CA;
import cn.aberic.fabric.service.CAService;
import cn.aberic.fabric.utils.SpringUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;

/**
 * 作者：Aberic on 2018/7/13 00:05
 * 邮箱：abericyang@gmail.com
 */
@CrossOrigin
@RestController
@RequestMapping("ca")
public class CaController {

    @Resource
    private CAService caService;

    @PostMapping(value = "submit")
    public ModelAndView submit(@ModelAttribute CA ca,
                               @RequestParam("intent") String intent,
                               @RequestParam("skFile") MultipartFile skFile,
                               @RequestParam("certificateFile") MultipartFile certificateFile) {
        switch (intent) {
            case "add":
                caService.add(ca, skFile, certificateFile);
                break;
            case "edit":
                caService.update(ca, skFile, certificateFile);
                break;
        }
        return new ModelAndView(new RedirectView("list"));
    }

    @GetMapping(value = "add")
    public ModelAndView add() {
        ModelAndView modelAndView = new ModelAndView("caSubmit");
        modelAndView.addObject("intentLittle", SpringUtil.get("enter"));
        modelAndView.addObject("submit", SpringUtil.get("submit"));
        modelAndView.addObject("intent", "add");
        modelAndView.addObject("ca", new CA());
        modelAndView.addObject("peers", caService.getFullPeers());
        return modelAndView;
    }

    @GetMapping(value = "edit")
    public ModelAndView edit(@RequestParam("id") int id) {
        ModelAndView modelAndView = new ModelAndView("caSubmit");
        modelAndView.addObject("intentLittle", SpringUtil.get("edit"));
        modelAndView.addObject("submit", SpringUtil.get("modify"));
        modelAndView.addObject("intent", "edit");

        CA ca = caService.get(id);
        modelAndView.addObject("ca", ca);
        modelAndView.addObject("peers", caService.getPeersByCA(ca));
        return modelAndView;
    }

    @GetMapping(value = "list")
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("cas");
        modelAndView.addObject("cas", caService.listFullCA());
        return modelAndView;
    }

    @GetMapping(value = "delete")
    public ModelAndView delete(@RequestParam("id") int id) {
        caService.delete(id);
        return new ModelAndView(new RedirectView("list"));
    }

}
