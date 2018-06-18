package cn.aberic.fabric.module.service.impl;

import cn.aberic.fabric.module.bean.dto.ChainCodeDTO;
import cn.aberic.fabric.module.bean.vo.ChainCodeVO;
import cn.aberic.fabric.module.mapper.ChainCodeMapper;
import cn.aberic.fabric.module.mapper.ChannelMapper;
import cn.aberic.fabric.module.service.ChainCodeService;
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
@Service("chainCodeService")
public class ChainCodeServiceImpl implements ChainCodeService {

    @Resource
    private ChannelMapper channelMapper;
    @Resource
    private ChainCodeMapper chainCodeMapper;

    @Override
    public String add(ChainCodeDTO chainCode) {
        chainCode.setDate(DateUtil.getCurrent("yyyy年MM月dd日"));
        if (chainCodeMapper.add(chainCode) > 0) {
            return responseSuccess(chainCode.toString());
        }
        return responseFail("add chainCode fail");
    }

    @Override
    public String list(int id) {
        return responseSuccess(JSONArray.parseArray(JSON.toJSONString(chainCodeMapper.list(id))));
    }

    @Override
    public String listAll() {
        List<ChainCodeDTO> chainCodeDTOS = chainCodeMapper.listAll();
        List<ChainCodeVO> chainCodeVOS = new ArrayList<>();
        for (ChainCodeDTO chainCodeDTO : chainCodeDTOS) {
            ChainCodeVO chainCodeVO = new ChainCodeVO();
            BeanUtils.copyProperties(chainCodeDTO, chainCodeVO);
            chainCodeVO.setChannelName(channelMapper.get(chainCodeVO.getChannelId()).getName());
            chainCodeVOS.add(chainCodeVO);
        }
        return responseSuccess(JSONArray.parseArray(JSON.toJSONString(chainCodeVOS)));
    }

    @Override
    public String update(ChainCodeDTO chainCode) {
        if (chainCodeMapper.update(chainCode) > 0) {
            return responseSuccess(chainCode.toString());
        }
        return responseFail("update chainCode fail");
    }

    @Override
    public String get(int id) {
        return responseSuccess(JSON.toJSONString(chainCodeMapper.get(id)));
    }
}
