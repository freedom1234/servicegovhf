<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
  String path = request.getContextPath();
  String basePath = request.getScheme() + "://"
          + request.getServerName() + ":" + request.getServerPort()
          + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
  <title>列表页</title>
  <link rel="stylesheet" type="text/css"
        href="/resources/themes/default/easyui.css">
  <link rel="stylesheet" type="text/css" href="/resources/themes/icon.css">
  <link href="/resources/css/ui.css" rel="stylesheet" type="text/css">
  <script type="text/javascript" src="/resources/js/jquery.min.js"></script>
  <script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
  <script type="text/javascript" src="/resources/js/easyui-lang-zh_CN.js"></script>
</head>

<body>
<form id="searchForm">
  <fieldset>
    <legend>条件搜索</legend>
    <table border="0" cellspacing="0" cellpadding="0">
      <tr>
        <th><nobr>操作人</nobr></th>
        <td><input id="optUser" class="easyui-textbox"/>
        </td>
        <th><nobr> 起始日期</nobr></th>
        <td><input class="easyui-datebox" style="width:80px" type="text" name="startDate" id="startDate"></td>
        <th><nobr> 结束日期</nobr></th>
        <td><input class="easyui-datebox" style="width:80px" type="text" name="endDate" id="endDate"></td>
        <th style="width:200px">
          <nobr>
          <a href="#" id="saveTagBtn" onclick="query()" class="easyui-linkbutton" iconCls="icon-search" style="margin-left:1em" >查询</a>
          <a href="#" id="clean" onclick="$('#searchForm').form('clear');" class="easyui-linkbutton" iconCls="icon-clear" style="margin-left:1em" >清空</a>
          </nobr>
        </th>
        <td></td>

      </tr>
    </table>


  </fieldset>
</form>
<div style="width:100%">
  <table id="resultList" class="easyui-datagrid"
         data-options="
			rownumbers:true,
			singleSelect:false,
			fitColumns:false,
			url:'/systemLog/query',
			method:'get',
			pagination:true,
				pageSize:50"
         style="height:auto; width:100%;">
    <thead>
    <tr>
      <th data-options="field:'',checkbox:true,width:50"></th>
      <th data-options="field:'optUser',width:100">用户账号</th>
      <th data-options="field:'optUser',width:100">用户名称</th>
      <th data-options="field:'chineseName',width:150">操作对象</th>
      <th data-options="field:'optType',width:80">操作类型</th>
      <th data-options="field:'params',width:250">操作内容</th>
      <th data-options="field:'optResult',width:80">结果</th>
      <th data-options="field:'optDate',width:150">操作时间</th>
      <!--
      <th data-options="field:'className',width:250">类名</th>
      <th data-options="field:'methodName',width:80">方法</th>

      -->

    </tr>
    </thead>
  </table>
</div>
<div id="w" class="easyui-window" title=""
     data-options="modal:true,closed:true,iconCls:'icon-add'"
     style="width:500px;height:200px;padding:10px;"></div>
<div id="opDialog" class="easyui-dialog"
     style="width:400px;height:280px;padding:10px 20px" closed="true"
     resizable="true"></div>
</body>
<script type="text/javascript">
  function query(){
    var params = {
      "optUser":$("#optUser").textbox("getValue"),
      "startDate":$("#startDate").datebox("getValue"),
      "endDate":$("#endDate").datebox("getValue")
    }
    $("#resultList").datagrid('options').queryParams = params;
    var p = $("#resultList").datagrid('getPager');
    var total =  $(p).pagination("options").total;
    if(total < 100){
      total = 100;
    }
    $(p).pagination({
      pageList: [10,20,50,total]
    });
    $("#resultList").datagrid('reload');
  }
</script>

</body>
</html>

