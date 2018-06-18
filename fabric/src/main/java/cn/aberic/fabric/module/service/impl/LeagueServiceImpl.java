package cn.aberic.fabric.module.service.impl;

import cn.aberic.fabric.module.bean.dto.LeagueDTO;
import cn.aberic.fabric.module.bean.vo.LeagueVO;
import cn.aberic.fabric.module.mapper.LeagueMapper;
import cn.aberic.fabric.module.mapper.OrgMapper;
import cn.aberic.fabric.module.service.LeagueService;
import cn.aberic.fabric.module.service.OrgService;
import cn.aberic.fabric.utils.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/4 15:03】
 */
@Service("leagueService")
public class LeagueServiceImpl implements LeagueService {

    @Resource
    private LeagueMapper leagueMapper;
    @Resource
    private OrgMapper orgMapper;

    @Override
    public String add(LeagueDTO league) {
        league.setDate(DateUtil.getCurrent("yyyy年MM月dd日"));
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

    @Override
    public List<LeagueVO> leagues() {
        List<LeagueDTO> leagueDTOS = leagueMapper.getLeagueList();
        List<LeagueVO> leagueVOS = new ArrayList<>();
        for (LeagueDTO leagueDTO: leagueDTOS) {
            LeagueVO leagueVO = new LeagueVO();
            leagueVO.setId(leagueDTO.getId());
            leagueVO.setName(leagueDTO.getName());
            leagueVO.setDate(leagueDTO.getDate());
            leagueVO.setCount(orgMapper.count(leagueDTO.getId()));
            leagueVOS.add(leagueVO);
        }
        return leagueVOS;
    }
}
