<%@ page contentType="text/html; charset=utf-8" language="java"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />


<body>
<form class="formui">
<table border="0" cellspacing="0" cellpadding="0"  style="font-size:12px">
  <tr  style="text-align:left">
    <th>提供方</th>
  </tr>
  <tr>
      <td>是否标准接口</td>
      <td>
        <select id="provider_isStandard" class="easyui-combobox"  panelHeight="auto" style="width: 155px"  data-options="editable:false,onSelect:exportManager.selectStandardProvider">
            <option value="1">否</option>
            <option value="0">是</option>

        </select>
      </td>
    </tr>
  <tr>
    <td>系统名称</td>
    <td>
         <select id="provider_systemId" class="easyui-combobox"   panelHeight="auto" style="width: 155px"  data-options="editable:false,valueField:'id',textField:'text',url: '/export/getSystem/${param.serviceId}/${param.operationId}/0'">
         </select>
    </td>
  </tr>
  <tr id="providerTr">
      <td>接口名称</td>
      <td>
        <select id="provider_interfaceId" class="easyui-combobox"  panelHeight="auto" style="width: 155px"  data-options="editable:false,valueField:'id',textField:'text',url: '/export/getInterface/${param.serviceId}/${param.operationId}/0'">
                 </select>
      </td>
  </tr>
  <tr id="providerprotocolTR" style="display:none">
        <td>协议</td>
        <td>
          <select id="provider_protocol" class="easyui-combobox"  panelHeight="auto" style="width: 155px"  data-options="editable:false">
              <option value="xml">XML</option>
              <option value="soap">SOAP</option>

          </select>
        </td>
      </tr>
  <tr style="text-align:left">
      <th>消费方</th>
  </tr>
   <tr>
        <td>是否标准接口</td>
        <td>
          <select id="consumer_isStandard" class="easyui-combobox"  panelHeight="auto" style="width: 155px"  data-options="editable:false,onSelect:exportManager.selectStandardConsumer">
              <option value="1">否</option>
              <option value="0">是</option>
          </select>
        </td>
      </tr>
  <tr>
    <td>系统名称</td>
    <td>
        <select id="consumer_systemId" class="easyui-combobox"  panelHeight="auto" style="width: 155px"  data-options="editable:false,valueField:'id',textField:'text',url: '/export/getSystem/${param.serviceId}/${param.operationId}/1'">
                         </select>
    </td>
  </tr>
  <tr id="consumerTr">
      <td>接口名称</td>
      <td>
        <select id="consumer_interfaceId" class="easyui-combobox"  panelHeight="auto" style="width: 155px"  data-options="editable:false,valueField:'id',textField:'text',url: '/export/getInterface/${param.serviceId}/${param.operationId}/1'">
                                 </select>
      </td>
  </tr>
  <tr id="consumerprotocolTR"  style="display:none">
      <td>协议</td>
      <td>
        <select id="consumer_protocol" class="easyui-combobox"  panelHeight="auto" style="width: 155px"  data-options="editable:false">
            <option value="xml">XML</option>
            <option value="soap">SOAP</option>

        </select>
      </td>
</tr>
  <tr>
    <td>&nbsp;</td>
    <td class="win-bbar">
		<a href="#" class="easyui-linkbutton"  iconCls="icon-cancel" onClick="$('#w').window('close')">取消</a>
		<a id="export_btn" onClick = "exportManager.exportHandle('${param.serviceId}','${param.operationId}');" href="#" class="easyui-linkbutton" iconCls="icon-save">保存</a>
	</td>
  </tr>
</table>
</form>
</body>

