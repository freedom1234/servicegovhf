<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
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
	<fieldset>
		<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<th>服务代码</th>
				<td><input class="easyui-textbox" type="text" name="1" id="1" value="${service.serviceId }"
					disabled="disabled">
				</td>
				<th>服务名称</th>
				<td><input class="easyui-textbox" type="text" name="2" id="2" value="${service.serviceName }"
					disabled="disabled">
				</td>
				<th>场景号</th>
				<td><input class="easyui-textbox" type="text" name="3" id="3" value="${operation.operationId }"
					disabled="disabled">
				</td>
				<th>场景名称</th>
				<td><input class="easyui-textbox" type="text" name="4" id="4" value="${operation.operationName }"
					disabled="disabled">
				</td>
			</tr>
		</table>


	</fieldset>
	<table id="ola" style="height:370px; width:auto;" title="服务OLA">
		<thead>
			<tr>
				<th field="olaId" width="100" editor="text"
					data-options="hidden:true">ID</th>
				<th field="olaName" width="100" editor="text" align="center">OLA指标</th>
				<th field="olaValue" width="150" align="center" editor="text">取值范围</th>
				<th field="olaDesc" width="150" align="center" editor="text">描
					述</th>
				<th field="olaRemark" width="150" align="center" editor="text">备
					注</th>
				
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
	<script type="text/javascript" src="/assets/ola/js/olaManager.js"></script>
	<script type="text/javascript">
		var serviceId = "${service.serviceId}";
		var operationId = "${operation.operationId}";

		var slaUrl = "/ola/getAll/" + serviceId + "/" + operationId;
		var toolbar = [ {
			text : '新增',
			iconCls : 'icon-add',
			handler : function() {
				$('#ola').edatagrid('addRow');
			}
		}, {
			text : '删除',
			iconCls : 'icon-remove',
			handler : function() {
				var row = $('#ola').edatagrid('getSelected');
				if(row==""||row==null){
				alert("请选择一条信息！");
				return false;
				}
				var rowIndex = $('#ola').edatagrid('getRowIndex', row);
				var deleteData = $("#ola").datagrid('getChanges','deleted');	
				$('#ola').edatagrid('deleteRow', rowIndex);
					olaManager.deleteByEntity(deleteData,function(result){
						if(result){
							$('#ola').datagrid('reload');
							alert("删除成功！");
						}else{alert("删除失败！");}
					});
			}
		}, {
			text : ' 保存',
			iconCls : 'icon-save',
			handler : function() {
			for ( var per in editedRows) {
				$("#ola").datagrid('endEdit', editedRows[per]);
			}
			var editData = $("#ola").datagrid('getChanges');
				olaManager.add(editData,serviceId,operationId,function(result){
					if(result){
						$('#ola').datagrid('reload');
					alert("保存成功！");
					}else{alert("保存失败！");}
				});
				editedRows = [];
			}
		}, {
			text : 'OLA模板',
			iconCls : 'icon-qxfp',
			handler : function() {
				uiinit.win({
					w : 900,
					iconCls : 'icon-add',
					title : "OLA模块",
					url : "/jsp/service/ola/olaTemplateEdit.jsp?serviceId="+serviceId+"&operationId="+operationId
				})
			}
		} ];
		
		var editedRows = [];
		$(function() {
			$('#ola').edatagrid({
				rownumbers:true,
				singleSelect:true,
				url:slaUrl,
				method:'get',
				toolbar:toolbar,
				onBeginEdit : function(index,row){
					editedRows.push(index);
				},
				onLoadError: function (responce) {
					var resText = responce.responseText;
					if(resText.toString().indexOf("没有操作权限") > 0){
//                    alert("没有权限！");
						window.location.href = "/jsp/403.jsp";
					}
				}
			});

		});
	</script>

</body>
</html>
