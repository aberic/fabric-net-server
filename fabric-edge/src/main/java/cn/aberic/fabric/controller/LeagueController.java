package cn.aberic.fabric.controller;

import cn.aberic.fabric.thrift.MultiServiceProvider;
import cn.aberic.thrift.league.LeagueInfo;
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
@RequestMapping("league")
public class LeagueController {

    @Resource
    private MultiServiceProvider multiService;

    @PostMapping(value = "submit")
    public ModelAndView submit(@ModelAttribute LeagueInfo league) {
        try {
            multiService.getLeagueService().add(league);
        } catch (TException e) {
            e.printStackTrace();
        }
        return list();
    }

    @GetMapping(value = "add")
    public ModelAndView add() {
        ModelAndView modelAndView = new ModelAndView("leagueAdd");
        modelAndView.addObject("intentLarge", "新建联盟");
        modelAndView.addObject("intentLittle", "新建");
        modelAndView.addObject("intent", "add");
        modelAndView.addObject("league", new LeagueInfo());
        return modelAndView;
    }

    @GetMapping(value = "edit")
    public ModelAndView edit() {
        ModelAndView modelAndView = new ModelAndView("leagueAdd");
        modelAndView.addObject("intentLarge", "编辑联盟");
        modelAndView.addObject("intentLittle", "编辑");
        modelAndView.addObject("intent", "edit");
        modelAndView.addObject("leagueDTO", new LeagueInfo());
        return modelAndView;
    }

    @PostMapping(value = "update")
    public String update(@RequestBody LeagueInfo league) {
        try {
            if (multiService.getLeagueService().update(league) > 0) {
                return "success";
            }
        } catch (TException e) {
            e.printStackTrace();
        }
        return "fail";
    }

    @GetMapping(value = "list")
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("leagues");
        try {
            List<LeagueInfo> leagues = multiService.getLeagueService().listAll();
            for (LeagueInfo league : leagues) {
                league.setOrgCount(multiService.getOrgService().countById(league.getId()));
            }
            modelAndView.addObject("leagues", leagues);
        } catch (TException e) {
            modelAndView.addObject("leagues", new ArrayList<>());
            e.printStackTrace();
        }
        return modelAndView;
    }

}
