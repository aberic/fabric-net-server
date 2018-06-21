package cn.aberic.fabric.controller;

import cn.aberic.fabric.thrift.MultiServiceProvider;
import cn.aberic.fabric.utils.FileUtil;
import cn.aberic.thrift.league.LeagueInfo;
import cn.aberic.thrift.org.OrgInfo;
import org.apache.thrift.TException;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.IOException;
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
    @Resource
    private Environment env;

    @PostMapping(value = "submit")
    public ModelAndView submit(@ModelAttribute OrgInfo org,
                               @RequestParam("intent") String intent,
                               @RequestParam("file") MultipartFile file,
                               @RequestParam("id") int id) {
        try {
            String path = String.format("%s/%s/%s", env.getProperty("config.dir"), multiService.getLeagueService().get(org.getLeagueId()).getName(), org.getName());
            switch (intent) {
                case "add":
                    org.setCryptoConfigDir(path);
                    if (multiService.getOrgService().add(org) <= 0) {
                        break;
                    }
                    if (file.isEmpty()) {
                        break;
                    }
                    FileUtil.unZipAndSave(file, path);
                    break;
                case "edit":
                    org.setId(id);
                    org.setCryptoConfigDir(path);
                    if (!file.isEmpty()) {
                        FileUtil.unZipAndSave(file, path);
                    }
                    multiService.getOrgService().update(org);
                    break;
            }
        } catch (TException | IOException e) {
            e.printStackTrace();
        }
        return list();
    }

    @GetMapping(value = "add")
    public ModelAndView add() {
        ModelAndView modelAndView = new ModelAndView("orgSubmit");
        modelAndView.addObject("intentLarge", "新建组织");
        modelAndView.addObject("intentLittle", "新建");
        modelAndView.addObject("submit", "新增");
        modelAndView.addObject("intent", "add");
        modelAndView.addObject("org", new OrgInfo());
        List<LeagueInfo> leagues;
        try {
            leagues = multiService.getLeagueService().listAll();
        } catch (TException e) {
            leagues = new ArrayList<>();
            e.printStackTrace();
        }
        modelAndView.addObject("leagues", leagues);
        return modelAndView;
    }

    @GetMapping(value = "edit")
    public ModelAndView edit(@RequestParam("id") int id) {
        ModelAndView modelAndView = new ModelAndView("orgSubmit");
        modelAndView.addObject("intentLarge", "编辑组织");
        modelAndView.addObject("intentLittle", "编辑");
        modelAndView.addObject("submit", "修改");
        modelAndView.addObject("intent", "edit");
        OrgInfo org;
        List<LeagueInfo> leagues;
        try {
            org = multiService.getOrgService().get(id);
            leagues = multiService.getLeagueService().listAll();
        } catch (TException e) {
            org = new OrgInfo();
            leagues = new ArrayList<>();
            e.printStackTrace();
        }
        modelAndView.addObject("org", org);
        modelAndView.addObject("leagues", leagues);
        return modelAndView;
    }

    @GetMapping(value = "list")
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("orgs");
        List<OrgInfo> orgs = new ArrayList<>();
        try {
            orgs.addAll(multiService.getOrgService().listAll());
            for (OrgInfo org : orgs) {
                org.setOrdererCount(multiService.getOrdererService().countById(org.getId()));
                org.setPeerCount(multiService.getPeerService().countById(org.getId()));
                org.setLeagueName(multiService.getLeagueService().get(org.getLeagueId()).getName());
            }
            modelAndView.addObject("orgs", orgs);
        } catch (TException e) {
            modelAndView.addObject("orgs", orgs);
            e.printStackTrace();
        }
        return modelAndView;
    }

}
