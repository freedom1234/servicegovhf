<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
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
    <script type="text/javascript">
        var userID = "";
        $(function () {
            userID = $("#userId").text();
            var url = '/process/' + userID + '/list';
            $("#taskTable").datagrid({
                rownumbers: true,
                border: false,
                toolbar: toolbar,
                singleSelect: false,
                url: url,
                method: 'get',
                pagination: true,
                pageSize: 10,
                pageList: [10, 20, 50]
            });
        })
        var taskFormatter = {
            "formatStatus": function (data, row) {
                if (data) {
                    if (data == "Reserved") {
                        return "待分派";
                    }
                    if (data == "Ready") {
                        return "已分派";
                    }
                    if (data == "InProgress") {
                        return "处理中";
                    }
                }
            },
            "formatPriority": function (data, row) {
                if (data != undefined) {
                    if ("0" == data) {
                        return "低";
                    }
                }
            },
            "formatCreateBy": function (data, row) {
                if (row) {
                    if (row.createdBy)
                        return row.createdBy.id;
                }
            },
            "formatActualOwner": function (data, row) {
                if (data) {
                    return data.id;
                }
            }
        }
    </script>
</head>

<body>
<div id="userId" style="display: none"><shiro:principal/></div>
<div class="easyui-tabs" id="taskTabs" style="width:100%;height:auto">
    <div title="未完成任务" style="padding:0px">
        <table id="taskTable" style="height:530px; width:auto;">
            <thead>
            <tr>
                <th data-options="field:'productid',checkbox:true"></th>
                <th data-options="field:'id'">任务ID</th>
                <th data-options="field:'name'">任务名称</th>
                <th data-options="field:'subject'">主题</th>
                <th data-options="field:'description'">描述</th>
                <th data-options="field:'status'" formatter="taskFormatter.formatStatus">状态</th>
                <th data-options="field:'priority'" formatter="taskFormatter.formatPriority">优先级</th>
                <th data-options="field:'createBy'" formatter="taskFormatter.formatCreateBy">创建人</th>
                <th data-options="field:'actualOwner'" formatter="taskFormatter.formatActualOwner">受理人</th>
            </tr>
            </thead>
        </table>
    </div>
    <%--<div title="任务详细" style="padding:0px">--%>
        <%--<table id="taskInfoTable" style="height:620px; width:auto;">--%>
            <%--<thead>--%>
            <%--<tr>--%>
                <%--<th data-options="field:'productid',checkbox:true"></th>--%>
                <%--<th data-options="field:'id'">任务ID</th>--%>
                <%--<th data-options="field:'name'">任务名称</th>--%>
                <%--<th data-options="field:'subject'">主题</th>--%>
                <%--<th data-options="field:'description'">描述</th>--%>
                <%--<th data-options="field:'status'" formatter="taskFormatter.formatStatus">状态</th>--%>
                <%--<th data-options="field:'priority'" formatter="taskFormatter.formatPriority">优先级</th>--%>
                <%--<th data-options="field:'createBy'" formatter="taskFormatter.formatCreateBy">创建人</th>--%>
                <%--<th data-options="field:'actualOwner'" formatter="taskFormatter.formatActualOwner">受理人</th>--%>
            <%--</tr>--%>
            <%--</thead>--%>
        <%--</table>--%>
    <%--</div>--%>

    <script type="text/javascript">
        var Global = {};
        var toolbar = [
            {
                text: '创建',
                iconCls: 'icon-edit',
                handler: function () {
                    uiinit.win({
                        w: 500,
                        iconCls: 'icon-add',
                        title: "创建任务",
                        url: "/jsp/task/addTask.jsp"
                    });
                }
            },
            {
                text: '分配',
                iconCls: 'icon-edit',
                handler: function () {
                    var checkedItems = $('#taskTable').datagrid('getChecked');
                    var checkedItem;
                    if (checkedItems != null && checkedItems.length > 0) {
                        if (checkedItems.length > 1) {
                            alert("请选择一个任务进行分配！");
                            return false;
                        }
                        else {
                            checkedItem = checkedItems[0];
                            Global.taskId = checkedItem.id;
                            uiinit.win({
                                w: 500,
                                iconCls: 'icon-edit',
                                title: "任务分配",
                                url: "/jsp/task/assignTask.jsp"
                            });
                        }
                    }
                    else {
                        alert("请选中要修改的数据！");
                    }
                }
            },
            {
                text: '处理',
                iconCls: 'icon-edit',
                handler: function () {
                    var checkedItems = $('#taskTable').datagrid('getChecked');
                    var checkedItem;
                    if (checkedItems != null && checkedItems.length > 0) {
                        if (checkedItems.length > 1) {
                            alert("请选择一个任务进行分配！");
                            return false;
                        }
                        else {
                            checkedItem = checkedItems[0];
                            Global.taskId = checkedItem.id;
                            Global.processInstanceId = checkedItem.processInstanceId;
                            Global.taskName = checkedItem.name;
                            var task = {};
                            task.processInstanceId = Global.processInstanceId;
                            task.taskId = Global.taskId;
                            task.userId = $("#userId").text();
                            task.name = Global.taskName;
                            task.name = task.name.replace(/(^\s*)|(\s$)/g, '');
                            parent.PROCESS_INFO.processId = checkedItem.processInstanceId;
                            parent.PROCESS_INFO.taskName = task.name;
                            parent.PROCESS_INFO.taskId = task.taskId;
                            parent.changeTaskName();
                            taskManager.processTask(task, function (result) {
                                if (task.name == '创建元数据') {
                                    $("#w").window("close");
                                    $('#taskTable').datagrid('reload');
                                    var content = '<iframe scrolling="auto" frameborder="0"  src="/process/metadataByTask/process/' + task.processInstanceId + '/task/' + task.taskId + '" style="width:100%;height:100%;"></iframe>';
                                    parent.addTab("创建元数据", content);
                                }
                                if (task.name == "元数据审核") {
                                    $("#w").window("close");
                                    $('#taskTable').datagrid('reload');
                                    var content = '<iframe scrolling="auto" frameborder="0"  src="/process/metadataAuditByTask/process/' + task.processInstanceId + '/task/' + task.taskId + '" style="width:100%;height:100%;"></iframe>';
                                    parent.addTab("创建元数据", content);
                                }
                                if (task.name == "服务定义") {
                                    $("#w").window("close");
                                    $('#taskTable').datagrid('reload');
                                    parent.SYSMENU.changeLeftMenu(4);
                                    alert("请在左侧服务目录菜单中新增服务。");
                                }
                                if (task.name == "创建公共代码") {
                                    var content = '<iframe scrolling="auto" frameborder="0"  src="/jsp/SGEnum/task/common.jsp?processId=' + task.processInstanceId + '&taskId=' + task.taskId + '" style="width:100%;height:100%;"></iframe>';
                                    parent.addTab("创建公共代码", content);
                                }
                                if (task.name == "公共代码审核") {
                                    $("#w").window("close");
                                    $('#taskTable').datagrid('reload');
                                    var content = '<iframe scrolling="auto" frameborder="0"  src="/process/sgenum/sgenumAuditByTask/process/' + task.processInstanceId + '/task/' + task.taskId + '" style="width:100%;height:100%;"></iframe>';
                                    parent.addTab("创建公共代码", content);
                                }
                                if (task.name == "接口需求上传") {
                                    $("#w").window("close");
                                    $('#taskTable').datagrid('reload');
                                    parent.SYSMENU.changeLeftMenu(6);
                                    var content = '<iframe scrolling="auto" frameborder="0"  src="/jsp/sysadmin/file_list.jsp" style="width:100%;height:100%;"></iframe>';
                                    parent.addTab("接口需求文件上传", content);
                                    alert("请上传接口需求文档。");
                                }
                                if (task.name == "接口定义") {
                                    $("#w").window("close");
                                    $('#taskTable').datagrid('reload');
//                                    parent.SYSMENU.changeLeftMenu(6);
                                    parent.SYSMENU.changeLeftMenuWithCallBack(6, function () {
                                        var callBack = function (result) {
                                            parent.SYSMENU.reloadTreeByValue('msinterfacetree', result);
                                        };
                                        taskManager.getSystemTreeByProcess(parent.PROCESS_INFO.processId, callBack);
                                    });
                                    alert("请在左侧系统菜单中右键新增接口。");
                                }
                                if (task.name == "服务审核") {
                                    $("#w").window("close");
                                    $('#taskTable').datagrid('reload');
                                    parent.SYSMENU.changeLeftMenuWithCallBack(4, function () {
                                        var callBack = function (result) {
                                            parent.SYSMENU.reloadTreeByValue('mxservicetree', result);
                                        };
                                        taskManager.getServiceByProcess(parent.PROCESS_INFO.processId, callBack);
                                    });
                                }
                                if (task.name == "服务发布") {
                                    $("#w").window("close");
                                    $('#taskTable').datagrid('reload');
                                    parent.SYSMENU.changeLeftMenu(6);
                                    var content = '<iframe scrolling="auto" frameborder="0"  src="/jsp/version/versionRelease.jsp" style="width:100%;height:100%;"></iframe>';
                                    parent.addTab("服务发布", content);
                                    alert("请选择服务并发布。");
                                }
                                if (task.name == "服务开发") {
                                    $("#w").window("close");
                                    $('#taskTable').datagrid('reload');
                                    parent.SYSMENU.changeLeftMenuWithCallBack(4, function () {
                                        var callBack = function (result) {
                                            parent.SYSMENU.reloadTreeByValue('mxservicetree', result);
                                        };
                                        taskManager.getServiceByProcess(parent.PROCESS_INFO.processId, callBack);
                                    });
                                    alert("请在右侧服务选择服务，进行开发。");
                                }
                                if (task.name == "服务上线") {
                                    $("#w").window("close");
                                    $('#taskTable').datagrid('reload');
                                    var content = '<iframe scrolling="auto" frameborder="0"  src="/jsp/version/baseLineMake.jsp" style="width:100%;height:100%;"></iframe>';
                                    parent.addTab("服务上线基线制作", content);
                                    alert("请在上线基线中发布上线内容。");
                                }
                            });
                        }
                    }
                    else {
                        alert("请选中要修改的数据！");
                    }
                }
            }, {
                text: '查看详情',
                iconCls: 'icon-edit',
                handler: function () {
                    var checkedItems = $('#taskTable').datagrid('getChecked');
                    var checkedItem;
                    if (checkedItems != null && checkedItems.length > 0) {
                        if (checkedItems.length > 1) {
                            alert("请选择一个任务进行分配！");
                            return false;
                        }
                        else {
                            checkedItem = checkedItems[0];
                            Global.taskId = checkedItem.id;
                            Global.processInstanceId = checkedItem.processInstanceId;
                            Global.taskName = checkedItem.name;
                            var task = {};
                            task.processInstanceId = Global.processInstanceId;
                            task.taskId = Global.taskId;
                            task.userId = $("#userId").text();
                            task.name = Global.taskName;
                            task.name = task.name.replace(/(^\s*)|(\s$)/g, '');
                            parent.PROCESS_INFO.processId = checkedItem.processInstanceId;
                            parent.PROCESS_INFO.taskName = task.name;
                            parent.PROCESS_INFO.taskId = task.taskId;
                            var taskInfoTab = $('#taskTabs').tabs('getTab', "任务详细");
                            var url = '<iframe scrolling="auto" frameborder="0"  src="/jsp/task/taskInfo.jsp?processId='+checkedItem.processInstanceId+'" style="width:100%;height:600px;"></iframe>';

                            if (taskInfoTab) {
                                $('#taskTabs').tabs("update", {
                                    tab: taskInfoTab,
                                    options: {
                                        content: url
                                    }
                                });
                            }else{
                                $('#taskTabs').tabs("add", {
                                    title: "任务详细",
                                    content: url
                                });
                            }
                            $("#taskTabs").tabs("select", "任务详细");
                        }
                    }
                    else {
                        alert("请选中要修改的数据！");
                    }

                }
            }
        ];
    </script>

</body>
</html>