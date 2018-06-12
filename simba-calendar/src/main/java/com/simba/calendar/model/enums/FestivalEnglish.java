package com.simba.calendar.model.enums;

/**
 * 西方节日
 * 
 * @author caozhejun
 *
 */
public enum FestivalEnglish {

	NEWYEAR("1-1", "元旦"),

	HAIGUAN("1-26", "国际海关日"),

	MAFENG("1-31", "国际麻风节"),

	SHIDI("2-2", "世界湿地日"),

	KANGAI("2-4", "世界抗癌症日"),

	SHIYUANNANFEI("2-7", "国际声援南非日"),

	QIXIANG("2-10", "国际气象节"),

	QINGREN("2-14", "情人节"),

	MUYU("2-21", "国际母语日"),

	QINGNIAN("2-24", "第三世界青年日"),

	HANJIANBING("2-29", "国际罕见病日"),

	HAIBAO("3-1", "国际海豹日"),

	AIER("3-3", "全国爱耳日"),

	LEIFENG("3-5", "学雷锋纪念日"),

	WOMAN("3-8", "妇女节"),

	ZHOUYAN("3-11", "国际尊严尊敬日"),

	TREE("3-12", "植树节"),

	WHITEQINGREN("3-14", "白色情人节"),

	XIAOFEI("3-15", "消费者权益日"),

	HANGHAI("3-17", "国际航海日"),

	WUROU("3-20", "世界无肉日"),

	SHUIMIAN("3-21", "世界睡眠日|世界森林日"),

	WATER("3-22", "世界水日"),

	WEATHER("3-23", "世界气象日"),

	YESHU("3-25", "耶稣受难日"),

	YUREN("4-1", "愚人节"),

	QINGMING("4-5", "清明节"),

	FUHUO("4-16", "复活节"),

	LABOUR("5-1", "劳动节"),

	YOUNG("5-4", "青年节"),

	CHILDREN("6-1", "儿童节"),

	DONG("7-1", "共产党纪念日|香港回归日"),

	JUN("8-1", "建军节"),

	TEATHER("9-10", "教师节"),

	GUOQING("10-1", "国庆节"),

	WANSHENG("11-1", "万圣节"),

	GUANGGUN("11-11", "光棍节"),

	DAXUESHENG("11-17", "国际大学生节"),

	ANMEN("12-20", "澳门回归日"),

	PINGAN("12-24", "平安夜"),

	CHENGDAN("12-25", "圣诞节");

	private String date;

	private String name;

	public String getDate() {
		return date;
	}

	public String getName() {
		return name;
	}

	private FestivalEnglish(String date, String name) {
		this.date = date;
		this.name = name;
	}

}
