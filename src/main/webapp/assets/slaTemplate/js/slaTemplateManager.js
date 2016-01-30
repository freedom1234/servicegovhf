
var slaTemplateManager = {
		"add" : function(data, callBack) {
		$.ajax({
			"type" : "POST",
			"contentType" : "application/json; charset=utf-8",
			"url" : "/slaTemplate/add/",
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
			"url" : "/slaTemplate/delete/",
			"dataType" : "json",
			"data": JSON.stringify(data),
			"success" : function(result) {
				callBack(result);
			}
		});
	},
	"addTemplate" : function(data,sId,oId,slaTemplateId, callBack) {
	        var url="/slaTemplate/addSla/"+sId+"/"+oId+"/"+slaTemplateId;
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
	"relateAll" : function(slaTemplateId, callBack) {
		var url="/slaTemplate/relateAll/"+slaTemplateId;
		$.ajax({
			"type" : "POST",
			"contentType" : "application/json; charset=utf-8",
			"url" : url,
			"dataType" : "json",
			"success" : function(result) {
				callBack(result);
			}
		});
	}
};
