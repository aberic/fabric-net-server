package cn.aberic.fabric.service.impl;

import cn.aberic.fabric.dao.Chaincode;
import cn.aberic.fabric.dao.Channel;
import cn.aberic.fabric.dao.mapper.ChaincodeMapper;
import cn.aberic.fabric.dao.mapper.ChannelMapper;
import cn.aberic.fabric.service.ChannelService;
import cn.aberic.fabric.utils.DateUtil;
import cn.aberic.fabric.utils.FabricHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service("channelService")
public class ChannelServiceImpl implements ChannelService {

    @Resource
    private ChannelMapper channelMapper;
    @Resource
    private ChaincodeMapper chaincodeMapper;

    @Override
    public int add(Channel channel) {
        if (StringUtils.isEmpty(channel.getName())) {
            log.debug("channel name is empty");
            return 0;
        }
        if (null != channelMapper.check(channel)) {
            log.debug("had the same channel in this peer");
            return 0;
        }
        channel.setDate(DateUtil.getCurrent("yyyy年MM月dd日"));
        return channelMapper.add(channel);
    }

    @Override
    public int update(Channel channel) {
        FabricHelper.obtain().removeManager(channelMapper.list(channel.getPeerId()), chaincodeMapper);
        return channelMapper.update(channel);
    }

    @Override
    public List<Channel> listAll() {
        return channelMapper.listAll();
    }

    @Override
    public List<Channel> listById(int id) {
        return channelMapper.list(id);
    }

    @Override
    public Channel get(int id) {
        return channelMapper.get(id);
    }

    @Override
    public int countById(int id) {
        return channelMapper.count(id);
    }

    @Override
    public int count() {
        return channelMapper.countAll();
    }

    @Override
    public int delete(int id) {
        List<Chaincode> chaincodes = chaincodeMapper.list(id);
        for (Chaincode chaincode: chaincodes) {
            chaincodeMapper.delete(chaincode.getId());
        }
        return channelMapper.delete(id);
    }
}
