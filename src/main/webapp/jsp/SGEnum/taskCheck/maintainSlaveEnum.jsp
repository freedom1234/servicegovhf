<%@ page contentType="text/html; charset=utf-8" language="java"%>
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
		<legend>从代码信息</legend>
		<table border="0" cellspacing="0" cellpadding="0">
			<tr style="display:none">
				<th>代码id</th>
				<td><input class="easyui-textbox" type="text" name="name" id="id"
					value="${master.id}">
				</td>
			</tr>
			<tr>
				<th>代码名称</th>
				<td><input class="easyui-textbox" type="text" name="name" id="name"
					value="${master.name}">
				</td>
				
				<th>是否标准代码</th>
				<td><select id="isStandard" 
					panelHeight="auto" style="width:140px">
				</select>
				</td>
				
				<th>代码状态</th>
				<td><select id="status" panelHeight="auto" style="width:140px">
				</select>
				</td>
			</tr>
			<tr>
				<th>数据来源</th>
				<td><input class="easyui-textbox" type="text" id="dataSource"
					value="${master.dataSource}">
				</td>
				
				<th>代码版本</th>
				<td><input class="easyui-textbox" type="text" id="version"
					value="${master.version}">
				</td>
				
				<th>备注</th>
				<td><input class="easyui-textbox" type="text" id="remark"
					value="${master.remark}">
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>
					<a href="#" onclick="save()" class="easyui-linkbutton" iconCls="icon-save">保存</a>
				</td>
			</tr>
		</table>


	</fieldset>
	<table title="枚举列表" id="elementdatagrid" style="height:330px; width:auto;">
		<thead>
			<tr>
				<th data-options="field:'productid',checkbox:true"></th>
				<th data-options="field:'elementName'">枚举名称</th>
				<th data-options="field:'bussDefine'">枚举定义</th>
				<th data-options="field:'remark'">枚举备注</th>
				<th data-options="field:'optUser'">修订人</th>
				<th data-options="field:'optDate'">修订时间</th>
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
		$(function(){
			$('#elementdatagrid').datagrid({
				rownumbers:true,
				singleSelect:false,
				collapsible:true,
				url:'/enum/getElementByMasterId/'+"${master.id}",
				method:'get',
				toolbar:toolbar2,
				pagination:true,
				pageSize:10
			});
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
		$('#status').combobox({
			valueField: 'value',
			textField: 'label',
			data: [{
				label: '使用',
				value: '1',
				selected : "${master.status}" =="1"
			},{
				label: '退役',
				value: '0',
				selected : "${master.status}" =="0"
			}]
		});
		var toolbar2 =[{
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
		},{
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
		}];
		function showZdm() {
			var content = '<iframe scrolling="auto" frameborder="0"  src="/jsp/SGEnum/masterEnum.jsp" style="width:100%;height:100%;"></iframe>';

			parent.uiinit.subtab.add({
				title : '主代码值',
				content : content
			});
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
			anEnum.isMaster = "${master.isMaster}";
			anEnum.optDate = "${master.optDate}";
			anEnum.optUser = "${master.optUser}";
			
			anEnum.name = $('#name').val();
			anEnum.dataSource = $('#dataSource').val();
			anEnum.version = $('#version').val();
			anEnum.isStandard = $('#isStandard').combobox('getValue');
			anEnum.status = $('#status').combobox('getValue');
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