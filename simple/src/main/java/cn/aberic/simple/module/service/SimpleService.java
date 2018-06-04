package cn.aberic.simple.module.service;

import cn.aberic.simple.base.BaseService;
import com.alibaba.fastjson.JSONObject;

/**
 * 描述：
 *
 * @author : Aberic 【2018/6/4 10:27】
 */
public interface SimpleService extends BaseService {

    /**
     * 合约接口
     *
     * @param json 合约JSON
     * @return 请求返回
     */
    String chainCode(JSONObject json);

    /**
     * 溯源接口
     *
     * @param json 溯源JSON
     * @return 请求返回
     */
    String trace(JSONObject json);

}
