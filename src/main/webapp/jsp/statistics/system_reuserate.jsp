<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
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
</head>

<body>
<form id="searchForm">
  <fieldset>
    <legend>条件搜索</legend>
    <table border="0" cellspacing="0" cellpadding="0">
      <tr>
        <th>系统编号</th>
        <td><input id="systemId" name="systemId" class="easyui-textbox" style="width:100px"
                   data-options="
                   onChange:function(newValue, oldValue){
                        this.value=newValue;
							var values = $('#systemName').combobox('getData');
							 $.each(values, function (index, item) {
							   if($.trim(item.id) == $.trim(newValue) || $.trim(item.text) == $.trim(newValue)){
							        $('#systemName').textbox('setText', item.chineseName);
							        }
							 });
                   }
                   "
                   type="text" >
        </td>
        <th>系统名称</th>
        <td><input name="systemName" id="systemName"  class="easyui-combobox" style="width:150px"
                   data-options="
                 method:'get',
                 url:'/system/getSystemAll',
                 textField:'chineseName',
                 valueField:'id',
                 onChange:function(newValue, oldValue){
							this.value=newValue;
							var values = $('#systemName').combobox('getData');
							 $.each(values, function (index, item) {
							   if($.trim(item.id) == $.trim(newValue) || $.trim(item.text) == $.trim(newValue)){
							        $('#systemId').textbox('setValue', newValue);
							        }
							 });
				    }
                 "
                >
        </td>
        <th>服务类型</th>
        <td>
          <select id="type" class="easyui-combobox" name="type"
                  data-options="width:100,valueField:'value', textField:'text',data:[
                {'value':'1','text':'调用方'},
                {'value':'0','text':'提供方'}
                ]">
          </select>
        </td>
        <th>

        </th>
        <td></td>
        <th>

        </th>
        <td></td>
        <th style="width:200px">
            <shiro:hasPermission name="exportStatistics-get">
                <a href="#" id="saveTagBtn" onclick="query()" class="easyui-linkbutton" iconCls="icon-search" style="margin-left:1em" >查询</a>
                <a href="#" id="clean" onclick="$('#searchForm').form('clear');" class="easyui-linkbutton" iconCls="icon-clear" style="margin-left:1em" >清空</a>
            </shiro:hasPermission>
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
			fitColumns:true,
			method:'get',toolbar:toolbar,
			pagination:true,
				pageSize:20"
         style="height:580px; width:100%;">
    <thead>
    <tr>
      <th data-options="field:'',checkbox:true,width:50"></th>
      <th data-options="field:'systemId',width:100">系统编码</th>
      <th data-options="field:'systemChineseName',width:100">系统名称</th>
      <th data-options="field:'type',width:80" formatter='formatter.typeText'>类型</th>
      <!--

      <th data-options="field:'useNum',width:80">调用数</th>

      <th data-options="field:'serviceNum',width:80">关联服务数</th>-->
      <th data-options="field:'operationNum',width:80">场景数</th>
      <th data-options="field:'resueOperationNum',width:80">复用场景数</th>
      <th data-options="field:'reuseRate',width:100">复用率</th>

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
  $(document).ready(function () {
    $("#resultList").datagrid({url:"/statistics/systemReuseRate"});
  });
  var formatter = {
    typeText: function (value, row, index) {
      if ("1" == value) {
        return "调用方";
      }
      if ("0" == value) {
        return "提供方";
      }
    }
  };
  var toolbar = [
    <shiro:hasPermission name="exportStatistics-get">
    {
      text:'导出EXCEL',
      iconCls:'icon-excel-export',
      handler: function () {
        var checkedItems = $('#resultList').datagrid('getChecked');

        if (checkedItems != null && checkedItems.length > 0) {
          var form=$("<form>");//定义一个form表单
          form.attr("style","display:none");
          form.attr("target","");
          form.attr("method","post");
          form.attr("action","/excelExporter/exportSystemReuserate");
          var fields = ["systemId", "systemChineseName","type", "serviceNum", "operationNum",  "resueOperationNum", "sum", "reuseRate"];
          for(var i=0; i < checkedItems.length; i++){
            for(var j=0; j < fields.length; j++){
              var input1=$("<input>");
              input1.attr("type","hidden");
              input1.attr("name","list["+i+"]."+fields[j]);
              input1.attr("value",checkedItems[i][fields[j]]);
              form.append(input1);
            }
          }

          $("body").append(form);//将表单放置在web中
          form.submit();//表单提交
        }
        else{
          alert("没有选中数据！");
        }
      }
    }
      </shiro:hasPermission>
  ];
  function query(){
    var params = {
      "systemId":$("#systemId").textbox("getValue"),
      "systemName":encodeURI($("#systemName").textbox("getText")),
      "type":$("#type").combobox("getValue")
//      "type":"0"
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

