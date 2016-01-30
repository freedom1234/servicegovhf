<%--
  Created by IntelliJ IDEA.
  User: vincentfxz
  Date: 15/9/14
  Time: 14:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String processId = request.getParameter("processId");
%>
<html>
<head>
    <title>任务详细</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>列表页</title>
    <link rel="stylesheet" type="text/css" href="/resources/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="/resources/themes/icon.css">
    <link href="/resources/css/ui.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/resources/js/jquery.min.js"></script>
    <script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="/resources/js/ui.js"></script>
    <script type="text/javascript" src="/assets/task/taskManager.js"></script>

</head>
<body>
<table id="taskInfoTable" style="height:580px; width:auto;">
    <thead>
    <tr>
        <th data-options="field:'processId'">流程号</th>
        <th data-options="field:'name'">环节名称</th>
        <th data-options="field:'key'" formatter='formatter.taskFormatter'>操作对象</th>
        <%--<th data-options="field:'value'">结果</th>--%>
        <th data-options="field:'remark'">备注</th>
        <th data-options="field:'optUser'">操作用户</th>
        <th data-options="field:'optDate'">操作时间</th>
    </tr>
    </thead>
</table>
</body>
<script type="text/javascript">
    var formatter = {
        taskFormatter: function (value, row, index) {
            if (value == "interface") {
                return "接口";
            }
            if (value == "file") {
                return "需求文件";
            }
            if (value == "system") {
                return "系统";
            }
            if (value == "service") {
                return "服务";
            }
            if (value == "operation") {
                return "场景";
            }
        }
    };

    $(function () {
        var processId = "<%=processId%>";
        if (processId) {
            var url = "/process/getContext/" + processId;
            $("#taskInfoTable").datagrid({
                rownumbers: true,
                border: false,
                singleSelect: false,
                url: url,
                method: 'get',
                pagination: true,
                pageSize: 10,
                pageList: [10, 20, 50]
            });
        }
    });
</script>
</html>
