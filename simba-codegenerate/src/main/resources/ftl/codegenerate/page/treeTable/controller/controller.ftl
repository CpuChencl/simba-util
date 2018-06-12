package ${packageName}.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.simba.bootstrap.model.TreeViewData;
import com.simba.framework.util.json.JsonResult;
import ${packageName}.model.${className};
import ${packageName}.service.${className}Service;
import com.simba.model.constant.ConstantData;

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
	public String list(${idType} parentID, ModelMap model) {
		if (parentID == null) {
			parentID = ConstantData.TREE_ROOT_ID;
		}
		String parentName = "${classDesc}树";
		if (parentID != ConstantData.TREE_ROOT_ID) {
			parentName = ${firstLower}Service.get(parentID).getText();
		}
		model.put("parentID", parentID);
		model.put("parentName", parentName);
		return "${firstLower}/list";
	}

	@ResponseBody
	@RequestMapping("/get${className}Tree")
	public List<TreeViewData> get${className}Tree() {
		TreeViewData root = new TreeViewData();
		root.setId(ConstantData.TREE_ROOT_ID);
		root.setText("${classDesc}树");
		root.setTags(ConstantData.TREE_ROOT_ID + "");
		List<${className}> all${className}s = ${firstLower}Service.listAll();
		Map<${idType}, TreeViewData> nodeMap = new HashMap<>();
		all${className}s.forEach((${className} ${firstLower}) -> {
			TreeViewData node = new TreeViewData();
			node.setId(${firstLower}.getId());
			node.setText(${firstLower}.getText());
			node.setTags(${firstLower}.getId() + "");
			node.setParentID(${firstLower}.getParentID());
			nodeMap.put(${firstLower}.getId(), node);
		});
		nodeMap.put(root.getId(), root);
		all${className}s.forEach((${className} ${firstLower}) -> {
			TreeViewData node = nodeMap.get(${firstLower}.getId());
			TreeViewData parentNode = nodeMap.get(${firstLower}.getParentID());
			parentNode.addChildren(node);
		});
		List<TreeViewData> list = new ArrayList<>(1);
		list.add(root);
		return list;
	}

	@RequestMapping("/get${className}List")
	public String get${className}List(${idType} parentID, ModelMap model) {
		List<${className}> list = ${firstLower}Service.listBy("parentID",parentID);
		model.put("list", list);
		return "${firstLower}/table";
	}

	/**
	 * 新增${classDesc}
	 * 
	 * @param ${firstLower}
	 * @return
	 */
	@RequestMapping("/add")
	public String add(${className} ${firstLower}) {
		${firstLower}Service.add(${firstLower});
		return "redirect:/${firstLower}/list?parentID=" + ${firstLower}.getParentID();
	}

	@RequestMapping("/toAdd")
	public String toAdd(${idType} parentID, ModelMap model) {
		String parentName = "${classDesc}树";
		if (parentID != ConstantData.TREE_ROOT_ID) {
			parentName = ${firstLower}Service.get(parentID).getText();
		}
		model.put("parentID", parentID);
		model.put("parentName", parentName);
		return "${firstLower}/add";
	}

	@RequestMapping("/toUpdate")
	public String toUpdate(${idType} id, ModelMap model) {
		${className} ${firstLower} = ${firstLower}Service.get(id);
		String parentName = "${classDesc}树";
		${idType} parentID = ${firstLower}.getParentID();
		if (parentID != ConstantData.TREE_ROOT_ID) {
			parentName = ${firstLower}Service.get(parentID).getText();
		}
		model.put("${firstLower}", ${firstLower});
		model.put("parentName", parentName);
		return "${firstLower}/update";
	}

	@RequestMapping("/update")
	public String update(${className} ${firstLower}) {
		${firstLower}Service.update(${firstLower});
		return "redirect:/${firstLower}/list?parentID=" + ${firstLower}.getParentID();
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
