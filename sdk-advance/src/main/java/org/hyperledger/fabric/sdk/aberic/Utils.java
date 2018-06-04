package org.hyperledger.fabric.sdk.aberic;

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
