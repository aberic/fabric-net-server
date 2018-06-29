package cn.aberic.fabric.controller;

import cn.aberic.fabric.dao.League;
import cn.aberic.fabric.service.LeagueService;
import cn.aberic.fabric.service.OrgService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
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
    private OrgService orgService;
    @Resource
    private LeagueService leagueService;

    @PostMapping(value = "submit")
    public ModelAndView submit(@ModelAttribute League league,
                               @RequestParam("intent") String intent,
                               @RequestParam("id") int id) {
        switch (intent) {
            case "add":
                leagueService.add(league);
                break;
            case "edit":
                league.setId(id);
                leagueService.update(league);
                break;
        }
        return new ModelAndView(new RedirectView("list"));
    }

    @GetMapping(value = "add")
    public ModelAndView add() {
        ModelAndView modelAndView = new ModelAndView("leagueSubmit");
        modelAndView.addObject("intentLarge", "录入联盟");
        modelAndView.addObject("intentLittle", "录入");
        modelAndView.addObject("submit", "录入");
        modelAndView.addObject("intent", "add");
        modelAndView.addObject("league", new League());
        return modelAndView;
    }

    @GetMapping(value = "edit")
    public ModelAndView edit(@RequestParam("id") int id) {
        ModelAndView modelAndView = new ModelAndView("leagueSubmit");
        modelAndView.addObject("intentLarge", "编辑联盟");
        modelAndView.addObject("intentLittle", "编辑");
        modelAndView.addObject("submit", "修改");
        modelAndView.addObject("intent", "edit");
        modelAndView.addObject("league", leagueService.get(id));
        return modelAndView;
    }

    @PostMapping(value = "update")
    public String update(@RequestBody League league) {
        if (leagueService.update(league) > 0) {
            return "success";
        }
        return "fail";
    }

    @GetMapping(value = "list")
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("leagues");
        List<League> leagues = leagueService.listAll();
        for (League league : leagues) {
            league.setOrgCount(orgService.countById(league.getId()));
        }
        modelAndView.addObject("leagues", leagues);
        return modelAndView;
    }

}
