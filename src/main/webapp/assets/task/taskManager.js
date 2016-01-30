/**
 * Created by vincentfxz on 15/7/6.
 */
var taskManager = {
    "createTask": function createTask(task, params, callBack) {
        //{user}/create/{type}
        var url = "/process/" + task.userId + "/create/" + task.taskType;
        $.ajax({
            "type": "POST",
            "contentType": "application/json;charset=utf-8",
            "url": url,
            "data": JSON.stringify(params),
            "dataType": "json",
            "success": function (result) {
                callBack(result);
            },
            "complete": function (responce) {
                var resText = responce.responseText;
                if(resText.toString().indexOf("没有操作权限") > 0){
                    alert("没有权限！");
                    //window.location.href = "/jsp/403.jsp";
                }
            }
        });
    },
    "assignTask": function assignTask(task, callBack) {
        //{user}/delegate/{targetUser}/task/{taskId}
        var url = "/process/" + task.userId + "/delegate/" + task.targetUserId + "/task/" + task.taskId;
        $.ajax({
            "type": "GET",
            "contentType": "application/json; charset=utf-8",
            "url": url,
            "dataType": "json",
            "success": function (result) {
                callBack(result);
            },
            "complete": function (responce) {
                var resText = responce.responseText;
                if(resText.toString().indexOf("没有操作权限") > 0){
                    alert("没有权限！");
                    //window.location.href = "/jsp/403.jsp";
                }
            }
        });
    },
    "processTask": function (task, callBack) {
        //{user}/work/{task}/taskName/{taskName}
        var url = "/process/" + task.userId + "/work/" + task.taskId;
        $.ajax({
            "type": "POST",
            "contentType": "application/json; charset=utf-8",
            "url": url,
            "data": JSON.stringify(task),
            "dataType": "json",
            "success": function (result) {
                callBack(result);
            }
        });
    },
    "completeTask": function (task, params, callBack) {
        //{user}/work/{task}
        var url = "/process/" + task.userId + "/complete/" + task.taskId;
        $.ajax({
            "type": "POST",
            "contentType": "application/json;charset=utf-8",
            "url": url,
            "data": JSON.stringify(params),
            "dataType": "json",
            "success": function (result) {
                callBack(result);
            },
            "complete": function (responce) {
                var resText = responce.responseText;
                if(resText.toString().indexOf("没有操作权限") > 0){
                    alert("没有权限！");
                    //window.location.href = "/jsp/403.jsp";
                }
            }
        });
    },
    "auditMetadata": function (task, callBack) {
        //"/audit/process/{processId}"
        var url = "/metadata/audit/process/" + task.processId;
        $.ajax({
            "type": "GET",
            "contentType": "application/json;charset=utf-8",
            "url": url,
            "dataType": "json",
            "success": function (result) {
                callBack(result);
            }
        });
    },

    "check": function (task, callBack) {
        var content = '<iframe scrolling="auto" frameborder="0"  src="/jsp/SGEnum/taskCheck/common.jsp?processId=' + task.processId + '&taskId=' + task.taskId + '" style="width:100%;height:100%;"></iframe>';
        parent.addTab("验收公共代码", content);
    },

    "getServiceByProcess": function getServiceByProcess(processId, callBack) {
        $.ajax({
            "type": "GET",
            "contentType": "application/json;charset=utf-8",
            "url": "/service/searchService/processId/" + processId,
            "dataType": "json",
            "success": function (result) {
                callBack(result);
            },
            "complete": function (responce) {
                var resText = responce.responseText;
                if(resText.toString().indexOf("没有操作权限") > 0){
                    alert("没有权限！");
                    //window.location.href = "/jsp/403.jsp";
                }
            }
        });
    },

    "getSystemTreeByProcess": function getSystemTreeByProcess(processId, callBack) {
        this.getProcessContext(processId, function(context){
            var systems = "";
            for(var i = 0; i < context.length; i++){
                if("system" == context[i].key){
                    systems += context[i].value + ",";
                }
            }
            systems = systems == "" ? "all" : systems;
            $.ajax({
                "type": "GET",
                "contentType": "application/json;charset=utf-8",
                "url": "/interface/getLeftTree/" + systems,
                "dataType": "json",
                "success": function (result) {
                    callBack(result);
                },
                "complete": function (responce) {
                    var resText = responce.responseText;
                    if(resText.toString().indexOf("没有操作权限") > 0){
                        alert("没有权限！");
                        //window.location.href = "/jsp/403.jsp";
                    }
                }
            });
        });
    },
    "getProcessContext": function getProcessContext(processId, callBack) {
        var url = "/process/getContext/" + processId;
        $.ajax({
            "type": "GET",
            "contentType": "application/json;charset=utf-8",
            "url": url,
            "dataType": "json",
            "success": function (result) {
                callBack(result);
            },
            "complete": function (responce) {
                var resText = responce.responseText;
                if(resText.toString().indexOf("没有操作权限") > 0){
                    alert("没有权限！");
                    //window.location.href = "/jsp/403.jsp";
                }
            }
        });
    }
};