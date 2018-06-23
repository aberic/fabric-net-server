package cn.aberic.fabric.service;

import cn.aberic.fabric.mapper.ChaincodeMapper;
import cn.aberic.fabric.mapper.ChannelMapper;
import cn.aberic.fabric.mapper.PeerMapper;
import cn.aberic.fabric.utils.DateUtil;
import cn.aberic.fabric.utils.FabricHelper;
import cn.aberic.thrift.channel.ChannelInfo;
import cn.aberic.thrift.channel.ChannelService;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service("channelService")
public class ChannelServiceImpl implements ChannelService.Iface {

    @Resource
    private ChannelMapper channelMapper;
    @Resource
    private PeerMapper peerMapper;
    @Resource
    private ChaincodeMapper chaincodeMapper;

    @Override
    public int add(ChannelInfo channelInfo) throws TException {
        if (StringUtils.isEmpty(channelInfo.getName())) {
            return 0;
        }
        channelInfo.setDate(DateUtil.getCurrent("yyyy年MM月dd日"));
        return channelMapper.add(channelInfo);
    }

    @Override
    public int update(ChannelInfo channelInfo) throws TException {
        FabricHelper.obtain().removeManager(channelMapper.list(channelInfo.getPeerId()), chaincodeMapper);
        return channelMapper.update(channelInfo);
    }

    @Override
    public List<ChannelInfo> listAll() throws TException {
        return channelMapper.listAll();
    }

    @Override
    public List<ChannelInfo> listById(int id) throws TException {
        return channelMapper.list(id);
    }

    @Override
    public ChannelInfo get(int id) throws TException {
        return channelMapper.get(id);
    }

    @Override
    public int countById(int id) throws TException {
        return channelMapper.count(id);
    }

    @Override
    public int count() throws TException {
        return channelMapper.countAll();
    }
}
