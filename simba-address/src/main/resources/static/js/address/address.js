/**
 * 中国名址数据库，四级联动工具
 * 
 * 使用方法：Address.init("省份Select的id", "城市Select的id", "县区Select的id", "乡镇Select的id",
 * "默认选择省份名称", "默认选择城市名称", "默认选择县区名称", "默认选择乡镇名称");
 * 
 * 一般情况下使用省份+城市+县区即可。 默认选择的4个参数可以不填。
 * 
 */
var Address = {

	"getProvinceUrl" : contextPath + "/address/provinces",

	"getCityUrl" : contextPath + "/address/citys",

	"getCountryUrl" : contextPath + "/address/countrys",

	"getTownUrl" : contextPath + "/address/towns",

	"init" : function(provinceDomId, cityDomId, countyDomId, townDomId, provinceName, cityName, countyName, townName) {
		var provinceDom = $("#" + provinceDomId);
		var cityDom = $("#" + cityDomId);
		var countyDom = $("#" + countyDomId);
		var townDom = $("#" + townDomId);
		// 省份切换事件
		provinceDom.change(function() {
			var province = provinceDom.val();
			if (cityDom.length > 0) {
				$.post(Address.getCityUrl, {
					"provinceName" : province
				}, function(data) {
					cityDom.empty();
					for (var i = 0; i < data.data.length; i++) {
						cityDom.append($("<option value='" + data.data[i] + "'>" + data.data[i] + "</option>"));
					}
					if (cityName) {
						cityDom.find("option[value='" + cityName + "']").attr("selected", true).parent().trigger(
								"change");
						cityName = undefined;
					} else {
						cityDom.find("option:eq(0)").attr("selected", true).parent().trigger("change");
					}
				}, "json");
			}
		});
		// 城市切换事件
		cityDom.change(function() {
			var province = provinceDom.val();
			var city = cityDom.val();
			if (countyDom.length > 0) {
				$.post(Address.getCountryUrl, {
					"provinceName" : province,
					"cityName" : city
				}, function(data) {
					countyDom.empty();
					for (var i = 0; i < data.data.length; i++) {
						countyDom.append($("<option value='" + data.data[i].name + "'>" + data.data[i].name
								+ "</option>"));
					}
					if (countyName) {
						countyDom.find("option[value='" + countyName + "']").attr("selected", true).parent().trigger(
								"change");
						countyName = undefined;
					} else {
						countyDom.find("option:eq(0)").attr("selected", true).parent().trigger("change");
					}
				}, "json");
			}
		});
		// 县区切换事件
		countyDom.change(function() {
			var province = provinceDom.val();
			var city = cityDom.val();
			var county = countyDom.val();
			if (townDom.length > 0) {
				$.post(Address.getTownUrl, {
					"provinceName" : province,
					"cityName" : city,
					"countyName" : county
				}, function(data) {
					townDom.empty();
					for (var i = 0; i < data.data.length; i++) {
						townDom
								.append($("<option value='" + data.data[i].name + "'>" + data.data[i].name
										+ "</option>"));
					}
					if (countyName) {
						townDom.find("option[value='" + countyName + "']").attr("selected", true).parent().trigger(
								"change");
						countyName = undefined;
					} else {
						townDom.find("option:eq(0)").attr("selected", true).parent().trigger("change");
					}
				}, "json");
			}
		});
		// 下载省份
		$.get(Address.getProvinceUrl, function(data) {
			provinceDom.empty();
			provinceDom.append($("<option value=''>选择省份</option>"));
			for (var i = 0; i < data.data.length; i++) {
				provinceDom.append($("<option value='" + data.data[i] + "'>" + data.data[i] + "</option>"));
			}
			if (provinceName) {
				provinceDom.find("option[value='" + provinceName + "']").attr("selected", true).parent().trigger(
						"change");
				provinceName = undefined;
			} else {
				provinceDom.find("option:eq(0)").attr("selected", true).parent().trigger("change");
			}
		}, "json");
	},

	"end" : null
};