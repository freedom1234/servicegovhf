<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
  String path = request.getContextPath();
  String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <title>服务治理平台 | 403 没有操作权限</title>

  </head>
  <body style="background-color : #FFFFFF">
  <div style="width:100%; height:100%; font-size: 3em; font-weight: bold; font-family: '楷体'">
  对不起，您没有权限进行此操作！
  </div>
  </body>
</html>
