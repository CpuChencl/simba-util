var Job = {

	"exportSql" : function() {
		window.self.location.href = contextPath + "/job/exportSql";
	},

	"toAdd" : function() {
		window.self.location.href = contextPath + "/job/toAdd";
	},

	"batchDelete" : function() {
		var ids = new Array();
		$("input[name='job']").each(function() {
			if (true == $(this).is(':checked')) {
				ids.push($(this).val());
			}
		});
		if (ids.length == 0) {
			parent.showInfo("请选择要删除的记录");
			return false;
		}
		$.ajax({
			type : "post",
			url : contextPath + "/job/batchDelete",
			data : {
				"ids" : ids.join(",")
			},
			async : true,
			dataType : "json",
			success : function(data) {
				if (data.code == 200) {
					Job.initJobList(0, Page.size);
				} else {
					parent.showInfo(data.msg);
				}
			}
		});
	},

	"initJobList" : function(start, pageSize) {
		$.ajax({
			type : "get",
			url : contextPath + "/job/getList",
			data : {
				"pageStart" : start,
				"pageSize" : pageSize
			},
			async : true,
			dataType : "html",
			success : function(html) {
				$("#table").find("tbody").html(html);
				CheckBox.init();
				setTimeout("CheckBox.bindCheckAll();", 1000);
			}
		});
		$.ajax({
			type : "get",
			url : contextPath + "/job/count",
			async : true,
			data : {},
			async : true,
			dataType : "json",
			success : function(data) {
				var total = data.data;
				var pageHtml = Page.init(total, start, pageSize, "Job.clickPager");
				$("#page").html(pageHtml);
			}
		});
	},

	"clickPager" : function(start, pageSize) {
		Job.initJobList(start, pageSize);
	},

	"toUpdate" : function(id) {
		window.self.location.href = contextPath + "/job/toUpdate?id=" + id;
	},

	"deleteJob" : function(id) {
		$.ajax({
			type : "post",
			url : contextPath + "/job/delete",
			data : {
				"id" : id
			},
			async : true,
			dataType : "json",
			success : function(data) {
				if (data.code == 200) {
					Job.initJobList(0, Page.size);
				} else {
					parent.showInfo(data.msg);
				}
			}
		});
	},

	"checkForm" : function() {
		var cronExpression = $("#cronExpression").val();
		var intervalTime = $("#intervalTime").val();
		if (!cronExpression && !intervalTime) {
			parent.showInfo("间隔时间和cron表达式不能同时为空");
			return false;
		}
		var name = $("#name").val();
		if (!name) {
			parent.showInfo("名称不能为空");
			return false;
		}
		var className = $("#className").val();
		if (!className) {
			parent.showInfo("完整类路径不能为空");
			return false;
		}
		var methodName = $("#methodName").val();
		if (!methodName) {
			parent.showInfo("执行类方法名不能为空");
			return false;
		}
		return true;
	},

	"toList" : function() {
		window.self.location.href = contextPath + "/job/list";
	},

	"startJob" : function(id) {
		$.ajax({
			url : contextPath + "/job/start?json",
			type : "post",
			dataType : "json",
			async : true,
			data : {
				id : id
			},
			success : function(data) {
				if (data.code == 200) {
					var currentPage = $(".pagination").find("li.active").find("a").html();
					currentPage = parseInt($.trim(currentPage));
					var start = (currentPage - 1) * Page.size;
					Job.initJobList(start, Page.size);
					parent.showSuccessInfo("启动成功");
				} else {
					parent.showInfo(data.msg);
				}
			},
			error : function() {
				parent.showInfo("启动失败");
			}
		});
	},

	"stopJob" : function(id) {
		$.ajax({
			url : contextPath + "/job/stop?json",
			type : "post",
			dataType : "json",
			async : true,
			data : {
				id : id
			},
			success : function(data) {
				if (data.code == 200) {
					var currentPage = $(".pagination").find("li.active").find("a").html();
					currentPage = parseInt($.trim(currentPage));
					var start = (currentPage - 1) * Page.size;
					Job.initJobList(start, Page.size);
					parent.showSuccessInfo("暂停成功");
				} else {
					parent.showInfo(data.msg);
				}
			},
			error : function() {
				parent.showInfo("暂停失败");
			}
		});
	},

	"end" : null
};