var exporImportManager = {
	"exporMetadata" : function(callBack) {
		$.ajax({
			"type" : "GET",
			"contentType" : "application/json; charset=utf-8",
			"url" : "/exportMetadata/export",
			"dataType" : "json",
			"success" : function(result) {
				callBack(result);
			}
		});
	}
};
