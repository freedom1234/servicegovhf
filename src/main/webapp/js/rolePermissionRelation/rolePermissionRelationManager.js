/**
 * Created by lenovo on 2015/7/15.
 */

var rolePermissionRelationManager= {
    "save" : function(param , callBack) {
        var url = "/rprelation/save";
        $.ajax({
            "type" : "POST",
            "contentType" : "application/json; charset=utf-8",
            data: JSON.stringify(param),
            "url" : url,
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
            url: "/rprelation/getById/"+data,
            dataType: "json",
            success: function(result) {
                callBack(result);
            }
        });
    },
    "modify" : function(param , callBack) {
        var url = "/rprelation/modify";
        $.ajax({
            "type" : "POST",
            "contentType" : "application/json; charset=utf-8",
            data: JSON.stringify(param),
            "url" : url,
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
    "delet" : function(param,callBack) {
        $.ajax({
            "type" : "DELETE",
            "contentType" : "application/json; charset=utf-8",
            "url" : "/rprelation/delete/"+param,
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
    }
};
