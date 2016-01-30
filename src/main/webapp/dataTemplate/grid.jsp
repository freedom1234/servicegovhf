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
    <td><a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-save">发布</a></td>
  </tr>
</table>

</fieldset>
<table class="easyui-datagrid" title="工作项发布清单(服务定义)" 
			data-options="rownumbers:true,singleSelect:false,url:'datagrid_data1.json',method:'get',toolbar:toolbar,pagination:true,
				pageSize:10" style="height:365px; width:auto;">
  <thead>
    <tr>
          <th data-options="field:'productid',checkbox:true"> </th>

    
      <th data-options="field:'itemid'">服务代码 </th>
      <th data-options="field:'status'">服务名称 </th>
      <th data-options="field:'listprice',align:'right'">服务场景 </th>
      <th data-options="field:'unitcost',width:80,align:'right'">服务场景名称 </th>
      <th data-options="field:'attr1'">消费方 </th>
      <th data-options="field:'status',width:60,align:'center'">交易码 </th>
      <th data-options="field:'attr1'"> 交易名称 </th>
      <th data-options="field:'attr1'"> 提供方 </th>
      <th data-options="field:'attr1'">修订类型 </th>
      <th data-options="field:'attr1'">版本号 </th>
    </tr>
  </thead>
</table>
<table class="easyui-datagrid" title="工作项发布清单(公共代码)" 
			data-options="rownumbers:true,singleSelect:false,url:'datagrid_data1.json',method:'get',toolbar:toolbar2,pagination:true,
				pageSize:10" style="height:365px; width:auto;">
  <thead>
    <tr>
          <th data-options="field:'productid',checkbox:true"> </th>

    
      <th data-options="field:'itemid'">服务代码 </th>
      <th data-options="field:'status'">服务名称 </th>
      <th data-options="field:'listprice',align:'right'">服务场景 </th>
      <th data-options="field:'unitcost',width:80,align:'right'">服务场景名称 </th>
      <th data-options="field:'attr1'">消费方 </th>
      <th data-options="field:'status',width:60,align:'center'">交易码 </th>
      <th data-options="field:'attr1'"> 交易名称 </th>
      <th data-options="field:'attr1'"> 提供方 </th>
      <th data-options="field:'attr1'">修订类型 </th>
      <th data-options="field:'attr1'">版本号 </th>
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
			text:'移出了',
			iconCls:'icon-cancel',
			handler:function(){alert('移出')}
		}];
	</script> 
<script type="text/javascript" src="/resources/js/jquery.min.js"></script> 
<script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
</body>
</html>