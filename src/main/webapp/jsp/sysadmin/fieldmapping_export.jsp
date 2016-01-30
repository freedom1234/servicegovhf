<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>配置管理</title>
		<link rel="stylesheet" type="text/css"
			href="/resources/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css"
			href="/resources/themes/icon.css">
		<link href="/resources/css/ui.css" rel="stylesheet" type="text/css">
		<script type="text/javascript" src="/resources/js/jquery.min.js"></script>
		<script type="text/javascript"
			src="/resources/js/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="/resources/js/ui.js"></script>
		<script type="text/javascript" src="/js/sysadmin/sysManager.js"></script>
	</head>

	<body>
		<fieldset>
			<legend>
				条件搜索
			</legend>
			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<th>
						系统简称
					</th>
					<td>
						<input class="easyui-textbox" type="text" id="systemAbSearch">
					</td>
				</tr>

				<tr>
					<th>
						接口名称
					</th>
					<td>
						<select class="" id="interfaceSearch" style="width:155px" panelHeight="auto" data-options="editable:false">
						</select>
					</td>
					<td>
						&nbsp;
					</td>
					<td>
						&nbsp;
					</td>
					<td>
						&nbsp;
					</td>
					<td>
						&nbsp;
					</td>
					<td>
						&nbsp;
					</td>
					<td align="right">
						<a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="searchData();">搜索</a>
					</td>
				</tr>
			</table>


		</fieldset>
		<table id="tg" style="height: 300px; width: 100%" data-options="pageSize:2">
			<thead>
				<tr>
					<th data-options="field:'systemId',width:'15%'">
						系统ID
					</th>
					<th data-options="field:'systemAb',width:'15%'">
						系统简称
					</th>
					<th data-options="field:'systemChineseName',width:'15%'">
						系统中文名称
					</th>
					<th data-options="field:'protocolName',width:'15%',align:'center'">
                    	系统协议
                    </th>
                    <th data-options="field:'principal1',width:'20%'">
                    	联系人
                    </th>
					<th data-options="field:'featureDesc',align:'right',width:'20%'">
						系统描述
					</th>
					<th data-options="field:'workRange',width:'15%',align:'right'">
						工作范围
					</th>

				</tr>
			</thead>
		</table>
		<div id="w" class="easyui-window" title=""
			data-options="modal:true,closed:true,iconCls:'icon-add'"
			style="width: 500px; height: 200px; padding: 10px;">

		</div>
		<script type="text/javascript">
		 $(document).ready(function(){ 
			$('#tg').datagrid({
	        title:'系统基本信息维护',
	        iconCls:'icon-edit',//图标 
	        width: 'auto', 
	        height: '320px',
	        collapsible: true,
	        method:'post',
	        url:'/system/getAll',
	        singleSelect:true,//是否单选 
	        pagination:true,//分页控件 
	        pageSize: 10,//每页显示的记录条数，默认为10
		    pageList: [10,15,20],//可以设置每页记录条数的列表
	        rownumbers:true,//行号
	        toolbar: [{
					text:'导出',
					iconCls:'icon-add',
					handler:function(){
					    


					}
				}],
			onLoadError: function (responce) {
				var resText = responce.responseText;
				if(resText.toString().indexOf("没有操作权限") > 0){
//                    alert("没有权限！");
					window.location.href = "/jsp/403.jsp";
				}
			}
	   		});
			
			var p = $('#tg').datagrid('getPager');
			
			$(p).pagination({ 
	       		
		        beforePageText: '第',//页数文本框前显示的汉字 
		        afterPageText: '页    共 {pages} 页', 
		        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	    	});



		 }) 
		 
		 function searchData(){
		 

		 	var systemAb = $("#systemAbSearch").val();
			var interfaceId = $("#interfaceIdSearch").combobox('getValue');

		 	 var queryParams = $('#tg').datagrid('options').queryParams;  
             queryParams.systemAb = systemAb;
             queryParams.interfaceId = interfaceId;

             $('#tg').datagrid('options').queryParams = queryParams;//传递值  
             $("#tg").datagrid('reload');//重新加载table  
		 }
		</script>

	</body>
</html>