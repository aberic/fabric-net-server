package cn.aberic.fabric.controller;

import cn.aberic.fabric.thrift.MultiServiceProvider;
import cn.aberic.thrift.league.LeagueInfo;
import org.apache.thrift.TException;
import org.springframework.web.bind.annotation.*;

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
    private MultiServiceProvider multiSService;

    @PostMapping(value = "add")
    public String add(@RequestBody LeagueInfo league) {
        try {
            if (multiSService.getLeagueService().add(league) > 0) {
                return "success";
            }
        } catch (TException e) {
            e.printStackTrace();
        }
        return "fail";
    }

    @PostMapping(value = "update")
    public String update(@RequestBody LeagueInfo league) {
        try {
            if (multiSService.getLeagueService().update(league) > 0) {
                return "success";
            }
        } catch (TException e) {
            e.printStackTrace();
        }
        return "fail";
    }

    @GetMapping(value = "listAll")
    public List<LeagueInfo> listAll() {
        try {
            return multiSService.getLeagueService().listAll();
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

}
