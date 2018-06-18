package cn.aberic.fabric.module.service.impl;

import cn.aberic.fabric.module.bean.dto.ChannelDTO;
import cn.aberic.fabric.module.bean.vo.ChannelVO;
import cn.aberic.fabric.module.mapper.ChannelMapper;
import cn.aberic.fabric.module.mapper.PeerMapper;
import cn.aberic.fabric.module.service.ChannelService;
import cn.aberic.fabric.utils.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/4 15:03】
 */
@Service("channelService")
public class ChannelServiceImpl implements ChannelService {

    @Resource
    private PeerMapper peerMapper;
    @Resource
    private ChannelMapper channelMapper;

    @Override
    public String add(ChannelDTO channel) {
        channel.setDate(DateUtil.getCurrent("yyyy年MM月dd日"));
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
    public String listAll() {
        List<ChannelDTO> channelDTOS = channelMapper.listAll();
        List<ChannelVO> channelVOS = new ArrayList<>();
        for (ChannelDTO channelDTO : channelDTOS) {
            ChannelVO channelVO = new ChannelVO();
            BeanUtils.copyProperties(channelDTO, channelVO);
            channelVO.setPeerName(peerMapper.get(channelVO.getPeerId()).getName());
            channelVO.setCount(channelMapper.count(channelVO.getId()));
            channelVOS.add(channelVO);
        }
        return responseSuccess(JSONArray.parseArray(JSON.toJSONString(channelVOS)));
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
