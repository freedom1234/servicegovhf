损latemplate<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
 <head>
 <meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>列表页</title>
	<link rel="stylesheet" type="text/css"
		  href="/resources/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css"
		  href="/resources/themes/icon.css">
	<link href="/resources/css/css.css" rel="stylesheet" type="text/css">
</head>

<body>

<table id="slaTemplate" style="height:370px; width:auto;" title="模版B"
	   data-options="rownumbers:true,singleSelect:true,url:'/slaTemplate/getSLA/${param.slaTemplateId}',method:'get',toolbar:slaTemplatetoolbar,pagination:true,
				pageSize:10">
	<thead>
	<tr>
		<th field="slaId" width="100" editor="text" data-options="hidden:true"  >ID</th>
		<th field="slaName" width="100" editor="text" align="left">SLA指标</th>
		<th field="slaValue" width="150" align="left" editor="text">取值范围</th>
		<th field="slaDesc" width="150" align="left" editor="text">描 述</th>
		<th field="slaRemark" width="150" align="left" editor="text">备 注</th>
		<th field="slaTemplateId" width="100" editor="text" data-options="hidden:true">模板</th>
	</tr>
	</thead>
</table>

<div id="w" class="easyui-window" title=""
	 data-options="modal:true,closed:true,iconCls:'icon-add'"
	 style="width:500px;height:200px;padding:10px;"></div>
<script type="text/javascript" src="/resources/js/jquery.min.js"></script>
<script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/resources/js/jquery.edatagrid.js"></script>
<script type="text/javascript" src="/resources/js/ui.js"></script>
<script type="text/javascript" src="/plugin/json/json2.js"></script>
<script type="text/javascript"
		src="/assets/sla/js/slaManager.js"></script>
<script type="text/javascript">
	var slaTemplatetoolbar = [
		{
			text : '新增',
			iconCls : 'icon-add',
			handler : function() {
				$('#slaTemplate').edatagrid('addRow');
			}
		},
		{
			text : '删除',
			iconCls : 'icon-remove',
			handler : function() {
				var row = $('#slaTemplate').edatagrid('getSelected');
				var rowIndex = $('#slaTemplate').edatagrid('getRowIndex', row);
				$('#slaTemplate').edatagrid('deleteRow', rowIndex);
			}
		},
		{
			text : ' 保存',
			iconCls : 'icon-save',
			handler : function() {
				var deletedDatas = $('#slaTemplate').edatagrid('getChanges',
						'deleted');
				var addDatas = $('#slaTemplate').edatagrid('getChanges',
						'inserted');
				var updatedDatas = $('#slaTemplate').edatagrid('getChanges',
						'updated');
				for ( var i = 0; i < addDatas.length; i++) {
					var addData = addDatas[i];
					var data = {};
					if (addData.slaName) {
						data.slaName = addData.slaName;
						data.slaValue = addData.slaValue;
						data.slaDesc = addData.slaDesc;
						data.slaRemark = addData.slaRemark;
						data.slaTemplateId="${param.slaTemplateId}";
						slaManager.add(data, function(result) {
							if (result) {
								alert(result);
								alert("保存成功");
							} else {
								alert("保存失败");
							}
						});
					}
				}
				for ( var j = 0; j < deletedDatas.length; j++) {
					var deleteData = deletedDatas[j];
					slaManager.deleteById(deleteData.slaId,
							function(result) {
								if (result) {
									alert("删除成功");
								} else {
									alert("删除失败");
								}
							});
				}
				for ( var k = 0; k < updatedDatas.length; k++) {
					var updatedData = updatedDatas[k];
					var data = {};
					data.slaName = updatedData.slaName;
						data.slaValue = updatedData.slaValue;
						data.slaDesc = updatedData.slaDesc;
						data.slaRemark = updatedData.slaRemark;
						data.operationId = "1";
						data.serviceId = "1";
						data.slaTemplateId="${param.slaTemplateId}";
						data.slaId=updatedData.slaId;
				slaManager.modify(data, function(result) {
						if (result) {
							alert("修改成功");
						} else {
							alert("修改失败");
						}
					});
				}
			}
	}
	 ];
	$(function() {
		$('#slaTemplate').edatagrid({
		});
		
	});

</script>

</body>
</html>
