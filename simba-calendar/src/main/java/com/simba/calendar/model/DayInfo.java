package com.simba.calendar.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 日历的日期数据对象
 * 
 * @author caozhejun
 *
 */
public class DayInfo implements Serializable {

	private static final long serialVersionUID = -4754321969721550946L;

	/**
	 * 公历日期
	 */
	private Date date;

	/**
	 * 公历年
	 */
	private int year;

	/**
	 * 公历月
	 */
	private int month;

	/**
	 * 公历日
	 */
	private int day;

	/**
	 * 农历年
	 */
	private int lunarYear;

	/**
	 * 农历月
	 */
	private int lunarMonth;

	/**
	 * 农历日
	 */
	private int lunarDay;

	/**
	 * 是否为闰月
	 */
	private boolean leap;

	/**
	 * 农历日描述(初一)
	 */
	private String lunarDayInfo;

	/**
	 * 农历长描述(2018年一月初一)
	 */
	private String lunarLongInfo;

	/**
	 * 农历短描述(一月初一)
	 */
	private String lunarShortInfo;

	/**
	 * 生肖
	 */
	private String cyclicalAnimal;

	/**
	 * 中文年(甲子)
	 */
	private String cyclicalYear;

	/**
	 * 中国节日
	 */
	private String festivalCh;

	/**
	 * 西方节日
	 */
	private String festivalEn;

	/**
	 * 节日
	 */
	private String festival;

	/**
	 * 星期(0:星期日)
	 */
	private int week;

	/**
	 * 星期描述(星期日)
	 */
	private String weekName;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getLunarYear() {
		return lunarYear;
	}

	public void setLunarYear(int lunarYear) {
		this.lunarYear = lunarYear;
	}

	public int getLunarMonth() {
		return lunarMonth;
	}

	public void setLunarMonth(int lunarMonth) {
		this.lunarMonth = lunarMonth;
	}

	public int getLunarDay() {
		return lunarDay;
	}

	public void setLunarDay(int lunarDay) {
		this.lunarDay = lunarDay;
	}

	public boolean isLeap() {
		return leap;
	}

	public void setLeap(boolean leap) {
		this.leap = leap;
	}

	public String getLunarDayInfo() {
		return lunarDayInfo;
	}

	public void setLunarDayInfo(String lunarDayInfo) {
		this.lunarDayInfo = lunarDayInfo;
	}

	public String getLunarLongInfo() {
		return lunarLongInfo;
	}

	public void setLunarLongInfo(String lunarLongInfo) {
		this.lunarLongInfo = lunarLongInfo;
	}

	public String getLunarShortInfo() {
		return lunarShortInfo;
	}

	public void setLunarShortInfo(String lunarShortInfo) {
		this.lunarShortInfo = lunarShortInfo;
	}

	public String getCyclicalAnimal() {
		return cyclicalAnimal;
	}

	public void setCyclicalAnimal(String cyclicalAnimal) {
		this.cyclicalAnimal = cyclicalAnimal;
	}

	public String getCyclicalYear() {
		return cyclicalYear;
	}

	public void setCyclicalYear(String cyclicalYear) {
		this.cyclicalYear = cyclicalYear;
	}

	public String getFestivalCh() {
		return festivalCh;
	}

	public void setFestivalCh(String festivalCh) {
		this.festivalCh = festivalCh;
	}

	public String getFestivalEn() {
		return festivalEn;
	}

	public void setFestivalEn(String festivalEn) {
		this.festivalEn = festivalEn;
	}

	public String getFestival() {
		return festival;
	}

	public void setFestival(String festival) {
		this.festival = festival;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public String getWeekName() {
		return weekName;
	}

	public void setWeekName(String weekName) {
		this.weekName = weekName;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DayInfo [date=");
		builder.append(date);
		builder.append(", year=");
		builder.append(year);
		builder.append(", month=");
		builder.append(month);
		builder.append(", day=");
		builder.append(day);
		builder.append(", lunarYear=");
		builder.append(lunarYear);
		builder.append(", lunarMonth=");
		builder.append(lunarMonth);
		builder.append(", lunarDay=");
		builder.append(lunarDay);
		builder.append(", leap=");
		builder.append(leap);
		builder.append(", lunarDayInfo=");
		builder.append(lunarDayInfo);
		builder.append(", lunarLongInfo=");
		builder.append(lunarLongInfo);
		builder.append(", lunarShortInfo=");
		builder.append(lunarShortInfo);
		builder.append(", cyclicalAnimal=");
		builder.append(cyclicalAnimal);
		builder.append(", cyclicalYear=");
		builder.append(cyclicalYear);
		builder.append(", festivalCh=");
		builder.append(festivalCh);
		builder.append(", festivalEn=");
		builder.append(festivalEn);
		builder.append(", festival=");
		builder.append(festival);
		builder.append(", week=");
		builder.append(week);
		builder.append(", weekName=");
		builder.append(weekName);
		builder.append("]");
		return builder.toString();
	}

}
