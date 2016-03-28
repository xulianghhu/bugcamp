<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="renderer" content="webkit">
    <title>Bug List</title>
    <link rel="stylesheet" type="text/css" href="<@spring.url "/resources/css/jquery.dataTables.min.css"/>">
</head>
<body>

<table id="bug_table" class="display" cellspacing="0" width="100%">
	<thead>
		<tr>
			<th>rptno</th>
			<th>rptdate</th>
			<th>programmer</th>
			<th>cs_priority</th>
			<th>status</th>
			<th>confirm_flag</th>
			<th>product_id</th>
			<th>category</th>
			<th>utility_version</th>
			<th>subject</th>
		</tr>
	</thead>
	<tbody> 
		<#if bugs??>
			<#list bugs as bug>
				<tr>
					<td>${bug.rptno!}</td>
					<td>${bug.rptdate?datetime}</td>
					<td>${bug.programmer!}</td>
					<td>${bug.cs_priority!}</td>
					<td>${bug.status!}</td>
					<td>${bug.confirm_flag!}</td>
					<td>${bug.product_id!}</td>
					<td>${bug.category!}</td>
					<td>${bug.utility_version!}</td>
					<td>${bug.subject!}</td>
				</tr>
			</#list>
		</#if>
	</tbody>
</table>
<script src="<@spring.url "/resources/javascript/jquery-1.11.3.min.js"/>"></script>
<script src="<@spring.url "/resources/javascript/jquery.dataTables.min.js"/>"></script>
<script>
	$(document).ready(function() {
    	$('#bug_table').DataTable();
	});
</script>
</body>
</html>