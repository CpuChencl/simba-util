package com.simba.address;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.simba.framework.util.json.JsonResult;

/** 名址库 */
@Controller
@RequestMapping("/address")
public class AddressDatabaseController {

	private AddressDatabase address = AddressDatabase.getInstance();

	@RequestMapping("/show")
	public String show() {
		return "address/show";
	}

	/** 省份列表 */
	@RequestMapping("/provinces")
	public String provinces(ModelMap model) {
		model.put("message", new JsonResult(address.searchProvinces()).toJson());
		return "message";
	}

	/** 查询城市 */
	@RequestMapping("/citys")
	public String citys(String provinceName, ModelMap model) {
		model.put("message", new JsonResult(address.searchCitys(provinceName)).toJson());
		return "message";
	}

	/** 查询县区 */
	@RequestMapping("/countrys")
	public String countrys(String provinceName, String cityName, ModelMap model) {
		model.put("message", new JsonResult(address.searchCountys(provinceName, cityName)).toJson());
		return "message";
	}

	/** 查询乡镇 */
	@RequestMapping("/towns")
	public String towns(String provinceName, String cityName, String countyName, ModelMap model) {
		model.put("message", new JsonResult(address.searchTowns(provinceName, cityName, countyName)).toJson());
		return "message";
	}

}
