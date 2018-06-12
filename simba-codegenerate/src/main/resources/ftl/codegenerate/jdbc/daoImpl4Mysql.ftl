package ${packageName}.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ${packageName}.dao.${className}Dao;
import com.simba.framework.util.jdbc.Jdbc;
import com.simba.framework.util.jdbc.Pager;
import com.simba.framework.util.jdbc.StatementParameter;
import ${packageName}.model.${className};
<#if isSearch == true>
import ${packageName}.model.form.${searchFormClassName};
</#if>
/**
 * ${classDesc} Dao实现类
 * 
 * @author caozj
 *  
 */
@Repository
public class ${className}DaoImpl implements ${className}Dao {

	@Autowired
	private Jdbc jdbc;

	private static final String table = "${firstLower}";

	@Override
	public void add(${className} ${firstLower}) {
	<#if idType!="String">
		String sql = "insert into " + table + "(${insertProperties}) values(${propertiesCount})";
		jdbc.updateForBoolean(sql, ${params});
	</#if> 
	<#if idType=="String">
		String sql = "insert into " + table + "(id,${insertProperties}) values(?,${propertiesCount})";	
		jdbc.updateForBoolean(sql, ${firstLower}.getId(), ${params});
	</#if> 
	}

	@Override
	public void update(${className} ${firstLower}) {
		String sql = "update " + table + " set ${updateProperties} where id = ?  ";
		jdbc.updateForBoolean(sql,${params}, ${firstLower}.getId());
	}

	@Override
	public void delete(${idType} id) {
		String sql = "delete from " + table + " where id = ? ";
		jdbc.updateForBoolean(sql, id);
	}

	@Override
	public List<${className}> page(Pager page) {
		String sql = "select * from " + table;
		return jdbc.queryForPage(sql, ${className}.class, page);
	}
	<#if isSearch == true>
	@Override
	public List<${className}> page(Pager page, ${searchFormClassName} ${searchFormFirstLower}) {
		String sql = "select * from " + table;
		StatementParameter param = new StatementParameter();
		return jdbc.queryForPage(buildCondition(sql, ${searchFormFirstLower}, param), ${className}.class, page, param);
	}
	</#if>
	@Override
	public List<${className}> listAll(){
		String sql = "select * from " + table;
		return jdbc.queryForList(sql, ${className}.class);
	}
	
	public ${countType} count(){
		String sql = "select count(*) from " + table;
		return jdbc.queryFor${countName}(sql); 
	}
	
	<#if isSearch == true>
	@Override
	public ${countType} count(${searchFormClassName} ${searchFormFirstLower}){
		String sql = "select count(*) from " + table;
		StatementParameter param = new StatementParameter();
		return jdbc.queryFor${countName}(buildCondition(sql, ${searchFormFirstLower}, param), param); 
	}
	</#if>
	@Override
	public ${className} get(${idType} id) {
		String sql = "select * from " + table + " where id = ? ";
		return jdbc.query(sql, ${className}.class, id);
	}
	
	@Override
	public ${className} getBy(String field, Object value) {
		String sql = "select * from " + table + " where " + field + " = ? ";
		return jdbc.query(sql, ${className}.class, value);
	}

	@Override
	public ${className} getByAnd(String field1, Object value1, String field2, Object value2) {
		String sql = "select * from " + table + " where " + field1 + " = ? and " + field2 + " = ? ";
		return jdbc.query(sql, ${className}.class, value1, value2);
	}

	@Override
	public ${className} getByOr(String field1, Object value1, String field2, Object value2) {
		String sql = "select * from " + table + " where " + field1 + " = ? or " + field2 + " = ? ";
		return jdbc.query(sql, ${className}.class, value1, value2);
	}

	@Override
	public List<${className}> listBy(String field, Object value) {
		String sql = "select * from " + table + " where " + field + " = ? ";
		return jdbc.queryForList(sql, ${className}.class, value);
	}

	@Override
	public List<${className}> listByAnd(String field1, Object value1, String field2, Object value2) {
		String sql = "select * from " + table + " where " + field1 + " = ? and " + field2 + " = ? ";
		return jdbc.queryForList(sql, ${className}.class, value1, value2);
	}

	@Override
	public List<${className}> listByOr(String field1, Object value1, String field2, Object value2) {
		String sql = "select * from " + table + " where " + field1 + " = ? or " + field2 + " = ? ";
		return jdbc.queryForList(sql, ${className}.class, value1, value2);
	}

	@Override
	public List<${className}> pageBy(String field, Object value, Pager page) {
		String sql = "select * from " + table + " where " + field + " = ? ";
		StatementParameter param = new StatementParameter();
		param.set(value);
		return jdbc.queryForPage(sql, ${className}.class, page, param);
	}

	@Override
	public List<${className}> pageByAnd(String field1, Object value1, String field2, Object value2, Pager page) {
		String sql = "select * from " + table + " where " + field1 + " = ? and " + field2 + " = ? ";
		StatementParameter param = new StatementParameter();
		param.set(value1);
		param.set(value2);
		return jdbc.queryForPage(sql, ${className}.class, page, param);
	}

	@Override
	public List<${className}> pageByOr(String field1, Object value1, String field2, Object value2, Pager page) {
		String sql = "select * from " + table + " where " + field1 + " = ? or " + field2 + " = ? ";
		StatementParameter param = new StatementParameter();
		param.set(value1);
		param.set(value2);
		return jdbc.queryForPage(sql, ${className}.class, page, param);
	}

	@Override
	public ${countType} countBy(String field, Object value) {
		String sql = "select count(*) from " + table + " where " + field + " = ? ";
		return jdbc.queryFor${countName}(sql, value);
	}
	
	@Override
	public void deleteBy(String field, Object value) {
		String sql = "delete from " + table + " where " + field + " = ? ";
		jdbc.updateForBoolean(sql, value);
	}

	@Override
	public ${countType} countByOr(String field1, Object value1, String field2, Object value2){
		String sql = "select count(*) from " + table + " where " + field1 + " = ? or " + field2 + " = ? ";
		return jdbc.queryFor${countName}(sql, value1, value2);
	}
	
	@Override
	public ${countType} countByAnd(String field1, Object value1, String field2, Object value2){
		String sql = "select count(*) from " + table + " where " + field1 + " = ? and " + field2 + " = ? ";
		return jdbc.queryFor${countName}(sql, value1, value2);
	}
	
	@Override
	public void deleteByAnd(String field1, Object value1, String field2, Object value2){
		String sql = "delete from " + table + " where " + field1 + " = ? and " + field2 + " = ? ";
		jdbc.updateForBoolean(sql, value1, value2);
	}
	
	@Override
	public void deleteByOr(String field1, Object value1, String field2, Object value2){
		String sql = "delete from " + table + " where " + field1 + " = ? or " + field2 + " = ? ";
		jdbc.updateForBoolean(sql, value1, value2);
	}
	<#if isSearch == true>
	private String buildCondition(String sql, ${searchFormClassName} ${searchFormFirstLower}, StatementParameter param) {
		sql += " where 1 = 1";
		<#if searchFormFields?exists>
		<#list searchFormFields as tableField>
        if (${searchFormFirstLower}.get${tableField["firstUpperKey"]}() != null 
        		&& StringUtils.isNotEmpty(${searchFormFirstLower}.get${tableField["firstUpperKey"]}()+"")) {
			sql += " and ${tableField["field"]}  ${tableField["oper"]} ?";
			param.set(${searchFormFirstLower}.get${tableField["firstUpperKey"]}());
		}
        </#list>  
		</#if>
		return sql;
	}
	</#if>
}
