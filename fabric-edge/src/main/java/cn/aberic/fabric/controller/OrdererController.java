package cn.aberic.fabric.controller;

import cn.aberic.fabric.dao.League;
import cn.aberic.fabric.dao.Orderer;
import cn.aberic.fabric.dao.Org;
import cn.aberic.fabric.service.LeagueService;
import cn.aberic.fabric.service.OrdererService;
import cn.aberic.fabric.service.OrgService;
import cn.aberic.fabric.utils.SpringUtil;
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
@RequestMapping("orderer")
public class OrdererController {

    @Resource
    private OrdererService ordererService;
    @Resource
    private OrgService orgService;
    @Resource
    private LeagueService leagueService;

    @PostMapping(value = "submit")
    public ModelAndView submit(@ModelAttribute Orderer orderer,
                               @RequestParam("intent") String intent,
                               @RequestParam("id") int id) {
        switch (intent) {
            case "add":
                ordererService.add(orderer);
                break;
            case "edit":
                orderer.setId(id);
                ordererService.update(orderer);
                break;
        }
        return new ModelAndView(new RedirectView("list"));
    }

    @GetMapping(value = "add")
    public ModelAndView add() {
        ModelAndView modelAndView = new ModelAndView("ordererSubmit");
        modelAndView.addObject("intentLittle", SpringUtil.get("enter"));
        modelAndView.addObject("submit", SpringUtil.get("submit"));
        modelAndView.addObject("intent", "add");
        modelAndView.addObject("orderer", new Orderer());
        modelAndView.addObject("orgs", getForPeerAndOrderer());
        return modelAndView;
    }

    @GetMapping(value = "edit")
    public ModelAndView edit(@RequestParam("id") int id) {
        ModelAndView modelAndView = new ModelAndView("ordererSubmit");
        modelAndView.addObject("intentLittle", SpringUtil.get("edit"));
        modelAndView.addObject("submit", SpringUtil.get("modify"));
        modelAndView.addObject("intent", "edit");
        Orderer orderer = ordererService.get(id);
        League league = leagueService.get(orgService.get(orderer.getOrgId()).getLeagueId());
        List<Org> orgs = orgService.listById(league.getId());
        for (Org org : orgs) {
            org.setLeagueName(league.getName());
        }
        modelAndView.addObject("orderer", orderer);
        modelAndView.addObject("orgs", orgs);
        return modelAndView;
    }

    @GetMapping(value = "list")
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("orderers");
        List<Orderer> orderers = ordererService.listAll();
        for (Orderer orderer : orderers) {
            orderer.setOrgName(orgService.get(orderer.getOrgId()).getName());
        }
        modelAndView.addObject("orderers", orderers);
        return modelAndView;
    }

    private List<Org> getForPeerAndOrderer() {
        List<Org> orgs = orgService.listAll();
        for (Org org : orgs) {
            org.setLeagueName(leagueService.get(org.getLeagueId()).getName());
        }
        return orgs;
    }

}
