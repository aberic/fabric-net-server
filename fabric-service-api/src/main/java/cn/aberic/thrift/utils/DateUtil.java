package cn.aberic.thrift.utils;

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
