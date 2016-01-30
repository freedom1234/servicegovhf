<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
  String path = request.getContextPath();
  String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
  <base href="<%=basePath%>">
  <title>My JSP 'MyJsp.jsp' starting page</title>
  <meta http-equiv="pragma" content="no-cache">
  <meta http-equiv="cache-control" content="no-cache">
  <meta http-equiv="expires" content="0">
  <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
  <meta http-equiv="description" content="This is my page">

</head>

<body>
<div id="userId" style="display: none"><shiro:principal/></div>
<script type="text/javascript" src="/assets/task/taskManager.js"></script>

<form class="formui" id="metadataForm" action="/metadata/add" method="post">
  <table border="0" cellspacing="0" cellpadding="0">
    <tr>
      <th>下节点处理人</th>
       <td><select class="easyui-combobox" style="width:173px;" name="nextUser" id="nextUser" ></td>
        </tr>

    <tr>
      <td>&nbsp;</td>
      <td class="win-bbar">
        <a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onClick="$('#w').window('close')">取消</a>
        <a href="#" id="completeTaskBtn" class="easyui-linkbutton" iconCls="icon-save">处理</a>
      </td>
    </tr>
  </table>
</form>
	<script type="text/javascript" src="/assets/task/completeTask.js"></script>
</body>
</html>