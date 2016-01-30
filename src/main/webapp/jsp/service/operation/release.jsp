<%@ page language="java" import="java.util.*, java.net.URLDecoder" pageEncoding="utf-8"%>
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
    <fieldset>
    	<table width="100%">
    		<tr>
    			<td width="100%">
    				场景名称：<%=URLDecoder.decode(request.getParameter("operationName"), "utf-8") %><br/>
    				版本号：${param.versionCode }
    			</td>
    		</tr>
    		<tr>
    			<td width="100%">
    				发布说明：<input id="versionDesc" type="text" class="easyui-textbox" style="width:200px, height:200" >
    			</td>
    		</tr>
    		<tr>
    			<td width="100%">
    				<div class="win-bbar" style="text-align:center"><a href="#" class="easyui-linkbutton"  iconCls="icon-cancel" onClick="$('#opDialog').dialog('close');">取消</a><a href="#" onclick="releaseOp($('#versionDesc').textbox('getValue'),'${param.operationId}');" class="easyui-linkbutton"  iconCls="icon-save">保存</a></div>
    			</td>
    		</tr>
    	</table>
    </fieldset>
  </body>
</html>
