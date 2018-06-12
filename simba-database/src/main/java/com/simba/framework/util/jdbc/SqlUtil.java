package com.simba.framework.util.jdbc;

import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * SQL相关工具
 * 
 */
public class SqlUtil {

	public static String getSQL(String sql, StatementParameter param) {
		if (param == null) {
			throw new RuntimeException("没有设置参数.");
		}
		int i = 0;
		while (sql.indexOf('?') > -1) {
			if (i >= param.size()) {
				return sql;
			}
			String value;
			int type = param.getTypes(i);

			if (type == Types.VARCHAR) {
				value = "'" + param.getString(i) + "'";
			} else if (type == Types.DATE) {
				value = "'" + getTime(param.getDate(i)) + "'";
			} else if (type == Types.INTEGER) {
				value = Integer.toString(param.getInt(i));
			} else if (type == Types.FLOAT) {
				value = Float.toString(param.getFloat(i));
			} else if (type == Types.DOUBLE) {
				value = Double.toString(param.getDouble(i));
			} else if (type == Types.BIGINT) {
				value = Long.toString(param.getLong(i));
			} else {
				throw new RuntimeException("未知数据类型[" + type + "]");
			}

			sql = sql.substring(0, sql.indexOf('?')) + value + sql.substring(sql.indexOf('?') + 1, sql.length());
			i++;
		}
		return sql;//
	}

	public static String getSQL(String sql, Object... args) {
		StatementParameter param = new StatementParameter();
		param.setArray(args);
		return SqlUtil.getSQL(sql, param);
	}

	/**
	 * 根据数组数量生成填充符（问号）的字符窜
	 * 
	 * @param args
	 *            如果为null或者无元素，直接返回空字符串
	 * 
	 * @return 返回“?,?,?”的形式
	 * 
	 * @author 2014年7月14日 下午3:31:27
	 */
	public static String generatePaddingSymbol(int[] args) {
		Integer[] ids = new Integer[args.length];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = args[i];
		}
		return generatePaddingSymbol(ids);
	}

	/**
	 * 根据数组数量生成填充符（问号）的字符窜
	 * 
	 * @param args
	 *            如果为null或者无元素，直接返回空字符串
	 * 
	 * @return 返回“?,?,?”的形式
	 * 
	 * @author 2014年7月14日 下午3:31:27
	 */
	public static String generatePaddingSymbol(Object[] args) {
		if (args == null || args.length == 0) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < args.length; i++) {
			sb.append("?,");
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	/**
	 * 根据集合元素数量生成填充符（问号）的字符窜
	 * 
	 * @param args
	 *            如果为null或者无元素，直接返回空字符串
	 * 
	 * @return 返回“?,?,?”的形式
	 * 
	 * @author 2014年7月14日 下午3:31:27
	 */
	public static String generatePaddingSymbol(List<?> args) {
		if (args == null || args.isEmpty()) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < args.size(); i++) {
			sb.append("?,");
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	/**
	 * 1. 创建一个SET语句填充符构造器<br>
	 * 2. 调用add(name, value)方法添加参数<br>
	 * 3. 最后调用toString()方法生成语句
	 * 
	 * @param param
	 *            用于填充参数的StatementParameter对象
	 * 
	 * @return 同 return new StringBuffer()
	 * 
	 * @author 2014年7月14日 下午3:56:53
	 */
	public static SQLSetBuilder createSQLSetBuilder(StatementParameter param) {
		return new SQLSetBuilder(param);
	}

	/**
	 * 1. 创建一个SET语句填充符构造器<br>
	 * 2. 调用add(name, value)方法添加参数<br>
	 * 3. 最后调用toString()方法生成语句
	 * 
	 * @param select
	 *            SELECT部分语句
	 * @param param
	 *            用于填充参数的StatementParameter对象
	 * 
	 * @return 同 return new StringBuffer()
	 * 
	 * @author 2014年7月14日 下午3:56:53
	 */
	public static SQLSetBuilder createSQLSetBuilder(String select, StatementParameter param) {
		return new SQLSetBuilder(select, param);
	}

	/**
	 * SET语句填充符构造器，生成样例：name1 = ?, name2 = ?, name3 = ?
	 * 
	 * @author 2014年7月14日 下午3:53:51
	 */
	public static class SQLSetBuilder {
		private final StringBuffer sb = new StringBuffer();
		private final StatementParameter param;
		private final String select;

		private SQLSetBuilder(StatementParameter param) {
			this.select = null;
			this.param = param;
		}

		private SQLSetBuilder(String select, StatementParameter param) {
			this.select = select;
			this.param = param;
		}

		/**
		 * 加入一个字段名
		 * 
		 * @param name
		 *            字段名
		 * @param value
		 *            值对象
		 * 
		 * @return 参数有效时返回true
		 */
		private boolean set(String name, Object value) {
			if (value == null) {
				return false;
			}
			if (sb.length() == 0) {
				sb.append(name + "=?");
			} else {
				sb.append("," + name + "=?");
			}
			return true;
		}

		public SQLSetBuilder add(String name, Boolean value) {
			if (set(name, value)) {
				param.setBool(value);
			}
			return this;
		}

		public SQLSetBuilder add(String name, Date value) {
			if (set(name, value)) {
				param.setDate(value);
			}
			return this;
		}

		public SQLSetBuilder add(String name, Double value) {
			if (set(name, value)) {
				param.setDouble(value);
			}
			return this;
		}

		public SQLSetBuilder add(String name, Float value) {
			if (set(name, value)) {
				param.setFloat(value);
			}
			return this;
		}

		public SQLSetBuilder add(String name, Integer value) {
			if (set(name, value)) {
				param.setInt(value);
			}
			return this;
		}

		public SQLSetBuilder add(String name, Long value) {
			if (set(name, value)) {
				param.setLong(value);
			}
			return this;
		}

		public SQLSetBuilder add(String name, String value) {
			if (set(name, value)) {
				param.setString(value);
			}
			return this;
		}

		public SQLSetBuilder add(String name, Enum<?> value) {
			if (set(name, value)) {
				param.setString(value.name());
			}
			return this;
		}

		public SQLSetBuilder addEnumOrdinal(String name, Enum<?> value) {
			if (set(name, value)) {
				param.setInt(value.ordinal());
			}
			return this;
		}

		/** 生成最终SQL语句，类似：name1 = ?, name2 = ?, name3 = ? */
		public String toString() {
			if (select == null) {
				return sb.toString();
			} else {
				return select + sb.toString();
			}
		}

	}

	/**
	 * 创建一个where语句构造器，详细用法请查看{@link SQLWhereBuilder}
	 * 
	 * @param param
	 *            用于填充参数的StatementParameter对象
	 * 
	 * @return 同 return new StringBuffer()
	 * 
	 * @author 2014年7月14日 下午3:56:53
	 */
	public static SQLWhereBuilder createSQLWhereBuilder(StatementParameter param) {
		return new SQLWhereBuilder(null, param);
	}

	/**
	 * 创建一个where语句构造器，详细用法请查看{@link SQLWhereBuilder}
	 * 
	 * @param select
	 *            SELECT部分语句
	 * @param param
	 *            用于填充参数的StatementParameter对象
	 * 
	 * @return 同 return new StringBuffer()
	 * 
	 * @author 2014年7月14日 下午3:56:53
	 */
	public static SQLWhereBuilder createSQLWhereBuilder(String select, StatementParameter param) {
		return new SQLWhereBuilder(select, param);
	}

	/**
	 * <pre>
	 * SQL WHERE 部分构造器，仅适用于动态条件
	 * 
	 * 1.创建SQLWhereBuilder并传入已初始化的StatementParameter（用于填充参数）
	 * 2.多次调用add方法：
	 *       sqlPart：是SQL的片段，这个语句会按照add的调用顺序追加到字符窜缓存中
	 *       values：与sqlPart中的占位符问号“？”相对应的参数
	 * 3.最后调用toString()方法生成WHERE语句。
	 *       如果有条件：返回“WHERE” + add进来的sqlPart的字符窜
	 *       如果无条件：直接返回空字符串“”
	 * 
	 * 样例：
	 *      // 无条件
	 *      SQLWhereBuilder b1 = new SQLWhereBuilder("SELECT * FROM T1", new StatementParameter());
	 *      System.out.println(b1); // SELECT * FROM T1
	 * 
	 *      // 单个AND条件
	 *      SQLWhereBuilder b2 = new SQLWhereBuilder("SELECT * FROM T1", new StatementParameter());
	 *      b2.add("and xxx1 = ?", 10);
	 *      System.out.println(b2); // SELECT * FROM T1 WHERE  xxx1 = ?
	 * 
	 *      // 多个AND条件
	 *      SQLWhereBuilder b3 = new SQLWhereBuilder("SELECT * FROM T1", new StatementParameter());
	 *      b3.add("and xxx1 = ?", 10);
	 *      b3.add("and xxx2 = ?", "hello");
	 *      String s3 = null;
	 *      b3.add("and xxx3 = ?", s3); // 参数为NULL，片段被忽略
	 *      System.out.println(b3); // SELECT * FROM T1 WHERE  xxx1 = ? and xxx2 = ?
	 * 
	 *      // 多参数
	 *      SQLWhereBuilder b4 = new SQLWhereBuilder("SELECT * FROM T1", new StatementParameter());
	 *      b4.add("and xxx1 = ?", 10);
	 *      b4.add("and ( xxx2 = ? or xxx3 = ?)", "hello", "baby");
	 *      System.out.println(b4); // SELECT * FROM T1 WHERE  xxx1 = ? and ( xxx2 = ? or xxx3 = ?)
	 * 
	 *      // 混合参数
	 *      SQLWhereBuilder b5 = new SQLWhereBuilder("SELECT * FROM T1", new StatementParameter());
	 *      b5.add("and xxx1 = ?", 10);
	 *      b5.add("and ( xxx2 = ? or xxx3 = ?)", "hello", "baby");
	 *      b5.add("or xxx4 in(?,?,?,?,?)", new Object[] { 1, 2, 3, 4, null }); // 有一个参数为null，片段被忽略
	 *      b5.add("or xxx5 in(?,?,?,?,?)", new Object[] { 1, 2, 3, 4, 5 });
	 *      b5.add("and ( xxx6 between ? and ?)", "aaa", "bbb");
	 *      System.out.println(b5); // SELECT * FROM T1 WHERE  xxx1 = ? and ( xxx2 = ? or xxx3 = ?) or xxx5 in(?,?,?,?,?) and ( xxx6 between ? and ?)
	 * 
	 *      // 混合条件
	 *      SQLWhereBuilder b7 = new SQLWhereBuilder(null, new StatementParameter());
	 *      SQLWhereBuilder b6 = new SQLWhereBuilder("SELECT * FROM T1", new StatementParameter());
	 *      b6.add("and xxx1 = ?", 10);
	 *      b6.add("and ( xxx4 in select id from T2 "); // 加入纯语句
	 *      b6.add(b7.add("and yyy1 = ?", "y1").add("and yyy2 = ?", "y2").toString()); // 加入子查询条件
	 *      b6.add(")"); // 子查询结尾
	 *      System.out.println(b6); // SELECT * FROM T1 WHERE  xxx1 = ? and ( xxx4 in select id from T2   WHERE  yyy1 = ? and yyy2 = ? )
	 * 
	 * </pre>
	 * 
	 * @author 2014年7月15日 上午11:00:36
	 */
	public static class SQLWhereBuilder {
		private final StringBuffer sb = new StringBuffer();
		private final StatementParameter param;
		private final String select;

		private SQLWhereBuilder(String select, StatementParameter param) {
			this.select = select;
			this.param = param;
		}

		/**
		 * 增加一个条件
		 * 
		 * @param sqlPart
		 *            SQL片段
		 * @param values
		 *            与sqlPart参数中占位符“？”相对于的参数，必须每个参数都不为NULL时才采用这个条件，否则丢弃
		 * 
		 * @return 返回SQLWhereBuilder，支持链式操作
		 * 
		 * @author 2014年7月15日 上午10:57:49
		 */
		@SuppressWarnings("unchecked")
		public <T> SQLWhereBuilder add(String sqlPart, T... values) {
			if (values == null) {
				// 参数没有意义，忽略
				return this;
			} else if (values.length == 0) {
				// 没有填充参数，直接追加sqlPart
				sb.append(sqlPart).append(' ');
			} else if (values.length == 1) {
				// 只有1个填充参数，其不为NULL时追加
				if (values[0] != null) {
					set(values[0]);
					sb.append(sqlPart).append(' ');
				}
			} else {
				// 2个及以上填充参数，在所有参数不为NULL时追加
				boolean noNull = true;
				for (T value : values) {
					if (value == null) {
						noNull = false;
						break;
					}
				}
				if (noNull) {
					for (Object value : values) {
						if (value != null) {
							set(value);
						}
					}
					sb.append(sqlPart).append(' ');
				}
			}
			return this;
		}

		private void set(Object value) {
			if (value instanceof String) {
				param.setString((String) value);
			} else if (value instanceof Boolean) {
				param.setBool((Boolean) value);
			} else if (value instanceof Date) {
				param.setDate((Date) value);
			} else if (value instanceof Double) {
				param.setDouble((Double) value);
			} else if (value instanceof Float) {
				param.setFloat((Float) value);
			} else if (value instanceof Integer) {
				param.setInt((Integer) value);
			} else if (value instanceof Long) {
				param.setLong((Long) value);
			} else if (value instanceof Enum) {
				param.setString(((Enum<?>) value).name());
			} else {
				throw new RuntimeException("不支持的类型：value=" + value + "; type=" + value.getClass());
			}
		}

		@Override
		public String toString() {
			String sql;
			if (sb.length() == 0) {
				sql = "";
			} else {
				sql = sb.toString().trim();
				if (sql.startsWith("AND ") || sql.startsWith("and ")) {
					sql = sql.substring(3);
				} else if (sql.startsWith("OR ") || sql.startsWith("or ")) {
					sql = sql.substring(2);
				}
				sql = " WHERE " + sql;
			}
			if (select == null) {
				return sql;
			} else {
				return select + sql;
			}
		}
	}

	private static String getTime(Date date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(date);
	}

	public static class SqlPart {
		public String sql;
		public StatementParameter params;

		@Override
		public String toString() {
			return "SqlPart [sql=" + sql + ", params=" + params + "]";
		}
	}

}
