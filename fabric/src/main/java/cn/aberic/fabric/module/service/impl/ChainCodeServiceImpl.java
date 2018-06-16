package cn.aberic.fabric.module.service.impl;

import cn.aberic.fabric.module.bean.dto.ChainCodeDTO;
import cn.aberic.fabric.module.mapper.ChainCodeMapper;
import cn.aberic.fabric.module.service.ChainCodeService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/4 15:03】
 */
@Service("chainCodeService")
public class ChainCodeServiceImpl implements ChainCodeService {

    @Resource
    private ChainCodeMapper chainCodeMapper;

    @Override
    public String add(ChainCodeDTO chainCode) {
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
