package com.simba.framework.util.jdbc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * SQL参数
 * 
 */
public class SimpleStatementParameter {

	private Map<String, Object> map = new HashMap<String, Object>();

	private String table;
	private String keyColumn;

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getKeyColumn() {
		return keyColumn;
	}

	public void setKeyColumn(String keyColumn) {
		this.keyColumn = keyColumn;
	}

	public SimpleStatementParameter(String table, String keyColumn) {
		this.table = table;
		this.keyColumn = keyColumn;
	}

	public void setDate(String column, Date value) {
		if (value == null) {
			throw new RuntimeException("参数值[" + map.size() + "]不能为空.");
		}
		map.put(column, value);
	}

	public void setDateString(String column, Date value) {
		if (value == null) {
			throw new RuntimeException("参数值[" + map.size() + "]不能为空.");
		}
		map.put(column, this.parseDateToString(value, "yyyy-MM-dd HH:mm:ss"));
	}

	public void setString(String column, String value) {
		if (value == null) {
			throw new RuntimeException("参数值[" + map.size() + "]不能为空.");
		}
		map.put(column, value);
	}

	public void setBool(String column, Boolean value) {
		if (value == null) {
			throw new RuntimeException("参数值[" + map.size() + "]不能为空.");
		}
		map.put(column, value ? 1 : 0);
	}

	public void setInt(String column, Integer value) {
		if (value == null) {
			throw new RuntimeException("参数值[" + map.size() + "]不能为空.");
		}
		map.put(column, value);
	}

	public void setLong(String column, Long value) {
		if (value == null) {
			throw new RuntimeException("参数值[" + map.size() + "]不能为空.");
		}
		map.put(column, value);
	}

	public void setDouble(String column, Double value) {
		if (value == null) {
			throw new RuntimeException("参数值[" + map.size() + "]不能为空.");
		}
		map.put(column, value);
	}

	public void setFloat(String column, Float value) {
		if (value == null) {
			throw new RuntimeException("参数值[" + map.size() + "]不能为空.");
		}
		map.put(column, value);
	}

	public Map<String, Object> getParameters() {
		return map;
	}

	public String parseDateToString(Date date, String pattern) {
		DateFormat df = new SimpleDateFormat(pattern);
		return df.format(date);
	}
}