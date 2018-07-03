package cn.aberic.fabric.service.impl;

import cn.aberic.fabric.dao.*;
import cn.aberic.fabric.dao.mapper.*;
import cn.aberic.fabric.service.LeagueService;
import cn.aberic.fabric.utils.DateUtil;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service("leagueService")
public class LeagueServiceImpl implements LeagueService {

    @Resource
    private LeagueMapper leagueMapper;
    @Resource
    private OrgMapper orgMapper;
    @Resource
    private PeerMapper peerMapper;
    @Resource
    private OrdererMapper ordererMapper;
    @Resource
    private ChannelMapper channelMapper;
    @Resource
    private ChaincodeMapper chaincodeMapper;

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

    @Override
    public int delete(int id) {
        List<Org> orgs = orgMapper.list(id);
        for (Org org: orgs) {
            List<Peer> peers = peerMapper.list(org.getId());
            for (Peer peer : peers) {
                List<Channel> channels = channelMapper.list(peer.getId());
                for (Channel channel : channels) {
                    List<Chaincode> chaincodes = chaincodeMapper.list(channel.getId());
                    for (Chaincode chaincode : chaincodes) {
                        chaincodeMapper.delete(chaincode.getId());
                    }
                    channelMapper.delete(channel.getId());
                }
                peerMapper.delete(peer.getId());
            }
            List<Orderer> orderers = ordererMapper.list(org.getId());
            for (Orderer orderer : orderers) {
                ordererMapper.delete(orderer.getId());
            }
            orgMapper.delete(org.getId());
        }
        return leagueMapper.delete(id);
    }

}
