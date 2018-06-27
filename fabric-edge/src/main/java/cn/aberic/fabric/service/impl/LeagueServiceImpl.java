package cn.aberic.fabric.service.impl;

import cn.aberic.fabric.dao.League;
import cn.aberic.fabric.dao.mapper.LeagueMapper;
import cn.aberic.fabric.service.LeagueService;
import cn.aberic.fabric.utils.DateUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service("leagueService")
public class LeagueServiceImpl implements LeagueService {

    @Resource
    private LeagueMapper leagueMapper;

    @Override
    public int add(League leagueInfo) {
        if (StringUtils.isEmpty(leagueInfo.getName())) {
            return 0;
        }
        leagueInfo.setDate(DateUtil.getCurrent("yyyy年MM月dd日"));
        return leagueMapper.add(leagueInfo);
    }

    @Override
    public int update(League leagueInfo) {
        return leagueMapper.update(leagueInfo);
    }

    @Override
    public List<League> listAll() {
        return leagueMapper.listAll();
    }

    @Override
    public League get(int id) {
        return leagueMapper.get(id);
    }

}
