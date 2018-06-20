package cn.aberic.fabric.service;

import cn.aberic.fabric.mapper.LeagueMapper;
import cn.aberic.thrift.league.LeagueInfo;
import cn.aberic.thrift.league.LeagueService;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("leagueService")
public class LeagueServiceImpl implements LeagueService.Iface {

    @Resource
    private LeagueMapper leagueMapper;

    @Override
    public int add(LeagueInfo leagueInfo) throws TException {
        return 0;
    }

    @Override
    public int update(LeagueInfo leagueInfo) throws TException {
        return 0;
    }

    @Override
    public List<LeagueInfo> listAll() throws TException {
        return leagueMapper.listAll();
    }

    @Override
    public LeagueInfo get(int id) throws TException {
        return null;
    }

}
