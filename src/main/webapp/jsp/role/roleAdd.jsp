<%@ page contentType="text/html; charset=utf-8" language="java"
         import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>列表页</title>
</head>

<body>
<fieldset>
    <table width="99%" border="0" cellspacing="0" cellpadding="0" id="roleTable">
        <tr>
            <th>角色代码</th>
            <td><input name="roleId" class="easyui-validatebox" type="text" id="roleId" data-options="required:true,validType:'englishB'" /><font color="#FF0000">*</font></td>
        </tr>
        <tr>
            <th>角色名称</th>
            <td><input name="roleName" class="easyui-textbox" type="text" id="roleName" data-options="required:true,validType:'chineseB'"/><font color="#FF0000">*</font>
            </td>
        </tr>
        <tr>
            <th>角色描述</th>
            <td><input name="remark" class="easyui-textbox" type="text" id="roleRemark" data-options="validType:'chineseB'"/></td>
        </tr>

    </table>
</fieldset>
<div style="margin-top:10px;"></div>

<div align="center"><a class="easyui-linkbutton" id="saveBtn">保存</a>&nbsp;&nbsp;<a class="easyui-linkbutton" id="close"
                                                                                   onclick="$('#w').window('close');">关闭</a>
</div>
<script type="text/javascript" src="/js/role/roleManager.js"></script>
<script type="text/javascript" src="/plugin/validate.js"></script>
<script type="text/javascript">
    $('#saveBtn').click(function () {
        if(!$("#roleTable").form('validate')){
            return false;
        }
    var isValid = $("#roleId").validatebox("isValid");
        if(isValid){
        var data = {};
        data.id = $('#roleId').val();
        data.name = $('#roleName').val();
        data.remark = $('#roleRemark').val();
        var checkUniqueCallBack = function checkUniqueCallBack(result) {
            if (result) {
                roleManager.add(data, function (result) {
                    if (result) {
                        alert("保存成功");
                        $('#tt').datagrid('reload');
                    } else {
                        alert("保存失败");
                    }
                });
                $('#w').window('close');
            } else {
                alert("该角色已经存在，放弃新增，或者选择修改");
            }
        };
            var checkUniqueCallBack2 = function checkUniqueCallBack2(result) {
                if (result) {
                    roleManager.checkUnique(data.id, checkUniqueCallBack);
                } else {
                    alert("该角色名已经存在，放弃新增，或者选择修改");
                }
            };
            roleManager.checkRoleNameUnique(data.name, checkUniqueCallBack2);

        }else{
            alert("输入错误！");
        }
    });
</script>
</body>
</html>
