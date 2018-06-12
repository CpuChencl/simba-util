package ${packageName}.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.simba.framework.util.json.JsonResult;
import ${packageName}.model.${className};
import ${packageName}.service.${className}Service;

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

	@ResponseBody
	@RequestMapping("/list")
	public List<${className}> list() {
		List<${className}> list = ${firstLower}Service.listAll();
		return list;
	}

	/**
	 * 新增${classDesc}
	 * 
	 * @param ${firstLower}
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/add")
	public JsonResult add(${className} ${firstLower}) {
		${firstLower}Service.add(${firstLower});
		return new JsonResult();
	}

	@ResponseBody
	@RequestMapping("/update")
	public JsonResult update(${className} ${firstLower}) {
		${firstLower}Service.update(${firstLower});
		return new JsonResult();
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
