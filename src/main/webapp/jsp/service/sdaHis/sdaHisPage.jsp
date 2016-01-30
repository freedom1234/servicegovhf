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
    
    <title>My JSP 'sdaHisPage.jsp' starting page</title>
  </head>
  
  <body>
   <table title="定义接口" class="easyui-treegrid" id="sdaHisTree" style=" width:100%;"
			data-options="
				iconCls: 'icon-ok',
				rownumbers: true,
				animate: true,
				fitColumns: true,
				url: '/sdaHis/sdaHisTree?autoId=<%=request.getParameter("autoId") %>',
				method: 'get',
				idField: 'id',
				treeField: 'text'
				"
                >
		<thead>
			<tr>
				<th data-options="field:'text',width:180,editor:'text'">字段名</th>
				<th data-options="field:'append1',width:60,align:'right',editor:'text'">中文名称</th>
				<th data-options="field:'append2',width:80,editor:'text'">功能描述</th>
				<th data-options="field:'append3',width:80,editor:'text'">备注</th>
			</tr>
		</thead>
	</table>
  </body>
</html>
