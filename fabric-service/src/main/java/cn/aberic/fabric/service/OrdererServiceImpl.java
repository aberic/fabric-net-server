package cn.aberic.fabric.service;

import cn.aberic.fabric.mapper.OrdererMapper;
import cn.aberic.fabric.utils.DateUtil;
import cn.aberic.thrift.orderer.OrdererInfo;
import cn.aberic.thrift.orderer.OrdererService;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("ordererService")
public class OrdererServiceImpl implements OrdererService.Iface {

    @Resource
    private OrdererMapper ordererMapper;

    @Override
    public int add(OrdererInfo ordererInfo) throws TException {
        ordererInfo.setDate(DateUtil.getCurrent("yyyy年MM月dd日"));
        return ordererMapper.add(ordererInfo);
    }

    @Override
    public int update(OrdererInfo ordererInfo) throws TException {
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
