package cn.aberic.fabric.module.service.impl;

import cn.aberic.fabric.module.bean.dto.PeerDTO;
import cn.aberic.fabric.module.bean.vo.PeerVO;
import cn.aberic.fabric.module.mapper.ChannelMapper;
import cn.aberic.fabric.module.mapper.OrgMapper;
import cn.aberic.fabric.module.mapper.PeerMapper;
import cn.aberic.fabric.module.service.PeerService;
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
@Service("peerService")
public class PeerServiceImpl implements PeerService {

    @Resource
    private OrgMapper orgMapper;
    @Resource
    private PeerMapper peerMapper;
    @Resource
    private ChannelMapper channelMapper;

    @Override
    public String add(PeerDTO peer) {
        peer.setDate(DateUtil.getCurrent("yyyy年MM月dd日"));
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
    public String listAll() {
        List<PeerDTO> peerDTOS = peerMapper.listAll();
        List<PeerVO> peerVOS = new ArrayList<>();
        for (PeerDTO peerDTO : peerDTOS) {
            PeerVO peerVO = new PeerVO();
            BeanUtils.copyProperties(peerDTO, peerVO);
            peerVO.setOrgName(orgMapper.get(peerVO.getOrgId()).getName());
            peerVO.setCount(channelMapper.count(peerVO.getId()));
            peerVOS.add(peerVO);
        }
        return responseSuccess(JSONArray.parseArray(JSON.toJSONString(peerVOS)));
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
