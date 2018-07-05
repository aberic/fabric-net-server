/*
 * Copyright (c) 2018. Aberic - aberic@qq.com - All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
