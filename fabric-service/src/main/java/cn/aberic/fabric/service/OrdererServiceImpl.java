package cn.aberic.fabric.service;

import cn.aberic.fabric.mapper.OrdererMapper;
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
        return 0;
    }

    @Override
    public int update(OrdererInfo ordererInfo) throws TException {
        return 0;
    }

    @Override
    public List<OrdererInfo> listAll() throws TException {
        return ordererMapper.listAll();
    }

    @Override
    public List<OrdererInfo> listById(int id) throws TException {
        return null;
    }

    @Override
    public OrdererInfo get(int id) throws TException {
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
