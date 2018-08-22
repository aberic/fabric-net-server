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

import cn.aberic.fabric.dao.entity.App;
import cn.aberic.fabric.bean.Home;
import cn.aberic.fabric.dao.entity.CA;
import cn.aberic.fabric.dao.entity.Peer;
import cn.aberic.fabric.dao.entity.User;
import cn.aberic.fabric.dao.mapper.AppMapper;
import cn.aberic.fabric.dao.mapper.CAMapper;
import cn.aberic.fabric.dao.mapper.PeerMapper;
import cn.aberic.fabric.sdk.FabricManager;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class CacheUtil {

    private static Cache<String, String> cacheString = CacheBuilder.newBuilder().maximumSize(1000)
            .expireAfterAccess(12, TimeUnit.HOURS).build();

    /** 存储 flag，ca */
    private static Cache<String, CA> cacheFlagCA = CacheBuilder.newBuilder().maximumSize(1000)
            .expireAfterAccess(12, TimeUnit.HOURS).build();

    /** 存储 app，bool */
    private static Cache<String, Boolean> cacheAppBool = CacheBuilder.newBuilder().maximumSize(1000)
            .expireAfterAccess(12, TimeUnit.HOURS).build();

    /** 存储 cc，fabric-manager*/
    private static Cache<String, FabricManager> cacheStringFabric = CacheBuilder.newBuilder().maximumSize(1000)
            .expireAfterAccess(12, TimeUnit.HOURS).build();

    /** 存储 channelId，fabric-manager*/
    private static Cache<Integer, FabricManager> cacheIntegerFabric = CacheBuilder.newBuilder().maximumSize(1000)
            .expireAfterAccess(12, TimeUnit.HOURS).build();

    /** 存储 channelId，fabric-manager*/
    private static Cache<String, Home> cacheHome = CacheBuilder.newBuilder().maximumSize(1)
            .expireAfterAccess(5, TimeUnit.MINUTES).build();

    private static Cache<String, User> cacheUser = CacheBuilder.newBuilder().maximumSize(1000)
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
            if (null == app) {
                return false;
            }
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


    static void putStringFabric(String key, FabricManager value) {
        cacheStringFabric.put(key, value);
    }

    static FabricManager getStringFabric(String key) {
        try {
            return cacheStringFabric.getIfPresent(key);
        } catch (Exception e) {
            return null;
        }
    }

    static void removeStringFabric(String key) {
        cacheStringFabric.invalidate(key);
    }

    static void putIntegerFabric(int key, FabricManager value) {
        cacheIntegerFabric.put(key, value);
    }

    static FabricManager getIntegerFabric(int key) {
        try {
            return cacheIntegerFabric.getIfPresent(key);
        } catch (Exception e) {
            return null;
        }
    }

    static void removeIntegerFabric(int key) {
        cacheIntegerFabric.invalidate(key);
    }

    public static void putHome(Home value) {
        cacheHome.put("do-home-cache", value);
    }

    public static Home getHome() {
        try {
            return cacheHome.getIfPresent("do-home-cache");
        } catch (Exception e) {
            return null;
        }
    }

    public static void removeHome() {
        cacheHome.invalidate("do-home-cache");
    }

    public static void putUser(String key, User value) {
        cacheUser.put(key, value);
    }

    public static User getUser(String key) {
        try {
            return cacheUser.getIfPresent(key);
        } catch (Exception e) {
            return null;
        }
    }

    public static void removeUser(String key) {
        cacheUser.invalidate(key);
    }

}
