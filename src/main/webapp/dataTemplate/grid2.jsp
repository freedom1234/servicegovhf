<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>列表页</title>
<link rel="stylesheet" type="text/css" href="/resources/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="/resources/themes/icon.css">
<link href="/resources/css/css.css" rel="stylesheet" type="text/css">
</head>

<body>
<fieldset>
 <legend>版本信息</legend>
 <table border="0" cellspacing="0" cellpadding="0">
  <tr>
    <th>基线版本号</th>
    <td><label for="textfield"></label>
      <input class="easyui-textbox" type="text" name="name" data-options="required:true"></td>
    <th>版本描述</th>
    <td><input class="easyui-textbox" type="text" name="name2" data-options="required:true"></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    </tr>
  <tr>
    <th>发布人</th>
    <td><input class="easyui-textbox" type="text" name="name2" data-options="required:true" /></td>
    <th>版本发布时间</th>
    <td><input class="easyui-textbox" type="text" name="name2" data-options="required:true" /></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    </tr>
  <tr>
    <th>&nbsp;</th>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td><a href="#" onclick="showHistoryVersion()" class="easyui-linkbutton" plain="true" iconcls="icon-search">历史基线版本</a></td>
    <td><a href="#" class="easyui-linkbutton" plain="true" iconcls="icon-save">版本下载</a></td>
    </tr>
 </table>

</fieldset>
<table class="easyui-datagrid" title="服务规范定义列表" 
			data-options="rownumbers:true,singleSelect:false,url:'datagrid_data1.json',method:'get',pagination:true,
				pageSize:10" style="height:365px; width:auto;">
  <thead>
    <tr>
          <th data-options="field:'productid',checkbox:true"> </th>

    
      <th data-options="field:'itemid'">字段1 </th>
      <th data-options="field:'status'">字段2 </th>
      <th data-options="field:'listprice',align:'right'">字段3 </th>
      <th data-options="field:'unitcost',width:80,align:'right'">字段4 </th>
      <th data-options="field:'attr1'">消费方 </th>
      <th data-options="field:'status',width:60,align:'center'">字段5 </th>
      <th data-options="field:'attr1'"> 字段6 </th>
      <th data-options="field:'attr1'"> 字段7 </th>
      <th data-options="field:'attr1'">字段8 </th>
      <th data-options="field:'attr1'">字段9 </th>
    </tr>
  </thead>
</table>
<table class="easyui-datagrid" title="服务接口映射列表"
			data-options="rownumbers:true,singleSelect:false,url:'datagrid_data1.json',method:'get',pagination:true,
				pageSize:10" style="height:365px; width:auto;">
  <thead>
    <tr>
          <th data-options="field:'productid',checkbox:true"> </th>

    
      <th data-options="field:'itemid'">字段1 </th>
      <th data-options="field:'status'">字段2 </th>
      <th data-options="field:'listprice',align:'right'">字段3 </th>
      <th data-options="field:'unitcost',width:80,align:'right'">字段4 </th>
      <th data-options="field:'attr1'">消费方 </th>
      <th data-options="field:'status',width:60,align:'center'">字段5 </th>
      <th data-options="field:'attr1'"> 字段6 </th>
      <th data-options="field:'attr1'"> 字段7 </th>
      <th data-options="field:'attr1'">字段8 </th>
      <th data-options="field:'attr1'">字段9 </th>
    </tr>
  </thead>
</table>
<table class="easyui-datagrid" title="服务公共代码" 
			data-options="rownumbers:true,singleSelect:false,url:'datagrid_data1.json',method:'get',pagination:true,
				pageSize:10" style="height:365px; width:auto;">
  <thead>
    <tr>
          <th data-options="field:'productid',checkbox:true"> </th>

    
      <th data-options="field:'itemid'">字段1 </th>
      <th data-options="field:'status'">字段2 </th>
      <th data-options="field:'listprice',align:'right'">字段3 </th>
      <th data-options="field:'unitcost',width:80,align:'right'">字段4 </th>
      <th data-options="field:'attr1'">消费方 </th>
      <th data-options="field:'status',width:60,align:'center'">字段5 </th>
      <th data-options="field:'attr1'"> 字段6 </th>
      <th data-options="field:'attr1'"> 字段7 </th>
      <th data-options="field:'attr1'">字段8 </th>
      <th data-options="field:'attr1'">字段9 </th>
    </tr>
  </thead>
</table>
<script type="text/javascript">
		var toolbar = [{
			text:'移出',
			iconCls:'icon-cancel',
			handler:function(){alert('移出')}
		}];
		var toolbar2 = [{
			text:'移出',
			iconCls:'icon-cancel',
			handler:function(){alert('移出')}
		}];
		function showHistoryVersion(){
			
			uiinit.win({
					w:500,
					iconCls:'icon-search',
					title:"历史基线版本",
					url : "/dataTemplate/historyVersion/sla.html"
				});	
		}
	</script> 
<script type="text/javascript" src="/resources/js/jquery.min.js"></script> 
<script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/resources/js/ui.js"></script>

</body>
</html>