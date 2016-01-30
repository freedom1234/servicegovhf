<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>列表页</title>
<link rel="stylesheet" type="text/css"
	href="/resources/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="/resources/themes/icon.css">
<link href="/resources/css/ui.css" rel="stylesheet" type="text/css">
</head>

<body>
	<fieldset>
		<legend>枚举映射</legend>
		<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<th><nobr>主代码名称</nobr></th>
				<td>
					<input class="easyui-text" readonly="true" value="${master.name}" style="width:140px" type="text" name="name" id="masterName"/>
				</td>
				<th><nobr>从代码名称</nobr></th>
				<td>
					<input class="easyui-text" readonly="true" value="${slave.name}" style="width:140px" type="text" name="name" id="slaveName"/>
				</td>
			</tr>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<!-- <tr>
				<th>主代码枚举名称</th>
				<td>
					<input class="easyui-combobox" panelHeight="auto" style="width:140px" type="text" name="name" id="masterElementName" editable="false"/>
				</td>
				<th>从代码枚举名称</th>
				<td>
					<input class="easyui-combobox" panelHeight="auto" style="width:140px" type="text" name="name" id="slaveElementName" editable="false"/>
				</td>
			</tr>
			<td><a href="#" id="btn" class="easyui-linkbutton" iconCls="icon-qxfp">设置映射关系</a></td> -->
		</table>
	</fieldset>
	<div id="div1" style="display: block">
		<table title="枚举映射" id="mappingdatagrid" style="height:330px; width:auto;">
	</table>
	</div>
	<div id="div2" style="display: none">
	<table title="枚举映射2" id="mappingdatagrid2" style="height:330px; width:auto;">
	</table>
	</div>
	<div id="w" class="easyui-window" title=""
		data-options="modal:true,closed:true,iconCls:'icon-add'"
		style="width:500px;height:200px;padding:10px;"></div>
	<script type="text/javascript" src="/resources/js/jquery.min.js"></script>
	<script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="/resources/js/ui.js"></script>
	<script type="text/javascript"
		src="/assets/enumManager/js/enumManager.js"></script>
	<script type="text/javascript">
		var editedRows = [];
		var processId = parent.processId;
		var taskId = parent.taskId;
		$(function(){
			var url = "/enum/getElementMapping/"+"${master.id}"+"/"+"${slave.id}";
			$('#mappingdatagrid').datagrid({
				//rownumbers:true,
				singleSelect:false,
				collapsible:true,
				url:url,
				method:'get',
				toolbar:toolbar,
				pagination:true,
				pageSize:10,
				columns:[[
			        {field:'productid',checkbox:true},
					{field:'REMARK',title:'主代码中文名称'},
			        {field:'MASTERNAME',title:'主代码枚举名称'},
					{field:'SLAVENAME',title:'从代码枚举名称',required : true,
						editor:{  
			                type:'combobox',
			                options:{  
			                	url : '/enum/getMasterElements/'+"${slave.id}",
								method : 'get',
								valueField : 'elementId',
								textField : 'elementName',
//								textField : 'remark',
								panelHeight : '150px'
			                }
			            }
			        },
					/*{field:'DIRECTION',title:'映射方向',editor:'text'},
					{field:'MAPPINGRELATION',title:'映射关系',editor:'text'}*/
			    ]],
			    onDblClickCell: function(index,field,value){
					$(this).datagrid('beginEdit', index);
					var ed = $(this).datagrid('getEditor', {index:index,field:field});
					$(ed.target).focus();
				},
				onBeginEdit : function(index,row){
					editedRows.push(index);
				},
			    onLoadSuccess : function (data){
			    	
			    },
				onLoadError: function (responce) {
					var resText = responce.responseText;
					if(resText.toString().indexOf("没有操作权限") > 0){
//                    alert("没有权限！");
						window.location.href = "/jsp/403.jsp";
					}
				}
				
			});

			var url2 = "/enum/getElementMappingSToM/"+"${master.id}"+"/"+"${slave.id}";
			$('#mappingdatagrid2').datagrid({
				//rownumbers:true,
				singleSelect:false,
				collapsible:true,
				url:url2,
				method:'get',
				toolbar:toolbar2,
				pagination:true,
				pageSize:10,
				columns:[[
					{field:'productid',checkbox:true},
					{field:'REMARK',title:'主代码中文名称'},
					{field:'SLAVENAME',title:'从代码枚举名称'},
					{field:'MASTERNAME',title:'主代码枚举名称',required : true,
						editor:{
							type:'combobox',
							options:{
								url : '/enum/getMasterElements/'+"${master.id}",
								method : 'get',
								valueField : 'elementId',
								textField : 'elementName',
//								textField : 'remark',
								panelHeight : '150px'
							}
						}
					},
					/*{field:'DIRECTION',title:'映射方向',editor:'text'},
					 {field:'MAPPINGRELATION',title:'映射关系',editor:'text'}*/
				]],
				onDblClickCell: function(index,field,value){
					$(this).datagrid('beginEdit', index);
					var ed = $(this).datagrid('getEditor', {index:index,field:field});
					$(ed.target).focus();
				},
				onBeginEdit : function(index,row){
					editedRows.push(index);
				},
				onLoadSuccess : function (data){

				},
				onLoadError: function (responce) {
					var resText = responce.responseText;
					if(resText.toString().indexOf("没有操作权限") > 0){
//                    alert("没有权限！");
						window.location.href = "/jsp/403.jsp";
					}
				}

			});
			
			$('#masterElementName').combobox({
				url : '/enum/getMasterElements/'+"${master.id}",
				method : 'get',
				valueField : 'elementId',
				textField : 'elementName'
			});
			
			$('#slaveElementName').combobox({
				url : '/enum/getMasterElements/'+"${slave.id}",
				method : 'get',
				valueField : 'elementId',
				textField : 'elementName'
			});
		});
		var toolbar = [];
		<shiro:hasPermission name="enum-delete">
		toolbar.push({
			text : '删除映射关系',
			iconCls : 'icon-remove',
			handler : function() {
				var selectData = $('#mappingdatagrid').datagrid('getSelections');
				if (selectData.length == 0) {
					alert("请先选择一条记录");
					return;
				}
				if(confirm('确定删除吗 ？')){
					enumManager.deleteElementsMapping(selectData, function(result){
						if(result){
							$('#mappingdatagrid').datagrid('reload');
						}
					});
				}
			}
		});
		</shiro:hasPermission>
		<shiro:hasPermission name="enum-update">
		toolbar.push({
			text : '保存映射关系',
			iconCls : 'icon-remove',
			handler : function() {
				for ( var per in editedRows) {
					$("#mappingdatagrid").datagrid('endEdit', editedRows[per]);
				}
				var editData = $("#mappingdatagrid").datagrid('getChanges');
				enumManager.saveElementMapping(editData,function(result){
					if(result){
						$('#mappingdatagrid').datagrid('reload');
					}
				});
				editedRows = [];
			}
		});
		</shiro:hasPermission>
		<shiro:hasPermission name="enum-get">
		toolbar.push({
			text:'改变映射方向',
			iconCls : 'icon-qxfp',
			handler:function(){
				document.getElementById('div1').style.display="none";
				document.getElementById('div2').style.display="block";
				$('#mappingdatagrid2').datagrid([]);
			}
		});
		</shiro:hasPermission>

		var toolbar2 = [];
		<shiro:hasPermission name="enum-delete">
		toolbar2.push({
			text : '删除映射关系',
			iconCls : 'icon-remove',
			handler : function() {
				var selectData = $('#mappingdatagrid2').datagrid('getSelections');
				if (selectData.length == 0) {
					alert("请先选择一条记录");
					return;
				}
				if(confirm('确定删除吗 ？')){
					enumManager.deleteElementsMapping(selectData, function(result){
						if(result){
							$('#mappingdatagrid2').datagrid('reload');
						}
					});
				}
			}
		});
		</shiro:hasPermission>
		<shiro:hasPermission name="enum-update">
		toolbar2.push({
			text : '保存映射关系',
			iconCls : 'icon-remove',
			handler : function() {
				for ( var per in editedRows) {
					$("#mappingdatagrid2").datagrid('endEdit', editedRows[per]);
				}
				var editData = $("#mappingdatagrid2").datagrid('getChanges');
				enumManager.saveElementMappingSToM(editData,function(result){
					if(result){
						$('#mappingdatagrid2').datagrid('reload');
					}
				});
				editedRows = [];
			}
		});
		</shiro:hasPermission>
		<shiro:hasPermission name="enum-get">
		toolbar2.push({
			text:'改变映射方向',
			iconCls : 'icon-qxfp',
			handler:function(){
				document.getElementById('div1').style.display="block";
				$('#mappingdatagrid').datagrid([]);
				document.getElementById('div2').style.display="none";
			}
		});
		</shiro:hasPermission>
	</script>
</body>
</html>