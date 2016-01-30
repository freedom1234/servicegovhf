/**
 * Created by lenovo on 2015/7/14.
 */
var permissionManager = {
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
    }

};