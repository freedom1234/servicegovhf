<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>列表页</title>
    <link rel="stylesheet" type="text/css" href="/resources/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="/resources/themes/icon.css">
    <link href="/resources/css/css.css" rel="stylesheet" type="text/css">
</head>

<body>
<table id="metadataList" class="easyui-datagrid" title="元数据管理"
       data-options="rownumbers:true,singleSelect:false,url:'/metadata/query/processId/${processId}',method:'get',toolbar:toolbar,pagination:true,
				pageSize:10" style="height:370px; width:auto;">
    <thead>
    <tr>
        <th data-options="field:'',checkbox:true"></th>
        <th data-options="field:'metadataId'">元数据名称</th>
        <th data-options="field:'chineseName'">中文名称</th>
        <th data-options="field:'metadataName'">英文名称</th>
        <th data-options="field:'categoryWordId'">类别词</th>
        <th data-options="field:'type'">数据格式</th>
        <th data-options="field:'bussDefine'">业务定义</th>
        <th data-options="field:'bussRule'">业务规则</th>
        <th data-options="field:'dataSource'">数据来源</th>
        <th data-options="field:'status'">状态</th>
        <th data-options="field:'version'">版本号</th>
        <th data-options="field:'  '">创建人</th>
        <th data-options="field:'optDate'">创建时间</th>
        <th data-options="field:'  '">审核人</th>
        <th data-options="field:'  '">审核时间</th>
    </tr>
    </thead>
</table>
<div id="w" class="easyui-window" title="" data-options="modal:true,closed:true,iconCls:'icon-add'"
     style="width:500px;height:200px;padding:10px;">
</div>
<script type="text/javascript">

    var taskId = "${taskId}";
    var processId = "${processId}";
    var toolbar = [
        {
            text: '审核通过',
            iconCls: 'icon-qxfp',
            handler: function () {
                var task = {};
                task.taskId = taskId;
                task.processId = processId;
                task.userId = "admin";
                var params = {};
                params.userId = "admin";
                params.approved = true;
                taskManager.completeTask(task,params,function(){
                    taskManager.auditMetadata(task,function(){
                        alert("审核通过");
                    })
                });
            }

        },{
            text: '驳回',
            iconCls: 'icon-qxfp',
            handler: function () {
                var task = {};
                task.taskId = taskId;
                task.processId = processId;
                task.userId = "admin";
                var params = {};
                params.userId = "admin";
                params.approved = false;
                taskManager.completeTask(task,params,function(){
                });
            }
        }];

</script>
<script type="text/javascript" src="/resources/js/jquery.min.js"></script>
<script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/resources/js/ui.js"></script>
<script type="text/javascript" src="/assets/metadata/metadata.js"></script>
<script type="text/javascript" src="/assets/task/taskManager.js"></script>
<script type="text/javascript" src="/assets/task/auditMetadataTask.js"></script>

<script type="text/javascript">
    $(function () {
        $(".datagrid-cell-group").width("auto");
    })
</script>
</body>
</html>