
var idaManager = {
    "getAll": function (callBack) {
        $.ajax({
            type: "GET",
            contentType: "application/json; charset=utf-8",
            url: "/permission/getAll",
            data: JSON.stringify(),
            dataType: "json",
            success: function (result) {
                callBack(result);
            },
            complete:function(responce){
                var resText = responce.responseText;
                if(resText.toString().indexOf("没有操作权限") > 0){
//                    alert("没有权限！");
                    window.location.href = "/jsp/403.jsp";
                }
            }
        });
    },
    "saveIdaMapping":function(data,callBack){
        $.ajax({
            "type" : "POST",
            "contentType" : "application/json;charset=utf-8",
            "url" : "/ida/saveIdaMapping",
            "data": JSON.stringify(data),
            "dataType": "json",
            "success": function(result) {
                callBack(result);
            }
        });
    },
    "deleteIdaMapping" : function(data,callBack){
        $.ajax({
            "type" : "DELETE",
            "contentType" : "application/json;charset=utf-8",
            "url" : "/ida/deleteIdaMapping",
            "data": JSON.stringify(data),
            "dataType": "json",
            "success": function(result) {
                callBack(result);
            }
        });
    }

};