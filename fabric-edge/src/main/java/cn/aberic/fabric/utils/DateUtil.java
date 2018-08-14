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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 一般日期（时间）处理通用公共方法类
 * 
 * @author Aberic
 */
public class DateUtil {

	/**
	 * 获取当前时间
	 *
	 * @param format
	 *            (yyyy年MM月dd日 HH时mm分ss秒|yyyy年MM月dd日
	 *            HH时mm分ss秒|yyyyMMddHHmmss|HH:mm:ss|yyyy年MM月dd日 等)
	 *
	 * @return 当前时间格式
	 */
	public static String getCurrent(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
		return sdf.format(new Date());
	}

	/**
	 * 将字符串转换为日期
	 *
	 * @param dateStr
	 *            实际日期字符串
	 * @param format
	 *            指定日期字符串格式
	 *
	 * @return date
	 */
	public static Date str2Date(String dateStr, String format) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
		return sdf.parse(dateStr);
	}

	public static String strDateFormat(String dateStr, String formatBefore, String formatAfter) throws Exception {
		return date2Str(str2Date(dateStr, formatBefore), formatAfter);
	}

	/**
	 * 将日期转换为字符串
	 *
	 * @param date
	 *            date日期
	 * @param format
	 *            日期格式
	 *
	 * @return 日期字符串
	 */
	public static String date2Str(Date date, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
			return sdf.format(date);
		} catch (Exception ex) {
			return "";
		}
	}

}
