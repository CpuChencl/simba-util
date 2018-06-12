package com.simba.framework.util.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

/**
 * jdbc操作数据库类(使用Spring的JdbcTemplate实现)
 * 
 * 分页查询使用的是mysql数据库的limit关键词，如果使用其他数据库，请修改对应的queryForPage方法
 * 
 * @author caozj
 * 
 */
@Component
public class Jdbc extends JdbcDaoSupport {

	@Value("${showSql}")
	private boolean log;

	@Value("${dbType}")
	private String dbType;

	@Resource
	private DataSource dataSource;

	@PostConstruct
	public void initDataSource() {
		super.setDataSource(dataSource);
	}

	@PreDestroy
	public void destroy() {
		logger.info("destroy");
	}

	@PostConstruct
	public void init() {
		logger.info("init");
	}

	/** 事务回滚. */
	public boolean rollback() {
		try {
			Connection con = super.getConnection();
			if (con == null) {
				return false;
			}
			con.rollback();
			return true;
		} catch (CannotGetJdbcConnectionException e) {
			logger.error(e.getMessage(), e);
			return false;
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	public String printSQL(String sql, StatementParameter param) {
		String sql1 = this.getSQL(sql, param);
		logger.info(sql1);
		return sql1;
	}

	public String printSQL(String sql, Object... args) {
		String sql1 = this.getSQL(sql, args);
		logger.info(sql1);
		return sql1;
	}

	public void printStackTrace(String sql, StatementParameter param, int updatedCount) {
		String str1 = this.getSQL(sql, param);
		logger.info("sql:" + str1);
		logger.info("updatedCount:" + updatedCount);
		Exception e = new Exception();
		logger.info(e.getMessage(), e);
	}

	public String getSQL(String sql, StatementParameter param) {
		return SqlUtil.getSQL(sql, param);
	}

	public String getSQL(String sql, Object... args) {
		return SqlUtil.getSQL(sql, args);
	}

	public int[] batchUpdate(String sql, BatchPreparedStatementSetter setter) {
		return this.getJdbcTemplate().batchUpdate(sql, setter);

	}

	public <T> T query(String sql, Class<T> elementType) {
		try {
			return this.getJdbcTemplate().queryForObject(sql, new BeanPropertyRowMapper<T>(elementType));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	public <T> T query(String sql, Class<T> elementType, Object... args) {
		try {
			if (log) {
				logger.info("sql:" + sql);
			}
			return this.getJdbcTemplate().queryForObject(sql, new BeanPropertyRowMapper<T>(elementType), args);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	public <T> T query(String sql, Class<T> elementType, StatementParameter param) {
		try {
			if (log) {
				logger.info("sql:" + sql);
			}
			return this.getJdbcTemplate().queryForObject(sql, param.getArgs(), new BeanPropertyRowMapper<T>(elementType));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	private void log(List<?> list, String sql, StatementParameter param) {
		int size = ListUtil.size(list);
		String sql1;
		if (param == null) {
			sql1 = sql;
		} else {
			sql1 = this.getSQL(sql, param);
		}
		this.logger.info("result size:" + size + " sql:" + sql1);
	}

	private void log(List<?> list, String sql, Object... args) {
		int size = ListUtil.size(list);
		String sql1;
		if (args == null || args.length == 0) {
			sql1 = sql;
		} else {
			sql1 = this.getSQL(sql, args);
		}
		this.logger.info("result size:" + size + " sql:" + sql1);
	}

	public List<Map<String, Object>> queryForMaps(String sql) {
		try {
			List<Map<String, Object>> list = this.getJdbcTemplate().queryForList(sql);
			return list;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ArrayList<>(0);
		}
	}

	public List<Map<String, Object>> queryForMaps(String sql, Object... args) {
		try {
			List<Map<String, Object>> list = this.getJdbcTemplate().queryForList(sql, args);
			return list;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ArrayList<>(0);
		}
	}

	public Map<String, Object> queryForMap(String sql) {
		try {
			Map<String, Object> map = this.getJdbcTemplate().queryForMap(sql);
			return map;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	public Map<String, Object> queryForMap(String sql, Object... args) {
		try {
			Map<String, Object> map = this.getJdbcTemplate().queryForMap(sql, args);
			return map;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	public <T> List<T> queryForList(String sql, Class<T> elementType) {
		try {
			List<T> list = this.getJdbcTemplate().query(sql, new BeanPropertyRowMapper<T>(elementType));
			if (log) {
				this.log(list, sql);
			}
			return list;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ArrayList<>(0);
		}
	}

	public List<Long> queryForLongs(String sql, StatementParameter param) {
		List<Long> list = (List<Long>) super.getJdbcTemplate().query(sql, param.getArgs(), new RowMapper<Long>() {
			public Long mapRow(ResultSet rs, int index) {
				try {
					return rs.getLong(1);
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		});
		if (log) {
			this.log(list, sql, param);
		}
		return list;
	}

	public List<Long> queryForLongs(String sql, Object... args) {
		List<Long> list = (List<Long>) super.getJdbcTemplate().query(sql, args, new RowMapper<Long>() {
			public Long mapRow(ResultSet rs, int index) {
				try {
					return rs.getLong(1);
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		});
		if (log) {
			this.log(list, sql, args);
		}
		return list;
	}

	public List<Integer> queryForInts(String sql, StatementParameter param) {
		List<Integer> list = (List<Integer>) super.getJdbcTemplate().query(sql, param.getArgs(), new RowMapper<Integer>() {
			public Integer mapRow(ResultSet rs, int index) {
				try {
					return rs.getInt(1);
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		});
		if (log) {
			this.log(list, sql, param);
		}
		return list;
	}

	public List<Integer> queryForInts(String sql, Object... args) {
		List<Integer> list = (List<Integer>) super.getJdbcTemplate().query(sql, args, new RowMapper<Integer>() {
			public Integer mapRow(ResultSet rs, int index) {
				try {
					return rs.getInt(1);
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		});
		if (log) {
			this.log(list, sql, args);
		}
		return list;
	}

	public List<String> queryForStrings(String sql, StatementParameter param) {
		List<String> list = (List<String>) super.getJdbcTemplate().query(sql, param.getArgs(), new RowMapper<String>() {
			public String mapRow(ResultSet rs, int index) {
				try {
					return rs.getString(1);
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		});
		if (log) {
			this.log(list, sql, param);
		}
		return list;
	}

	public List<String> queryForStrings(String sql, Object... args) {
		List<String> list = (List<String>) super.getJdbcTemplate().query(sql, args, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int index) {
				try {
					return rs.getString(1);
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		});
		if (log) {
			this.log(list, sql, args);
		}
		return list;
	}

	public <T> List<T> queryForList(String sql, Class<T> elementType, StatementParameter param) {
		try {
			List<T> list = this.getJdbcTemplate().query(sql, param.getArgs(), new BeanPropertyRowMapper<T>(elementType));
			if (log) {
				this.log(list, sql, param);
			}
			return list;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ArrayList<>(0);
		}
	}

	public <T> List<T> queryForList(String sql, Class<T> elementType, Object... args) {
		try {
			List<T> list = this.getJdbcTemplate().query(sql, args, new BeanPropertyRowMapper<T>(elementType));
			if (log) {
				this.log(list, sql, args);
			}
			return list;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ArrayList<>(0);
		}
	}

	public long queryForLong(String sql) {
		try {
			long result = this.getJdbcTemplate().queryForObject(sql, Long.class);
			if (log) {
				logger.info("result:" + result + " sql:" + sql);
			}
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return -1;
		}
	}

	public long queryForLong(String sql, StatementParameter param) {
		Object[] args = param.getArgs();
		try {
			long result = this.getJdbcTemplate().queryForObject(sql, args, Long.class);
			if (log) {
				logger.info("result:" + result + " sql:" + this.getSQL(sql, param));
			}
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return -1;
		}
	}

	public long queryForLong(String sql, Object... args) {
		try {
			long result = this.getJdbcTemplate().queryForObject(sql, args, Long.class);
			if (log) {
				logger.info("result:" + result + " sql:" + this.getSQL(sql, args));
			}
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return -1;
		}
	}

	public int queryForInt(String sql) {
		try {
			int result = this.getJdbcTemplate().queryForObject(sql, Integer.class);
			if (log) {
				logger.info("result:" + result + " sql:" + sql);
			}
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return -1;
		}
	}

	public int queryForInt(String sql, StatementParameter param) {
		Object[] args = param.getArgs();
		try {
			int result = this.getJdbcTemplate().queryForObject(sql, args, Integer.class);
			if (log) {
				logger.info("result:" + result + " sql:" + this.getSQL(sql, param));
			}
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return -1;
		}
	}

	public int queryForInt(String sql, Object... args) {
		try {
			int result = this.getJdbcTemplate().queryForObject(sql, args, Integer.class);
			if (log) {
				logger.info("result:" + result + " sql:" + this.getSQL(sql, args));
			}
			return result;
		} catch (Exception e) {
			return -1;
		}
	}

	public Date queryForDate(String sql) {
		try {
			Date result = this.getJdbcTemplate().queryForObject(sql, Date.class);
			if (log) {
				logger.info("result:" + result + " sql:" + sql);
			}
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	public Date queryForDate(String sql, StatementParameter param) {
		Object[] args = param.getArgs();
		try {
			Date result = this.getJdbcTemplate().queryForObject(sql, args, Date.class);
			if (log) {
				logger.info("result:" + result + " sql:" + this.getSQL(sql, param));
			}
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	public Date queryForDate(String sql, Object... args) {
		try {
			Date result = this.getJdbcTemplate().queryForObject(sql, args, Date.class);
			if (log) {
				logger.info("result:" + result + " sql:" + this.getSQL(sql, args));
			}
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	public String queryForString(String sql) {
		try {
			String result = this.getJdbcTemplate().queryForObject(sql, String.class);
			if (log) {
				logger.info("result:" + result + " sql:" + sql);
			}
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	public String queryForString(String sql, StatementParameter param) {
		Object[] args = param.getArgs();
		try {
			String result = this.getJdbcTemplate().queryForObject(sql, args, String.class);
			if (log) {
				logger.info("result:" + result + " sql:" + this.getSQL(sql, param));
			}
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	public String queryForString(String sql, Object... args) {
		try {
			String result = this.getJdbcTemplate().queryForObject(sql, args, String.class);
			if (log) {
				logger.info("result:" + result + " sql:" + this.getSQL(sql, args));
			}
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	public boolean updateForBoolean(String sql, StatementParameter param) {
		int updatedCount = this.updateForRecord(sql, param);
		return (updatedCount > 0);
	}

	public boolean updateForBoolean(String sql, Object... args) {
		int updatedCount = this.updateForRecord(sql, args);
		return (updatedCount > 0);
	}

	/**
	 * 执行更新操作并返回数据库服务器自动生成的自动ID
	 * 
	 * @param sql
	 * @param param
	 * 
	 * @return
	 * 
	 * @author 2014年7月16日 下午1:48:16
	 */
	public Number updateForGeneratedKey(final String sql, final StatementParameter param) {
		PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				for (int i = 0; i < param.size(); i++) {
					ps.setObject(i + 1, param.getObject(i));
				}
				return ps;
			}
		};
		KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
		int updatedCount = getJdbcTemplate().update(psc, generatedKeyHolder);
		if (log) {
			String sql1 = this.getSQL(sql, param);
			logger.info("updatedCount:" + updatedCount + " sql:" + sql1);
		}
		return generatedKeyHolder.getKey();
	}

	public Number updateForGeneratedKey(final String sql, final Object... args) {
		PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				for (int i = 0; i < args.length; i++) {
					ps.setObject(i + 1, args[i]);
				}
				return ps;
			}
		};
		KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
		int updatedCount = getJdbcTemplate().update(psc, generatedKeyHolder);
		if (log) {
			String sql1 = this.getSQL(sql, args);
			logger.info("updatedCount:" + updatedCount + " sql:" + sql1);
		}
		return generatedKeyHolder.getKey();
	}

	public int updateForRecord(String sql, StatementParameter param) {
		int updatedCount = this.getJdbcTemplate().update(sql, param.getParameters());
		if (log) {
			String sql1 = this.getSQL(sql, param);
			logger.info("updatedCount:" + updatedCount + " sql:" + sql1);
		}
		return updatedCount;
	}

	public int updateForRecord(String sql, Object... args) {
		int updatedCount = this.getJdbcTemplate().update(sql, args);
		if (log) {
			String sql1 = this.getSQL(sql, args);
			logger.info("updatedCount:" + updatedCount + " sql:" + sql1);
		}
		return updatedCount;
	}

	public int executeAndReturnKey(SimpleStatementParameter param) {
		SimpleJdbcInsert insertActor = new SimpleJdbcInsert(this.getJdbcTemplate().getDataSource()).withTableName(param.getTable()).usingGeneratedKeyColumns(param.getKeyColumn());
		Number newId = insertActor.executeAndReturnKey(param.getParameters());
		return newId.intValue();
	}

	public int updateForRecord(String sql) {
		int updatedCount = this.getJdbcTemplate().update(sql);
		if (log) {
			logger.info("updatedCount:" + updatedCount + " sql:" + sql);
		}
		return updatedCount;
	}

	public int batchUpdate(List<String> sqlList) {
		int updatedCount = 0;
		for (String sql : sqlList) {
			updatedCount += this.updateForRecord(sql);
		}
		return updatedCount;
	}

	public String beanName() {
		return this.getClass().getName();
	}

	/********************************************* 分页查询A计划start ***************************************/

	/**
	 * 拼装分页sql
	 * 
	 * @param sql
	 * @param start
	 * @param size
	 * @param param
	 * @return
	 */
	private String pageSql(String sql, long start, int size, StatementParameter param) {
		if ("mysql".equalsIgnoreCase(dbType)) {
			param.setLong(start);
			param.setInt(size);
			return sql + " limit ?,?";
		} else if ("oracle".equalsIgnoreCase(dbType)) {
			sql = "select * from (select a.* , rownum row_num from (" + sql + ") a ) b where b.row_num between ? and ? ";
			param.setLong(start + 1);
			param.setLong(start + size);
			return sql;
		} else if ("h2".equalsIgnoreCase(dbType)) {
			param.setInt(size);
			param.setLong(start);
			return sql + " LIMIT ? OFFSET ?";
		} else if ("postgresql".equalsIgnoreCase(dbType)) {
			param.setInt(size);
			param.setLong(start);
			return sql + " LIMIT ? OFFSET ?";
		} else if ("db2".equalsIgnoreCase(dbType)) {
			sql = "SELECT SUB.* FROM (" + sql + ")RES ) SUB WHERE SUB.rnk >= ? AND SUB.rnk < ?";
			param.setLong(start);
			param.setLong(start + size);
			return sql;
		} else if ("mssql".equalsIgnoreCase(dbType)) {
			sql = "SELECT SUB.* FROM (" + sql + ")RES ) SUB WHERE SUB.rnk >= ? AND SUB.rnk < ?";
			param.setLong(start);
			param.setLong(start + size);
			return sql;
		} else {
			throw new RuntimeException("配置的数据库类型暂时不支持自动拼装分页sql，请自己编写sql语句或者联系开发者支持新的数据库类型:" + dbType);
		}
	}

	/**
	 * 分页查询
	 * 
	 * @param sql
	 *            单表SELECT语句，代码从第一个“from(不区分大小写)”关键字开始截取，头尾添加select
	 *            count(0)和limit ?,?进行查询
	 * @param elementType
	 *            返回类型
	 * @param pager
	 *            包含pageNo和pageSize或者start和pageSize的分页容器
	 * @param param
	 *            填充参数
	 * @return 返回一个分页对象
	 */
	public <T> List<T> queryForPage(String sql, Class<T> elementType, Pager pager, StatementParameter param) {
		final String lowerSql = sql.toLowerCase();
		final int fromIndex = lowerSql.indexOf("from");
		String countSql = "SELECT COUNT(1) " + sql.substring(fromIndex);
		final long count = queryForLong(countSql, param);
		pager.setTotalCount(count);
		if (count == 0) {
			return new ArrayList<T>();
		} else {
			String pageSql = pageSql(sql, pager.getPageStart(), pager.getPageSize(), param);
			List<T> list = queryForList(pageSql, elementType, param);
			return list;
		}
	}

	public <T> List<T> queryForPage(String sql, Class<T> elementType, Pager pager) {
		final String lowerSql = sql.toLowerCase();
		final int fromIndex = lowerSql.indexOf("from");
		String countSql = "SELECT COUNT(1) " + sql.substring(fromIndex);
		final long count = queryForLong(countSql);
		pager.setTotalCount(count);
		if (count == 0) {
			return new ArrayList<T>();
		} else {
			StatementParameter param = new StatementParameter();
			String pageSql = pageSql(sql, pager.getPageStart(), pager.getPageSize(), param);
			List<T> list = queryForList(pageSql, elementType, param);
			return list;
		}
	}

	/********************************************* 分页查询A计划end ***************************************/

	/**
	 * 批量删除,默认删除的字段名为id(支持单条删除).
	 * 
	 * @param tablename
	 *            -------------表名.
	 * @param ids
	 *            -------------表名的id数组.
	 * 
	 * @return boolean.
	 */
	public boolean batchDelete(final String tablename, final Integer... ids) {
		return baseBatchDelete(tablename, "id", ids);
	}

	/**
	 * 批量删除(支持单条删除).
	 * 
	 * @param tablename
	 *            -------------表名.
	 * @param colname
	 *            -------------字段名.
	 * @param ids
	 *            -------------表名的id数组.
	 * 
	 * @return boolean.
	 */
	public boolean batchDelete(final String tablename, final String colname, final Integer... ids) {
		return baseBatchDelete(tablename, colname, ids);
	}

	/**
	 * 批量删除(支持单条删除).
	 * 
	 * @param table
	 *            -------------表名.
	 * @param colname
	 *            -------------字段名.
	 * @param ids
	 *            -------------表名的id数组.
	 * 
	 * @return boolean.
	 */
	private boolean baseBatchDelete(final String tablename, final String colname, final Integer... ids) {
		final StringBuffer delSql = new StringBuffer();
		delSql.append("DELETE FROM ").append(tablename).append(" WHERE id in (").append(SqlUtil.generatePaddingSymbol(ids)).append(")");
		StatementParameter param = new StatementParameter();
		for (Integer id : ids) {
			param.setInt(id);
		}
		return updateForBoolean(delSql.toString(), param);
	}

	/********************************************* 分页查询B计划start ***************************************/

	/**
	 * 分页查询
	 * 
	 * @param sql
	 *            单表SELECT语句，代码从第一个“from(不区分大小写)”关键字开始截取，头尾添加select
	 *            count(0)和limit ?,?进行查询
	 * @param type
	 *            返回类型
	 * @param start
	 *            开始位置
	 * @param limit
	 *            最大数量
	 * 
	 * @return 返回一个分页对象
	 * 
	 * @author 2014年7月15日 上午10:06:02
	 */
	public <T> Page<T> queryForLimit(String sql, Class<T> type, StatementParameter param, Integer start, Integer limit) {
		Page<T> page = new Page<T>();
		page.pageNo = -1; // 请在客户端计算
		if (start != null) {
			page.pageStart = start;
		}
		if (limit != null) {
			page.pageSize = limit;
		}
		return queryForPage(sql, type, param, page);
	}

	/**
	 * 分页查询
	 * 
	 * @param sql
	 *            单表SELECT语句，代码从第一个“from(不区分大小写)”关键字开始截取，头尾添加select
	 *            count(0)和limit ?,?进行查询
	 * @param type
	 *            返回类型
	 * @param pageNo
	 *            页码，默认：1
	 * @param pageSize
	 *            每页大小，默认：10
	 * 
	 * @return 返回一个分页对象
	 * @author 2014年7月15日 上午10:06:02
	 */
	public <T> Page<T> queryForPage(String sql, Class<T> type, StatementParameter param, Integer pageNo, Integer pageSize) {
		if (pageNo == null) {
			pageNo = 1;
		}
		if (pageSize == null) {
			pageSize = 10;
		}
		Page<T> page = new Page<T>();
		page.pageNo = pageNo;
		page.pageSize = pageSize;
		page.pageStart = (pageNo - 1) * pageSize;
		return queryForPage(sql, type, param, page);
	}

	/**
	 * 分页查询
	 * 
	 * @param sql
	 *            单表SELECT语句，代码从第一个“FROM(不区分大小写)”关键字开始截取到“ORDER
	 *            BY(不区分大小写，中间只有一个空格)”，头尾添加select count(0)和limit ?,?进行查询
	 * @param type
	 *            返回类型
	 * @param param
	 *            填充参数
	 * @param page
	 *            包含pageNo和pageSize或者start和pageSize的分页容器
	 * 
	 * @return 返回一个分页对象
	 * @author 2014年7月15日 上午10:06:02
	 */
	private <T> Page<T> queryForPage(String sql, Class<T> type, StatementParameter param, Page<T> page) {
		String lowerSql = sql.toUpperCase();
		int orderbyIndex = lowerSql.lastIndexOf("ORDER BY");
		if (orderbyIndex == -1) {
			orderbyIndex = sql.length();
		}
		String countSql = "SELECT COUNT(1) FROM (" + sql.substring(0, orderbyIndex) + ") AS COUNT";
		long count = queryForLong(countSql, param);
		page.total = count;
		page.list.clear();
		if (count >= 0) {
			String pageSql = pageSql(countSql, page.pageStart, page.pageSize, param);
			List<T> list = queryForList(pageSql, type, param);
			page.list.addAll(list);
		}
		return page;
	}

	/**
	 * 承载分页参数和结果集合的分页容器
	 * 
	 * @param <T>
	 *            结果类型
	 */
	public static class Page<T> {

		private int pageNo = 1;

		private int pageSize = 10;

		private long pageStart = 0;

		private long total = 10;

		private final List<T> list = new ArrayList<T>();

		/** 为兼容分页A计划而衍生的方法 */
		public Pager toPager() {
			Pager pager = new Pager();
			pager.setPageSize(pageSize);
			pager.setPageStart(pageStart);
			pager.setTotalCount(total);
			return pager;
		}

		@Override
		public String toString() {
			return "Page [pageNo=" + pageNo + ", pageSize=" + pageSize + ", pageStart=" + pageStart + ", total=" + total + ", list=" + list + "]";
		}

		/** 页码，默认：1 */
		public int getPageNo() {
			return pageNo;
		}

		/** 每页大小，默认：10 */
		public int getPageSize() {
			return pageSize;
		}

		/** 开始记录位置 */
		public long getPageStart() {
			return pageStart;
		}

		/** 符合条件的记录总数 */
		public long getTotal() {
			return total;
		}

		/** 结果集合 */
		public List<T> getList() {
			return list;
		}

		/**
		 * 分页参数
		 * 
		 */
		public static class PageParams {

			/** 起始查询位置 */
			private Long pageStart;

			/** 页码，与rowStart二选一 */
			private Long pageNo;

			/** 分页大小 */
			private Long pageSize;

			public Long getPageStart() {
				if (pageStart == null && pageNo == null) {
					return 0L; // 默认0
				} else if (pageStart != null) {
					return pageStart;
				} else {
					return (pageNo - 1) * getPageSize();
				}
			}

			public Long getPageNo() {
				if (pageStart == null && pageNo == null) {
					return 1L; // 默认1
				} else if (pageNo != null) {
					return pageNo;
				} else {
					return pageStart / getPageSize() + 1;
				}
			}

			public Long getPageSize() {
				if (pageSize == null) {
					return 15L; // 默认15
				} else {
					return pageSize;
				}
			}

			public void setPageStart(Long pageStart) {
				this.pageStart = pageStart;
			}

			public void setPageSize(Long pageSize) {
				this.pageSize = pageSize;
			}

			public void setPageNo(Long pageNo) {
				this.pageNo = pageNo;
			}

		}

	}

	/********************************************* 分页查询B计划end ***************************************/

}
