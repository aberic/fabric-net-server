package cn.aberic.fabric.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

public class CacheUtil {

    private static Cache<String, String> cacheString = CacheBuilder.newBuilder().maximumSize(5)
            .expireAfterAccess(30, TimeUnit.MINUTES).build();

    public static void put(String key, String value) {
        cacheString.put(key, value);
    }

    public static String get(String key) {
        try {
            return cacheString.getIfPresent(key);
        } catch (Exception e) {
            return "";
        }
    }

    public static void remove(String key) {
        cacheString.invalidate(key);
    }

}
