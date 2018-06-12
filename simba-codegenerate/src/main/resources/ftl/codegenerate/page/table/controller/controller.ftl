package ${packageName}.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.simba.framework.util.jdbc.Pager;
import com.simba.framework.util.json.JsonResult;
import ${packageName}.model.${className};
import ${packageName}.service.${className}Service;
<#if isSearch == true>
import ${packageName}.model.form.${searchFormClassName};
</#if >
/**
 * ${classDesc}控制器
 * 
 * @author caozj
 * 
 */
@Controller
@RequestMapping("/${firstLower}")
public class ${className}Controller {

	@Autowired
	private ${className}Service ${firstLower}Service;

	@RequestMapping("/list")
	public String list() {
		return "${firstLower}/list";
	}
	
	@RequestMapping("/getList")
	public String getList(Pager pager,ModelMap model){
		List<${className}> list = ${firstLower}Service.page(pager);
		model.put("list", list);
		return "${firstLower}/table";
	}
	
	<#if isSearch == true>
	@RequestMapping("/doSearch")
	public String getList(Pager pager, ${searchFormClassName} ${searchFormFirstLower}, ModelMap model){
		List<${className}> list = ${firstLower}Service.page(pager, ${searchFormFirstLower});
		model.put("list", list);
		return "${firstLower}/table";
	}
	
	@ResponseBody
	@RequestMapping("/count")
	public JsonResult count(${searchFormClassName} ${searchFormFirstLower}) {
		${countType} count = ${firstLower}Service.count(${searchFormFirstLower});
		return new JsonResult(count, "", 200);
	}
	<#else>
	@ResponseBody
	@RequestMapping("/count")
	public JsonResult count() {
		${countType} count = ${firstLower}Service.count();
		return new JsonResult(count, "", 200);
	}
	</#if>
	
	@RequestMapping("/toAdd")
	public String toAdd() {
		return "${firstLower}/add";
	}

	@RequestMapping("/add")
	public String add(${className} ${firstLower}) {
		${firstLower}Service.add(${firstLower});
		return "redirect:/${firstLower}/list";
	}

	@RequestMapping("/toUpdate")
	public String toUpdate(${idType} id, ModelMap model) {
		${className} ${firstLower} = ${firstLower}Service.get(id);
		model.put("${firstLower}", ${firstLower});
		return "${firstLower}/update";
	}

	@RequestMapping("/update")
	public String update(${className} ${firstLower}) {
		${firstLower}Service.update(${firstLower});
		return "redirect:/${firstLower}/list";
	}

	@ResponseBody
	@RequestMapping("/delete")
	public JsonResult delete(${idType} id, ModelMap model) {
		${firstLower}Service.delete(id);
		return new JsonResult();
	}

	@ResponseBody
	@RequestMapping("/batchDelete")
	public JsonResult batchDelete(${idType}[] id, ModelMap model) {
		${firstLower}Service.batchDelete(Arrays.asList(id));
		return new JsonResult();
	}

	

}
