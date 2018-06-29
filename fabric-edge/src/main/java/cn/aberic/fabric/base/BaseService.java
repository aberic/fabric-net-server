package cn.aberic.fabric.base;

import cn.aberic.fabric.utils.VerifyUtil;
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
        JSONObject jsonObject = parseResult(result);
        jsonObject.put("code", SUCCESS);
        return jsonObject.toString();
    }

    default JSONObject responseSuccessJson(String result) {
        JSONObject jsonObject = parseResult(result);
        jsonObject.put("code", SUCCESS);
        return jsonObject;
    }

    default String responseSuccess(String result, String txid) {
        JSONObject jsonObject = parseResult(result);
        jsonObject.put("code", SUCCESS);
        jsonObject.put("txid", txid);
        return jsonObject.toString();
    }

    default JSONObject responseSuccessJson(String result, String txid) {
        JSONObject jsonObject = parseResult(result);
        jsonObject.put("code", SUCCESS);
        jsonObject.put("txid", txid);
        return jsonObject;
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

    default JSONObject responseFailJson(String result) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", FAIL);
        jsonObject.put("error", result);
        return jsonObject;
    }

    default JSONObject parseResult(String result) {
        JSONObject jsonObject = new JSONObject();
        int jsonVerify = VerifyUtil.isJSONValid(result);
        switch (jsonVerify) {
            case 0:
                jsonObject.put("data", result);
                break;
            case 1:
                jsonObject.put("data", JSONObject.parseObject(result));
                break;
            case 2:
                jsonObject.put("data", JSONObject.parseArray(result));
                break;
        }
        return jsonObject;
    }

}
