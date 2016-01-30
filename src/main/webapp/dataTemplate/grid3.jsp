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
 <legend>条件过滤</legend>
 <table border="0" cellspacing="0" cellpadding="0">
  <tr>
    <th>关键字</th>
    <td>
      <input class="easyui-textbox" type="text" name="name" ></td>
   
    <td><a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-search">过滤</a></td>

  </tr>
</table>

</fieldset>
<table class="easyui-datagrid" title="版本历史" 
			data-options="rownumbers:true,singleSelect:false,url:'datagrid_data1.json',method:'get',toolbar:toolbar,pagination:true,
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
			text:'选择',
			iconCls:'icon-ok',
			handler:function(){alert('选择')}
		},
		{
			text:'撤销',
			iconCls:'icon-cancel',
			handler:function(){alert('移出')}
		}];
	</script> 
<script type="text/javascript" src="/resources/js/jquery.min.js"></script> 
<script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
</body>
</html>