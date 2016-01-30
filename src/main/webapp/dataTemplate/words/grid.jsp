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

<body>
<fieldset>
 <legend>条件搜索</legend>
<table border="0" cellspacing="0" cellpadding="0">
  <tr>
      <th>元数据名称</th>
    <td> <input class="easyui-textbox" type="text" name="name" ></td>
   
   <th>中文名称</th>
    <td> <input class="easyui-textbox" type="text" name="name" >
      </td>
   <th>&nbsp;</th>
    <td>&nbsp; </td>
  </tr>
  <tr>
     <th>是否标准代码</th>
    <td><select class="easyui-combobox" panelHeight="auto" style="width:155px">
			<option value="1">是</option>
			<option value="0">否</option>
		</select>
	</td>
    <th>标准代码ID</th>
    <td> <input class="easyui-textbox" type="text" name="name" ></td>
     <th>主代码数据来源</th>
    <td>
    	<select class="easyui-combobox" panelHeight="auto" style="width:155px">
			<option value="1">是</option>
			<option value="0">否</option>
		</select>
	</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td align="right"><a href="#" class="easyui-linkbutton"  iconCls="icon-search">搜索</a></td>
  </tr>
</table>


</fieldset>
<table class="easyui-datagrid" title="主代码列表 " id="dg"
			data-options="rownumbers:true,singleSelect:true,url:'/enum/getAll',method:'get',toolbar:toolbar,pagination:true,
				pageSize:10" style="height:370px;width:auto">
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

<script type="text/javascript" src="/resources/js/jquery.min.js"></script> 
<script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/resources/js/ui.js"></script>
<script type="text/javascript" src="/assets/enumManager/js/enumManager.js"></script>
<script type="text/javascript">
		var toolbar = [
		{
			text:'新增',
			iconCls:'icon-add',
			handler:function(){
				uiinit.win({
					w:500,
					iconCls:'icon-add',
					title:"新增服务",
					url : "/dataTemplate/formTemplate/enumForm/enumAppandForm.jsp"
				});
			}
		},{
			text:'维护',
			iconCls:'icon-edit',
			handler:function(){
				var selectData = $('#dg').datagrid('getSelected');
				if(selectData == null){
					return;
				}
				var content = '<iframe scrolling="auto" frameborder="0"  src="/dataTemplate/words/grid2.jsp?id='+selectData.id+'" style="width:100%;height:100%;"></iframe>';
				var title = "公共代码维护";
				if (parent.$('#subtab').tabs('exists', title)){
					parent.$('#subtab').tabs('select', title);
				} else {
					parent.$('#subtab').tabs('add',{
						"title":title,
						"content" : content,
						"closable":true
					});
				}
			}
		},{
			text:'删除',
			iconCls:'icon-remove',
			handler:function(){}
		},
		{
			text:'检出',
			iconCls:'icon-qxfp',
			handler:function(){
				
			}
		},
		{
			text:'提交任务',
			iconCls:'icon-qxfp',
			handler:function(){
				
			}
		}];
	</script> 
</body>
</html>