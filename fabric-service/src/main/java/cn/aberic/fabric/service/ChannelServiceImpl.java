package cn.aberic.fabric.service;

import cn.aberic.fabric.mapper.ChannelMapper;
import cn.aberic.fabric.utils.DateUtil;
import cn.aberic.thrift.channel.ChannelInfo;
import cn.aberic.thrift.channel.ChannelService;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("channelService")
public class ChannelServiceImpl implements ChannelService.Iface {

    @Resource
    private ChannelMapper channelMapper;

    @Override
    public int add(ChannelInfo channelInfo) throws TException {
        channelInfo.setDate(DateUtil.getCurrent("yyyy年MM月dd日"));
        return channelMapper.add(channelInfo);
    }

    @Override
    public int update(ChannelInfo channelInfo) throws TException {
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
