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

import cn.aberic.fabric.dao.mapper.ChaincodeMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

public class CacheUtil {

    private static Cache<String, String> cacheString = CacheBuilder.newBuilder().maximumSize(10)
            .expireAfterAccess(30, TimeUnit.MINUTES).build();

    private static Cache<String, Integer> cacheKeyChaincodeId = CacheBuilder.newBuilder().maximumSize(100)
            .expireAfterAccess(30, TimeUnit.MINUTES).build();

    private static Cache<Integer, Boolean> cacheChaincodeId = CacheBuilder.newBuilder().maximumSize(20)
            .expireAfterAccess(30, TimeUnit.MINUTES).build();

    public static void putString(String key, String value) {
        cacheString.put(key, value);
    }

    public static String getString(String key) {
        try {
            return cacheString.getIfPresent(key);
        } catch (Exception e) {
            return "";
        }
    }

    public static void removeString(String key) {
        cacheString.invalidate(key);
    }

    public static void putKeyChaincodeId(String key, int value) {
        cacheKeyChaincodeId.put(key, value);
    }

    public static int getKeyChaincodeId(String key) {
        try {
            return cacheKeyChaincodeId.getIfPresent(key);
        } catch (Exception e) {
            return -1;
        }
    }

    public static void removeKeyChaincodeId(String key) {
        cacheKeyChaincodeId.invalidate(key);
    }

    public static void putChaincodeId(int key, boolean value) {
        cacheChaincodeId.put(key, value);
    }

    public static boolean getChaincodeId(int key, ChaincodeMapper chaincodeMapper) {
        try {
            return cacheChaincodeId.getIfPresent(key);
        } catch (Exception e) {
            boolean isOpen = chaincodeMapper.get(key).isOpen();
            putChaincodeId(key, isOpen);
            return isOpen;
        }
    }

    public static void removeChaincodeId(int key) {
        cacheChaincodeId.invalidate(key);
    }

}
