/**
 * Created by vincentfxz on 15/7/15.
 */
var metadataManager ={
    "query" : function(data,callBack){
//        var pageNumber = $('#metadataList').datagrid('getPager').data("pagination").options.pageNumber;
//        var pageSize = $('#metadataList').datagrid('getPager').data("pagination").options.pageSize;
        $.ajax({
            "type" : "POST",
            "contentType" : "application/json;charset=utf-8",
            "url" : "/metadata/query",
            "data": JSON.stringify(data),
            "dataType": "json",
            "success": function(result) {
                callBack(result);
            }
        });
    }
};