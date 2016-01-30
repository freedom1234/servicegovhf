<%@ page contentType="text/html; charset=utf-8" language="java"%>
<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<form class="formui" id="form1">
	<table border="0" cellspacing="0" cellpadding="0">

		<tr>
			<th>代码名称</th>
			<td><input class="easyui-textbox" type="text" id="_enumName" data-options="required:true, validType:['unique','englishB']">
			</td>
		</tr>
		<tr>
			<th>中文名称</th>
			<td><input class="easyui-textbox" type="text" id="_remark" data-options="required:true, validType:['uniqueChineseName','chineseB']">
			</td>
		</tr>
		<tr>
			<th>是否标准代码</th>
			<td><select id="_isStandard" editable="false"
				panelHeight="auto" style="width:172px">
			</select>
			</td>
		</tr>
		<tr>
			<th>是否主代码</th>
			<td><select class="easyui-combobox" id="_isMaster" editable="false" readonly="true"
				panelHeight="auto" style="width:172px">
					<option value="1">是</option>
					<option value="0">否</option>
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
			<th>代码状态</th>
			<td><select id="_status" editable="false" panelHeight="auto" style="width:172px">
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
				iconCls="icon-cancel" id="cancelBtn">取消</a><a
				id="saveBtn" href="#" class="easyui-linkbutton"
				iconCls="icon-save">保存</a>
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript"
	src="/assets/enumManager/js/enumManager.js"></script>
<script type="text/javascript" src="/plugin/validate.js"></script>
<script type="text/javascript">
	$.extend($.fn.validatebox.defaults.rules, {
		unique: {
			validator: function (value, param) {
				var result;
				$.ajax({
					type: "get",
					async: false,
					url: "/enum/uniqueValid",
					dataType: "json",
					data: {"name": value},
					success: function (data) {
						result = data;
					}
				});
				return result;
			},
			message: '已存在相同代码名称'
		},
		uniqueChineseName: {
			validator: function (value, param) {
				var result;
				$.ajax({
					type: "get",
					async: false,
					url: "/enum/uniqueChineseName",
					dataType: "json",
					data: {"remark": encodeURI(encodeURI(value))},
					success: function (data) {
						result = data;
					}
				});
				return result;
			},
			message: '已存在相同中文名称'
		}
	});

	$('#cancelBtn').click(function(){
		$('#w').window('close');
	});
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
			value: '1',
			selected:true
		},{
			label: '否',
			value: '0'
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
		//数据校验
		if(!$("#form1").form('validate')){
			alert("请正确输入必输项");
			return false;
		}
		var anEnum = {};
		anEnum.name = $('#_enumName').textbox('getValue');
		anEnum.isStandard = $('#_isStandard').combobox('getValue');
		anEnum.isMaster = $('#_isMaster').combobox('getValue');
		anEnum.dataSource = $('#_dataSource').val();
		anEnum.version = $('#_version').val();
		anEnum.remark = $('#_remark').textbox('getValue');
		anEnum.status = $('#_status').combobox('getValue');
//		anEnum.optUser = $('#_optUser').val();
		anEnum.processId = $('#taskIdInput').textbox('getValue');
		//anEnum.optDate = $('#_optDate').val();
		enumManager.addEnum(anEnum,function (result){
			if(result){
				$('#dg').datagrid('reload');
				$('#w').window('close');
			}
		});
	});
	if(processId){
		$('#taskIdInput').textbox('setValue',processId);
	}
</script>
