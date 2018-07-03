package cn.aberic.fabric.service.impl;

import cn.aberic.fabric.dao.Orderer;
import cn.aberic.fabric.dao.mapper.ChaincodeMapper;
import cn.aberic.fabric.dao.mapper.ChannelMapper;
import cn.aberic.fabric.dao.mapper.OrdererMapper;
import cn.aberic.fabric.dao.mapper.PeerMapper;
import cn.aberic.fabric.service.OrdererService;
import cn.aberic.fabric.utils.DateUtil;
import cn.aberic.fabric.utils.FabricHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service("ordererService")
public class OrdererServiceImpl implements OrdererService {

    @Resource
    private OrdererMapper ordererMapper;
    @Resource
    private PeerMapper peerMapper;
    @Resource
    private ChannelMapper channelMapper;
    @Resource
    private ChaincodeMapper chaincodeMapper;

    @Override
    public int add(Orderer orderer) {
        if (StringUtils.isEmpty(orderer.getName()) ||
                StringUtils.isEmpty(orderer.getLocation())) {
            return 0;
        }
        orderer.setDate(DateUtil.getCurrent("yyyy年MM月dd日"));
        return ordererMapper.add(orderer);
    }

    @Override
    public int update(Orderer orderer) {
        FabricHelper.obtain().removeManager(peerMapper.list(orderer.getOrgId()), channelMapper, chaincodeMapper);
        return ordererMapper.update(orderer);
    }

    @Override
    public List<Orderer> listAll() {
        return ordererMapper.listAll();
    }

    @Override
    public List<Orderer> listById(int id) {
        return ordererMapper.list(id);
    }

    @Override
    public Orderer get(int id) {
        return ordererMapper.get(id);
    }

    @Override
    public int countById(int id) {
        return ordererMapper.count(id);
    }

    @Override
    public int count() {
        return ordererMapper.countAll();
    }

    @Override
    public int delete(int id) {
        return ordererMapper.delete(id);
    }
}
