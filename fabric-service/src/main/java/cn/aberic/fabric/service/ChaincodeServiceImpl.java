package cn.aberic.fabric.service;

import cn.aberic.fabric.mapper.ChaincodeMapper;
import cn.aberic.thrift.chaincode.ChaincodeInfo;
import cn.aberic.thrift.chaincode.ChaincodeService;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("chaincodeService")
public class ChaincodeServiceImpl implements ChaincodeService.Iface {

    @Resource
    private ChaincodeMapper chaincodeMapper;

    @Override
    public int add(ChaincodeInfo chaincodeInfo) throws TException {
        return 0;
    }

    @Override
    public int update(ChaincodeInfo chaincodeInfo) throws TException {
        return 0;
    }

    @Override
    public List<ChaincodeInfo> listAll() throws TException {
        return chaincodeMapper.listAll();
    }

    @Override
    public List<ChaincodeInfo> listById(int id) throws TException {
        return null;
    }

    @Override
    public ChaincodeInfo get(int id) throws TException {
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
