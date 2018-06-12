var ${className} = {
	<#if isSearch == true>
	"toSearch": function() {
		${className}.init${className}List(0, Page.size, "doSearch");
	},
	</#if>
	
	"toAdd": function() {
		window.self.location.href = contextPath + "/${firstLower}/toAdd";
	},

	"batchDelete": function() {
		var ids = new Array();
		$("input[name='${firstLower}']").each(function() {
			if(true == $(this).is(':checked')) {
				ids.push($(this).val());
			}
		});
		if(ids.length == 0) {
			parent.showInfo("请选择要删除的记录");
			return false;
		}
		$.ajax({
			type: "post",
			url: contextPath + "/${firstLower}/batchDelete",
			data: {
				"id": ids.join(",")
			},
			async: true,
			dataType: "json",
			success: function(data) {
				if(data.code == 200) {
					${className}.init${className}List(0, Page.size);
				} else {
					parent.showInfo(data.msg);
				}
			}
		});
	},
	"init${className}List": function(start, pageSize, method) {
		var data = {};
		var data2 = {};
		method = method || "getList";
		<#if searchFormFields?exists>
		<#list searchFormFields as tableField>
		data["${tableField["key"]}"] = $("#${tableField["key"]}").val();
		</#list>
		</#if>
		$.extend(data2,data);
		data["pageStart"] = start;
		data["pageSize"] = pageSize;
		$.ajax({
			type: "get",
			url: contextPath + "/${firstLower}/" + method,
			data: data,
			async: true,
			dataType: "html",
			success: function(html) {
				$("#table").find("tbody").html(html);
				CheckBox.init();
				setTimeout("CheckBox.bindCheckAll();", 1000);
			}
		});
		$.ajax({
			type: "get",
			url: contextPath + "/${firstLower}/count",
			async: true,
			data: data2,
			dataType: "json",
			success: function(data) {
				var total = data.data;
				var pageHtml = Page.init(total, start, pageSize, "${className}.clickPager");
				$("#page").html(pageHtml);
			}
		});
	},
	"clickPager": function(start, pageSize) {
		${className}.init${className}List(start, pageSize<#if isSearch == true>, "doSearch"</#if>);
	},

	"toUpdate": function(id) {
		window.self.location.href = contextPath + "/${firstLower}/toUpdate?id=" + id;
	},

	"delete${className}": function(id) {
		$.ajax({
			type: "post",
			url: contextPath + "/${firstLower}/batchDelete",
			data: {
				"id": id
			},
			async: true,
			dataType: "json",
			success: function(data) {
				if(data.code == 200) {
					${className}.init${className}List(0, Page.size);
				} else {
					parent.showInfo(data.msg);
				}
			}
		});
	},

	"checkForm": function() {
		return true;
	},

	"toList": function() {
		window.self.location.href = contextPath + "/${firstLower}/list";
	},

	"end": null
};