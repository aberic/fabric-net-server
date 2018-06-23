package cn.aberic.fabric.controller;

import cn.aberic.fabric.thrift.MultiServiceProvider;
import cn.aberic.thrift.league.LeagueInfo;
import cn.aberic.thrift.orderer.OrdererInfo;
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
@RequestMapping("orderer")
public class OrdererController {

    @Resource
    private MultiServiceProvider multiService;

    @PostMapping(value = "submit")
    public ModelAndView submit(@ModelAttribute OrdererInfo orderer,
                               @RequestParam("intent") String intent,
                               @RequestParam("id") int id) {
        try {
            switch (intent) {
                case "add":
                    multiService.getOrdererService().add(orderer);
                    break;
                case "edit":
                    orderer.setId(id);
                    multiService.getOrdererService().update(orderer);
                    break;
            }
        } catch (TException e) {
            e.printStackTrace();
        }
        return list();
    }

    @GetMapping(value = "add")
    public ModelAndView add() {
        ModelAndView modelAndView = new ModelAndView("ordererSubmit");
        modelAndView.addObject("intentLarge", "新建排序服务");
        modelAndView.addObject("intentLittle", "新建");
        modelAndView.addObject("submit", "新增");
        modelAndView.addObject("intent", "add");
        modelAndView.addObject("orderer", new OrdererInfo());
        List<OrgInfo> orgs = multiService.getForPeerAndOrderer();
        modelAndView.addObject("orgs", orgs);
        return modelAndView;
    }

    @GetMapping(value = "edit")
    public ModelAndView edit(@RequestParam("id") int id) {
        ModelAndView modelAndView = new ModelAndView("ordererSubmit");
        modelAndView.addObject("intentLarge", "编辑排序服务");
        modelAndView.addObject("intentLittle", "编辑");
        modelAndView.addObject("submit", "修改");
        modelAndView.addObject("intent", "edit");
        OrdererInfo orderer;
        List<OrgInfo> orgs;
        try {
            orderer = multiService.getOrdererService().get(id);
            LeagueInfo league = multiService.getLeagueService().get(multiService.getOrgService().get(orderer.getOrgId()).getLeagueId());
            orgs = multiService.getOrgService().listById(league.getId());
            for (OrgInfo org : orgs) {
                org.setLeagueName(league.getName());
            }
        } catch (TException e) {
            orderer = new OrdererInfo();
            orgs = new ArrayList<>();
            e.printStackTrace();
        }
        modelAndView.addObject("orderer", orderer);
        modelAndView.addObject("orgs", orgs);
        return modelAndView;
    }

    @GetMapping(value = "list")
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("orderers");
        try {
            List<OrdererInfo> orderers = multiService.getOrdererService().listAll();
            for (OrdererInfo orderer : orderers) {
                orderer.setOrgName(multiService.getOrgService().get(orderer.getOrgId()).getName());
            }
            modelAndView.addObject("orderers", orderers);
        } catch (TException e) {
            modelAndView.addObject("orderers", new ArrayList<>());
            e.printStackTrace();
        }
        return modelAndView;
    }

}
