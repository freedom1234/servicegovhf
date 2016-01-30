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
   <table id="olaHisList" class="easyui-datagrid"
		data-options="
				rownumbers:true,
				singleSelect:true,
				url:'/slaHis/getByOperation?autoId=<%=request.getParameter("autoId") %>',
				method:'get',
				pagination:true,
				pageSize:10"
		style=" width:auto;">
		<thead>
			<tr>
				<th field="slaId" width="100" editor="text"
					data-options="hidden:true">ID</th>
				<th field="slaName" width="100" editor="text" align="center">SLA指标</th>
				<th field="slaValue" width="150" align="center" editor="text">取值范围</th>
				<th field="slaDesc" width="150" align="center" editor="text">描
					述</th>
				<th field="slaRemark" width="150" align="center" editor="text">备
					注</th>
			</tr>
		</thead>
	</table>
	
  </body>
</html>
