<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>列表页</title>
<link rel="stylesheet" type="text/css" href="/resources/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="/resources/themes/icon.css">
<link href="/resources/css/ui.css" rel="stylesheet" type="text/css">
</head>

<body onload="a()">
<fieldset>
 <legend>主代码</legend>
<table border="0" cellspacing="0" cellpadding="0">
  <tr>
      <th>代码名称</th>
    <td> <input class="easyui-textbox" type="text" id="name" ></td>
   
   <th>数据来源</th>
    <td> <input class="easyui-textbox" type="text" id="dataSource" >
      </td>
   <th>代码版本</th>
   <td>
   	<input class="easyui-textbox" type="text" id="version" >
   </td>
   <th>&nbsp;</th>
    <td>&nbsp; </td>
  </tr>
  <tr>
     <th>是否标准代码</th>
    <td><select class="easyui-combobox" panelHeight="auto" style="width:155px" id="isStandard">
				<option value="1">是</option>
				<option value="0">否</option>
			</select></td>
     <th>代码状态</th>
    <td><select class="easyui-combobox" panelHeight="auto" style="width:155px" id="status">
				<option value="1">使用</option>
				<option value="0">退役</option>
			</select></td>
	<th>备注</th>
   <td>
   	<input class="easyui-textbox" type="text" id="remark" >
   </td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td colspan="4" align="left"><a href="#" class="easyui-linkbutton"  iconCls="icon-qxfp" onclick="showZdm()">主代码</a>&nbsp;&nbsp;<a href="#" class="easyui-linkbutton"  iconCls="icon-qxfp" onclick="showCdm()">从代码值</a>&nbsp;&nbsp;<a href="#" onclick="showYsgz()" class="easyui-linkbutton"  iconCls="icon-qxfp">映射规则</a>&nbsp;&nbsp;<a href="#" class="easyui-linkbutton"  iconCls="icon-save">保存</a></td>
    </tr>
</table>


</fieldset>
<table class="easyui-datagrid" title="从代码列表" id="datagrid"
			data-options="rownumbers:true,singleSelect:false,url:'../datagrid_data1.json',method:'get',toolbar:toolbar,pagination:true,
				pageSize:10" style="height:370px; width:auto;">
  <thead>
    <tr>
      <th data-options="field:'productid',checkbox:true"> </th>
      <th data-options="field:'name',width:100">代码名称 </th>
      <th data-options="field:'isStandard',width:50">是否标准代码 </th>
      <th data-options="field:'isMaster',width:50,align:'right'">是否主代码 </th>
      <th data-options="field:'dataSource',width:80">数据来源 </th>
      <th data-options="field:'status'">代码状态 </th>
      <th data-options="field:'version',width:60,align:'center'">代码版本 </th>
      <th data-options="field:'remark',width:60"> 备注 </th>
      <th data-options="field:'optUser',width:60"> 操作用户 </th>
      <th data-options="field:'optDate',width:60">操作时间 </th>
    </tr>
  </thead>
</table>
<div id="w" class="easyui-window" title="" data-options="modal:true,closed:true,iconCls:'icon-add'" style="width:500px;height:200px;padding:10px;">
		
</div>
<script type="text/javascript">
	/* $(function(){
		alert(44);
		var selectData = parent.$('#dg').datagrid('getSelected');
		if(selectData != null){
			alert(222);
		} 
	}); */
	function a(){
		/* var _name = "${param.name}";
		var _isStandard = "${param.isStandard}";
		var _version = "${param.version}";
		var _dataSource = "${param.dataSource}";
		var _status = "${param.status}";
		var _remark = "${param.remark}"; */
		var id = "${param.id}";
		$.ajax({
			"type" : "GET",
			"contentType" : "application/json;charset=utf-8",
			"url" : "/enum/getByEnumId/"+id,
			"data" : JSON.stringify(id),
			"dataType" : "json",
			"success" : function(result){
				var master = result.master;
				alert(master.name);
				$('#datagrid').datagrid('load',result.slave);
			}
		});
		
		/* $('#name').val(_name);
		$('#isStandard').val(_isStandard);
		$('#version').val(_version);
		$('#dataSource').val(_dataSource);
		$('#status').val(_status);
		$('#remark').val(_remark); */
		
		/* var selectData = parent.$('#subtab').tabs('getTab',"公共代码").$('#dg').datagrid('getSelected');
		if(selectData != null){
			alert(3333);
		} */ 
	}
	var toolbar = [
	{
		text:'删除',
		iconCls:'icon-remove',
		handler:function(){
			
		}
	}];
	function showZdm(){
		var content = '<iframe scrolling="auto" frameborder="0"  src="/dataTemplate/words/grid3.html" style="width:100%;height:100%;"></iframe>';

			parent.uiinit.subtab.add({
				title:'主代码值',
				content:content	
			})	
	}
	function showCdm(){
		var content = '<iframe scrolling="auto" frameborder="0"  src="/dataTemplate/words/grid5.html" style="width:100%;height:100%;"></iframe>';

			parent.uiinit.subtab.add({
				title:'从代码值',
				content:content	
			})	
	}
	function showYsgz(){
		var content = '<iframe scrolling="auto" frameborder="0"  src="/dataTemplate/words/grid4.html" style="width:100%;height:100%;"></iframe>';

			parent.uiinit.subtab.add({
				title:'映射规则',
				content:content	
			})	
	}
</script> 
<script type="text/javascript" src="/resources/js/jquery.min.js"></script> 
<script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/resources/js/ui.js"></script>
</body>
</html>