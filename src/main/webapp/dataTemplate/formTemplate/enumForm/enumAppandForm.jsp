<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>

<form class="formui">
<table border="0" cellspacing="0" cellpadding="0">

  <tr>
    <th>代码名称</th>
    <td><input class="easyui-textbox" type="text" id="enumName" ></td>
  </tr>
  <tr>
    <th>是否标准代码</th>
    <td>
    	<select class="easyui-combobox" id="isStandard" panelHeight="auto" style="width:172px">
			<option value="1">是</option>
			<option value="0">否</option>
		</select>
	</td>
  </tr>
  <tr>
    <th>是否主代码</th>
    <td>
    	<select class="easyui-combobox" id="isMaster" panelHeight="auto" style="width:172px">
			<option value="1">是</option>
			<option value="0">否</option>
		</select>
  </tr>
<tr>
    <th>数据来源</th>
    <td><input class="easyui-textbox" type="text" id="dataSource" ></td>
  </tr>
  <tr>
    <th>代码状态</th>
    <td><input class="easyui-textbox" type="text" id="status" ></td>
  </tr>
  <tr>
    <th>代码版本</th>
    <td><input class="easyui-textbox" type="text" id="version" ></td>
  </tr>
  <tr>
    <th>备注</th>
    <td><input class="easyui-textbox" type="text" id="remark" ></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td class="win-bbar"><a href="#" class="easyui-linkbutton"  iconCls="icon-cancel" onClick="$('#w').window('close')">取消</a><a id="saveBtn" href="#" class="easyui-linkbutton" onClick="save()" iconCls="icon-save">保存</a></td>
  </tr>
</table>
</form>
<script type="text/javascript" src="/assets/enumManager/js/enumManager.js"></script>
<script>
function save(){
	var anEnum={};
	anEnum.name = $('#enumName').val();
	anEnum.isStandard = $('#isStandard').val();
	anEnum.isMaster = $('#isMaster').val();
	anEnum.dataSource = $('#dataSource').val();
	anEnum.status = $('#status').val();
	anEnum.version = $('#version').val();
	anEnum.remark = $('#remark').val();
	enumManager.addEnum(anEnum);
}
</script>
