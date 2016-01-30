<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
    <base href="<%=basePath%>">
    <title></title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">

</head>

<body>
<form class="formui" id="metadataForm" action="/metadata/add" method="post">
    <table border="0" cellspacing="0" cellpadding="0">
        <tr>
            <th>任务名称</th>
            <td><input class="easyui-textbox" type="text" name="metadataName"></td>
        </tr>
        <tr>
            <th>任务类型</th>
            <td><input type="text" name="taskType" id="taskType"
                       class="easyui-combobox"
                       data-options="
						url:'taskType.json',
				 		 method:'get',
				 		 valueField: 'id',
				 		 textField: 'name',
				 		 onChange:function(newValue, oldValue){
							this.value=newValue;
						}
					"
                    /></td>
        </tr>
        <tr>
            <th>处理人</th>
            <td><input style="width:173px;" name="user" id="user" ></td>
        </tr>
        <tr>
            <th>任务描述</th>
            <td><input class="easyui-textbox" type="text" style="width: 100%" id="description"></td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td class="win-bbar">
                <a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onClick="$('#w').window('close')">取消</a>
                <a href="#" id="createTaskBtn" class="easyui-linkbutton" iconCls="icon-save">保存</a>
            </td>
        </tr>
    </table>
</form>
    <script type="text/javascript" src="/assets/task/addTask.js"></script>
</body>
</html>