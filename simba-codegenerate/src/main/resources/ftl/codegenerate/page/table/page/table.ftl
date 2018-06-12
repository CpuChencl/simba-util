<#assign dollar = '$'>
<#assign pound = '#'>
<${pound}list list as ${firstLower}>
	<tr>
		<td><input type="checkbox" name="${firstLower}" value="${dollar}{${firstLower}.id}"></td>
		<#list filedsWithPage as field> 
		<td>${dollar}{${firstLower}.${field.key}}</td>
		</#list>
		<td>
			<button type="button" class="btn btn-default btn-sm" onclick="${className}.toUpdate(${dollar}{${firstLower}.id});"><i class="fa fa-pencil-square-o"></i>修改</button>
			<button type="button" class="btn btn-default btn-sm" onclick="${className}.delete${className}(${dollar}{${firstLower}.id});"><i class="fa fa-remove"></i>删除</button>
		</td>
	</tr>
</${pound}list>