package cn.aberic.fabric.service;

import cn.aberic.fabric.mapper.PeerMapper;
import cn.aberic.fabric.utils.DateUtil;
import cn.aberic.thrift.peer.PeerInfo;
import cn.aberic.thrift.peer.PeerService;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service("peerService")
public class PeerServiceImpl implements PeerService.Iface {

    @Resource
    private PeerMapper peerMapper;


    @Override
    public int add(PeerInfo peerInfo) throws TException {
        if (StringUtils.isEmpty(peerInfo.getName()) ||
                StringUtils.isEmpty(peerInfo.getLocation()) ||
                StringUtils.isEmpty(peerInfo.getEventHubName()) ||
                StringUtils.isEmpty(peerInfo.getEventHubLocation())) {
            return 0;
        }
        peerInfo.setDate(DateUtil.getCurrent("yyyy年MM月dd日"));
        return peerMapper.add(peerInfo);
    }

    @Override
    public int update(PeerInfo peerInfo) throws TException {
        return peerMapper.update(peerInfo);
    }

    @Override
    public List<PeerInfo> listAll() throws TException {
        return peerMapper.listAll();
    }

    @Override
    public List<PeerInfo> listById(int id) throws TException {
        return peerMapper.list(id);
    }

    @Override
    public PeerInfo get(int id) throws TException {
        return peerMapper.get(id);
    }

    @Override
    public int countById(int id) throws TException {
        return peerMapper.count(id);
    }

    @Override
    public int count() throws TException {
        return peerMapper.countAll();
    }
}
