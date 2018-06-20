package cn.aberic.fabric.service;

import cn.aberic.fabric.mapper.PeerMapper;
import cn.aberic.thrift.peer.PeerInfo;
import cn.aberic.thrift.peer.PeerService;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("peerService")
public class PeerServiceImpl implements PeerService.Iface {

    @Resource
    private PeerMapper peerMapper;


    @Override
    public int add(PeerInfo peerInfo) throws TException {
        return 0;
    }

    @Override
    public int update(PeerInfo peerInfo) throws TException {
        return 0;
    }

    @Override
    public List<PeerInfo> listAll() throws TException {
        return peerMapper.listAll();
    }

    @Override
    public List<PeerInfo> listById(int id) throws TException {
        return null;
    }

    @Override
    public PeerInfo get(int id) throws TException {
        return null;
    }

    @Override
    public int countById(int id) throws TException {
        return 0;
    }

    @Override
    public int count() throws TException {
        return 0;
    }
}
