<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>列表页</title>
</head>

<body>
<form class="easyui-form" id="userForm">
<fieldset>
    <legend></legend>
    <table width="99%" border="0" cellspacing="0" cellpadding="0" id="userEdit">
        <tr>
            <th>用户代码</th>
            <td><input name="userId" class="easyui-textbox" type="text" id="userId" value="${user.id}"
                       disabled='disabled'/><font color="#FF0000">*</font></td>
        </tr>
        <tr>
            <th>用户名称</th>
            <td><input name="userName" class="easyui-textbox" type="text" id="userName" value="${user.name}" data-options="required:true,validType:'chineseB'"/><font
                    color="#FF0000">*</font></td>
        </tr>
        <tr>
            <th>手机号码</th>
            <td><input name="userMobile" class="easyui-textbox" type="text" id="userMobile" value="${user.userMobile}" data-options="validType:'mobile'"/>
            </td>
        </tr>
        <tr>
            <th>电话号码</th>
            <td><input name="userTel" class="easyui-textbox" type="text" id="userTel" value="${user.userTel}" data-options="validType:'phone'"/></td>
        </tr>
        <tr>
            <th>所属机构</th>
            <td><input class="easyui-combobox" style="width:173px;" name="select" id="orgId" value="${user.orgId}" data-options="required:true,
            url: '/org/getAll',
             method: 'get',
            mode: 'remote',
            editable:false,
             valueField: 'orgId',
            textField: 'orgName'
            " />
            <font color="#FF0000">*</font></td>
        </tr>
        <tr style="display: none">
            <th>生效日期</th>
            <td><input name="startdate" type="text" class="easyui-datebox" id="startdate" value="${user.startdate}"/>
            </td>
        </tr>
        <tr>
            <th>失效日期</th>
            <td><input name="lastdate" class="easyui-datebox" id="lastdate" value="${user.lastdate}"/></td>
        </tr>
        <tr>
            <th>备 注</th>
            <td><input name="remark" class="easyui-textbox" type="text" id="remark" value="${user.remark}"/></td>
        </tr>
        <tr>
            <th>密 码</th>
            <td><input name="password" class="easyui-textbox" type="password" id="password" value="${user.password}" data-options="disabled:true, required:true" /><a href="javascript:void(0);" onclick="changePW()">修改密码</a></td>
        </tr>
        <tr>
            <th>密码确认</th>
            <td><input name="password" class="easyui-textbox" type="password" id="password2" value="${user.password}"  data-options="disabled:true, required:true"  /></td>
        </tr>
    </table>
</fieldset>
<div style="margin-top:10px;"></div>
<div align="center"><a class="easyui-linkbutton" id="saveBtn" onclick="saveUserInfo()">保存</a>&nbsp;&nbsp;<a class="easyui-linkbutton" id="close"
                                                                                   onclick="$('#userDlg').window('close');">关闭</a>
</div>
</form>
<script type="text/javascript">
    function changePW(){
        $("#password").textbox({disabled:false});
        $("#password2").textbox({disabled:false});
    }

    function saveUserInfo(){
        if(!$("#userForm").form("validate")){
            alert("请完善数据！");
            return false;
        }
        if($("#password").val() == $("#password2").val()){
            var SGUser={};
            SGUser.id = $("#userId").textbox("getValue");
            SGUser.name = $("#userName").textbox("getValue");
            SGUser.userMobile = $("#userMobile").textbox("getValue");
            SGUser.userTel = $("#userTel").textbox("getValue");
            SGUser.orgId = $("#orgId").combobox("getValue");
            SGUser.lastdate = $("#lastdate").datebox("getValue");
            SGUser.remark = $("#remark").textbox("getValue");
            SGUser.password = $("#password").val();
            $.ajax({
                type: "post",
                async: false,
                contentType: "application/json; charset=utf-8",
                url: "/user/modify2",
                dataType: "json",
                data: JSON.stringify(SGUser),
                success: function (data) {
                    $("#userDlg").dialog("close");
                    alert("保存成功！");
                }
            });
        }
        else{
            alert("两次输入的密码不一致！");
            return false;
        }

    }

</script>

</body>
</html>
