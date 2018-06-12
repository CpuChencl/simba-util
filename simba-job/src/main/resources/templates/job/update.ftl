<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
		<title>系统首页</title>
		<#include "../adminlte.ftl"/>
		<#include "../datetimepicker.ftl"/>
		<script type="text/javascript" src="${base}/js/job/job.js"></script>
	</head>

	<body>
		<div>
			<!-- Content Wrapper. Contains page content -->
			<div class="">
				<section class="content">
					<div class="row">

						<!-- /.col -->
						<div class="col-md-12">
							<div class="box box-primary">
								<div class="box-header with-border">
									<h3 class="box-title">修改任务</h3>
								</div>
								<form role="form" onsubmit="return Job.checkForm();" id="form" action="${base}/job/update">
									<input type="hidden" id="id" name="id" value="${job.id}" />
									<div class="box-body">
										<div class="form-group">
											<label for="name">名称</label>
											<input type="text" class="form-control" id="name" name="name" value="${job.name}" placeholder="请输入名称">
										</div>
										<div class="form-group">
											<label for="description">描述</label>
											<input type="text" class="form-control" id="description" name="description" value="${job.description}" placeholder="请输入描述">
										</div>
										<div class="form-group">
											<label for="cronExpression">cron表达式</label>
											<input type="text" class="form-control" id="cronExpression" name="cronExpression" value="${job.cronExpression}" placeholder="请输入cron表达式">
										</div>
										<div class="form-group">
											<label for="startTime">开始执行时间</label>
											<input type="text" class="form-control datetimepicker" id="startTime" name="startTime" value="${job.startTime}" placeholder="请输入开始执行时间">
										</div>
										<div class="form-group">
											<label for="endTime">结束执行时间</label>
											<input type="text" class="form-control datetimepicker" id="endTime" name="endTime" value="${job.endTime}" placeholder="请输入结束执行时间">
										</div>
										<div class="form-group">
											<label for="maxExeCount">最大执行次数</label>
											<input type="text" class="form-control" id="maxExeCount" name="maxExeCount" value="${job.maxExeCount}" placeholder="请输入最大执行次数">
										</div>
										<div class="form-group">
											<label for="className">完整类路径</label>
											<input type="text" class="form-control" id="className" name="className" value="${job.className}" placeholder="请输入完整类路径">
										</div>
										<div class="form-group">
											<label for="methodName">执行类方法名</label>
											<input type="text" class="form-control" id="methodName" name="methodName" value="${job.methodName}" placeholder="请输入执行类方法名">
										</div>
										<div class="form-group">
											<label for="delayTime">延迟时间</label>
											<input type="text" class="form-control" id="delayTime" name="delayTime" value="${job.delayTime}" placeholder="请输入延迟时间">
										</div>
										<div class="form-group">
											<label for="intervalTime">间隔时间</label>
											<input type="text" class="form-control" id="intervalTime" name="intervalTime" value="${job.intervalTime}" placeholder="请输入间隔时间">
										</div>
									</div>
									<!-- /.box-body -->

									<div class="box-footer">
										<button type="submit" class="btn btn-success">提交</button>
										<button type="button" class="btn" onclick="Job.toList();">取消</button>
									</div>
								</form>

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
		});
	</script>

</html>