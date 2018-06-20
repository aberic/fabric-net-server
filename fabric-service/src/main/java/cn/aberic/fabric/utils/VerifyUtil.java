package cn.aberic.fabric.utils;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * 作者：Aberic on 2018/6/17 18:07
 * 邮箱：abericyang@gmail.com
 */
public class VerifyUtil {

    /**
     * 判断字符串类型
     *
     * @param str 字符串
     *
     * @return 0-string；1-JsonObject；2、JsonArray
     */
    public static int isJSONValid(String str) {
        try {
            JSONObject.parseObject(str);
            return 1;
        } catch (JSONException ex) {
            try {
                JSONObject.parseArray(str);
                return 2;
            } catch (JSONException ex1) {
                return 0;
            }
        }
    }

}
