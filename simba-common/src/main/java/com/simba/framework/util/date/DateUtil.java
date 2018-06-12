package com.simba.framework.util.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 日期工具类
 * 
 * @author caozj
 * 
 */
public class DateUtil {
	protected static final Log logger = LogFactory.getLog(DateUtil.class);

	public static final long HOUR = 60 * 60L * 1000L;

	public static final int HOUR_SECOND = 60 * 60;
	public static final int EIGHT_HOUR_SECOND = 60 * 60 * 8;// 8小时
	public static final int DAY_SECOND = 60 * 60 * 24;

	public static final long DAY_MILLI_SECOND = DAY_SECOND * 1000L;

	public static final long EIGHT_HOUR_MILLI_SECOND = EIGHT_HOUR_SECOND * 1000L;

	public final static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public final static String DAY_FORMAT = "yyyy-MM-dd";

	/**
	 * Date转字符串，格式为yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 *            日期
	 * @return 字符串
	 */
	public static String date2String(Date date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TIME_FORMAT);
		return simpleDateFormat.format(date);
	}

	/**
	 * 获取日期
	 * 
	 * @param date
	 *            日期
	 * @return 若date为空，返回当前日期
	 */
	public static Date defaultDate(Date date) {
		if (date == null) {
			return new Date();
		}
		return date;
	}

	/**
	 * 获取日期
	 * 
	 * @param date
	 *            日期
	 * @param defaultDate
	 *            默认日期
	 * @return 若date为空，返回defaultDate转换后的日期
	 */
	public static Date defaultDate(Date date, long defaultDate) {
		if (date == null) {
			return new Date(defaultDate);
		}
		return date;
	}

	/**
	 * 获取日期
	 * 
	 * @param date
	 *            日期
	 * @param defaultDate
	 *            默认日期
	 * @return 若date为空，返回defaultDate
	 */
	public static Date defaultDate(Date date, Date defaultDate) {
		if (date == null) {
			return defaultDate;
		}
		return date;
	}

	/**
	 * 根据formatString将Date转成字符串
	 * 
	 * @param date
	 *            日期
	 * @param formatString
	 *            转换格式
	 * @return 字符串
	 */
	public static String date2String(Date date, String formatString) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatString);
		return simpleDateFormat.format(date);
	}

	/**
	 * 字符串日期转换成Date
	 * 
	 * @param dateString
	 * @return
	 */
	public static Date str2Date(String dateString) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TIME_FORMAT);
			return simpleDateFormat.parse(dateString);
		} catch (ParseException e) {
			throw new RuntimeException("时间转化格式错误!" + "[dateString=" + dateString + "]" + "[FORMAT_STRING=" + TIME_FORMAT + "]");
		}
	}

	/**
	 * 字符串转成Date
	 * 
	 * @param dateString
	 *            字符串日期
	 * @param defaultDate
	 *            默认日期
	 * @return 若出现异常返回默认日期
	 */
	public static Date str2Date(String dateString, Date defaultDate) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TIME_FORMAT);
			return simpleDateFormat.parse(dateString);
		} catch (Exception e) {
			return defaultDate;
		}

	}

	/**
	 * 字符串日期转Date
	 * 
	 * @param dateString
	 *            字符串日期
	 * @param formatDate
	 *            转换格式
	 * @param defaultDate
	 *            默认日起
	 * @return 若发生异常返回默认日期
	 */
	public static Date str2Date(String dateString, String formatDate, Date defaultDate) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatDate);
			return simpleDateFormat.parse(dateString);
		} catch (Exception e) {
			return defaultDate;
		}

	}

	/**
	 * 当前时间增加指定的分钟数
	 * 
	 * @param minute
	 *            分钟数
	 * @return 增加分钟数后的Date
	 */
	public static Date addTime(final int minute) {
		long millis = System.currentTimeMillis();
		millis = millis + (minute * 60L * 1000L);
		Date date = new Date(millis);
		return date;
	}

	/**
	 * 将Date的HH-mm-ss置为零
	 * 
	 * @param date
	 *            日期Date
	 * @return 转换后的Date
	 */
	public static Date getOnlyDate(Date date) {
		String dateStr = DateUtil.getDate(date);
		return DateUtil.toDate(dateStr + " 00:00:00");
	}

	/**
	 * 日期Date增加指定天数
	 * 
	 * @param date
	 *            日期Date
	 * @param daynum
	 *            天数
	 * @return 增加天数后的Date
	 */
	public static Date addDate(final Date date, final int daynum) {
		int minute = daynum * 60 * 24;
		return addTime(date, minute);
	}

	/**
	 * 日期Date增加指定分钟数
	 * 
	 * @param startDate
	 *            日期Date
	 * @param minute
	 *            分钟数
	 * @return 增加分钟数后的Date
	 */
	public static Date addTime(final Date startDate, final int minute) {
		long millis = startDate.getTime();
		millis = millis + (minute * 60L * 1000L);
		Date date = new Date(millis);
		return date;
	}

	/**
	 * 返回当前时间值
	 * 
	 * @return
	 */
	public static Date getTime() {
		return new Date();
	}

	/**
	 * 根据Date获取字符串日期yyyy-MM-dd
	 * 
	 * @param date
	 *            日期Date
	 * @return 字符串日期
	 */
	public static String getDate(Date date) {
		String time = DateUtil.getTime(date);
		if (time == null) {
			return null;
		}
		return time.substring(0, 10);
	}

	/**
	 * 根据Date获取字符串日期yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 *            日期Date
	 * @return 字符串日期
	 */
	public static String getTime(Date date) {
		if (date == null) {
			return null;
		}
		long millis = date.getTime();
		return DateTime.getTime(millis);
	}

	/**
	 * 根据指定格式从Date获取字符串日期
	 * 
	 * @param date
	 *            日期Date
	 * @param format
	 *            转换格式
	 * @return 字符串日期
	 */
	public static String getTime(Date date, String format) {
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

	/**
	 * 时间值（毫秒数）转Date
	 * 
	 * @param time
	 *            毫秒数
	 * @return 日期Date
	 */
	public static Date toDate(Long time) {
		if (time == null || time <= 0) {
			return null;
		}
		return new Date(time);
	}

	/**
	 * 根据Date获取时间戳
	 * 
	 * @param date
	 *            日期Date
	 * @return 时间值，以毫秒为单位
	 */
	public static long getTimestamp(final Date date) {
		return date.getTime();
	}

	/**
	 * 字符串日期转Date
	 * 
	 * @param datetime
	 *            字符串日期
	 * @return 日期Date
	 */
	public static Date toDate(String datetime) {
		if (StringUtils.isEmpty(datetime)) {
			return null;
		}
		long time = DateTime.getTimestamp(datetime);
		if (time <= 0) {
			new Exception("非法日期:" + datetime).printStackTrace();
		}
		return new Date(time);
	}

	/**
	 * 获取秒数(从1970-01-01 00:00:00开始)
	 * 
	 * @return 秒数
	 */
	public static int getSeconds() {
		int seconds = (int) (System.currentTimeMillis() / 1000L);
		return seconds;
	}

	/**
	 * 获取秒数(从2010-01-01 00:00:00开始)
	 * 
	 * @return 秒数
	 */
	public static int getShortSeconds() {
		int seconds = getSeconds();
		return seconds - 1262275200;
	}

	/**
	 * 获取秒数(从2010-01-01 00:00:00开始)
	 * 
	 * @return 秒数
	 */
	public static int getShortSeconds(Date date) {
		int seconds = (int) (date.getTime() / 1000L);
		return seconds - 1262275200;
	}

	/**
	 * 返回时间值(2010-01-01 00:00:00)增加指定秒数后的日期Date
	 * 
	 * @param time
	 *            秒数
	 * @return 增加秒数后的Date
	 */
	public static Date toLongDate(Double time) {
		if (time == null) {
			return null;
		}
		return toLongDate(time.intValue());
	}

	/**
	 * 返回时间值(2010-01-01 00:00:00)增加指定秒数后的日期Date
	 * 
	 * @param time
	 *            秒数
	 * @return 增加秒数后的Date
	 */
	public static Date toLongDate(int time) {
		if (time <= 0) {
			return null;
		}
		int seconds = time + 1262275200;
		return new Date(seconds * 1000L);
	}

	/**
	 * 根据Date获取秒数
	 * 
	 * @param date
	 *            日期Date
	 * @return 秒数
	 */
	public static int getSecond(final Date date) {
		long time = date.getTime();
		return (int) (time / 1000);
	}

	/**
	 * 判断传入的日期是否为今天
	 * 
	 * @return boolean
	 */
	public static boolean isToday(Date date) {
		if (date == null) {
			return false;
		}
		String time = getTime(date);
		return DateTime.isToday(time);
	}

	/**
	 * 获取当前时间x秒前/后的时间
	 * 
	 * @param second
	 *            （-x：前 x：后）
	 * @return
	 */
	public static Date getBeforeSecond(Date date, int second) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, second);
		return cal.getTime();
	}

	/**
	 * 字符串日期根据指定格式转换成Date
	 * 
	 * @param dateString
	 *            字符串日期
	 * @param formatString
	 *            转换格式
	 * @return 转换后的Date
	 */
	public static Date str2Date(String dateString, String formatString) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatString);
			return simpleDateFormat.parse(dateString);
		} catch (ParseException e) {
			throw new RuntimeException("时间转化格式错误!" + "[dateString=" + dateString + "]" + "[FORMAT_STRING=" + formatString + "]");
		}
	}

	/**
	 * 判断date1是否早于date2
	 * 
	 * @param date1
	 *            日期1
	 * @param date2
	 *            日期2
	 * @return boolean
	 */
	public static boolean before(Date date1, Date date2) {
		if (date1 == null && date2 == null) {
			return false;
		}
		if (date1 == null && date2 != null) {
			return false;
		}
		if (date1 != null && date2 == null) {
			return true;
		}
		return date1.before(date2);
	}

	/**
	 * 判断两个时间值是否为同一时间
	 * 
	 * @param second1
	 *            时间1
	 * @param second2
	 *            时间2
	 * @return boolean
	 */
	public static boolean isEqualsDay(int second1, int second2) {
		int diffDayCount = getDiffDayCount(second1, second2);
		return diffDayCount == 0;
	}

	/**
	 * 根据秒数获取小时
	 * 
	 * @param second
	 *            描述
	 * @return 小时
	 */
	public static int getHour(int second) {
		int hour = (second + EIGHT_HOUR_SECOND) % DAY_SECOND / HOUR_SECOND;
		return hour;
	}

	/**
	 * 获取两个时间值(以秒为单位)相差的天数
	 * 
	 * @param postSecond
	 *            时间1
	 * @param currentSecond
	 *            时间2
	 * @return 相差的天数
	 */
	public static int getDiffDayCount(int postSecond, int currentSecond) {
		int posttimeDay = (postSecond + EIGHT_HOUR_SECOND) / DAY_SECOND;
		int currentDay = (currentSecond + EIGHT_HOUR_SECOND) / DAY_SECOND;
		return currentDay - posttimeDay;
	}

	/**
	 * 计算二个时间相差的天数
	 * 
	 * @param date1
	 *            日期
	 * @param date2
	 *            日期
	 * @return 相差的天数
	 */
	public static int getDays(String date1, String date2) {
		// 转换为标准时间
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = null;
		java.util.Date mydate = null;
		try {
			date = myFormatter.parse(date1);
			mydate = myFormatter.parse(date2);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
		return (int) day;
	}

	public static String now() {
		return DateUtil.date2String(DateUtil.getTime());
	}

	public static int getAge(Date birthday) {
		Date now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		int thisYear = calendar.get(Calendar.YEAR);
		int thisMonth = calendar.get(Calendar.MONTH);
		int thisDay = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.setTime(birthday);
		int birthYear = calendar.get(Calendar.YEAR);
		int birthMonth = calendar.get(Calendar.MONTH);
		int birthDay = calendar.get(Calendar.DAY_OF_MONTH);
		int age = thisYear - birthYear;
		if (thisMonth > birthMonth || (thisMonth == birthMonth && thisDay > birthDay)) {
			age = age + 1;
		}
		return age;
	}

	public static int getAge(String birthday) {
		Date date = DateUtil.str2Date(birthday, DAY_FORMAT);
		return DateUtil.getAge(date);
	}

	public static String getActualAge(String birthday) {
		return DateUtil.getAge(birthday) + "";
	}

	public static String getWeekName(Date date) {
		SimpleDateFormat sdw = new SimpleDateFormat("E");
		return sdw.format(date);
	}

	public static int getWeekNum(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (week_index < 0) {
			week_index = 0;
		}
		return week_index;
	}

	public static int getWeekOfYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int week_of_year = cal.get(Calendar.WEEK_OF_YEAR);
		return week_of_year;
	}

	public static int getDaysOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		int days_of_month = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		return days_of_month;
	}

	public static int getDaysBetween(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		return getDaysBetween(cal1, cal2);
	}

	public static int getDaysBetween(Calendar cal1, Calendar cal2) {
		int day1 = cal1.get(Calendar.DAY_OF_YEAR);
		int day2 = cal2.get(Calendar.DAY_OF_YEAR);

		int year1 = cal1.get(Calendar.YEAR);
		int year2 = cal2.get(Calendar.YEAR);
		if (year1 != year2) {
			int timeDistance = 0;
			for (int i = year1; i < year2; i++) {
				if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) // 闰年
				{
					timeDistance += 366;
				} else // 不是闰年
				{
					timeDistance += 365;
				}
			}
			return timeDistance + (day2 - day1);
		} else {
			return day2 - day1;
		}
	}
}
