package cn.aberic.fabric.controller;

import cn.aberic.fabric.thrift.MultiServiceProvider;
import cn.aberic.thrift.org.OrgInfo;
import org.apache.thrift.TException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
    private MultiServiceProvider multiService;

    @PostMapping(value = "add")
    public String add(@RequestBody OrgInfo org) {
        try {
            if (multiService.getOrgService().add(org) > 0) {
                return "success";
            }
        } catch (TException e) {
            e.printStackTrace();
        }
        return "fail";
    }

    @PostMapping(value = "submit")
    public ModelAndView submit(@ModelAttribute OrgInfo org, @RequestParam("intent") String intent, @RequestParam("id") int id) {
        try {
            switch (intent) {
                case "add":
                    multiService.getOrgService().add(org);
                    break;
                case "edit":
                    org.setId(id);
                    multiService.getOrgService().update(org);
                    break;
            }
        } catch (TException e) {
            e.printStackTrace();
        }
        return list();
    }

    @GetMapping(value = "add")
    public ModelAndView add() {
        ModelAndView modelAndView = new ModelAndView("orgSubmit");
        modelAndView.addObject("intentLarge", "新建组织");
        modelAndView.addObject("intentLittle", "新建");
        modelAndView.addObject("intent", "add");
        modelAndView.addObject("org", new OrgInfo());
        return modelAndView;
    }

    @PostMapping(value = "update")
    public String update(@RequestBody OrgInfo org) {
        try {
            if (multiService.getOrgService().update(org) > 0) {
                return "success";
            }
        } catch (TException e) {
            e.printStackTrace();
        }
        return "fail";
    }

    @GetMapping(value = "list")
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("orgs");
        try {
            List<OrgInfo> orgs = multiService.getOrgService().listAll();
            for (OrgInfo org : orgs) {
                org.setOrdererCount(multiService.getOrdererService().countById(org.getId()));
                org.setPeerCount(multiService.getPeerService().countById(org.getId()));
            }
            modelAndView.addObject("orgs", orgs);
        } catch (TException e) {
            modelAndView.addObject("orgs", new ArrayList<>());
            e.printStackTrace();
        }
        return modelAndView;
    }

}
