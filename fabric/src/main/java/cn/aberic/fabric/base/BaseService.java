package cn.aberic.fabric.base;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 业务层基类
 *
 * @author 杨毅 【2017年10月24日 - 20:58:52】
 */
public interface BaseService {

    int SUCCESS = 200;
    int FAIL = 9999;

    default String responseSuccess(String result) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", SUCCESS);
        jsonObject.put("data", result);
        return jsonObject.toString();
    }

    default String responseSuccess(String result, String txid) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", SUCCESS);
        jsonObject.put("data", result);
        jsonObject.put("txid", txid);
        return jsonObject.toString();
    }

    default String responseSuccess(JSONObject json) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", SUCCESS);
        jsonObject.put("data", json);
        return jsonObject.toString();
    }

    default String responseSuccess(JSONArray array) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", SUCCESS);
        jsonObject.put("data", array);
        return jsonObject.toString();
    }

    default String responseFail(String result) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", FAIL);
        jsonObject.put("error", result);
        return jsonObject.toString();
    }

}
