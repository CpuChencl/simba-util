<!DOCTYPE html>
<html>
<#assign dollar = '$'>
<#assign pound = '#'>
	<head>
		<meta charset="utf-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
		<title>系统首页</title>
		<${pound}include "../adminlte.ftl"/>
		<script type="text/javascript" src="${dollar}{base}/js/plugins/bootstrap-treeview.min.js"></script>
		<script type="text/javascript" src="${dollar}{base}/js/util/treeviewutil.js"></script>
		<script type="text/javascript" src="${dollar}{base}/js/${firstLower}/${firstLower}.js"></script>
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
									<h3 class="box-title">修改${classDesc}</h3>
								</div>
								<form role="form" onsubmit="return ${className}.checkForm();" id="form" action="${dollar}{base}/${firstLower}/update">
									<input type="hidden" id="id" name="id" value="${dollar}{${firstLower}.id}" />
									<div class="box-body">
										<#list filedsWithPage as field> 
										<div class="form-group">
											<label for="${field.key}">${field.desc}</label>
											<input type="text" class="form-control" id="${field.key}" name="${field.key}" value="${dollar}{${firstLower}.${field.key}}" placeholder="请输入${field.desc}">
										</div>
										</#list> 
										<div class="form-group">
											<label for="parentName">父${classDesc}</label>
											<input type="hidden" id="parentID" name="parentID" value="${dollar}{${firstLower}.parentID}" />
											<input type="text" onclick="$('#tree').fadeToggle();" class="form-control" id="parentName" name="parentName" value="${dollar}{parentName}" placeholder="请选择父${classDesc}">
											<div id="tree" style="display: none;"></div>
										</div>
									</div>
									<!-- /.box-body -->

									<div class="box-footer">
										<button type="submit" class="btn btn-success">提交</button>
										<button type="button" class="btn" onclick="${className}.toList();">取消</button>
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
		${dollar}(document).ready(function() {
			${className}.initSelect${className}Tree($("#parentID").val(), $("#parentName").val());
		});
	</script>

</html>