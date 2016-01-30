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
     <div class="win-bbar" style="text-align:center"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
                                                           onClick="$('#dlg').dialog('close');">取消</a><a href="javascript:void(0)"
                                                                                      onclick="selectService()"
                                                                                      class="easyui-linkbutton"
                                                                                      iconCls="icon-save">确定</a></div>
  	<ul id="serviceTree" class="easyui-tree mxservicetree" data-options="url:'/service/getTree',method:'get',animate:true"></ul>
  </body>
</html>
