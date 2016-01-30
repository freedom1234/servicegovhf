var slaManager = {
	"addList" : function(data, callBack) {
		$.ajax({
			"type" : "POST",
			"contentType" : "application/json; charset=utf-8",
			"url" : "/sla/addList",
			"data" : JSON.stringify(data),
			"dataType" : "json",
			"success" : function(result) {
				callBack(result);
			}
		});
	},
	"add" : function(data,sId,oId, callBack) {
	        var url="/sla/add/"+sId+"/"+oId;
		$.ajax({
			"type" : "POST",
			"contentType" : "application/json; charset=utf-8",
			"url" : url,
			"data" : JSON.stringify(data),
			"dataType" : "json",
			"success" : function(result) {
				callBack(result);
			}
		});
	},
	"modify" : function(data, callBack) {
		$.ajax({
			"type" : "POST",
			"contentType" : "application/json; charset=utf-8",
			"url" : "/sla/modify",
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
				"url" : "/sla/delete/",
				"dataType" : "json",
				"data": JSON.stringify(data),
				"success" : function(result) {
					callBack(result);
				}
			});
	},
	"deleteByAll" : function(sId,pId, callBack) {
		var url = "/sla/deleteAll/" + sId+"/"+pId;
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
		var url = "/slaTemplate/getSLA/" + param;
		$.ajax({
			"type" : "GET",
			"contentType" : "application/json; charset=utf-8",
			"url" : url,
			"dataType" : "json",
			"success" : function(result) {
				callBack(result);
			}
		});
	},
	"setTemplateData":function(serviceId,operationId,slaTemplateId,callBack){
		var url = "/slaTemplate/setTemplateData/" + serviceId + "/" + operationId + "/" + slaTemplateId;
		$.ajax({
			"type" : "GET",
			"contentType" : "application/json; charset=utf-8",
			"url" : url,
			"dataType" : "json",
			"success" : function(result) {
				callBack(result);
			}
		});
	}
};
