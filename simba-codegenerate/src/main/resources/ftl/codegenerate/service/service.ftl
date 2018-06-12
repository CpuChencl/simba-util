package  ${packageName}.service;

import java.util.List;

import com.simba.framework.util.jdbc.Pager;
import ${packageName}.model.${className};
<#if isSearch == true>
import ${packageName}.model.form.${searchFormClassName};
</#if>
/**
 *${classDesc} Service
 * 
 * @author caozj
 * 
 */
public interface ${className}Service {

	void add(${className} ${firstLower});

	void update(${className} ${firstLower});

	void delete(${idType} id);

	List<${className}> listAll();
		
	${countType} count();
	
	<#if isSearch == true>
	${countType} count(${searchFormClassName} ${searchFormFirstLower});
	</#if>
	${countType} countBy(String field, Object value);
	
	${countType} countByAnd(String field1, Object value1, String field2, Object value2);
	
	${countType} countByOr(String field1, Object value1, String field2, Object value2);
	
	void deleteBy(String field, Object value);
	
	void deleteByAnd(String field1, Object value1, String field2, Object value2);
	
	void deleteByOr(String field1, Object value1, String field2, Object value2);
	
	List<${className}> page(Pager page);
	
	<#if isSearch == true>
	List<${className}> page(Pager page, ${searchFormClassName} ${searchFormFirstLower});
	</#if>
	${className} get(${idType} id);
	
	void batchDelete(List<${idType}> idList);

	${className} getBy(String field, Object value);

	${className} getByAnd(String field1, Object value1, String field2, Object value2);

	${className} getByOr(String field1, Object value1, String field2, Object value2);

	List<${className}> listBy(String field, Object value);

	List<${className}> listByAnd(String field1, Object value1, String field2, Object value2);

	List<${className}> listByOr(String field1, Object value1, String field2, Object value2);

	List<${className}> pageBy(String field, Object value, Pager page);

	List<${className}> pageByAnd(String field1, Object value1, String field2, Object value2, Pager page);

	List<${className}> pageByOr(String field1, Object value1, String field2, Object value2, Pager page);

}
