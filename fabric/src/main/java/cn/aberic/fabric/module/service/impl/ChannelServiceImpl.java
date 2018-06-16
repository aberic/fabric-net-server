package cn.aberic.fabric.module.service.impl;

import cn.aberic.fabric.module.bean.dto.ChannelDTO;
import cn.aberic.fabric.module.mapper.ChannelMapper;
import cn.aberic.fabric.module.service.ChannelService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/4 15:03】
 */
@Service("channelService")
public class ChannelServiceImpl implements ChannelService {

    @Resource
    private ChannelMapper channelMapper;

   @Override
    public String add(ChannelDTO channel) {
        if (channelMapper.add(channel) > 0) {
            return responseSuccess(channel.toString());
        }
        return responseFail("add channel fail");
    }

    @Override
    public String list(int id) {
        return responseSuccess(JSONArray.parseArray(JSON.toJSONString(channelMapper.list(id))));
    }

    @Override
    public String update(ChannelDTO channel) {
        if (channelMapper.update(channel) > 0) {
            return responseSuccess(channel.toString());
        }
        return responseFail("update channel fail");
    }

    @Override
    public String get(int id) {
        return responseSuccess(JSON.toJSONString(channelMapper.get(id)));
    }
}
