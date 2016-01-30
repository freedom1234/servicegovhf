$(function(){
	var queryMetadata = function queryMetadata() {
		var queryMetadataCallBack = function queryMetadataCallBack(data){
			$('#metadataList').datagrid('loadData', data);
		};
		var params = {
			"metadataId" : $("#metadataId").textbox("getValue"),
			"metadataName" : $("#metadataName").textbox("getValue"),
			"chineseName" : encodeURI($("#chineseName").textbox("getValue")),
			"metadataAlias" : $("#metadataAlias").textbox("getValue"),
	//		"status" : $("#status").combobox("getValue"),
			"categoryWordId" : $("#categoryWordId").combobox("getValue"),
	//		"version" : $("#version").textbox("getValue"),
			"optUser" : $("#optUser").textbox("getValue"),
			"startDate" : $("#startDate").datebox("getValue"),
			"endDate" : $("#endDate").datebox("getValue")
		};
		$("#metadataList").datagrid('options').queryParams = params;
		$("#metadataList").datagrid('reload');
//		metadataManager.query(params,queryMetadataCallBack);
	};
	$("#queryMetadataBtn").click(function(){
		queryMetadata();
	});

});

function modify(formId,oldMetadataId){
	if(!$("#"+formId).form('validate')){
		return false;
	}
	var result = false;
	var checkedItems = $('#metadataList').datagrid('getChecked');
	$.ajax({
		type: "get",
		async: false,
		url: "/operation/judgeByMetadataId/" + checkedItems[0].metadataId,
		dataType: "json",
		success: function (data) {
			if (data) {//如果有关联的场景
				if(confirm("该元数据已经关联了服务场景，是否同步更新?")){
					result = true;
				}
				//$.messager.defaults = {ok:"是", cancel:"否", async:"false"};
				//$.messager.confirm('关联','该元数据已经关联了服务场景，是否同步更新 ',function(r){
				//	result = r;
				//});
			}
		}
	});

	var params = $("#"+formId).serialize();
	params = decodeURIComponent(params, true);
	params += "&updateSDAFlag="+result;
	$.ajax({
		type: "post",
		async: false,
		url: "/metadata/modify/"+oldMetadataId,
		dataType: "json",
		data: params,
		success: function(data){
			if(data){
				//关闭窗口
				alert("保存成功！");
				$("#w").window("close");
				//刷新查询列表
				$('#metadataList').datagrid('reload');
			}else{
				alert("元数据已存在");
			}

		},
		complete:function(responce){
			var resText = responce.responseText;
			if(resText.toString().indexOf("没有操作权限") > 0){
				alert("没有权限！");
				//window.location.href = "/jsp/403.jsp";
			}
		}
	});
}
function judegeOperation(){
	var result = false;
	var checkedItems = $('#metadataList').datagrid('getChecked');
	$.ajax({
		type: "get",
		async: false,
		url: "/operation/judgeByMetadataId/" + checkedItems[0].metadataId,
		dataType: "json",
		success: function (data) {
			if (data) {//如果有关联的场景
				$.messager.defaults = {ok:"是", cancel:"否"};
				$.messager.confirm('Confirm','该元数据已经关联了服务场景，是否同步更新 ',function(r){
					result = r;
				});
			}
		}
	});
	return result;
}
function save(formId){
	if(!$("#"+formId).form('validate')){
		return false;
	}
	var params = $("#"+formId).serialize();
	params = decodeURIComponent(params, true);
	 $.ajax({
         type: "post",
         async: false,
         url: "/metadata/add",
         dataType: "json",
         data: params,
         success: function(data){
        	 //关闭窗口
        	 $("#w").window("close");
        	 $('#metadataList').datagrid('reload');
        	 //刷新查询列表 
            },
		 complete:function(responce){
			 var resText = responce.responseText;
			 if(resText.toString().indexOf("没有操作权限") > 0){
				 alert("没有权限！");
				 //window.location.href = "/jsp/403.jsp";
			 }
		 }
	 	});
}

function deleteObj(){
	var checkedItems = $('#metadataList').datagrid('getChecked');
	if(checkedItems != null && checkedItems.length > 0){
		if(confirm("确定要删除已选中的"+checkedItems.length+"项吗？一旦删除无法恢复！")){
			var ids = [];
			$.each(checkedItems, function(index, item) {
				ids.push(item.metadataId);
			});
			 $.ajax({
		         type: "post",
		         async: false,
		         url: "/metadata/deletes",
		         dataType: "json",
		         data: {"metadataIds":ids.join(",")},
		         success: function(result){
					 if(result){
						 alert("操作成功");
						 $('#metadataList').datagrid('reload');
					 }else{
						 alert("关联服务场景不予删除");
					 }

		            }
			 	});
		}
	}else{
		alert("没有选中项！");
	}
}