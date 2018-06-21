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
    public ModelAndView submit(@ModelAttribute LeagueInfo league,
                               @RequestParam("intent") String intent,
                               @RequestParam("id") int id) {
        try {
            switch (intent) {
                case "add":
                    multiService.getLeagueService().add(league);
                    break;
                case "edit":
                    league.setId(id);
                    multiService.getLeagueService().update(league);
                    break;
            }
        } catch (TException e) {
            e.printStackTrace();
        }
        return list();
    }

    @GetMapping(value = "add")
    public ModelAndView add() {
        ModelAndView modelAndView = new ModelAndView("leagueSubmit");
        modelAndView.addObject("intentLarge", "新建联盟");
        modelAndView.addObject("intentLittle", "新建");
        modelAndView.addObject("submit", "新增");
        modelAndView.addObject("intent", "add");
        modelAndView.addObject("league", new LeagueInfo());
        return modelAndView;
    }

    @GetMapping(value = "edit")
    public ModelAndView edit(@RequestParam("id") int id) {
        ModelAndView modelAndView = new ModelAndView("leagueSubmit");
        modelAndView.addObject("intentLarge", "编辑联盟");
        modelAndView.addObject("intentLittle", "编辑");
        modelAndView.addObject("submit", "修改");
        modelAndView.addObject("intent", "edit");
        LeagueInfo league;
        try {
            league = multiService.getLeagueService().get(id);
        } catch (TException e) {
            league = new LeagueInfo();
            e.printStackTrace();
        }
        modelAndView.addObject("league", league);
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
