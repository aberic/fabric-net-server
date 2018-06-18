package cn.aberic.fabric.module.service;

import cn.aberic.fabric.base.BaseService;
import cn.aberic.fabric.base.DTOService;
import cn.aberic.fabric.module.bean.dto.OrgDTO;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/4 10:27】
 */
public interface OrgService extends BaseService, DTOService<OrgDTO> {

    /**
     * 根据id查询总数
     *
     * @param id id
     *
     * @return 总数
     */
    int count(int id);


}
