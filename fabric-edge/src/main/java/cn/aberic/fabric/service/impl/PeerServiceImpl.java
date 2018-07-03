package cn.aberic.fabric.service.impl;

import cn.aberic.fabric.dao.Chaincode;
import cn.aberic.fabric.dao.Channel;
import cn.aberic.fabric.dao.Peer;
import cn.aberic.fabric.dao.mapper.ChaincodeMapper;
import cn.aberic.fabric.dao.mapper.ChannelMapper;
import cn.aberic.fabric.dao.mapper.PeerMapper;
import cn.aberic.fabric.service.PeerService;
import cn.aberic.fabric.utils.DateUtil;
import cn.aberic.fabric.utils.FabricHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service("peerService")
public class PeerServiceImpl implements PeerService {

    @Resource
    private PeerMapper peerMapper;
    @Resource
    private ChannelMapper channelMapper;
    @Resource
    private ChaincodeMapper chaincodeMapper;


    @Override
    public int add(Peer peer) {
        if (StringUtils.isEmpty(peer.getName()) ||
                StringUtils.isEmpty(peer.getLocation()) ||
                StringUtils.isEmpty(peer.getEventHubName()) ||
                StringUtils.isEmpty(peer.getEventHubLocation())) {
            return 0;
        }
        peer.setDate(DateUtil.getCurrent("yyyy年MM月dd日"));
        return peerMapper.add(peer);
    }

    @Override
    public int update(Peer peer) {
        FabricHelper.obtain().removeManager(peerMapper.list(peer.getOrgId()), channelMapper, chaincodeMapper);
        return peerMapper.update(peer);
    }

    @Override
    public List<Peer> listAll() {
        return peerMapper.listAll();
    }

    @Override
    public List<Peer> listById(int id) {
        return peerMapper.list(id);
    }

    @Override
    public Peer get(int id) {
        return peerMapper.get(id);
    }

    @Override
    public int countById(int id) {
        return peerMapper.count(id);
    }

    @Override
    public int count() {
        return peerMapper.countAll();
    }

    @Override
    public int delete(int id) {
        List<Channel> channels = channelMapper.list(id);
        for (Channel channel: channels) {
            List<Chaincode> chaincodes = chaincodeMapper.list(channel.getId());
            for (Chaincode chaincode: chaincodes) {
                chaincodeMapper.delete(chaincode.getId());
            }
            channelMapper.delete(channel.getId());
        }
        return peerMapper.delete(id);
    }
}
