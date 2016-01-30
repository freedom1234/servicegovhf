/**
 * Created by vincentfxz on 15/7/7.
 */
//{user}/work/{task}
$(function () {
	$('#userProcess').combobox({
        url:'/user/getAllUser',
		method:'get',
		mode:'remote',
		valueField:'id',
		textField:'name'
});
    $("#processTaskBtn").click(function () {
        var task = {};
        task.processInstanceId = Global.processInstanceId;
        task.taskId = Global.taskId;
        task.userId = $("#userId").text();
        task.name = Global.taskName;
        taskManager.processTask(task,function(){
            if(task.name=='创建元数据'){
                $("#w").window("close");
                $('#taskTable').datagrid('reload');
                var content = '<iframe scrolling="auto" frameborder="0"  src="/process/metadataByTask/process/'+task.processInstanceId+'/task/'+task.taskId+'" style="width:100%;height:100%;"></iframe>';
                parent.addTab("创建元数据", content);
            }
            if(task.name=="元数据审核"){
                $("#w").window("close");
                $('#taskTable').datagrid('reload');
                var content = '<iframe scrolling="auto" frameborder="0"  src="/process/metadataAuditByTask/process/'+task.processInstanceId+'/task/'+task.taskId+'" style="width:100%;height:100%;"></iframe>';
                parent.addTab("创建元数据", content);
            }
            if(task.name=="服务定义"){
                $("#w").window("close");
                $('#taskTable').datagrid('reload');
                parent.SYSMENU.changeLeftMenu(4);
            }
            if(task.name=="创建公共代码"){
                var content = '<iframe scrolling="auto" frameborder="0"  src="/jsp/SGEnum/commonEnum.jsp?processId='+task.processInstanceId+'&taskId='+task.taskId+'" id="gonggongdaima" style="width:100%;height:100%;"></iframe>';
                parent.addTab("创建公共代码", content);
            }
            if(task.name=="公共代码审核"){
                alert("aaa");
                $("#w").window("close");
                $('#taskTable').datagrid('reload');
                var content = '<iframe scrolling="auto" frameborder="0"  src="/process/sgenum/sgenumAuditByTask/process/'+task.processInstanceId+'/task/'+task.taskId+'" style="width:100%;height:100%;"></iframe>';
                parent.addTab("创建公共代码", content);
            }

        });
    });
});