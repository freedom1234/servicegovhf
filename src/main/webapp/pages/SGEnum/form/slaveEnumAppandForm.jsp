<%@ page contentType="text/html; charset=utf-8" language="java"%>
<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>

<form class="formui">
	<table border="0" cellspacing="0" cellpadding="0">

		<tr>
			<th>代码名称</th>
			<td><input class="easyui-textbox" type="text" id="_enumName">
			</td>
		</tr>
		<tr>
			<th>是否标准代码</th>
			<td><select id="_isStandard" editable="false"
				panelHeight="auto" style="width:172px">
			</select></td>
		</tr>
		<tr>
			<th>是否主代码</th>
			<td><select id="_isMaster" readonly="true" editable="false"
				panelHeight="auto" style="width:172px">
			</select></td>
		</tr>
		<tr>
			<th>数据来源</th>
			<td><input class="easyui-textbox" type="text" id="_dataSource">
			</td>
		</tr>
		<tr style="display: none">
			<th>代码版本</th>
			<td><input class="easyui-textbox" type="text" id="_version">
			</td>
		</tr>
		<tr>
			<th>备注</th>
			<td><input class="easyui-textbox" type="text" id="_remark">
			</td>
		</tr>
		<tr>
			<th>代码状态</th>
			<td><select id="_status" editable="false"
				panelHeight="auto" style="width:172px">
			</select></td>
		</tr>
		<%--<tr>
			<th>操作人</th>
			<td><input class="easyui-textbox" type="text" id="_optUser">
			</td>
		</tr>--%>
		<tr style="display: none">
			<th>任务id</th>
			<td><input class="easyui-textbox" type="text" readonly="true" name="processId" id="taskIdInput"></td>
		</tr>
		<!-- <tr>
			<th>操作时间</th>
			<td><input class="easyui-textbox" type="text" id="_optDate">
			</td>
		</tr> -->
		<tr>
			<td>&nbsp;</td>
			<td class="win-bbar"><a href="#" class="easyui-linkbutton"
				iconCls="icon-cancel" onClick="$('#w').window('close')">取消</a><a
				id="saveBtn" href="#" class="easyui-linkbutton"
				iconCls="icon-save">保存</a>
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript"
	src="/assets/enumManager/js/enumManager.js"></script>
<script>
	$('#_isStandard').combobox({
		valueField: 'value',
		textField: 'label',
		data: [{
			label: '是',
			value: '1',
			selected:true
		},{
			label: '否',
			value: '0'
		}]
	});
	$('#_isMaster').combobox({
		valueField: 'value',
		textField: 'label',
		data: [{
			label: '是',
			value: '1'
		},{
			label: '否',
			value: '0',
			selected:true
		}]
	});
	$('#_status').combobox({
		valueField: 'value',
		textField: 'label',
		data: [{
			label: '使用',
			value: '1',
			selected:true
		},{
			label: '退役',
			value: '0'
		}]
	});
	
	$('#saveBtn').click(function() {
		var anEnum = {};
		anEnum.name = $('#_enumName').val();
		anEnum.isStandard = $('#_isStandard').combobox('getValue');
		anEnum.isMaster = $('#_isMaster').combobox('getValue');
		anEnum.dataSource = $('#_dataSource').val();
		anEnum.version = $('#_version').val();
		anEnum.remark = $('#_remark').val();
		anEnum.status = $('#_status').combobox('getValue');
//		anEnum.optUser = $('#_optUser').val();
		anEnum.processId = $('#taskIdInput').textbox('getValue');
		//anEnum.optDate = $('#_optDate').val();
		enumManager.addSlaveEnum(anEnum,$('#id').val(),function(result){
			if(result){
				$('#slavedatagrid').datagrid('reload');
				$('#w').window('close');
			}
		});
	});
	if(processId){
		$('#taskIdInput').textbox('setValue',processId);
	}
	
</script>
