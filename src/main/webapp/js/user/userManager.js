
/**
用户管理
**/
var userManager = {
    "add" : function(data, callBack){
        $.ajax({
            type: "POST",
            contentType: "application/json; charset=utf-8",
            url: "/user/add",
            data: JSON.stringify(data),
            dataType: "json",
            success: function(result) {
                callBack(result);
            },
            complete:function(responce){
                var resText = responce.responseText;
                if(resText.toString().indexOf("没有操作权限") > 0){
                    alert("没有权限！");
                    //window.location.href = "/jsp/403.jsp";
                }
            }
        });
    },
    "getAll" : function(callBack){
        $.ajax({
            type: "GET",
            contentType: "application/json; charset=utf-8",
            url: "/user/getAll",
            data: JSON.stringify(),
            dataType: "json",
            success: function(result) {
                callBack(result);
            }
        });
    },
    "query" : function(params, callBack){
        $.ajax({
            type: "POST",
            contentType: "application/json; charset=utf-8",
            url: "/user/query",
            data: JSON.stringify(params),
            dataType: "json",
            success: function(result) {
                callBack(result);
            }
        });
    },
    "deleteById" : function(id,callBack) {
        $.ajax({
            "type" : "DELETE",
            "contentType" : "application/json; charset=utf-8",
            "url" : "/user/delete/"+id,
            "dataType" : "json",
            "success" : function(result) {
                callBack(result);
            },
            "complete":function(responce){
                var resText = responce.responseText;
                if(resText.toString().indexOf("没有操作权限") > 0){
                    alert("没有权限！");
                    //window.location.href = "/jsp/403.jsp";
                }
            }
        });
    },
    
    "getById": function(data, callBack){
        $.ajax({
            type: "GET",
            contentType: "application/json; charset=utf-8",
            url: "/user/getById/"+data,
            data: JSON.stringify(data),
            dataType: "json",
            success: function(result) {
                callBack(result);
            }
        });
    },
    "modify": function(data,callBack){
        $.ajax({
            type: "post",
            contentType: "application/json; charset=utf-8",
            url: "/user/modify",
            data: JSON.stringify(data),
            dataType: "json",
            success: function(result) {
                callBack(result);
            },
            complete:function(responce){
                var resText = responce.responseText;
                if(resText.toString().indexOf("没有操作权限") > 0){
                    alert("没有权限！");
                    //window.location.href = "/jsp/403.jsp";
                }
            }
        });
    },
    "passWord": function(userI,str2,callBack){
    	
        $.ajax({
            type: "post",
            contentType: "application/json; charset=utf-8",
            url: "/user/passWord/"+userI+"/"+str2,
//            data: JSON.stringify(data),
            dataType: "json",
            success: function(result) {
                callBack(result);
            },
            complete:function(responce){
                var resText = responce.responseText;
                if(resText.toString().indexOf("没有操作权限") > 0){
                    alert("没有权限！");
                    //window.location.href = "/jsp/403.jsp";
                }
            }
        });
    },
    "initPassWord": function(userI,str2,callBack){

        $.ajax({
            type: "post",
            contentType: "application/json; charset=utf-8",
            url: "/user/passWord/"+userI+"/"+str2,
//            data: JSON.stringify(data),
            dataType: "json",
            success: function(result) {
                callBack(result);
            },
            complete:function(responce){
                var resText = responce.responseText;
                if(resText.toString().indexOf("没有操作权限") > 0){
                    alert("没有权限！");
                    //window.location.href = "/jsp/403.jsp";
                }
            }
        });
    },
    "assignRoles" : function(data,callBack){
        $.ajax({
            type: "post",
            contentType: "application/json; charset=utf-8",
            url: "/user/assignRoles",
            data: JSON.stringify(data),
            dataType: "json",
            success: function(result) {
                callBack(result);
            }
        });
    },
    "checkUnique" : function(userId, callBack){
        $.ajax({
            type: "GET",
            contentType: "application/json; charset=utf-8",
            url: "/user/checkUnique/userId/" + userId,
            dataType: "json",
            success: function(result) {
                callBack(result);
            }
        });
    },
    "saveUserSystem" : function(userId, systemIdsStr, callBack){
        $.ajax({
            type: "get",
            url: "/userSystemRelation/saveUserSystem",
            dataType: "json",
            data:{
                "userId":userId,
                "systemIdsStr":systemIdsStr
            },
            success: function(result) {
                callBack(result);
            }
        });
    }
};