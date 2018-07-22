package org.hyperledger.fabric.sdk.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 一般日期（时间）处理通用公共方法类
 * 
 * @author 杨毅
 *
 * @date 2017年10月19日 - 下午5:33:00
 * @email abericyang@gmail.com
 */
public class DateUtil {

	private static DateUtil instance;

	public static DateUtil obtain() {
		if (null == instance) {
			synchronized (DateUtil.class) {
				if (null == instance) {
					instance = new DateUtil();
				}
			}
		}
		return instance;
	}

	private DateUtil() {
	}

	// 使用DateFormat

	/**
	 * 获取当前时间
	 *
	 * @param format
	 *            (yyyy年MM月dd日 HH时mm分ss秒|yyyy年MM月dd日
	 *            HH时mm分ss秒|yyyyMMddHHmmss|HH:mm:ss|yyyy年MM月dd日 等)
	 *
	 * @return 当前时间格式
	 */
	public String getCurrentDate(String format) {
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
	public Date parseStringToDate(String dateStr, String format) throws Exception {
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
	public String parseDateFormat(Date date, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
			return sdf.format(date);
		} catch (Exception ex) {
			return "";
		}
	}

	/**
	 * 获得指定日期的前一天
	 *
	 * @param specifiedDay
	 *            指定日期
	 *
	 * @return 指定日期的前一天
	 */
	public String getSpecifiedDayBefore(String specifiedDay) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd", Locale.CHINA).parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - 1);

		return new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(c.getTime());
	}

	/**
	 * 返回日期中的指定部分--YYYY 年份，MM 月份，DD 天，HH 小时 MI 分 SS 秒
	 *
	 * @param date
	 *            date日期
	 * @param type
	 *            日期中的指定部分
	 *
	 * @return 日期中的指定部分值
	 */
	public int getDatePart(Date date, DatePartType type) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		switch (type) {
		case Year:
			return cal.get(Calendar.YEAR);
		case Month:
			return cal.get(Calendar.MONTH);
		case Day:
			return cal.get(Calendar.DAY_OF_MONTH);
		case Hour:
			return cal.get(Calendar.HOUR_OF_DAY);
		case Minute:
			return cal.get(Calendar.MINUTE);
		case Second:
			return cal.get(Calendar.SECOND);
		default:
			return 0;
		}
	}

	public enum DatePartType {
		Year, Month, Day, Hour, Minute, Second
	}

	/**
	 * 计算两个日期之间的时间差 传入的对象为两个日期,字符串和时间对象即可
	 *
	 * @param dateFrom
	 *            起始日期/字符串/时间对象
	 * @param dateTo
	 *            截止日期/字符串/时间对象
	 *
	 * @return 时间差
	 *
	 * @throws Exception
	 */
	public String dateDiff(Object dateFrom, Object dateTo) throws Exception {
		Date date1 = getDateByObject(dateFrom);
		Date date2 = getDateByObject(dateTo);
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date2);
		cal2.setTime(date1);
		float a = ((float) (cal1.getTimeInMillis() - cal2.getTimeInMillis()) / 86400) / 1000;
		int b = (int) a;
		return "" + Math.abs(((b == a) ? b : b + 1));
	}

	/**
	 * 根据传入参数产生日期对象
	 *
	 * @param obj
	 *            传入参数
	 *
	 * @return Date
	 *
	 * @throws Exception
	 */
	public Date getDateByObject(Object obj) throws Exception {
		if (null == obj) {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).parse(getCurrentDate(null));
		}

		if (obj instanceof Date) {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).parse(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(obj));
		}
		if (obj instanceof String) {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).parse((String) obj);
		}
		return null;
	}

	/**
	 * 获取当前年/月/日
	 *
	 * @return yyyy/MM/dd
	 */
	public String getNowDateFor(String dateFro) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFro, Locale.CHINA);
		return sdf.format(new Date());
	}

	/**
	 * 功能描述：返回小时
	 *
	 * @param date
	 *            日期
	 *
	 * @return 返回小时
	 */
	public int getHour(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 功能描述：将字符串转换为long型
	 *
	 * @param string
	 *            日期
	 * @param pattern
	 *            日期格式
	 *
	 * @return 返回long型时间
	 */
	public long parseDate(String string, String pattern) {
		SimpleDateFormat timeLine = new SimpleDateFormat(pattern, Locale.CHINA);
		long l = 0;
		try {
			l = timeLine.parse(string).getTime();
			return l;
		} catch (ParseException e) {
			System.out.print("parseDate 时间解析错误");
		}
		return l;
	}
}
