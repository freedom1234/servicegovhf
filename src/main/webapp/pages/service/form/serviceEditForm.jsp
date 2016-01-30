<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript">

</script>
<form class="formui">
<table border="0" cellspacing="0" cellpadding="0">
  <tr>
    <th>服务码</th>
    <td><input class="easyui-textbox" type="text" id="serviceId" disabled="disabled"></td>
  </tr>
  <tr>
    <th>服务名称</th>
    <td><input class="easyui-textbox" type="text" id="serviceName" data-options="required:true, validType:['chineseB']"></td>
  </tr>
  <tr>
    <th>服务功能描述</th>
    <td><input class="easyui-textbox" type="text" id="discription" data-options="validType:['chineseB']"></td>
  </tr>
  <tr>
    <th>服务分类</th>
    <td><input class="easyui-textbox" type="text" id="serviceCategory" disabled="disabled"></td>
  </tr>
  <tr>
    <th>服务备注</th>
    <td><input class="easyui-textbox" type="text" id="remark"></td>
  </tr>
<tr style="display:none">
    <th>version</th>
    <td><input class="easyui-textbox" type="text" id="version" ></td>
  </tr>
  <tr style="display: none">
    <th>state</th>
    <td><input class="easyui-textbox" type="text" id="state" ></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td class="win-bbar">
		<a href="#" class="easyui-linkbutton"  iconCls="icon-cancel" onClick="$('#w').window('close')">取消</a>
		<a id="serviceEditBtn" href="#" class="easyui-linkbutton" iconCls="icon-save">保存</a>
	</td>
  </tr>
</table>
</form>
<script type="text/javascript" src="/assets/service/js/serviceEditForm.js"></script>
<script type="text/javascript" src="/plugin/validate.js"></script>
