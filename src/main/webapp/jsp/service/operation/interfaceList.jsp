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
  </head>
  
  <body>
   <table id="intefaceList" class="easyui-datagrid"
		style="height:370px; width:auto;">
		<thead>
			<tr>
				<%--<th data-options="field:'invokeId',checkbox:true"></th>
				<th data-options="field:'systemId', width:80">系统id</th>
				<th data-options="field:'systemChineseName', width:100">系统名称</th>
				<th data-options="field:'interfaceId', width:150">接口id</th>
				<th data-options="field:'interfaceName', width:150">接口名称</th>
				<th data-options="field:'isStandard', width:100">是否标准</th>--%>
			</tr>
		</thead>
	</table>
	
	<div id="tb" style="padding:5px;height:auto">
    	<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><a href="javascript:void(0)" onclick="$('#opDlg').dialog('close');" class="easyui-linkbutton" iconCls="icon-cancel" plain="true">取消</a>&nbsp;&nbsp;
	    <a href="javascript:void(0)" onclick="selectInterface('${param.newListId}', '${param.type }');" class="easyui-linkbutton" iconCls="icon-ok" plain="true">确定</a>
    </td>
	  <td>
		  </td>
	  <td>
	  </td>
	  <td>
		  <input id="interfaceText" class="easyui-textbox" /> <a href="javascript:void(0)" onclick="queryInterfaceList()" class="easyui-linkbutton"  iconCls="icon-search">查询</a>

	  </td>
    </tr>
    </table>
    </div>
   <script type="text/javascript">
	   var editedRows = [];
	   var formatter = {
		   isStandard: function (value, row, index) {
			   if (value == 0) {
				   return "标准";
			   }
			   if (value == 1) {
				   return "非标";
			   }
		   }
	   }
	   $(function(){
		   $("#intefaceList").datagrid({
			   rownumbers:true,
			   singleSelect:true,
			   url:'/serviceLink/getInterface2?systemId=${param.systemId}&type=${param.type}',
			   method:'post',
			   toolbar:'#tb',
			   pagination:true,
			   pageSize:10,
			   columns:[[
				   {field:'invokeId',checkbox:true},
				   {field:'systemId',title:'系统id'},
				   {field:'systemChineseName',title:'系统名称'},
				   {field:'interfaceId',title:'接口id'},
				   {field:'interfaceName',title:'接口名称'},
				   {field:'isStandard',formatter:formatter.isStandard,title:'是否标准（双击选择）',required : true,
					   editor:{
						   type:'combobox',
						   options:{
							   valueField: 'value',
							   textField: 'label',
							   data: [{
								   label: '非标',
								   value: '1',
								   selected:true
							   },{
								   label: '标准',
								   value: '0'
							   }],
							   panelHeight : '150px',
							   onSelect:function(record){
								   for ( var per in editedRows) {
									   $("#intefaceList").datagrid('endEdit', editedRows[per]);
								   }
								   editedRows = [];
							   }
						   }
					   }
				   }
			   ]],
			   onBeginEdit : function(index,row){
				   editedRows.push(index);
			   },
			   onDblClickCell: function(index,field,value){
				   $(this).datagrid('beginEdit', index);
				   var ed = $(this).datagrid('getEditor', {index:index,field:field});
				   $(ed.target).focus();
			   }
		   });
	   });

	   function queryInterfaceList(){
		   var params = {
			   "text" : $("#interfaceText").textbox("getValue")
		   };
		   $("#intefaceList").datagrid('options').queryParams = params;
		   $("#intefaceList").datagrid('reload');
	   }
   </script>
  </body>
</html>
