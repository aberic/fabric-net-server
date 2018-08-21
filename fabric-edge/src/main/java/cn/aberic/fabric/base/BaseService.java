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

import com.alibaba.fastjson.JSONObject;

/**
 * 业务层基类
 *
 * @author 杨毅 【2017年10月24日 - 20:58:52】
 */
public interface BaseService {

    int FAIL = 9999;

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

}
