<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
    <meta charset="UTF-8">
    <title>服务治理平台</title>
    <link rel="stylesheet" type="text/css"
          href="resources/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css"
          href="resources/themes/icon.css">
    <link href="resources/css/css.css" rel="stylesheet" type="text/css">

</head>
<body class="easyui-layout">
<div data-options="region:'north',border:false" id="header">
    <div id="logo"></div>
    <ul id="nav">
        <li>
            <a href="javascript:;" mid="1">平台管理1</a>
        </li>
        <li>
            <a href="javascript:;" mid="2">公共信息管理</a>
        </li>
        <li>
            <a href="javascript:;" mid="3">系统管理</a>
        </li>
        <li>
            <a href="javascript:;" mid="6">接口管理</a>
        </li>
        <li>
            <a href="javascript:;" mid="4">服务管理</a>
        </li>
        <li>
            <a href="javascript:;" mid="5">交易链路管理</a>
        </li>
        <li>
            <a href="javascript:;" mid="11">统计报表</a>
        </li>
    </ul>
    <div id="header_toolsbar">
        <a href="javascript:;" class="login_user"><shiro:principal></shiro:principal></a>
        <a href="javascript:;">帮助</a>
    </div>
</div>
<div data-options="region:'west',collapsible:true,title:'菜单'"
     class="west-menu" id="west-menu">

</div>
<div data-options="region:'center',collapsible:true,border:false,tabHeight:39"
     class="easyui-tabs " id="mainContentTabs">
    <div title="首页"></div>
</div>
<script type="text/javascript" src="resources/js/jquery.min.js"></script>
<script type="text/javascript" src="resources/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="resources/js/app.js"></script>
<script type="text/javascript" src="resources/js/ui.js"></script>
<script type="text/javascript" src="js/sysadmin/sysManager.js"></script>
<script type="text/javascript" src="js/sysadmin/interfaceManager.js"></script>
<script type="text/javascript" src="<%=basePath%>/assets/service/js/serviceManager.js"></script>
<script type="text/javascript" src="<%=basePath%>/assets/service/js/serviceUIHelper.js"></script>
<script type="text/javascript" src="<%=basePath%>/assets/service/js/serviceTree.js"></script>

<div id="mm-mxsysadmintree" class="easyui-menu" style="width: 120px;">
    <div onclick="sysManager.append()" data-options="iconCls:'icon-add'">
        新增
    </div>
    <div onclick="sysManager.edit()" data-options="iconCls:'icon-edit'">
        编辑
    </div>
    <div onclick="sysManager.remove()" data-options="iconCls:'icon-remove'">
        删除
    </div>

</div>
<!--服务管理页面，服务树的右键菜单-->
<div id="mm-mxservicetree" class="easyui-menu" style="width: 120px;">
    <div id="serviceTreeAddBtn" data-options="iconCls:'icon-add'">新增</div>
    <div id="serviceTreeEditBtn" data-options="iconCls:'icon-edit'">编辑</div>
    <div id="serviceTreeDeleteBtn" data-options="iconCls:'icon-remove'">删除</div>
</div>

<div id="mm-mslinktree" class="easyui-menu" style="width: 120px;">
    <div onclick="appendSysapi()" data-options="iconCls:'icon-add'">
        新增
    </div>
    <div onclick="editSysapi()" data-options="iconCls:'icon-edit'">
        编辑
    </div>
    <div onclick="removeSysapi()" data-options="iconCls:'icon-remove'">
        删除
    </div>

</div>

<div id="mm-mxsystemtree1" class="easyui-menu" style="width: 120px;">
    <div onclick="interfaceManager.append()" data-options="iconCls:'icon-add'">
        新增接口
    </div>
    <div onclick="sysManager.editSystemPage()" data-options="iconCls:'icon-edit'">
        编辑系统
    </div>
    <div onclick="sysManager.deleteSystem()" data-options="iconCls:'icon-remove'">
        删除系统
    </div>
</div>

<div id="mm-mxsystemtree" class="easyui-menu" style="width: 120px;">
    <div onclick="sysManager.addSystemPage()" data-options="iconCls:'icon-add'">
        新增系统
    </div>
  <div onclick="sysManager.querySystemPage()" data-options="iconCls:'icon-add'">
        查看系统
    </div>
</div>

<div id="mm-mxinterfacetree1" class="easyui-menu" style="width: 120px;">
    <div onclick="interfaceManager.append()" data-options="iconCls:'icon-add'">
        新增接口
    </div>
    <div onclick="interfaceManager.edit()" data-options="iconCls:'icon-edit'">
        编辑接口
    </div>
    <div onclick="interfaceManager.remove()" data-options="iconCls:'icon-remove'">
        删除接口
    </div>
</div>
</body>
</html>