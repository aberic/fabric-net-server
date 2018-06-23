package cn.aberic.fabric.service;

import cn.aberic.fabric.mapper.ChaincodeMapper;
import cn.aberic.fabric.mapper.ChannelMapper;
import cn.aberic.fabric.mapper.OrdererMapper;
import cn.aberic.fabric.mapper.PeerMapper;
import cn.aberic.fabric.utils.DateUtil;
import cn.aberic.fabric.utils.FabricHelper;
import cn.aberic.thrift.orderer.OrdererInfo;
import cn.aberic.thrift.orderer.OrdererService;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service("ordererService")
public class OrdererServiceImpl implements OrdererService.Iface {

    @Resource
    private OrdererMapper ordererMapper;
    @Resource
    private PeerMapper peerMapper;
    @Resource
    private ChannelMapper channelMapper;
    @Resource
    private ChaincodeMapper chaincodeMapper;

    @Override
    public int add(OrdererInfo ordererInfo) throws TException {
        if (StringUtils.isEmpty(ordererInfo.getName()) ||
                StringUtils.isEmpty(ordererInfo.getLocation())) {
            return 0;
        }
        ordererInfo.setDate(DateUtil.getCurrent("yyyy年MM月dd日"));
        return ordererMapper.add(ordererInfo);
    }

    @Override
    public int update(OrdererInfo ordererInfo) throws TException {
        FabricHelper.obtain().removeManager(peerMapper.list(ordererInfo.getOrgId()), channelMapper, chaincodeMapper);
        return ordererMapper.update(ordererInfo);
    }

    @Override
    public List<OrdererInfo> listAll() throws TException {
        return ordererMapper.listAll();
    }

    @Override
    public List<OrdererInfo> listById(int id) throws TException {
        return ordererMapper.list(id);
    }

    @Override
    public OrdererInfo get(int id) throws TException {
        return ordererMapper.get(id);
    }

    @Override
    public int countById(int id) throws TException {
        return ordererMapper.count(id);
    }

    @Override
    public int count() throws TException {
        return ordererMapper.countAll();
    }
}
