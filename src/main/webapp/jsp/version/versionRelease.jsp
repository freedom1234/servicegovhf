<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
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
		<link href="/resources/css/css.css" rel="stylesheet" type="text/css">
			<script type="text/javascript" src="/resources/js/jquery.min.js"></script>
	<script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="/jsp/version/version.js"></script>
	<script type="text/javascript">
		$(function(){
			$('#operationList').datagrid({
				rownumbers:true,
				singleSelect:false,
				url:'/version/operationList',
				method:'get',
//				toolbar:toolbar,
				pagination:true,
				pageSize:10
			});
			$("#search").click(function(){
				var queryParams = $('#operationList').datagrid('options').queryParams;
				queryParams.serviceId = $("#serviceId").textbox("getValue");
				queryParams.serviceName = encodeURI($("#serviceName").textbox("getValue"));
				queryParams.operationId = $("#operationId").textbox("getValue");
				queryParams.operationName = encodeURI($("#operationName").textbox("getValue"));
				if (queryParams.serviceId || queryParams.serviceName) {
					$("#operationList").datagrid('options').queryParams = queryParams;//传递值
					$("#operationList").datagrid('reload');//重新加载table
				} else {
					$("#operationList").datagrid('reload');
				}
			});
		});
		$.extend($.fn.validatebox.defaults.rules, {
			length : {
				validator : function(value) {

					return value.length >= 10;
				},
				message : '长度不少于十个字'
			}
		});

		var toolbar = [ {
			text : '移出',
			iconCls : 'icon-cancel',
			handler : function() {
				var rows = $('#operationList').datagrid("getSelections");	//获取你选择的所有行
		      //循环所选的行
		      	for(var i =0;i<rows.length;i++){    
		            var index = $('#operationList').datagrid('getRowIndex',rows[i]);//获取某行的行号
		            $('#operationList').datagrid('deleteRow',index);	//通过行号移除该行
		        }
			}
		} ];
		var toolbar2 = [ {
			text : '移出',
			iconCls : 'icon-cancel',
			handler : function() {
				var rows = $('#pcList').datagrid("getSelections");	//获取你选择的所有行
		      //循环所选的行
		      	for(var i =0;i<rows.length;i++){    
		            var index = $('#pcList').datagrid('getRowIndex',rows[i]);//获取某行的行号
		            $('#pcList').datagrid('deleteRow',index);	//通过行号移除该行
		        }
			}
		} ];
		function releaseBatch() {
			if(!$("#baseForm").form('validate')){
				return false;
			}
			var opItems = $('#operationList').datagrid('getChecked');
			if(opItems == null || opItems.length == 0){
				alert("不能发布空数据！");
				return false;
			}
			var operations = new Array();
			$.each(opItems, function(index, item) {
				var opertion = {};
				opertion.serviceId = item.serviceId;
				opertion.operationId = item.operationId;
				opertion.operationDesc =  $("#desc").textbox("getValue");
				operations.push(opertion);
			});
			$.ajax({
				type: "post",
				async: false,
				contentType:"application/json; charset=utf-8",
				url: "/operation/releaseBatch",
				dataType: "json",
				data: JSON.stringify(operations),
				success: function(data){
					alert("操作成功");
					 $('#operationList').datagrid('reload');
				}
			});
		}
	</script>
</head>

<body>
	<fieldset>
		<legend>版本信息</legend>
		<form id="baseForm">
			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<!-- 
					<th>发布版本号</th>
					<td><label for="textfield"></label> <input id="code"
						class="easyui-textbox" type="text" name="code"
						data-options="required:true">
					</td>
					 -->
					<th><nobr>版本描述</nobr></th>
					<td><input class="easyui-textbox" type="text" id="desc" name="desc"
						data-options="required:true,validType:['length']">
					</td>
					<td>
						<shiro:hasPermission name="version-add">
						<a href="javascript:void(0)" onclick="releaseBatch()" class="easyui-linkbutton" plain="true"
						iconCls="icon-save">发布</a>
						</shiro:hasPermission>
					</td>
				</tr>
				<tr>
					<th><nobr>服务代码</nobr></th>
					<td>
						<input class="easyui-textbox" id="serviceId"/>
					</td>
					<th><nobr>服务名称</nobr></th>
					<td>
						<input class="easyui-textbox" id="serviceName"/>
					</td>
					<th><nobr>场景代码</nobr></th>
					<td>
						<input class="easyui-textbox" id="operationId"/>
					</td>
				</tr>
				<tr>
					<th><nobr>场景名称</nobr></th>
					<td>
						<input class="easyui-textbox" id="operationName"/>
					</td>
					<th>&nbsp;</th>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>
						<shiro:hasPermission name="service-get">
						<nobr>
							<a href="#" id="search" class="easyui-linkbutton" iconCls="icon-search" style="margin-left:1em">查询</a>
							<a href="#" id="clean" onclick="$('#baseForm').form('clear');" class="easyui-linkbutton" iconCls="icon-clear" style="margin-left:1em" >清空</a>
						</nobr>
						</shiro:hasPermission>
					</td>
				</tr>
		</table>
		</form>
	</fieldset>
	<table id="operationList" class="easyui-datagrid" title="工作项发布清单(服务定义)" style="height:425px; width:auto;">
		<thead>
			<tr>
				<th data-options="field:'productid',checkbox:true"></th>
				<th data-options="field:'service.serviceId'" formatter='service.serviceId'>服务代码
				</th>
				<th data-options="field:'service.serviceName'"
					formatter='service.serviceName'>服务名称</th>
				<th data-options="field:'operationId',width:40,align:'left'">服务场景</th>
				<th data-options="field:'operationName',width:120,align:'left'">服务场景名称</th>
				<th data-options="field:'operationDesc',width:380,align:'left'">场景描述</th>
				<th data-options="field:'version.optType'" formatter="version.optType">修订类型</th>
				<th data-options="field:'version.code'" formatter="version.code">版本号</th>
			</tr>
		</thead>
	</table>
	<%--<table id="pcList" class="easyui-datagrid" title="工作项发布清单(公共代码)"
		data-options="
			rownumbers:true,
			singleSelect:false,
			url:'datagrid_data1.json',
			method:'get',
			pagination:true,
				pageSize:10"
		style="height:365px; width:auto;">
		<thead>
			<tr>
				<th data-options="field:'productid',checkbox:true"></th>


				<th data-options="field:'itemid'">服务代码</th>
				<th data-options="field:'status'">服务名称</th>
				<th data-options="field:'listprice',align:'right'">服务场景</th>
				<th data-options="field:'unitcost',width:80,align:'right'">服务场景名称
				</th>
				<th data-options="field:'attr1'">消费方</th>
				<th data-options="field:'status',width:60,align:'center'">交易码</th>
				<th data-options="field:'attr1'">交易名称</th>
				<th data-options="field:'attr1'">提供方</th>
				<th data-options="field:'attr1'">修订类型</th>
				<th data-options="field:''">版本号</th>
			</tr>
		</thead>
	</table>--%>
	
</body>
</html>