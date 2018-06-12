package com.simba.calendar.model.enums;

/**
 * 中国节日
 * 
 * @author caozhejun
 *
 */
public enum FestivalChina {

	NEWYEAR("1-1", "春节"),

	YUANXIAO("1-15", "元宵节"),

	LONGTAITOU("2-2", "龙抬头"),

	DUANWU("5-5", "端午节"),

	QINGREN("7-7", "七夕情人节"),

	GUI("7-15", "中元节"),

	ZHONGQIU("8-15", "中秋节"),

	CHONGYANG("9-9", "重阳节"),

	LABA("12-8", "腊八节"),

	SMALLYEAR("12-23", "小年"),

	EVE("12-30", "除夕");

	private String date;

	private String name;

	public String getDate() {
		return date;
	}

	public String getName() {
		return name;
	}

	private FestivalChina(String date, String name) {
		this.date = date;
		this.name = name;
	}

}
