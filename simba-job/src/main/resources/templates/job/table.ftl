<#list list as job>
	<tr>
		<td><input type="checkbox" name="job" value="${job.id}"></td>
		<td>${job.name}</td>
		<td>${job.description}</td>
		<td>${job.cronExpression}</td>
		<td>${job.startTime}</td>
		<td>${job.endTime}</td>
		<td>${job.exeCount}</td>
		<td>${job.maxExeCount}</td>
		<td>${job.className}</td>
		<td>${job.methodName}</td>
		<td>${job.delayTime}</td>
		<td>${job.intervalTime}</td>
		<#if job.status=="waiting">
		<td>未启动</td>
		</#if>
		<#if job.status=="running">
		<td>运行中</td>
		</#if>
		<#if job.status=="error">
		<td>运行异常</td>
		</#if>
		<#if job.status=="finish">
		<td>运行完成</td>
		</#if>
		<#if job.status=="suspend">
		<td>暂停</td>
		</#if>
		<td>
			<button type="button" class="btn btn-default btn-sm" onclick="Job.toUpdate(${job.id});"><i class="fa fa-pencil-square-o"></i>修改</button>
			<button type="button" class="btn btn-default btn-sm" onclick="Job.deleteJob(${job.id});"><i class="fa fa-remove"></i>删除</button>
			
			<#if job.status=="waiting">
			<button type="button" class="btn btn-default btn-sm" onclick="Job.stopJob(${job.id});"><i class="fa fa-ban"></i>暂停</button>
			</#if>
			<#if job.status=="running">
			<button type="button" class="btn btn-default btn-sm" onclick="Job.stopJob(${job.id});"><i class="fa fa-ban"></i>暂停</button>
			</#if>
			<#if job.status=="error">
			<button type="button" class="btn btn-default btn-sm" onclick="Job.stopJob(${job.id});"><i class="fa fa-ban"></i>暂停</button>
			</#if>
			<#if job.status=="suspend">
			<button type="button" class="btn btn-default btn-sm" onclick="Job.startJob(${job.id});"><i class="fa fa-rocket"></i>启动</button>
			</#if>
		</td>
	</tr>
</#list>