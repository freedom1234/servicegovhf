/**
 * Created by vincentfxz on 15/7/6.
 */
var href = window.location.href;
var params = href.split("&");

$(function () {
	$('#userAssign').combobox({
        url:'/user/getAllUser',
		method:'get',
		mode:'remote',
		valueField:'id',
		textField:'name'
});
    $("#assignTaskBtn").click(function () {
        var task = {};
        task.taskId = Global.taskId;
        task.targetUserId = $('#userAssign').combobox('getValue');
        task.userId = $("#userId").text();
        taskManager.assignTask(task, function () {
            alert("任务已经分派！");
            $("#w").window("close");
            $('#taskTable').datagrid('reload');
        });
    });
});



