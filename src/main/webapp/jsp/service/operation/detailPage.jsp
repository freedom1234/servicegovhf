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
<table border="0" cellspacing="0" cellpadding="0" width="100%">

  <tr>
     <th width="15%">服务代码</th>
    <td width="35%">${service.serviceId }</td>
    <th width="15%">服务名称</th>
    <td width="35%">${service.serviceName }</td>
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
    <td>${operation.operationId }</td>
     <th>场景名称</th>
   		 <td>${operation.operationName }</td>

     </tr>
    <tr>
        <th>版本号</th>
        <td>${operation.version.code }</td>
        <th>版本描述</th>
        <td>${operation.version.remark} </td>
    </tr>
   <tr>
     <th>最后更新时间</th>
   		 <td>${operation.optDate }</td>
   	 <th>最后更新用户</th>
    <td>${operation.optUser }</td>
   </tr>
    <tr>
        <th>功能描述</th>
        <td>${operation.operationDesc }</td>
        <th>备注</th>
        <td>${operation.operationRemark }</td>
    </tr>
</table>


</fieldset>
	<fieldset>
     <legend>SDA</legend>
         <table class="easyui-treegrid" style=" width:100%;"
                data-options="
                    iconCls: 'icon-ok',
                    rownumbers: true,
                    animate: true,
                    collapsible: true,
                    collapsed:false,
                    fitColumns: false,
                    url: '/sda/sdaTree?serviceId=${service.serviceId }&operationId=${operation.operationId }',
                    method: 'get',
                    idField: 'id',
                    treeField: 'text'
                    "
                    >
            <thead>
                <tr>
                    <th data-options="field:'text',width:'25%',editor:'text'">字段名</th>
                    <th data-options="field:'append1',width:'25%',align:'right',editor:'text'">中文名称</th>
                    <th data-options="field:'append2',width:'25%',editor:'text'">类型</th>
                    <th data-options="field:'append6',width:'25%',editor:'text'">备注</th>
                </tr>
            </thead>
        </table>
    </fieldset>
    <%--
    <fieldset>
     <legend>SLA列表</legend>
         <table id="sla" style="width:auto;" title="SLA" class="easyui-datagrid"
            data-options="
                iconCls: 'icon-ok',
                rownumbers:true,
                collapsible: true,
                collapsed:true,
                singleSelect:true,
                url:'/sla/getAll/${service.serviceId }/${operation.operationId }',
                method:'get',
                pagination:true,
                pageSize:10">
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
    </fieldset>
    <fieldset>
     <legend>OLA列表</legend>
     <table style="width:auto" title="OLA" class="easyui-datagrid"
            data-options="
                    iconCls: 'icon-ok',
                    rownumbers:true,
                    collapsible: true,
                    collapsed:true,
                    singleSelect:true,
                    url:'/ola/getAll/${service.serviceId }/${operation.operationId }',
                    method:'get',
                    pagination:true">
            <thead>
                <tr>
                    <th field="olaId" width="100" editor="text"
                        data-options="hidden:true">ID</th>
                    <th field="olaName" width="100" editor="text" align="center">OLA指标</th>
                    <th field="olaValue" width="150" align="center" editor="text">取值范围</th>
                    <th field="olaDesc" width="150" align="center" editor="text">描
                        述</th>
                    <th field="olaRemark" width="150" align="center" editor="text">备
                        注</th>
                </tr>
            </thead>
        </table>
    </fieldset>
    <fieldset>
     <legend>接口列表</legend>
     <table style="width:auto" title="接口" class="easyui-datagrid"
            data-options="
                    iconCls: 'icon-ok',
                    rownumbers:true,
                    collapsible: true,
                    collapsed:true,
                    singleSelect:true,
                    url:'/serviceLink/getInterfaceByOSS?operationId=${operation.operationId }&serviceId=${service.serviceId }',
                    method:'get',
                    pagination:true">
            <thead>
                <tr>
                    <th data-options="field:'systemId', width:50">系统id</th>
                            <th data-options="field:'systemChineseName', width:150">系统名称</th>
                            <th data-options="field:'isStandard', width:50"
                                formatter='ff.isStandardText'>标准</th>
                            <th data-options="field:'interfaceId', width:50">接口id</th>
                            <th data-options="field:'interfaceName', width:150">接口名称</th>
                            <th data-options="field:'type', width:50"
                                formatter='ff.typeText'>类型</th>
                            <th data-options="field:'desc', width:100">描述</th>
                            <th data-options="field:'remark', width:100">备注</th>
                </tr>
            </thead>
        </table>
    </fieldset>--%>

    </div>
  
  </body>
</html>
