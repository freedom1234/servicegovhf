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

  <title>sda版本对比</title>

  <link rel="stylesheet" type="text/css" href="/resources/themes/default/easyui.css">
  <link rel="stylesheet" type="text/css" href="/resources/themes/icon.css">
  <link href="/resources/css/ui.css" rel="stylesheet" type="text/css">
  <script type="text/javascript" src="/resources/js/jquery.min.js"></script>
  <script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
  <script type="text/javascript" src="/resources/js/ui.js"></script>
  <script type="text/javascript">
    $(document).ready(function() {
      $.ajax({//获取服务场景信息，当前sda和最新一个历史版本的sdaHis
        type: "post",
        async: false,
        url: urlPath,
        dataType: "json",
        data: params,
        success: function (data) {
            $("#serviceId").textbox("setValue", data.service.serviceId);
            $("#serviceName").textbox("setValue", data.service.serviceName);
            $("#operationId").textbox("setValue", data.operation.operationId);
            $("#operationName").textbox("setValue", data.operation.operationName);
            compare(data.sda1, data.sda2);
        }

      });
    });
    function compare(sda1, sda2){//对比两棵树
      for(var i = 0; i < sda1.length; i++){
        var sdaNode1 = sd1[i];
      }
      $("#sda1").tree("loadData", sda1);

    }
    function compareNode(node1, node2){
      if(node1 != null && node2 != null){
        if(node1.text == node2.text){

        }
      }
    }
  </script>
<head>
</head>
<body>
<fieldset>
  <legend>版本选择</legend>
  <table border="0" cellspacing="0" cellpadding="0">
    <tr>
      <th>服务代码</th>
      <td><input class="easyui-textbox" type="text" id="serviceId" readonly="true"></td>
      <th>服务名称</th>
      <td><input class="easyui-textbox" type="text" id="serviceName" readonly="true"></td>
      <th>场景号</th>
      <td> <input class="easyui-textbox"  type="text" id="operationId" readonly="true"></td>
      <th>场景名称</th>
      <td> <input class="easyui-textbox"  type="text" id="operationName" readonly="true"></td>
    <tr>
      <th>版本1</th>
      <td>
        <input class="easyui-textbox" id="version1" value="${service.serviceName }"/>&nbsp;&nbsp;
        <a iconcls="icon-search" class="easyui-linkbutton l-btn l-btn-small" onclick="choseService('dlg')"
           href="javascript:void(0)">选择服务</a>
      </td>
    <th>版本2</th>
    <td>
      <input class="easyui-textbox" id="version2" value="${service.serviceName }"/>&nbsp;&nbsp;
      <a iconcls="icon-search" class="easyui-linkbutton l-btn l-btn-small" onclick="choseService('dlg')"
         href="javascript:void(0)">选择服务</a>
    </td>
    </tr>
  </table>
</fieldset>
  <div>
    <table title="sda1" class="easyui-treegrid" id="sda1" style=" width:auto;"
           data-options="
				rownumbers: true,
				animate: true,
				collapsible: true,
				fitColumns: true,
				method: 'get',
				idField: 'id',
				treeField: 'text'
				"
            >
      <thead>
      <tr>
        <th data-options="field:'text',width:100" editor="{type:'text'}">字段名</th>
        <th data-options="field:'append1',width:60">字段别名</th>
        <th data-options="field:'append2',width:50" >类型/长度</th>
        <th  data-options="field:'append4',width:50">元数据</th>
        <th data-options="field:'append5',width:50">是否必输</th>
        <th data-options="field:'append7',width:50">约束条件</th>
        <th data-options="field:'append6',width:80">备注</th>
      </tr>
      </thead>
    </table>
  </div>
  <div>
    <table title="sda2" class="easyui-treegrid" id="sda2" style=" width:auto;"
           data-options="
				rownumbers: true,
				animate: true,
				collapsible: true,
				fitColumns: true,
				method: 'get',
				idField: 'id',
				treeField: 'text'
				"
            >
      <thead>
      <tr>
        <th data-options="field:'text',width:100" editor="{type:'text'}">字段名</th>
        <th data-options="field:'append1',width:60">字段别名</th>
        <th data-options="field:'append2',width:50" >类型/长度</th>
        <th  data-options="field:'append4',width:50">元数据</th>
        <th data-options="field:'append5',width:50">是否必输</th>
        <th data-options="field:'append7',width:50">约束条件</th>
        <th data-options="field:'append6',width:80">备注</th>
      </tr>
      </thead>
    </table>

  </div>
</body>
</html>
