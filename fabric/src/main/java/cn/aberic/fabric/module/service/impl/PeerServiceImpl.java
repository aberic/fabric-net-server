package cn.aberic.fabric.module.service.impl;

import cn.aberic.fabric.module.bean.dto.PeerDTO;
import cn.aberic.fabric.module.mapper.PeerMapper;
import cn.aberic.fabric.module.service.PeerService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/4 15:03】
 */
@Service("peerService")
public class PeerServiceImpl implements PeerService {

    @Resource
    private PeerMapper peerMapper;

    @Override
    public String add(PeerDTO peer) {
        if (peerMapper.add(peer) > 0) {
            return responseSuccess(peer.toString());
        }
        return responseFail("add peer fail");
    }

    @Override
    public String list(int id) {
        return responseSuccess(JSONArray.parseArray(JSON.toJSONString(peerMapper.list(id))));
    }

    @Override
    public String update(PeerDTO peer) {
        if (peerMapper.update(peer) > 0) {
            return responseSuccess(peer.toString());
        }
        return responseFail("update peer fail");
    }

    @Override
    public String get(int id) {
        return responseSuccess(JSON.toJSONString(peerMapper.get(id)));
    }
}
