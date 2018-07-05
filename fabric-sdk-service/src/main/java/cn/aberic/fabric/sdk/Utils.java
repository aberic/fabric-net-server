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

package cn.aberic.fabric.sdk;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 描述：SDK通用工具类
 *
 * @author : Aberic 【2018/5/10 11:31】
 */
class Utils {

    /**
     * 将日期转换为字符串
     *
     * @param date
     *            date日期
     *
     * @return 日期字符串
     */
    static String parseDateFormat(Date date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA);
            return sdf.format(date);
        } catch (Exception ex) {
            return "";
        }
    }

}
