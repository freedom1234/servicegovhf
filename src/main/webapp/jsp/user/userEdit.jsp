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
    <legend>新增用户</legend>
    <table width="99%" border="0" cellspacing="0" cellpadding="0" id="userEdit">
        <tr>
            <th>用户代码</th>
            <td><input name="userId" class="easyui-textbox" type="text" id="userId" value="${user.id}"
                       disabled='disabled'/><font color="#FF0000">*</font></td>
            <th>用户名称</th>
            <td><input name="userName" class="easyui-textbox" type="text" id="userName" value="${user.name}" data-options="required:true,validType:'chineseB'"/><font
                    color="#FF0000">*</font></td>
            <th>密 码</th>
            <td><input name="password" class="easyui-textbox" type="password" id="password" value="${user.password}"
                       disabled='disabled'/><font color="#FF0000">*</font></td>
        </tr>
        <tr>
            <th>手机号码</th>
            <td><input name="userMobile" class="easyui-textbox" type="text" id="userMobile" value="${user.userMobile}" data-options="validType:'mobile'"/>
            </td>
            <th>电话号码</th>
            <td><input name="userTel" class="easyui-textbox" type="text" id="userTel" value="${user.userTel}" data-options="validType:'phone'"/></td>
            <th>所属机构</th>
            <td><select class="easyui-combobox" style="width:173px;" name="select" id="orgEdit" data-options="required:true">
                <option value="${user.orgId}"></option>
            </select><font color="#FF0000">*</font></td>
        </tr>
        <tr style="display: none">
            <th>生效日期</th>
            <td><input name="startdate" type="text" class="easyui-datebox" id="startdate" value="${user.startdate}"/>
            </td>
            <th>失效日期</th>
            <td><input name="lastdate" class="easyui-datebox" id="lastdate" value="${user.lastdate}"/></td>
        </tr>
        <tr>
            <th>备 注</th>
            <td><input name="remark" class="easyui-textbox" type="text" id="remark" value="${user.remark}"/></td>
        </tr>
    </table>
</fieldset>
<div style="margin-top:10px;"></div>
<table id="roleEdit" style="height:auto; width:auto;"
       title="">
    <thead>
    <tr>
        <th data-options="field:'roleIds',checkbox:true" name="rolecheckbox"></th>
        <th field="id" width="100" editor="text"
            data-options="hidden:true">ID
        </th>
        <th field="name" width="150px" align="center" editor="text">角色名</th>
    </tr>
    </thead>
</table>
<div align="center"><a class="easyui-linkbutton" id="saveBtn">保存</a>&nbsp;&nbsp;<a class="easyui-linkbutton" id="close"
                                                                                   onclick="$('#w').window('close');">关闭</a>
</div>
<script type="text/javascript">
    var strs = "${roleId}";
    var orgI = "${user.orgId}";
    var userI = "${user.id}";
    var pasW = "${user.password}";
    $(function () {
        $('#roleEdit').datagrid({
            rownumbers: true,
            singleSelect: false,
            url: '/role/getAll',
            method: 'get',
            pagination: true,
            pageSize: 10,
            onLoadSuccess: function (data) {
                if (data) {
                    $.each(data.rows, function (index, item) {
                        var str = strs.split(",");
                        for (var i = 0; i < str.length; i++) {
                            if (item.id == str[i]) {
                                $('#roleEdit').datagrid('checkRow', index);
                            }
                        }
                    });
                }
            },
            onLoadError: function (responce) {
                var resText = responce.responseText;
                if(resText.toString().indexOf("没有操作权限") > 0){
//                    alert("没有权限！");
                    window.location.href = "/jsp/403.jsp";
                }
            }
        });

        $('#orgEdit').combobox({
            url: '/org/getAll',
            method: 'get',
            mode: 'remote',
            editable:false,
            valueField: 'orgId',
            textField: 'orgName',
            onLoadSuccess: function () {
                if (orgI != '') {
                    $('#orgEdit').combobox('select', orgI);
                }
            }
        });
    });
    $('#saveBtn').click(function () {
        if(!$("#userEdit").form('validate')){
            return false;
        }
    	var name=$('#userName').val();
    	var orgEdit=$('#orgEdit').combobox('getValue');
    	var str1 = $('#startdate').datebox('getValue');
        var str2 = $('#lastdate').datebox('getValue');
        var startdate =new Date(str1);
		var lastdate = new Date(str2);
        var data = {};
        var rows = $("#roleEdit").datagrid("getSelections");
        if(name==null || name == ''){
			alert("请填写用户名称");
			return;
		}
		if(orgEdit==null || orgEdit == ''){
			alert("请填选择所属机构");
			return;
		}
		if(rows==null || rows == ''){
			alert("给用户至少选择一个角色");
			return;
		}
		 if(Date.parse(startdate)>Date.parse(lastdate)){
        alert("生效时间不能晚于失效时间！");
        return false;
        }
         if(Date.parse(startdate)==Date.parse(lastdate)){
        alert("生效时间和失效时间不能是同一天！");
        return false;
        }
        data.id = userI;
        data.name = name;
        data.password = pasW;
        data.userMobile = $('#userMobile').val();
        data.userTel = $('#userTel').val();
        data.startdate = $('#startdate').datebox('getValue');
        data.lastdate = $('#lastdate').datebox('getValue');
        data.orgId = orgEdit;
        data.remark = $('#remark').val();
        var roles = [];
        for (var i = 0; i < rows.length; i++) {
            var userRoleRelation = {};
            userRoleRelation.roleId = rows[i].id;
            userRoleRelation.userId = $('#userId').val();
            roles.push(userRoleRelation);
        }
//			data.userRoleRelations = roles;
        userManager.modify(data, function (result) {
            if (result) {
                userManager.assignRoles(roles, function (result) {
                    if (result) {
                        alert("保存成功");
                        $('#tt').datagrid('reload');
                    } else {
                        alert("保存失败");
                    }
                });
            } else {
                alert("保存失败");
            }
        });
        $('#w').window('close');
    });

</script>
</body>
</html>
