var ${className} = {

	"initSelect${className}Tree": function(id, text) {
		TreeViewUtil.initTree("tree", contextPath + "/${firstLower}/get${className}Tree", function(data) {
			$("#parentName").val(data.text);
			$("#parentID").val(data.id);
			$('#tree').fadeOut();
		}, function() {
			if(!!id && !!text) {
				TreeViewUtil.selectTreeNode("tree", id, text);
			}
		});
	},

	"init${className}Tree": function(id, text) {
		TreeViewUtil.initTree("tree", contextPath + "/${firstLower}/get${className}Tree", function(data) {
			$("#parentName").val(data.text);
			$("#parentID").val(data.id);
			${className}.init${className}List();
		}, function() {
			if(!!id && !!text) {
				TreeViewUtil.selectTreeNode("tree", id, text);
			}
		});
	},

	"init${className}List": function() {
		$.ajax({
			type: "get",
			url: contextPath + "/${firstLower}/get${className}List",
			data: {
				"parentID": $("#parentID").val()
			},
			async: true,
			dataType: "html",
			success: function(html) {
				$("#table").find("tbody").html(html);
				CheckBox.init();
				setTimeout("CheckBox.bindCheckAll();", 1000);
			}
		});
	},

	"toAdd": function() {
		window.self.location.href = contextPath + "/${firstLower}/toAdd?parentID=" + $("#parentID").val();
	},

	"toUpdate": function(id) {
		window.self.location.href = contextPath + "/${firstLower}/toUpdate?id=" + id;
	},

	"delete${className}": function(id) {
		$.ajax({
			type: "post",
			url: contextPath + "/${firstLower}/delete",
			data: {
				"id": id
			},
			async: true,
			dataType: "json",
			success: function(data) {
				if(data.code == 200) {
					${className}.init${className}List();
					${className}.init${className}Tree($("#parentID").val(), $("#parentName").val());
				} else {
					parent.showInfo(data.msg);
				}
			}
		});
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
					${className}.init${className}List();
					${className}.init${className}Tree($("#parentID").val(), $("#parentName").val());
				} else {
					parent.showInfo(data.msg);
				}
			}
		});
	},

	"toList": function() {
		window.self.location.href = contextPath + "/${firstLower}/list?parentID=" + $("#parentID").val();
	},

	"checkForm": function() {
		var text = $("#text").val();
		if(!text) {
			parent.showInfo("名称不能为空");
			return false;
		}
		var parentID = $("#parentID").val();
		if(!parentID) {
			parent.showInfo("父${classDesc}不能为空");
			return false;
		}
		return true;
	},

	"end": null

};