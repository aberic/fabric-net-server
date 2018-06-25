package cn.aberic.fabric.service;

import cn.aberic.fabric.mapper.*;
import cn.aberic.fabric.utils.FabricHelper;
import cn.aberic.fabric.utils.FileUtil;
import cn.aberic.thrift.org.OrgInfo;
import cn.aberic.thrift.org.OrgService;
import cn.aberic.thrift.utils.DateUtil;
import org.apache.thrift.TException;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.List;

@Service("orgService")
public class OrgServiceImpl implements OrgService.Iface {

    @Resource
    private OrgMapper orgMapper;
    @Resource
    private Environment env;
    @Resource
    private LeagueMapper leagueMapper;
    @Resource
    private PeerMapper peerMapper;
    @Resource
    private ChannelMapper channelMapper;
    @Resource
    private ChaincodeMapper chaincodeMapper;


    @Override
    public int add(OrgInfo orgInfo, ByteBuffer buff, String fileName) throws TException {
        if (StringUtils.isEmpty(orgInfo.getName()) ||
                StringUtils.isEmpty(orgInfo.getMspId()) ||
                StringUtils.isEmpty(orgInfo.getDomainName()) ||
                StringUtils.isEmpty(orgInfo.getOrdererDomainName()) ||
                StringUtils.isEmpty(orgInfo.getUsername())) {
            return 0;
        }
        orgInfo.setDate(DateUtil.getCurrent("yyyy年MM月dd日"));
        String path = String.format("%s/%s/%s", env.getProperty("config.dir"), leagueMapper.get(orgInfo.getLeagueId()).getName(), orgInfo.getName());
        orgInfo.setCryptoConfigDir(path + File.separator + fileName.split("\\.")[0]);
        FileUtil.save(buff, fileName, path, true);
        return orgMapper.add(orgInfo);
    }

    @Override
    public int update(OrgInfo orgInfo, ByteBuffer buff, String fileName) throws TException {
        if (null != buff) {
            String path = String.format("%s/%s/%s", env.getProperty("config.dir"), leagueMapper.get(orgInfo.getLeagueId()).getName(), orgInfo.getName());
            orgInfo.setCryptoConfigDir(path + File.separator + fileName.split("\\.")[0]);
            FileUtil.save(buff, fileName, path, true);
        }
        FabricHelper.obtain().removeManager(peerMapper.list(orgInfo.getId()), channelMapper, chaincodeMapper);
        return orgMapper.update(orgInfo);
    }

    @Override
    public List<OrgInfo> listAll() throws TException {
        return orgMapper.listAll();
    }

    @Override
    public List<OrgInfo> listById(int id) throws TException {
        return orgMapper.list(id);
    }

    @Override
    public OrgInfo get(int id) throws TException {
        return orgMapper.get(id);
    }

    @Override
    public int countById(int id) throws TException {
        return orgMapper.count(id);
    }

    @Override
    public int count() throws TException {
        return orgMapper.countAll();
    }

}
