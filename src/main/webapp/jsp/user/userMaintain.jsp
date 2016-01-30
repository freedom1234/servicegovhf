<%@ page contentType="text/html; charset=utf-8" language="java"
		 errorPage=""%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>列表页</title>
	<link rel="stylesheet" type="text/css"
		  href="/resources/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css"
		  href="/resources/themes/icon.css">
	<link href="/resources/css/css.css" rel="stylesheet" type="text/css">
</head>

<body>
<form id="searchForm">
<fieldset>
	<legend>条件搜索</legend>
	<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<th><nobr>用户代码</nobr></th>
			<td><input class="easyui-textbox" type="text" name="Id" id="Id">
			</td>
			<th><nobr>用户名称</nobr></th>
			<td><input class="easyui-textbox" type="text" name="Name" id="Name">
			</td>
			<th><nobr>所属机构</nobr></th>
			<td><input class="easyui-textbox" type="text" name="OrgId" id="OrgId">
			</td>
		</tr>
		<%--<tr>
			<th>生效日期</th>
			<td><input class="easyui-datebox" type="text" name="Startdate" id="Startdate">
			</td>
			<th>失效日期</th>
			<td><input class="easyui-datebox" type="text" name="Lastdate" id="Lastdate">
			</td>
		</tr>--%>

		<tr>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td align="right"><a href="#" class="easyui-linkbutton"
								 iconCls="icon-search" id="search">搜索用户</a>
				<a href="#" id="clean" onclick="$('#searchForm').form('clear');" class="easyui-linkbutton" iconCls="icon-clear" style="margin-left:1em" >清空</a>
			</td>
		</tr>
	</table>


</fieldset>
</form>
<table id="tt" style="height:500px; width:auto;" title="所有用户"
	   data-options="rownumbers:true,singleSelect:true,url:'/user/getAll',method:'get',toolbar:toolbar,pagination:true,
				pageSize:10">
	<thead>
	<tr>
				<th data-options="field:'user',checkbox:true"></th>
				<th field="id" width="130px" type="text" align="center">用户代码</th>
				<th field="name" width="130px" align="center" >用户名称</th>
				<th field="userMobile" width="130px" align="center">手机号码</th>
				<th field="userTel" width="130px" align="center">电话号码</th>
				<th field="orgId" width="130px" align="center">所属机构</th>
				<th field="roleNames" width="130px" align="center">角色</th>
				<%--<th field="startdate" width="130px" align="center">生效日期</th>
				<th field="lastdate" width="130px" align="center">失效日期</th>--%>
				<th field="remark" width="130px" align="center">备 注</th>
	</tr>
	</thead>
</table>
<div id="systemBar" style="text-align:center;">
	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onClick="javascript:$('#dlg').dialog('close');">取消</a>
	<a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="addUserSystem()">确定</a>
</div>
<div id="w" class="easyui-window" title=""
	 data-options="modal:true,closed:true,iconCls:'icon-add'"
	 style="width:500px;height:200px;padding:10px;"></div>
<div id="dlg"></div>
<script type="text/javascript" src="/resources/js/jquery.min.js"></script>
<script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/resources/js/jquery.edatagrid.js"></script>
<script type="text/javascript" src="/resources/js/ui.js"></script>
<script type="text/javascript" src="/plugin/json/json2.js"></script>
<script type="text/javascript"
		src="/js/user/userManager.js"></script>
<script type="text/javascript">
	var toolbar = [
			<shiro:hasRole name="admin">
		{
			text : '新增',
			iconCls : 'icon-add',
			handler : function() {
					uiinit.win({
					w : 900,
					iconCls : 'icon-add',
					title : "新增用户",
					url : "userAdd.jsp"
				})
			}
		},
		{
			text : '修改',
			iconCls : 'icon-edit',
			handler : function() {
			var row = $('#tt').edatagrid('getSelected');
			var checkedItems = $('#tt').edatagrid('getChecked');
            if (checkedItems != null && checkedItems.length > 0) {
				uiinit.win({
 					w : 900,
 					iconCls : 'icon-edit',
 					title : "修改用户",
					url : "/user/getById/"+row.id
 				})
			  }else {
                alert("请选中要修改的数据！");
             }
		   }
		},
		{
			text : '删除',
			iconCls : 'icon-remove',
			handler : function() {
			var row = $('#tt').edatagrid('getSelected');
			var rowIndex = $('#tt').edatagrid('getRowIndex', row);
			var checkedItems = $('#tt').edatagrid('getChecked');
            if (checkedItems != null && checkedItems.length > 0) {
				if (!confirm("确定要删除该用户吗？")) {
					return;
				}
				if(row.id == 'admin') {
					alert("无法删除admin用户");
					return;
				}
				userManager.deleteById(row.id,function(result) {
								if (result) {
									alert("删除成功");
								} else {
									alert("删除失败");
								}
							});
				$('#tt').edatagrid('deleteRow', rowIndex);
			  }else {
                alert("请选中要删除的数据！");
             }
		   }
		},
		</shiro:hasRole>
		<shiro:hasPermission name="password-update">
		{
			text : '修改密码',
			iconCls : 'icon-qxfp',
			handler : function() {
				var row = $('#tt').edatagrid('getSelected');
				var checkedItems = $('#tt').edatagrid('getChecked');
				if (checkedItems != null && checkedItems.length > 0) {
				uiinit.win({
						w : 370,
						iconCls : 'icon-qxfp',
						title : "修改密码",
						url : "/user/getByPW/"+row.id
					})
			}else {
				alert("请选中要修改密码的用户！");
			 }
		   }
		},
		</shiro:hasPermission>
		<shiro:hasRole name="admin">
		{
			text : '初始化密码',
			iconCls : 'icon-qxfp',
			handler : function() {
				var row = $('#tt').edatagrid('getSelected');
				var checkedItems = $('#tt').edatagrid('getChecked');
				if (checkedItems != null && checkedItems.length > 0) {
					if (!confirm("确定要初始化密码吗？初始化后密码变为：123456")) {
						return;
					}
					userManager.initPassWord(row.id,'123456',function(result){
						if(result){
							alert("初始化成功，密码变为：123456")
						}
					})
				}else {
					alert("请选中要初始化密码的用户！");
				}
			}
		}
		</shiro:hasRole>
		
	 ];

	<shiro:hasRole name="admin">
	toolbar.push(
	{
		text : '系统分配',
				iconCls : 'icon-qxfp',
			handler : function() {
		var row = $('#tt').edatagrid('getSelected');
		var checkedItems = $('#tt').edatagrid('getChecked');
		if (checkedItems != null && checkedItems.length > 0) {
			$('#dlg').dialog({
				title: '系统分配',
				width: 600,
				height: 500,
				closed: false,
				cache: false,
				href: '/jsp/user/systemDis.jsp?userId='+row.id,
				modal: true,
				toolbar:'#systemBar'
			});
		}else {
			alert("请选中一个用户！");
		}
	}
	}
	);
	function addUserSystem(){
		var rows = $("#systemList").datagrid("getChecked");
		if(null != rows && rows.length > 0){
			var systemIds = [];
			var user = $('#tt').edatagrid('getSelected');
			for(var i = 0; i < rows.length; i++){
				systemIds.push(rows[i].systemId);
			}
			var systemIdsStr = systemIds.join(",");
			userManager.saveUserSystem(user.id, systemIdsStr,function(result){
				if(result){
					alert("保存成功！")
					$('#dlg').dialog("close");
				}
			})
		}else{
			alert("没有选中任何一个系统（如果想要屏蔽用户系统权限请在角色权限管理中设置）！")
		}

	}
	</shiro:hasRole>
	$(function() {
		$('#tt').edatagrid({
			autoSave : false,
			saveUrl : '/',
			updateUrl : '/',
			destroyUrl : '/'
		});
		$('#search').click(function(){
			var id=$('#Id').val();
			var name= $('#Name').val();
			var orgId=$('#OrgId').val();
//			var startdate=$('#Startdate').datebox('getValue');
//			var lastdate=$('#Lastdate').datebox('getValue');
			if(id==""&&name==""&&orgId==""){
			window.location.reload();
			}else{
			var  param = {};
			param.id = $('#Id').val();
			param.name = $('#Name').val();
			param.orgId = $('#OrgId').val();
//			param.startdate = $('#Startdate').datebox('getValue');
//			param.lastdate = $('#Lastdate').datebox('getValue');
			userManager.query(param,function(result){
				$('#tt').edatagrid('loadData',result);
			});
			}
		});
	});
</script>

</body>
</html>