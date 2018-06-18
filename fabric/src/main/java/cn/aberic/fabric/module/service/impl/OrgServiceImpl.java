package cn.aberic.fabric.module.service.impl;

import cn.aberic.fabric.module.bean.dto.OrgDTO;
import cn.aberic.fabric.module.bean.vo.OrgVO;
import cn.aberic.fabric.module.mapper.LeagueMapper;
import cn.aberic.fabric.module.mapper.OrdererMapper;
import cn.aberic.fabric.module.mapper.OrgMapper;
import cn.aberic.fabric.module.mapper.PeerMapper;
import cn.aberic.fabric.module.service.OrgService;
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
@Service("orgService")
public class OrgServiceImpl implements OrgService {

    @Resource
    private LeagueMapper leagueMapper;
    @Resource
    private OrgMapper orgMapper;
    @Resource
    private OrdererMapper ordererMapper;
    @Resource
    private PeerMapper peerMapper;

    @Override
    public String add(OrgDTO org) {
        org.setDate(DateUtil.getCurrent("yyyy年MM月dd日"));
        if (orgMapper.add(org) > 0) {
            return responseSuccess(org.toString());
        }
        return responseFail("add org fail");
    }

    @Override
    public String update(OrgDTO org) {
        if (orgMapper.update(org) > 0) {
            return responseSuccess(org.toString());
        }
        return responseFail("update org fail");
    }

    @Override
    public String list(int id) {
        return responseSuccess(JSONArray.parseArray(JSON.toJSONString(orgMapper.list(id))));
    }

    @Override
    public String listAll() {
        List<OrgDTO> orgDTOS = orgMapper.listAll();
        List<OrgVO> orgVOS = new ArrayList<>();
        for (OrgDTO orgDTO : orgDTOS) {
            OrgVO orgVO = new OrgVO();
            BeanUtils.copyProperties(orgDTO, orgVO);
            orgVO.setLeagueName(leagueMapper.get(orgVO.getLeagueId()).getName());
            orgVO.setOrdererCount(ordererMapper.count(orgVO.getId()));
            orgVO.setPeerCount(peerMapper.count(orgVO.getId()));
            orgVOS.add(orgVO);
        }
        return responseSuccess(JSONArray.parseArray(JSON.toJSONString(orgVOS)));
    }

    @Override
    public String get(int id) {
        return responseSuccess(JSON.toJSONString(orgMapper.get(id)));
    }

    @Override
    public int count(int id) {
        return orgMapper.count(id);
    }
}
