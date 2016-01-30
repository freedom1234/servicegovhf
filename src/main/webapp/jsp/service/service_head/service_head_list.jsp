<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <title>列表页</title>
  <link rel="stylesheet" type="text/css"
        href="/resources/themes/default/easyui.css">
  <link rel="stylesheet" type="text/css"
        href="/resources/themes/icon.css">
  <link href="/resources/css/ui.css" rel="stylesheet" type="text/css">
  <script type="text/javascript" src="/resources/js/jquery.min.js"></script>
  <script type="text/javascript"
          src="/resources/js/jquery.easyui.min.js"></script>
  <script type="text/javascript" src="/resources/js/ui.js"></script>
</head>

<body>
<form id="searchForm">
  <fieldset>
    <legend>条件搜索</legend>
    <table border="0" cellspacing="0" cellpadding="0">
      <tr>
        <th><nobr>
          名称
        </nobr>
        </th>
        <td>
          <input class="easyui-textbox" type="text" id="headName">
        </td>

        <th><nobr>
          描述
        </nobr>
        </th>
        <td>
          <input class="easyui-textbox" type="text" id="headDesc">
        </td>
        <th>
          <nobr>
            类型
          </nobr>
        </th>
        <td>
          <select id="type" class="easyui-combobox"  panelHeight="auto" style="width: 170px"  data-options="editable:false">
            <option value="">全部</option>
            <option value="sys_head">sys_head</option>
            <option value="app_head">app_head</option>
          </select>
        </td>
        <td align="right">
          <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" onclick="searchData();">搜索</a>
          <a href="javascript:void(0)" id="clean" onclick="$('#searchForm').form('clear');" class="easyui-linkbutton" iconCls="icon-clear" style="margin-left:1em" >清空</a>
        </td>
      </tr>
    </table>


  </fieldset>
</form>
<table id="tg" style="height: 370px; width: auto;">
  <thead>
  <tr>
    <th data-options="field:'headId',checkbox:true"></th>
    <th data-options="field:'headName',width:'25%'">
      报文头名称
    </th>
    <th data-options="field:'type',width:'10%'">
      类型
    </th>
    <th data-options="field:'headDesc',width:'25%'">
      描述
    </th>
    <th data-options="field:'headRemark',width:'25%'">
      备注
    </th>
  </tr>
  </thead>
</table>
<div id="dlg" class="easyui-dialog" closed="true" resizable="true"></div>
<script type="text/javascript">
  var toolbar = [];
  <shiro:hasPermission name="serviceHead-add">
  toolbar.push({
    text:'新增',
    iconCls:'icon-add',
    handler:function(){
      var urlPath = "/jsp/service/service_head/service_head_add.jsp"
      $('#dlg').dialog({
        title: '服务报文头新增',
        width: 500,
        left:150,
        top:50,
        closed: false,
        cache: false,
        href: urlPath,
        modal: true
      });
    }
  });
  </shiro:hasPermission>
  <shiro:hasPermission name="serviceHead-update">
  toolbar.push({
    text:'修改',
    iconCls:'icon-edit',
    handler:function(){
      var row = $('#tg').datagrid('getSelected');
      if(row){
        var urlPath = "/serviceHead/editPage?headId="+row.headId;
        $('#dlg').dialog({
          title: '服务报文头修改',
          width: 500,
          left:150,
          top:50,
          closed: false,
          cache: false,
          href: urlPath,
          modal: true
        });
      }else{
        alert('请先选中一行数据！');
      }
    }
  });
  </shiro:hasPermission>
  <shiro:hasPermission name="serviceHead-delete">
  toolbar.push({
    text:'删除',
    iconCls:'icon-remove',
    handler:function(){
      var row = $("#tg").treegrid("getSelected");
      if(row){
        $.ajax({
          type: "post",
          async: false,
          url: '/serviceHead/delete',
          dataType: "json",
          data: {"headId":row.headId},
          success: function (data) {
            alert('删除成功 ！');
            $("#tg").datagrid('reload');
          }
        });
      }else{
        alert("请选择要删除的行");
      }
    }
  });
  </shiro:hasPermission>
  $(document).ready(function() {
    $('#tg').datagrid({
      title: '基本信息维护',
      iconCls: 'icon-edit',//图标
      width: '100%',
      height: '500px',
      method: 'post',
      collapsible: true,
      url: '/serviceHead/query',
      singleSelect: true,//是否单选
      pagination: true,//分页控件
      pageSize: 15,//每页显示的记录条数，默认为10
      pageList: [15, 20, 30],//可以设置每页记录条数的列表
      rownumbers: true,//行号
      toolbar: toolbar,
      onDblClickRow: dbclick
    });
  });
    var p = $('#tg').treegrid('getPager');

    $(p).pagination({

      beforePageText: '第',//页数文本框前显示的汉字
      afterPageText: '页    共 {pages} 页',
      displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });

   function dbclick(index,field,value){
    var url = '/serviceHeadSda/sdaPage?serviceHeadId='+field.headId;
    var title = "";
    title = '服务报文头:' + field.headName;
    var mainTabs = parent.$('#mainContentTabs');
    if (mainTabs.tabs('exists', title)) {
      mainTabs.tabs('select', title);
    } else {
      var content = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:98%;"></iframe>';
      mainTabs.tabs('add', {
        title: title,
        content: content,
        closable: true
      });
    }
  }

  function searchData() {

    var headName = $("#headName").val();
    var headDesc = $("#headDesc").val();
    var headRemark = $("#headRemark").val();
    var type = $("#type").combobox('getValue');

    var queryParams = $('#tg').datagrid('options').queryParams;
    queryParams.headName = headName;
    queryParams.headDesc = headDesc;
    queryParams.headRemark = headRemark;
    queryParams.type = type;
    $('#tg').datagrid('options').queryParams = queryParams;//传递值
    $("#tg").datagrid('reload');//重新加载table
  }
</script>

</body>
</html>