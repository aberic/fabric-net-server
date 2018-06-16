package cn.aberic.fabric.module.service.impl;

import cn.aberic.fabric.module.bean.dto.OrdererDTO;
import cn.aberic.fabric.module.mapper.OrdererMapper;
import cn.aberic.fabric.module.service.OrdererService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/4 15:03】
 */
@Service("ordererService")
public class OrdererServiceImpl implements OrdererService {

    @Resource
    private OrdererMapper ordererMapper;

   @Override
    public String add(OrdererDTO orderer) {
        if (ordererMapper.add(orderer) > 0) {
            return responseSuccess(orderer.toString());
        }
        return responseFail("add orderer fail");
    }

    @Override
    public String list(int id) {
        return responseSuccess(JSONArray.parseArray(JSON.toJSONString(ordererMapper.list(id))));
    }

    @Override
    public String update(OrdererDTO orderer) {
        if (ordererMapper.update(orderer) > 0) {
            return responseSuccess(orderer.toString());
        }
        return responseFail("update orderer fail");
    }

    @Override
    public String get(int id) {
        return responseSuccess(JSON.toJSONString(ordererMapper.get(id)));
    }
}
