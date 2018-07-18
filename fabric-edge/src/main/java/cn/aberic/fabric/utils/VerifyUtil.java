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

package cn.aberic.fabric.utils;

import cn.aberic.fabric.base.BaseChain;
import cn.aberic.fabric.dao.mapper.AppMapper;
import cn.aberic.fabric.dao.mapper.ChaincodeMapper;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * 作者：Aberic on 2018/6/17 18:07
 * 邮箱：abericyang@gmail.com
 */
public class VerifyUtil {

    /** 判断key有效性 */
    public static String getCc(BaseChain baseChain, ChaincodeMapper chaincodeMapper, AppMapper appMapper) {
        String cc = null;
        if (CacheUtil.getAppBool(baseChain.getKey(), appMapper)) {
            cc = CacheUtil.getString(baseChain.getKey());
            if (StringUtils.isEmpty(cc)) {
                try {
                    cc = chaincodeMapper.get(appMapper.getByKey(baseChain.getKey()).getChaincodeId()).getCc();
                    CacheUtil.putString(baseChain.getKey(), cc);
                } catch (Exception e) {
                    cc = null;
                }
            }
        }
        return cc;
    }

    public static List<String> versions() {
        List<String> versions = new LinkedList<>();
        versions.add("1.0");
        versions.add("1.1");
        versions.add("1.2");
        return versions;
    }

}
