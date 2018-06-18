package cn.aberic.fabric.module.service.impl;

import cn.aberic.fabric.module.bean.dto.OrdererDTO;
import cn.aberic.fabric.module.bean.vo.OrdererVO;
import cn.aberic.fabric.module.mapper.OrdererMapper;
import cn.aberic.fabric.module.mapper.OrgMapper;
import cn.aberic.fabric.module.service.OrdererService;
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
@Service("ordererService")
public class OrdererServiceImpl implements OrdererService {

    @Resource
    private OrgMapper orgMapper;
    @Resource
    private OrdererMapper ordererMapper;

    @Override
    public String add(OrdererDTO orderer) {
        orderer.setDate(DateUtil.getCurrent("yyyy年MM月dd日"));
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
    public String listAll() {
        List<OrdererDTO> ordererDTOS = ordererMapper.listAll();
        List<OrdererVO> ordererVOS = new ArrayList<>();
        for (OrdererDTO ordererDTO : ordererDTOS) {
            OrdererVO ordererVO = new OrdererVO();
            BeanUtils.copyProperties(ordererDTO, ordererVO);
            ordererVO.setOrgName(orgMapper.get(ordererVO.getOrgId()).getName());
            ordererVOS.add(ordererVO);
        }
        return responseSuccess(JSONArray.parseArray(JSON.toJSONString(ordererVOS)));
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
