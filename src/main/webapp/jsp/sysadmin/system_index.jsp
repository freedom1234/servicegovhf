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

	<title>系统管理</title>

	<link rel="stylesheet" type="text/css" href="/resources/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="/resources/themes/icon.css">
	<link href="/resources/css/ui.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="/resources/js/jquery.min.js"></script>
	<script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="/resources/js/ui.js"></script>
</head>

<body style="margin:0px;">

<div class="easyui-tabs" id="sysContentTabs" name="sysContentTabs" style="width:100%">
	<div title="系统管理" style="padding:0px;">
	</div>
</div>

<script type="text/javascript">
	$('#sysContentTabs').tabs({
		border: false,
		border: false,
		width: "auto",
		height: $("body").height()
	});
	var tabUrl = "/jsp/sysadmin/system_manager.jsp";
	var tabItem = $('#sysContentTabs').tabs('getTab','系统管理');
	$('#sysContentTabs').tabs('update', {
		tab: tabItem,
		options: {
			content: ' <iframe  scrolling="auto" frameborder="0"  src=" ' + tabUrl + '?_t=' + new Date().getTime() + '"  style="width:100%;height:99%;"></iframe>',
			fit:true
		}
	});
</script>
</body>
</html>