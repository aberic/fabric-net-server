package cn.aberic.fabric.module.service.impl;

import cn.aberic.fabric.module.bean.dto.OrgDTO;
import cn.aberic.fabric.module.mapper.OrgMapper;
import cn.aberic.fabric.module.service.OrgService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/4 15:03】
 */
@Service("orgService")
public class OrgServiceImpl implements OrgService {

    @Resource
    private OrgMapper orgMapper;

    @Override
    public String add(OrgDTO org) {
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
    public String get(int id) {
        return responseSuccess(JSON.toJSONString(orgMapper.get(id)));
    }
}
