package cn.aberic.fabric.module.service.impl;

import cn.aberic.fabric.module.bean.dto.LeagueDTO;
import cn.aberic.fabric.module.mapper.LeagueMapper;
import cn.aberic.fabric.module.service.LeagueService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/4 15:03】
 */
@Service("leagueService")
public class LeagueServiceImpl implements LeagueService {

    @Resource
    private LeagueMapper leagueMapper;

   @Override
    public String add(LeagueDTO league) {
        if (leagueMapper.addLeague(league) > 0) {
            return responseSuccess(league.toString());
        }
        return responseFail("add league fail");
    }

    @Override
    public String list(int id) {
        return responseSuccess(JSONArray.parseArray(JSON.toJSONString(leagueMapper.getLeagueList())));
    }

    @Override
    public String update(LeagueDTO league) {
        if (leagueMapper.updateLeagueById(league) > 0) {
            return responseSuccess(league.toString());
        }
        return responseFail("update league fail");
    }

    @Override
    public String get(int id) {
        return "";
    }
}
