var serviceManager ={

	"deleteById" : function deleteById(id,callback){
		$.ajax({
			"type" : "DELETE",
			"contentType" : "application/json;charset=utf-8",
			"url": "/service/deleteService/"+id,
			"data": JSON.stringify(id),
			"dataType": "json",
			"success": function(result) {
				if(result){
					alert("删除成功");
				}else{
					alert("请先删除服务下的场景");
				}
				callback(result);
			}
		});
	},

	"deleteCategoryById" : function deleteCategoryById(id,callback){
		$.ajax({
			"type" : "DELETE",
			"contentType" : "application/json;charset=utf-8",
			"url": "/service/deleteServiceCategory/"+encodeURI(encodeURI(id)),
			"data": {"id":id},
			"dataType": "json",
			"success": function(result) {
				if(result){
					alert("删除成功");
				}else{
					alert("请先删除子节点下的服务类及服务");
				}
				callback(result);
			}
		});
	},
	"add" : function add(service,callBack){
		var processId = parent.PROCESS_INFO.processId;
		var url = "/service/addService";
		if(processId){
			url = "/service/addService/" + processId;
		}
		$.ajax({
			"type" : "POST",
			"contentType" : "application/json;charset=utf-8",
			"url": url,
			"data": JSON.stringify(service),
			"dataType": "json",
			"success": function(result) {
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
	"update" : function update(data,callBack){
		$.ajax({
			"type" : "POST",
			"contentType" : "application/json;charset=utf-8",
			"url": "/service/editService",
			"data": JSON.stringify(data),
			"dataType": "json",
			"success": function(result) {
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
	
	"addServiceCategory" : function addServiceCategory(data,callBack){
		$.ajax({
			"type" : "POST",
			"contentType" : "application/json;charset=utf-8",
			"url": "/service/addServiceCategory",
			"data": JSON.stringify(data),
			"dataType": "json",
			"success": function(result) {
				callBack(result);
			},
			complete:function(responce){
				var resText = responce.responseText;
				if(resText.toString().indexOf("没有操作权限") > 0){
					alert("没有权限！");
					//window.location.href = "/jsp/403.jsp";
				}
			}
		});
	},
	
	"updateServiceCategory" : function updateServiceCategory(data,callBack){
		$.ajax({
			"type" : "POST",
			"contentType" : "application/json;charset=utf-8",
			"url": "/service/editServiceCategory",
			"data": JSON.stringify(data),
			"dataType": "json",
			"success": function(result) {
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
