/**
 * Created by vincentfxz on 15/7/20.
 */
var tagManager = {
    "url": "/tag",
    "getTagForInterface": function (interfaceId, callBack) {
        var getTagForInterfaceUrl = this.url + "/interface/" + interfaceId+ "?_t="+ new Date().getTime();
        $.ajax({
            "type": "GET",
            "contentType": "application/json; charset=utf-8",
            "url": getTagForInterfaceUrl,
            "dataType": "json",
            "success": function (result) {
                callBack(result);
            }
        });
    },
    "addTagForInterface": function (interfaceId, tags, callBack) {
        var addTagForInterfaceUrl = this.url + "/interface/" + interfaceId;
        $.ajax({
            "type": "POST",
            "contentType": "application/json; charset=utf-8",
            "url": addTagForInterfaceUrl,
            "data": JSON.stringify(tags),
            "dataType": "json",
            "success": function (result) {
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
    "getTagForService": function (serviceId, callBack) {
        var getTagForServiceUrl = this.url + "/service/" + serviceId+ "?_t="+ new Date().getTime();
        $.ajax({
            "type": "GET",
            "contentType": "application/json; charset=utf-8",
            "url": getTagForServiceUrl,
            "dataType": "json",
            "success": function (result) {
                callBack(result);
            }
        });
    },
    "addTagForService": function (serviceId, tags, callBack) {
        var addTagForServiceUrl = this.url + "/service/" + serviceId;
        $.ajax({
            "type": "POST",
            "contentType": "application/json; charset=utf-8",
            "url": addTagForServiceUrl,
            "data": JSON.stringify(tags),
            "dataType": "json",
            "success": function (result) {
                callBack(result);
            }
        });
    },
    "getTagForOperation": function (serviceId, operationId, callBack) {
        var getTagForOperationUrl = this.url + "/service/" + serviceId + "/operation/" + operationId+ "?_t="+ new Date().getTime();
        $.ajax({
            "type": "GET",
            "contentType": "application/json; charset=utf-8",
            "url": getTagForOperationUrl,
            "dataType": "json",
            "success": function (result) {
                callBack(result);
            }
        });
    },
    "addTagForOperation": function (serviceId, operationId, tags, callBack) {
        var addTagForOperationUrl = this.url + "/service/" + serviceId + "/operation/" + operationId;
        $.ajax({
            "type": "POST",
            "contentType": "application/json; charset=utf-8",
            "url": addTagForOperationUrl,
            "data": JSON.stringify(tags),
            "dataType": "json",
            "success": function (result) {
                callBack(result);
            }
        });
    }
};