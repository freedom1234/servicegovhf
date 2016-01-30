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

    <title>sda详细信息</title>

	<link rel="stylesheet" type="text/css" href="/resources/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="/resources/themes/icon.css">
<link href="/resources/css/ui.css" rel="stylesheet" type="text/css">
	 <script type="text/javascript" src="/resources/js/jquery.min.js"></script>
        <script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="/resources/js/treegrid-dnd.js"></script>

<script type="text/javascript" src="/resources/js/ui.js"></script>
<script type="text/javascript" src="/jsp/service/operation/operation.js"></script>

</head>
<body >
<div>
<fieldset>
 <legend>基本信息</legend>
<table border="0" cellspacing="0" cellpadding="0">

  <tr>
     <th>服务代码</th>
    <td>${service.serviceId }</td>
    <th>服务名称</th>
    <td>${service.serviceName }</td>
      <th></th>
    <td> </td>
   </tr>
  <%--<tr>
     <th>服务头代码</th>
   		 <td>${serviceHead.headId }</td>
   	 <th>服务头名称</th>
    <td>${serviceHead.headName }</td>
     <th></th>
    <td> </td>
   </tr>--%>
  <tr>
     <th>场景号</th>
    <td>${operationHis.operationId }</td>
     <th>场景名称</th>
   		 <td>${operationHis.operationName }</td>
   	 <th>版本号</th>
    <td>${operationHis.versionHis.code }</td>
     </tr>
  <tr>
     <th>功能描述</th>
   		 <td>${operationHis.operationDesc }</td>
   	 <th>备注</th>
    <td>${operationHis.operationRemark }</td>
     <th></th>
    <td> </td>
   </tr>
   <tr>
     <th>最后更新时间</th>
   		 <td>${operationHis.optDate }</td>
   	 <th>最后更新用户</th>
    <td>${operationHis.optUser }</td>
     <th></th>
    <td> </td>
   </tr>
</table>
</fieldset>
<fieldset>
    <legend>SDA</legend>
    <table title="定义接口" class="easyui-treegrid" id="sdaHisTree" style=" width:100%;"
           data-options="
				iconCls: 'icon-ok',
				rownumbers: true,
				animate: true,
				fitColumns: true,
				url: '/sdaHis/sdaHisTree?autoId=${operationHis.autoId }',
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
</fieldset>
    </div>

  </body>
</html>
