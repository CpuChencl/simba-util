<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
		<title>系统首页</title>
		<#include "../adminlte.ftl"/>
		<#include "../iCheck.ftl"/>
		<script type="text/javascript" src="${base}/js/common/checkbox.js"></script>
		<script type="text/javascript" src="${base}/js/common/page.js"></script>
		<script type="text/javascript" src="${base}/js/job/job.js"></script>
	</head>

	<body>
		<div>
			<!-- Content Wrapper. Contains page content -->
			<div class="">
				<section class="content">
					<div class="row">
						<div class="col-md-12">
							<div class="box box-primary">
								<div class="box-header with-border">
									<h3 class="box-title">任务管理</h3>
								</div>
								<!-- /.box-header -->
								<div class="box-body no-padding">
									<div class="mailbox-controls">
										<!-- Check all button -->
										<button type="button" class="btn btn-default btn-sm checkbox-toggle" onclick="Job.toAdd();"><i class="fa fa-plus"></i>
                新增</button>
										<button type="button" class="btn btn-default btn-sm" onclick="Job.batchDelete();"><i class="fa fa-remove"></i>删除</button>
										<div class="pull-right">
											<button type="button" class="btn btn-default btn-sm" onclick="Job.exportSql();"><i class="fa fa-paper-plane"></i>导出SQL数据</button>
										</div>
									</div>
									<!-- /.pull-right -->
								</div>
								<div class="table-responsive">
									<table class="table table-hover table-striped table-bordered" id="table">
										<thead>
											<tr>
												<th><input type="checkbox" name="checkAll" id="checkAll">全选</th>
												<th>名称</th>
												<th>描述</th>
												<th>cron表达式</th>
												<th>开始执行时间</th>
												<th>结束执行时间</th>
												<th>执行次数</th>
												<th>最大执行次数</th>
												<th>完整类路径</th>
												<th>执行类方法名</th>
												<th>延迟时间</th>
												<th>间隔时间</th>
												<th>状态</th>
												<th>操作</th>
											</tr>
										</thead>
										<tbody>

										</tbody>
									</table>
									<!-- /.table -->
									<div id="page">
									</div>
								</div>
								<!-- /.mail-box-messages -->
							</div>
						</div>
						<!-- /. box -->
					</div>
					<!-- /.col -->
			</div>
			<!-- /.row -->
			</section>
			<!-- /.content -->
		</div>
		<!-- /.content-wrapper -->
		<!-- Add the sidebar's background. This div must be placed
       immediately after the control sidebar -->
		</div>
		<!-- ./wrapper -->
	</body>
	<script type="text/javascript">
		$(document).ready(function() {
			Job.initJobList(0, Page.size);
		});
	</script>

</html>