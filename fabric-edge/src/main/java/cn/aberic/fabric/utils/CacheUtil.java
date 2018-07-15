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

import cn.aberic.fabric.bean.App;
import cn.aberic.fabric.dao.CA;
import cn.aberic.fabric.dao.Peer;
import cn.aberic.fabric.dao.mapper.AppMapper;
import cn.aberic.fabric.dao.mapper.CAMapper;
import cn.aberic.fabric.dao.mapper.PeerMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class CacheUtil {

    private static Cache<String, String> cacheString = CacheBuilder.newBuilder().maximumSize(10)
            .expireAfterAccess(30, TimeUnit.MINUTES).build();

    /** 存储 flag，ca */
    private static Cache<String, CA> cacheFlagCA = CacheBuilder.newBuilder().maximumSize(20)
            .expireAfterAccess(30, TimeUnit.MINUTES).build();

    /** 存储 app，bool */
    private static Cache<String, Boolean> cacheAppBool = CacheBuilder.newBuilder().maximumSize(20)
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

    private static void putFlagCA(String flag, CA ca) {
        cacheFlagCA.put(flag, ca);
    }

    public static CA getFlagCA(String flag, CAMapper caMapper) {
        CA ca = cacheFlagCA.getIfPresent(flag);
        if (null == ca) {
            ca = caMapper.getByFlag(flag);
            if (null == ca) {
                return null;
            } else {
                putFlagCA(flag, ca);
            }
        }
        return ca;
    }

    public static void removeFlagCA(int leagueId, PeerMapper peerMapper, CAMapper caMapper) {
        List<Peer> peers = peerMapper.list(leagueId);
        for (Peer peer : peers) {
            removeFlagCA(peer.getId(), caMapper);
        }
    }

    public static void removeFlagCA(int peerId, CAMapper caMapper) {
        List<CA> cas = caMapper.list(peerId);
        for (CA ca : cas) {
            removeFlagCA(ca.getFlag());
        }
    }

    public static void removeFlagCA(String flag) {
        cacheFlagCA.invalidate(flag);
    }

    private static void putAppBool(String key, boolean value) {
        cacheAppBool.put(key, value);
    }

    static boolean getAppBool(String key, AppMapper appMapper) {
        try {
            return cacheAppBool.getIfPresent(key);
        } catch (Exception e) {
            App app = appMapper.getByKey(key);
            boolean flag = app.isActive();
            if (flag) {
                putAppBool(key, true);
            } else {
                putAppBool(key, false);
            }
            return flag;
        }
    }

    public static void removeAppBool(String key) {
        cacheAppBool.invalidate(key);
    }

}
