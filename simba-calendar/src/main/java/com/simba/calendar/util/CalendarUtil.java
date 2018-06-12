package com.simba.calendar.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.simba.calendar.model.DayInfo;
import com.simba.calendar.model.enums.FestivalChina;
import com.simba.calendar.model.enums.FestivalEnglish;
import com.simba.framework.util.date.DateUtil;

/**
 * 日历工具类
 * 
 * @author caozhejun
 *
 */
public class CalendarUtil {

	private Map<String, String> festivalEns = new HashMap<>();

	private Map<String, String> festivalChs = new HashMap<>();

	private CalendarUtil() {
		init();
	}

	private void init() {
		initFestivalEns();
		initFestivalChs();
	}

	private void initFestivalChs() {
		for (FestivalChina festival : FestivalChina.values()) {
			festivalChs.put(festival.getDate(), festival.getName());
		}
	}

	private void initFestivalEns() {
		for (FestivalEnglish festival : FestivalEnglish.values()) {
			festivalEns.put(festival.getDate(), festival.getName());
		}
	}

	private static final class CalendarUtilHolder {
		private static final CalendarUtil instance = new CalendarUtil();
	}

	public static CalendarUtil getInstance() {
		return CalendarUtilHolder.instance;
	}

	/**
	 * 获取某年的日历相关信息对象
	 * 
	 * @param year
	 * @return
	 */
	public List<DayInfo> get(int year) {
		List<DayInfo> all = new ArrayList<>();
		for (int i = 1; i <= 12; i++) {
			List<DayInfo> list = this.get(year, i);
			all.addAll(list);
		}
		return all;
	}

	/**
	 * 获取日历中整个月的日历相关信息对象
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public List<DayInfo> get(int year, int month) {
		int days = DateUtil.getDaysOfMonth(year, month);
		List<DayInfo> list = new ArrayList<>(days);
		for (int i = 1; i <= days; i++) {
			DayInfo info = this.get(year, month, i);
			list.add(info);
		}
		return list;
	}

	/**
	 * 获取日历某天日历相关信息对象
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public DayInfo get(int year, int month, int day) {
		Lunar lunar = new Lunar(year, month, day);
		DayInfo info = new DayInfo();
		info.setDate(lunar.getDate());
		info.setYear(year);
		info.setMonth(month);
		info.setDay(day);
		info.setLunarYear(lunar.getYear());
		info.setLunarMonth(lunar.getMonth());
		info.setLunarDay(lunar.getDay());
		info.setLeap(lunar.isLeap());
		info.setLunarDayInfo(Lunar.getChinaDayString(lunar.getDay()));
		info.setLunarLongInfo(lunar.toString());
		info.setLunarShortInfo(lunar.toShortString());
		info.setCyclicalAnimal(lunar.animalsYear());
		info.setCyclicalYear(lunar.cyclical());
		String festivalChKey = lunar.getMonth() + "-" + lunar.getDay();
		String festivalEnKey = month + "-" + day;
		String festivalCh = festivalChs.get(festivalChKey);
		String festivalEn = festivalEns.get(festivalEnKey);
		festivalCh = StringUtils.defaultString(festivalCh);
		festivalEn = StringUtils.defaultString(festivalEn);
		info.setFestivalCh(festivalCh);
		info.setFestivalEn(festivalEn);
		if (StringUtils.isNotEmpty(festivalCh) && StringUtils.isNotEmpty(festivalEn)) {
			info.setFestival(festivalCh + "|" + festivalEn);
		} else if (StringUtils.isNotEmpty(festivalCh)) {
			info.setFestival(festivalCh);
		} else if (StringUtils.isNotEmpty(festivalEn)) {
			info.setFestival(festivalEn);
		} else {
			info.setFestival(StringUtils.EMPTY);
		}
		info.setWeek(DateUtil.getWeekNum(lunar.getDate()));
		info.setWeekName(DateUtil.getWeekName(lunar.getDate()));
		return info;
	}

}
