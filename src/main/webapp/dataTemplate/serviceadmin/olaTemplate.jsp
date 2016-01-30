<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
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

<table id="olaTemplate" style="height:370px; width:auto;" title="模版B"
	   data-options="rownumbers:true,singleSelect:true,url:'/olaTemplate/getOLA/${param.olaTemplateId}',method:'get',toolbar:olaTemplatetoolbar,pagination:true,
				pageSize:10">
	<thead>
	<tr>
		<th field="olaId" width="100" editor="text" data-options="hidden:true"  >ID</th>
		<th field="olaName" width="100" editor="text" align="center">SLA指标</th>
		<th field="olaValue" width="150" align="center" editor="text">取值范围</th>
		<th field="olaDesc" width="150" align="center" editor="text">描 述</th>
		<th field="olaRemark" width="150" align="center" editor="text">备 注</th>
		<th field="olaTemplateId" width="100" editor="text" data-options="hidden:true"  >模板</th>
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
		src="/assets/ola/js/olaManager.js"></script>
<script type="text/javascript">
	var olaTemplatetoolbar = [
		{
			text : '新增',
			iconCls : 'icon-add',
			handler : function() {
				$('#olaTemplate').edatagrid('addRow');
			}
		},
		{
			text : '删除',
			iconCls : 'icon-remove',
			handler : function() {
				var row = $('#olaTemplate').edatagrid('getSelected');
				var rowIndex = $('#olaTemplate').edatagrid('getRowIndex', row);
				$('#olaTemplate').edatagrid('deleteRow', rowIndex);
			}
		},
		{
			text : ' 保存',
			iconCls : 'icon-save',
			handler : function() {
				var deletedDatas = $('#olaTemplate').edatagrid('getChanges',
						'deleted');
				var addDatas = $('#olaTemplate').edatagrid('getChanges',
						'inserted');
				var updatedDatas = $('#olaTemplate').edatagrid('getChanges',
						'updated');
				for ( var i = 0; i < addDatas.length; i++) {
					var addData = addDatas[i];
					var data = {};
					if (addData.olaName) {
						data.olaName = addData.olaName;
						data.olaValue = addData.olaValue;
						data.olaDesc = addData.olaDesc;
						data.olaRemark = addData.olaRemark;
						data.olaTemplateId="${param.olaTemplateId}";
						data.operationId = "1";
						data.serviceId = "1";
						olaManager.add(data, function(result) {
							if (result) {
								alert("保存成功");
							} else {
								alert("保存失败");
							}
						});
					}
				}
				for ( var j = 0; j < deletedDatas.length; j++) {
					var deleteData = deletedDatas[j];
					olaManager.deleteById(deleteData.olaId,
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
					data.olaName = updatedData.olaName;
						data.olaValue = updatedData.olaValue;
						data.olaDesc = updatedData.olaDesc;
						data.olaRemark = updatedData.olaRemark;
						data.operationId = "1";
						data.serviceId = "1";
						data.olaTemplateId="${param.olaTemplateId}";
						data.olaId=updatedData.olaId;
				olaManager.modify(data, function(result) {
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

		$('#olaTemplate').edatagrid({
			autoSave : false,
			saveUrl : '/',
			updateUrl : '/',
			destroyUrl : '/'
		});
		
	});

</script>

</body>
</html>
