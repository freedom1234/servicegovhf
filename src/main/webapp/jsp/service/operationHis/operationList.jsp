<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  <meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
    <base href="<%=basePath%>">
  </head>
  
  <body>
   <table id="operationList" class="easyui-datagrid"
		data-options="
				rownumbers:true,
				singleSelect:true,
				url:'/operation/getOperationByServiceId/<%=request.getParameter("serviceId") %>',
				method:'get',
				toolbar:'#tb',
				pagination:true,
				pageSize:10"
		style="height:370px; width:auto;">
		<thead>
			<tr>
				<th data-options="field:'operationId',checkbox:true"></th>
				<th data-options="field:'operationName'">场景名称</th>
				<th data-options="field:'operationDesc'">功能描述</th>
				<th data-options="field:'version'">版本号</th>
				<th data-options="field:'optDate'">更新时间</th>
				<th data-options="field:'optUser'">更新用户</th>
			</tr>
		</thead>
	</table>
	
	<div id="tb" style="padding:5px;height:auto">
    	<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><a href="javascript:void(0)" onclick="$('#dlg').dialog('close');" class="easyui-linkbutton" iconCls="icon-cancel" plain="true">取消</a>&nbsp;&nbsp;
	    <a href="javascript:void(0)" onclick="selectOperation();" class="easyui-linkbutton" iconCls="icon-ok" plain="true">确定</a>
    </td>
    </tr>
    </table>
    </div>
	
  </body>
</html>
