package cn.aberic.fabric.service;

import cn.aberic.fabric.mapper.LeagueMapper;
import cn.aberic.thrift.league.LeagueInfo;
import cn.aberic.thrift.league.LeagueService;
import cn.aberic.thrift.utils.DateUtil;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service("leagueService")
public class LeagueServiceImpl implements LeagueService.Iface {

    @Resource
    private LeagueMapper leagueMapper;

    @Override
    public int add(LeagueInfo leagueInfo) throws TException {
        if (StringUtils.isEmpty(leagueInfo.getName())) {
            return 0;
        }
        leagueInfo.setDate(DateUtil.getCurrent("yyyy年MM月dd日"));
        return leagueMapper.add(leagueInfo);
    }

    @Override
    public int update(LeagueInfo leagueInfo) throws TException {
        return leagueMapper.update(leagueInfo);
    }

    @Override
    public List<LeagueInfo> listAll() throws TException {
        return leagueMapper.listAll();
    }

    @Override
    public LeagueInfo get(int id) throws TException {
        return leagueMapper.get(id);
    }

}
