<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  <meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
    <base href="<%=basePath%>">
    
    <title>sdaHis详细信息</title>
    
	<link rel="stylesheet" type="text/css" href="/resources/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="/resources/themes/icon.css">
<link href="/resources/css/ui.css" rel="stylesheet" type="text/css">
	 <script type="text/javascript" src="/resources/js/jquery.min.js"></script> 
        <script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="/resources/js/treegrid-dnd.js"></script>

<script type="text/javascript" src="/resources/js/ui.js"></script>
	  <script type="text/javascript" src="/js/version/versionManager.js"></script>
<script type="text/javascript">
var serviceId = "${service.serviceId}";
var operationId = "${operation.operationId}";
var formatter = {
	version:function(value, row, index){
 		try {
			return row.version.code
		} catch (exception) {
		}
 	}
}


function choseService(){
	$('#dlg').dialog({
		title : '服务',
		width : 500,
		closed : false,
		cache : false,
		href : '/jsp/service/serviceTreePage.jsp',
		modal : true
	});
}
function selectService() {
 var node = $("#serviceTree").tree("getSelected");
 if(node.type != "service"){
 	alert("请选择服务！");
 	return false;
 }
    $('#dlg').dialog('close');
              serviceId = node.id;
              $("#serviceId").textbox("setValue", serviceId);
              $("#serviceId").textbox("setText", node.text);

}
function choseOperation(){
	$('#dlg').dialog({
		title : '场景',
		width : 930,
		height : 430,
		left : 100,
		top : 200,
		closed : false,
		cache : false,
		href : '/jsp/service/operationHis/operationList.jsp?serviceId='+serviceId,
		modal : true
	});
}

function selectOperation(){
	var checkedItems = $('#operationList').datagrid('getChecked');
	if(checkedItems != null && checkedItems.length > 0){
		operationId = checkedItems[0].operationId;
		$("#operationId").textbox("setValue", operationId);
		$("#operationId").textbox("setText", checkedItems[0].operationName);
		$('#dlg').dialog('close');
		$("#operationHisList").datagrid({
			url:'/operationHis/getByOS/' + serviceId + '/' + operationId
		});
		$("#operationHisList").datagrid("reload");
	}
}

/*function formatConsole(val,row,index){
 var s = '<a iconcls="icon-search"  class="easyui-linkbutton l-btn l-btn-small" onclick="sdaList(\''+row.autoId+'\')"  href="javascript:void(0)" >SDAHis</a>&nbsp;&nbsp;\
 <a iconcls="icon-search"  class="easyui-linkbutton l-btn l-btn-small" onclick="slaList(\''+row.autoId+'\')"  href="javascript:void(0)" >SLAHis</a>&nbsp;&nbsp;\
 <a iconcls="icon-search"  class="easyui-linkbutton l-btn l-btn-small" onclick="olaList(\''+row.autoId+'\')"  href="javascript:void(0)" >OLAHis</a>&nbsp;&nbsp;\
 ';
 return s;
 }*/

function formatConsole(val,row,index){
	var s = '<a iconcls="icon-search"  class="easyui-linkbutton l-btn l-btn-small" onclick="sdaList(\''+row.autoId+'\')"  href="javascript:void(0)" >SDAHis</a>&nbsp;&nbsp;\
			';
	return s;
}
function operation(val,row,index){
	var s = "";
	try{
		s = '<a iconcls="icon-search"  class="easyui-linkbutton l-btn l-btn-small" onclick="comparePage(\''+row.versionHis.autoId+'\')"  href="javascript:void(0)" >版本对比</a>&nbsp;&nbsp;';
	}catch(exception){
	}
	return s;
}
//弹出对比页面
function comparePage(autoId){
	$.ajax({
		type: "get",
		async: false,
		url: "/versionHis/judgeVersionPre?autoId="+autoId,
		dataType: "json",
		success: function (data) {
			if(data.autoId != null){
				var urlPath = "/jsp/version/sdaComparePage.jsp?autoId1="+autoId+"&type=1&autoId2="+data.autoId+"&versionId="+ data.id;
				$("#dlg").dialog({
					title: '版本对比',
					left:'50px',
					width: 1000,
					height:'auto',
					closed: false,
					cache: false,
					href: urlPath,
					modal: true
				});
			}else{
				alert("该版本为初始版本!");
			}
		}
	});

}
function sdaList(id){
	$('#dlg').dialog({
		title : 'SDAHis',
		width : 600,
		closed : false,
		cache : false,
		href : '/jsp/service/sdaHis/sdaHisPage.jsp?autoId='+id,
		modal : true
	});
	
}
function slaList(id){
	$('#dlg').dialog({
		title : 'SDAHis',
		width : 500,
		closed : false,
		cache : false,
		href : '/jsp/service/slaHis/slaHisPage.jsp?autoId='+id,
		modal : true
	});
}
function olaList(id){
	$('#dlg').dialog({
		title : 'SDAHis',
		width : 500,
		closed : false,
		cache : false,
		href : '/jsp/service/olaHis/olaHisPage.jsp?autoId='+id,
		modal : true
	});
}
var formatter = {
	versionHis: function (value, row, index) {
		try {
			return row.versionHis.code
		} catch (exception) {
		}
	}
};
</script>
</head>
<body >
<div>
<fieldset>
 <legend>基本信息</legend>
<table border="0" cellspacing="0" cellpadding="0">
  <tr>
     <th>服务</th>
    <td >
    	<input class="easyui-textbox" id="serviceId" value="${service.serviceName }(${service.serviceId})" style="width:240px;" />&nbsp;&nbsp;
    	<a iconcls="icon-search"  class="easyui-linkbutton l-btn l-btn-small" onclick="choseService()" href="javascript:void(0)" >选择服务</a>
    	</td>
     </tr>
  <tr>
     <th>场景</th>
    <td>
    	<input class="easyui-textbox" id="operationId" value="${operation.operationName }" style="width:240px;"/>&nbsp;&nbsp;
    	<a iconcls="icon-search"  class="easyui-linkbutton l-btn l-btn-small" onclick="choseOperation()"  href="javascript:void(0)" >选择场景</a>
    	</td>
     </tr>
</table>


</fieldset>

<fieldset>
 <legend>历史场景列表</legend>
 	<table id="operationHisList" style="width:auto;" title="" class="easyui-datagrid"
		data-options="
			iconCls: 'icon-ok',
			rownumbers:true,
			collapsible: true,
			singleSelect:true,
			url:'/operationHis/getByOS/' + serviceId + '/' + operationId ,
			method:'get',
			pagination:true,
			pageSize:10">
		<thead>
			<tr>
				<th data-options="field:'autoId',checkbox:true"></th>
				<th data-options="field:'operationId',width:40">场景编号</th>
				<th data-options="field:'operationName',width:70">场景名称</th>
				<th data-options="field:'operationDesc',width:360">功能描述</th>
				<%--<th data-options="field:'operationRemark',width:50">备注</th>--%>
				<th data-options="field:'versionHis',width:50" formatter='formatter.versionHis'>版本号</th>
				<%--<th data-options="field:'versionHis.code'" formatter="versionHis.code">版本号</th>--%>
				<th data-options="field:'optDate',width:120">更新时间</th>
				<th data-options="field:'optUser',width:50">更新用户</th>
				<th data-options="field:' ',formatter:formatConsole,width:120">数据</th>
				<th data-options="field:'  ',formatter:operation,width:120">操作</th>
			</tr>
		</thead>
	</table>
</fieldset>


    </div>
  <div id="dlg" class="easyui-dialog"
		style="width:400px;height:280px;padding:10px 20px" closed="true"
		resizable="true"></div>
  </body>
</html>
