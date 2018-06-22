package cn.aberic.fabric.service;

import cn.aberic.fabric.utils.SystemUtil;
import cn.aberic.thrift.common.SystemInfo;
import cn.aberic.thrift.common.SystemService;
import org.apache.thrift.TException;
import org.hyperic.sigar.SigarException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service("systemService")
public class CommonServiceImpl implements SystemService.Iface {


    @Override
    public SystemInfo get() throws TException {
        SystemInfo systemInfo = null;
        try {
            systemInfo = SystemUtil.get();
        } catch (SigarException e) {
            e.printStackTrace();
        }
        return systemInfo;
    }

    @Override
    public boolean init() throws TException {
        try {
            SystemUtil.initSigar();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isInit() throws TException {
        return SystemUtil.isIsInit();
    }
}
