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

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {

    /**
     * 将字符串md5散列值24位
     *
     * @param content
     *            散列值内容
     * @return 散列值结果
     */
    public static String md516(String content) {
        String str = md5(content);
        return str.substring(8, 24);
    }

    /**
     * MD5方法
     *
     * @param text 明文
     * @return 密文
     */
    public static String md5(String text) {
        //加密后的字符串
        return DigestUtils.md5Hex(text);
    }

    /**
     * MD5验证方法
     *
     * @param text 明文
     * @param md5  密文
     * @return true/false
     */
    public static boolean verify(String text, String md5) {
        //根据传入的密钥进行验证
        String md5Text = md5(text);
        return md5Text.equalsIgnoreCase(md5);
    }

}
