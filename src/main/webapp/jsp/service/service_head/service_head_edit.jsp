<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
  String path = request.getContextPath();
  String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
<head>
  <title></title>
</head>
<body>

<form class="formui" id="serviceHeadForm" method="post" action="/serviceHead/add">
  <input type="hidden" name="headId" value="${entity.headId}">
  <table border="0" cellspacing="0" cellpadding="0">
    <tr>
      <th>报文头名称</th>
      <td><input class="easyui-textbox" type="text" name="headName" id="headName"
                 value="${entity.headName}"
                 data-options="required:true"></td>
    </tr>
    <tr>
      <th>类型</th>
      <td>
        <input
                id = "type"
                name = "type"
                value="${entity.type}"
                class="easyui-combobox"
                data-options="valueField: 'value',textField: 'label',
						data: [{label: 'sys_head',value: 'sys_head'},
						{label: 'app_head',value: 'app_head'}
							]"
                />
      </td>
    </tr>
    <tr>
      <th>描述</th>
      <td><input class="easyui-textbox" type="text" id="headDesc" name="headDesc" value="${entity.headDesc}"></td>
    </tr>
    <tr>
      <th>备注</th>
      <td><input class="easyui-textbox" type="text" id="headRemark" name="headRemark" value="${entity.headRemark}"></td>
    </tr>
    <tr>
      <td colspan="2" style="text-align:center">
        <a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onClick="$('#dlg').dialog('close')">取消</a>
        <a href="#" onclick="save()" class="easyui-linkbutton" iconCls="icon-save">保存</a>
      </td>
    </tr>
  </table>
</form>
<script type="text/javascript">
  function save(){
    if($('#serviceHeadForm').form('validate')){
      var params = $("#serviceHeadForm").serialize();
      params = decodeURIComponent(params, true);
      $.ajax({
        type: "post",
        async: false,
        url: '/serviceHead/add',
        dataType: "json",
        data: params,
        success: function (data) {

          alert('保存成功 ！');
          $('#dlg').dialog('close');
          $("#tg").datagrid('reload');
        }
      });
    }else{
      return false;
    }
  }
</script>
</body>
</html>
