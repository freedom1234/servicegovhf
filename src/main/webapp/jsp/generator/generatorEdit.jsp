<%@ page contentType="text/html; charset=utf-8" language="java"
         import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
  <title>生成类修改页</title>
</head>

<body>

<table width="99%" border="0" cellspacing="0" cellpadding="0" id="generatorInfo">
  <input name="id" type="hidden" id="id" value="${generator.id}"/>
  <tr>
    <th>生成类名</th>
    <td><input name="name" class="easyui-textbox" type="text" id="name" data-options="required:true" value="${generator.name}"/></td>
  </tr>
  <tr>
    <th>描述</th>
    <td><input name="desc" class="easyui-textbox" type="text" id="desc" value="${generator.desc}"/>
    </td>
  </tr>
  <tr>
    <th>类路径</th>
    <td><input name="implements" class="easyui-textbox" type="text" id="implements" value="${generator.implementsClazz}"/>
    </td>
  </tr>
</table>
<div style="margin-top:10px;"></div>

<div align="center"><a class="easyui-linkbutton" id="saveBtn">保存</a>&nbsp;&nbsp;<a class="easyui-linkbutton" id="close"
                                                                                   onclick="$('#w').window('close');">关闭</a>
</div>

<script type="text/javascript" src="/plugin/validate.js"></script>

<script type="text/javascript">
  $(function () {
    $('#saveBtn').click(function () {

      var name = $('#name').val();
      var data = {};
      if (name == null || name == '') {
        alert("请填写生成类名称");
        return;
      }
      data.id=$('#id').val();
      data.name = name;
      data.desc = $('#desc').val();
      data.implementsClazz = $('#implements').val();

      var checkExitCallBack = function checkExitCallBack(result) {
        if (result) {
          generatorManager.modify(data, function (result) {
            if (result) {
              if (result) {
                alert("保存成功");
                $('#tt').datagrid('reload');
              } else {
                alert("保存失败");
              }
            } else {
              alert("保存失败");
            }
          });
          $('#w').window('close');
        } else {
          alert("该生成类不存在，放弃新增，或者选择修改");
        }
      }
      generatorManager.checkExit(data.implementsClazz, checkExitCallBack);
    });
  });


</script>
</body>
</html>
