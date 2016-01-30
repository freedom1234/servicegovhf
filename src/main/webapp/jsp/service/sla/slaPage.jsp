<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
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
            <th><nobr>服务代码</nobr></th>
            <td><input class="easyui-textbox" type="text" name="1" id="1" value="${service.serviceId }"
                       disabled="disabled">
            </td>
            <th><nobr>服务名称</nobr></th>
            <td><input class="easyui-textbox" type="text" name="2" id="2" value="${service.serviceName }"
                       disabled="disabled">
            </td>
            <th><nobr>场景号</nobr></th>
            <td><input class="easyui-textbox" type="text" name="3" id="3" value="${operation.operationId }"
                       disabled="disabled">
            </td>
            <th><nobr>场景名称</nobr></th>
            <td><input class="easyui-textbox" type="text" name="4" id="4" value="${operation.operationName }"
                       disabled="disabled">
            </td>
        </tr>
    </table>


</fieldset>
<table id="sla" style="height:470px; width:auto;" title="服务SLA">
    <thead>
    <tr>
        <th field="slaId" width="100" editor="text"
            data-options="hidden:true">ID
        </th>
        <th field="slaName" width="150" editor="{type:'validatebox',options:{required:true,validType:['unique','chineseB']}}" align="left">SLA指标</th>
        <th field="slaValue" width="150" align="left" editor="{type:'validatebox'}">取值范围</th>
        <th field="slaDesc" width="400" align="left" editor="{type:'validatebox'}">描
            述
        </th>
        <th field="slaRemark" width="150" align="left" editor="text">备
            注
        </th>
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
<script type="text/javascript" src="/assets/sla/js/slaManager.js"></script>
<script type="text/javascript" src="<%=basePath %>/assets/sla/js/slaPage.js"></script>
<script type="text/javascript" src="/plugin/validate.js"></script>
<script type="text/javascript">
	$(function () {
		$.extend($.fn.validatebox.defaults.rules, {
			unique: {
				validator: function (value, param) {
					var result;
					$.ajax({
						type: "get",
						async: false,
						url: "/sla/uniqueValid",
						dataType: "json",
						data: {"slaName": value},
						success: function (data) {
							result = data;
						}
					});
					return result;
				},
				message: '已存在相同SLA指标'
			}
		});
	});
	var serviceId = "${service.serviceId}";
	var operationId = "${operation.operationId}";

	var slaUrl = "/sla/getAll/" + serviceId + "/" + operationId;
	var toolbar = [];
	<shiro:hasPermission name="sla-add">
	toolbar.push({
		text : '新增',
		iconCls : 'icon-add',
		handler : function() {
			$('#sla').edatagrid('addRow');
		}
	})
	</shiro:hasPermission>
	<shiro:hasPermission name="sla-delete">
	toolbar.push({
		text : '删除',
		iconCls : 'icon-remove',
		handler : function() {
			var row = $('#sla').edatagrid('getSelected');
			if(row==""||row==null){
				alert("请选择一条信息！");
				return false;
			}
			var rowIndex = $('#sla').edatagrid('getRowIndex', row);
			var deleteData = $("#sla").datagrid('getChanges','deleted');
			$('#sla').edatagrid('deleteRow', rowIndex);
			slaManager.deleteByEntity(deleteData,function(result){
				if(result){
					$('#sla').datagrid('reload');
					alert("删除成功！");
				}else{alert("删除失败！");}
			});
		}
	});
	</shiro:hasPermission>
	<shiro:hasPermission name="sla-update">
	toolbar.push({
		text : ' 保存',
		iconCls : 'icon-save',
		handler : function() {
			for ( var per in editedRows) {
				$("#sla").datagrid('endEdit', editedRows[per]);
				if(!$("#sla").datagrid('validateRow',editedRows[per])){
					alert("请以正确格式输入必输项");
					return false;
				}
			}
			var editData = $("#sla").datagrid('getChanges');
			slaManager.add(editData,serviceId,operationId,function(result){
				if(result){
					$('#sla').datagrid('reload');
					alert("保存成功！");
				}else{alert("保存失败！");}
			});

			editedRows = [];

		}
	});
	</shiro:hasPermission>
	<shiro:hasPermission name="slaTemp-get">
	toolbar.push({
		text : 'SLA模版',
		iconCls : 'icon-qxfp',
		handler : function() {
			uiinit.win({
				w : 900,
				iconCls : 'icon-add',
				title : "SLA模块",
				url : "/jsp/service/sla/slaTemplateEdit.jsp?serviceId="+serviceId+"&operationId="+operationId
			})
		}
	});
	</shiro:hasPermission>

		var editedRows = [];
		$(function() {
			$('#sla').edatagrid({
				rownumbers:true,
				singleSelect:true,
				url:slaUrl,
				method:'get',
				toolbar:toolbar,
				pagination: true,
				pageSize: 15,
				pageList: [15,30,50],
				onBeginEdit : function(index,row){
					editedRows.push(index);
				}
			});

		});
	</script>
</body>
</html>
