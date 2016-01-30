<%@ page contentType="text/html; charset=utf-8" language="java"%>
<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>

<form class="formui">
	<table border="0" cellspacing="0" cellpadding="0">

		<tr>
			<th>枚举名称</th>
			<td><input class="easyui-textbox" type="text" id="_elementName">
			</td>
		</tr>
		<tr>
			<th>业务定义</th>
			<td><input class="easyui-textbox" type="text" id="_bussDefine">
			</td>
		</tr>
		<tr>
			<th>中文名称</th>
			<td><input class="easyui-textbox" type="text" id="_remark">
			</td>
		</tr>
		<tr>
			<th>备注</th>
			<td><input class="easyui-textbox" type="text" id="_remark">
			</td>
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
	$('#saveBtn').click(function() {
		var element = {};
		element.elementName = $('#_elementName').val();
		element.remark = $('#_remark').val();
		element.bussDefine = $('#_bussDefine').val();
//		element.optUser = $('#_optUser').val();
		//element.optDate = $('#_optDate').val();
		element.enumId = $('#id').val();
		element.processId = $('#taskIdInput').textbox('getValue');
		enumManager.addElement(element);
	});
	if(processId){
		$('#taskIdInput').textbox('setValue',processId);
	}
</script>
