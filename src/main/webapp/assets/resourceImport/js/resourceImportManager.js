var resourceImportManager = {
	"resourceImport" : function(data, callBack) {
		$.ajax({
			"type" : "POST",
			"contentType" : "application/json; charset=utf-8",
			"url" : "/resourceImport/import",
			"data" : JSON.stringify(data),
			"dataType" : "json",
			"success" : function(result) {
				callBack(result);
			}
		});
	}
};
