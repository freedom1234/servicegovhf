var olaManager = {
	"addList" : function(data, callBack) {
		$.ajax({
			"type" : "POST",
			"contentType" : "application/json; charset=utf-8",
			"url" : "/ola/addList",
			"data" : JSON.stringify(data),
			"dataType" : "json",
			"success" : function(result) {
				callBack(result);
			}
		});
	},
	"add" : function(data,sId,oId, callBack) {
	        var url="/ola/add/"+sId+"/"+oId;
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
	},
	"modify" : function(data, callBack) {
		$.ajax({
			"type" : "POST",
			"contentType" : "application/json; charset=utf-8",
			"url" : "/ola/modify",
			"data" : JSON.stringify(data),
			"dataType" : "json",
			"success" : function(result) {
				callBack(result);
			}
		});
	},
	"deleteByEntity" : function(data, callBack) {
	        $.ajax({
				"type" : "DELETE",
				"contentType" : "application/json; charset=utf-8",
				"url" : "/ola/delete/",
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
	"deleteByAll" : function(sId,pId, callBack) {
		var url = "/ola/deleteAll/" + sId+"/"+pId;
		$.ajax({
			"type" : "DELETE",
			"contentType" : "application/json; charset=utf-8",
			"url" : url,
			"dataType" : "json",
			"success" : function(result) {
				callBack(result);
			}
		});
	},
	"getByParams" : function(param, callBack) {
		var url = "/olaTemplate/getOLA/" + param;
		$.ajax({
			"type" : "GET",
			"contentType" : "application/json; charset=utf-8",
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
	}
};
