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
<table title="公共代码列表 " id="dg"
       style="height:370px;width:auto">
    <thead>
    <tr>
        <th data-options="field:'productid',checkbox:true"></th>
        <th data-options="field:'name'">代码名称</th>
        <th data-options="field:'isStandard'">是否标准代码</th>
        <th data-options="field:'status'">代码状态</th>
        <th data-options="field:'dataSource'">主代码数据来源</th>
        <!-- <th data-options="field:'isMaster'">是否主代码</th>
        <th data-options="field:'version'">代码版本</th>
        <th data-options="field:'remark',width:60">备注</th> -->
        <th data-options="field:'optUser',width:60">修订人</th>
        <th data-options="field:'optDate',width:100">修订时间</th>
        <th data-options="field:'  '">审核人</th>
        <th data-options="field:'  '">审核时间</th>
    </tr>
    </thead>
</table>
<div id="w" class="easyui-window" title="" data-options="modal:true,closed:true,iconCls:'icon-add'"
     style="width:500px;height:200px;padding:10px;">
</div>
<script type="text/javascript" src="/resources/js/jquery.min.js"></script>
<script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/resources/js/ui.js"></script>
<script type="text/javascript" src="/assets/task/taskManager.js"></script>
<script type="text/javascript">
   $(function(){
        $('#dg').datagrid({
            rownumbers:true,
            singleSelect:true,
            collapsible:true,
            url:"/enum/query/processId/${processId}",
            method:'POST',
            toolbar:toolbar,
            pagination:true,
            pageSize:'10',
            onLoadError: function (responce) {
                var resText = responce.responseText;
                if(resText.toString().indexOf("没有操作权限") > 0){
//                    alert("没有权限！");
                    window.location.href = "/jsp/403.jsp";
                }
            }
        });
    });
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
        },{
            text: '验收',
            iconCls: 'icon-qxfp',
            handler: function () {
                var task = {};
                task.taskId = taskId;
                task.processId = processId;
                task.userId = "admin";
                taskManager.check(task,function(){

                });
            }
        }];

</script>

<script type="text/javascript">
    $(function () {
        $(".datagrid-cell-group").width("auto");
    })
</script>
</body>
</html>