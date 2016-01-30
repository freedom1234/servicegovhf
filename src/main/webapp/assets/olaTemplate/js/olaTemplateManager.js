
var olaTemplateManager = {
		"add" : function(data, callBack) {
		$.ajax({
			"type" : "POST",
			"contentType" : "application/json; charset=utf-8",
			"url" : "/olaTemplate/add/",
			"data" : JSON.stringify(data),
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
	"deleteByEntity" : function(data, callBack) {
        $.ajax({
			"type" : "DELETE",
			"contentType" : "application/json; charset=utf-8",
			"url" : "/olaTemplate/delete/",
			"dataType" : "json",
			"data": JSON.stringify(data),
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
	"addTemplate" : function(data,sId,oId,olaTemplateId, callBack) {
	        var url="/olaTemplate/addOla/"+sId+"/"+oId+"/"+olaTemplateId;
		$.ajax({
			"type" : "POST",
			"contentType" : "application/json; charset=utf-8",
			"url" : url,
			"data" : JSON.stringify(data),
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
