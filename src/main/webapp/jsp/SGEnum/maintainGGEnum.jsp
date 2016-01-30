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
	<link rel="stylesheet" type="text/css"
		href="/resources/themes/icon.css">
		<link href="/resources/css/ui.css" rel="stylesheet" type="text/css">
</head>

<body>
	<fieldset>
		<legend>主代码</legend>
		<table border="0" cellspacing="0" cellpadding="0">
			<tr style="display:none">
				<th><nobr>代码id</nobr></th>
				<td><input class="easyui-textbox" type="text" name="name"
					id="id" value="${master.id}">
				</td>
			</tr>

			<tr>
				<th><nobr>中文名称</nobr></th>
				<td><input class="easyui-textbox" type="text" id="remark"
						   value="${master.remark}">
				</td>
				<th><nobr>代码名称</nobr></th>
				<td><input class="easyui-textbox" type="text" name="name"
					id="name" readonly="true" value="${master.name}">
				</td>
				<th><nobr>是否标准代码</nobr></th>
				<td><select name="isStandard" id="isStandard" readonly="true"
					panelHeight="auto" style="width:140px" value="${master.isStandard}">
				</select>
				</td>
				<th><nobr>主代码数据来源</nobr></th>
				<td><input class="easyui-textbox" readonly="true" type="text" name="dataSource"
					value="${master.dataSource}">
				</td>
				<shiro:hasPermission name="enum-get">
				<td colspan="4" align="right"><nobr><a href="#"
												 class="easyui-linkbutton" iconCls="icon-qxfp" onclick="showZdm()">主代码值</a></nobr>
					&nbsp;&nbsp;
				</td>
				</shiro:hasPermission>

			</tr>
		</table>

	</fieldset>
	<table title="从代码列表" id="slavedatagrid"
		style="height:330px; width:auto;">
		<thead>
			<tr>
				<th data-options="field:'productid',checkbox:true"></th>
				<th data-options="field:'name'">代码名称</th>
				<th data-options="field:'isStandard'" formatter='formatter.isStandard'>是否标准代码</th>
				<th data-options="field:'isMaster',align:'right'" formatter='formatter.isMaster'>是否主代码</th>
				<th data-options="field:'dataSource'">数据来源</th>
				<%--<th data-options="field:'status'" formatter='formatter.status'>代码状态</th>
				<th data-options="field:'version',align:'center'">代码版本</th>--%>
				<th data-options="field:'remark'">备注</th>
				<th data-options="field:'optUser'">操作用户</th>
				<th data-options="field:'optDate'">操作时间</th>
			</tr>
		</thead>
	</table>
	<div id="w" class="easyui-window" title=""
		data-options="modal:true,closed:true,iconCls:'icon-add'"
		style="width:500px;height:200px;padding:10px;"></div>
	<script type="text/javascript" src="/resources/js/jquery.min.js"></script>
	<script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="/resources/js/ui.js"></script>
	<script type="text/javascript"
		src="/assets/enumManager/js/enumManager.js"></script>
	<script type="text/javascript">
		var processId = parent.processId;
		var taskId = parent.taskId;
		$(function(){
			$('#slavedatagrid').datagrid({
				rownumbers : true,
				singleSelect : true,
				collapsible : true,
				url : '/enum/getSlaveByMasterId/${master.id}',
				method : 'get',
				toolbar : toolbar,
				pagination : true,
				pageSize : 10,
				onLoadError: function (responce) {
					var resText = responce.responseText;
					if(resText.toString().indexOf("没有操作权限") > 0){
//                    alert("没有权限！");
						window.location.href = "/jsp/403.jsp";
					}
				}
			});
			$('#isStandard').combobox({
				valueField: 'value',
				textField: 'label',
				data: [{
					label: '是',
					value: '1',
					selected : "${master.isStandard}" =="1"
				},{
					label: '否',
					value: '0',
					selected : "${master.isStandard}" =="0"
				}]
			});
		});

		var formatter = {
			isStandard: function (value, row, index) {
				if (value == 0) {
					return "<font>否</font>";
				}
				if (value == 1) {
					return "<font>是</font>";
				}
			},
			status: function (value, row, index) {
				if (value == 0) {
					return "<font>退役</font>";
				}
				if (value == 1) {
					return "<font>使用</font>";
				}
			},
			isMaster: function (value, row, index) {
				if (value == 0) {
					return "<font>否</font>";
				}
				if (value == 1) {
					return "<font>是</font>";
				}
			}
		};
		var toolbar = [];
		<shiro:hasPermission name="enum-delete">
		toolbar.push({
			text : '删除',
			iconCls : 'icon-remove',
			handler : function() {
				var selectData = $('#slavedatagrid').datagrid(
						'getSelected');
				if (selectData == null) {
					alert("请先选择一条记录");
					return;
				}
				if (confirm('确定删除吗 ？')) {
					enumManager.deleteEnum(selectData.id, function(
							result) {
						if (result) {
							$('#slavedatagrid').datagrid('reload');
						}
					});
				}
			}
		});
		</shiro:hasPermission>
		<shiro:hasPermission name="enum-add">
		toolbar.push({
			text : '新增从代码',
			iconCls : 'icon-add',
			handler : function() {
				uiinit.win({
					w : 500,
					iconCls : 'icon-add',
					title : "新增从代码",
					url : "/pages/SGEnum/form/slaveEnumAppandForm.jsp"
				});
			}
		});
		</shiro:hasPermission>
		<shiro:hasPermission name="enum-get">
		toolbar.push({
			text : '从代码维护',
			iconCls : 'icon-edit',
			handler : function() {
				var selectData = $('#slavedatagrid').datagrid(
						'getSelected');
				if (selectData == null) {
					alert("请先选择一条记录");
					return;
				}
				var content = '<iframe scrolling="auto" frameborder="0"  src="/enum/getByEnumId/'
						+ selectData.id
						+ '/'
						+ selectData.isMaster
						+ '/0" style="width:100%;height:100%;"></iframe>';
				var title = "从代码维护";

				if (parent.$('#subtab').tabs('exists', title)) {
					parent.$('#subtab').tabs('select', title);
					var tab = parent.$('#subtab').tabs('getSelected');
					parent.$('#subtab').tabs('update', {
						tab : tab,
						options : {
							"title" : title,
							"content" : content,
							"closable" : true
						}
					});
				} else {
					parent.$('#subtab').tabs('add', {
						"title" : title,
						"content" : content,
						"closable" : true
					});
				}
			}
		});
		</shiro:hasPermission>
		<shiro:hasPermission name="enum-get">
		toolbar.push({
			text : '枚举映射',
			iconCls : 'icon-add',
			handler : function() {
				var selectData = $('#slavedatagrid').datagrid(
						'getSelected');
				if (selectData == null) {
					alert("请先选择一条记录");
					return;
				}
				var content = '<iframe scrolling="auto" frameborder="0"  src="/enum/getMappingBySlaveId/'
						+ selectData.id
						+ '" style="width:100%;height:100%;"></iframe>';
				var title = "代码枚举映射";
				if (parent.$('#subtab').tabs('exists', title)) {
					parent.$('#subtab').tabs('select', title);
				} else {
					parent.$('#subtab').tabs('add', {
						"title" : title,
						"content" : content,
						"closable" : true
					});
				}
			}
		});
		</shiro:hasPermission>

		var toolbar2 = [];
		<shiro:hasPermission name="enum-delete">
		toolbar2.push({
			text : '删除',
			iconCls : 'icon-remove',
			handler : function() {
				var selectData = $('#elementdatagrid').datagrid(
						'getSelections');
				if (selectData.length == 0) {
					alert("请先选择一条记录");
					return;
				}
				if (confirm('确定删除吗 ？')) {
					enumManager.deleteEnumElements(selectData,
							function(result) {
								if (result) {
									$('#elementdatagrid').datagrid(
											'reload');
								}
							});
				}
			}
		});

		</shiro:hasPermission>
		<shiro:hasPermission name="enum-add">
		toolbar2.push({
			text : '新增',
			iconCls : 'icon-add',
			handler : function() {
				uiinit.win({
					w : 500,
					iconCls : 'icon-add',
					title : "新增枚举",
					url : "/pages/SGEnum/form/elementAppandForm.jsp"
				});
			}
		});
		</shiro:hasPermission>
		function showZdm() {
			var content = '<iframe scrolling="auto" frameborder="0"  src="/enum/getByEnumId/'
					+ "${master.id}"
					+ '/'
					+ "${master.isMaster}"
					+ '/0" style="width:100%;height:100%;"></iframe>';
			var title = "主代码维护";

			if (parent.$('#subtab').tabs('exists', title)) {
				parent.$('#subtab').tabs('select', title);
				var tab = parent.$('#subtab').tabs('getSelected');
				parent.$('#subtab').tabs('update', {
					tab : tab,
					options : {
						"title" : title,
						"content" : content,
						"closable" : true
					}
				});
			} else {
				parent.$('#subtab').tabs('add', {
					"title" : title,
					"content" : content,
					"closable" : true
				});
			}
		}
		function showCdm() {
			var content = '<iframe scrolling="auto" frameborder="0"  src="/dataTemplate/words/grid5.html" style="width:100%;height:100%;"></iframe>';

			parent.uiinit.subtab.add({
				title : '从代码值',
				content : content
			});
		}
		function showYsgz() {
			var content = '<iframe scrolling="auto" frameborder="0"  src="/dataTemplate/words/grid4.html" style="width:100%;height:100%;"></iframe>';

			parent.uiinit.subtab.add({
				title : '映射规则',
				content : content
			});
		}
		function save() {
			var anEnum = {};
			anEnum.id = "${master.id}";
			anEnum.name = $('#mastername').val();
			anEnum.dataSource = $('#dataSource').val();
			anEnum.version = $('#version').val();
			anEnum.isStandard = $('#isStandard').val();
			anEnum.status = $('#status').val();
			anEnum.remark = $('#remark').val();
			enumManager.saveEnum(anEnum, function(result) {
				if (result) {
					alert("保存成功");
				}
			});
		}
	</script>
</body>
</html>