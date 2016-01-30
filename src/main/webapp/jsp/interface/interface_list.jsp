<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
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
		<link href="/resources/css/ui.css" rel="stylesheet" type="text/css">
		<script type="text/javascript" src="/resources/js/jquery.min.js"></script>
		<script type="text/javascript"
			src="/resources/js/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="/resources/js/ui.js"></script>
		<script type="text/javascript" src="/js/sysadmin/interfaceManager.js"></script>
	</head>

	<body>
	<form id="searchForm">
		<fieldset>
			<legend>条件搜索</legend>
			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<th><nobr>
						交易码
					</nobr>
					</th>
					<td>
						<input class="easyui-textbox" type="text" id="ecode">
					</td>

					<th><nobr>
						交易名称
						</nobr>
					</th>
					<td>
						<input class="easyui-textbox" type="text" id="interfaceName">
					</td>
					<th><nobr>
						接口功能描述
						</nobr>
					</th>
					<td>
						<input class="easyui-textbox" type="text" id="remarkSearch">
					</td>
					<th>
						<nobr>
						状态
						</nobr>
					</th>
					<td>

						<select id="statusSearch" class="easyui-combobox"  panelHeight="auto" style="width: 170px"  data-options="editable:false">
							<option value="">全部</option>
							<option value="0">投产</option>
							<option value="1">废弃</option>
						</select>
					</td>
				</tr>
				<tr>
					<th><nobr>
						接口标签
					</nobr>
					</th>
					<td>
						<select class="easyui-textbox" id="interfaceTag" style="width: 170px">
						</select>
					</td>
					<th><nobr>
						报文头
					</nobr>
					</th>
					<td>
						<select class="easyui-combobox" id="headIdSearch" style="width: 170px" panelHeight="auto" data-options="editable:false">
                         </select>
					</td>
					<th><nobr>
						通讯协议
						</nobr>
					</th>
					<td>
						<select class="easyui-combobox" id="protocolIdSearch" style="width: 165px" panelHeight="auto" data-options="editable:false">
                        </select>
					</td>
					<td>
						&nbsp;
					</td>
					<td align="right">
						<a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="searchData();">搜索</a>
						<a href="#" id="clean" onclick="$('#searchForm').form('clear');" class="easyui-linkbutton" iconCls="icon-clear" style="margin-left:1em" >清空</a>
					</td>
				</tr>
			</table>


		</fieldset>
	</form>
		<table id="tg" style="height: 370px; width: auto;">
			<thead>
				<tr>
					<th data-options="field:'',checkbox:true"></th>
					<th data-options="field:'interfaceId',width:'10%'">
						接口ID
                    </th>
					<th data-options="field:'ecode',width:'10%'">
						交易码
					</th>
					<th data-options="field:'interfaceName',width:'15%'">
						交易名称
					</th>
					<th data-options="field:'desc',width:'15%'">
						接口功能描述
					</th>
					<th data-options="field:'headName',width:'15%'">
						报文头
					</th>
					<th data-options="field:'protocolName',width:'10%'">
						接口协议
					</th>
					<th data-options="field:'status',width:'9%',align:'left'" formatter='formatter.interfaceState'>
						状态
					</th>
					<th data-options="field:'versionId',width:'10%'" formatter='formatter.version'>
						版本号
					</th>
					<th data-options="field:'optDate',width:'15%',align:'center'">
						修订时间
					</th>
					<th data-options="field:'optUser'">
						更新用户
					</th>
				</tr>
			</thead>
		</table>
		<div id="w" class="easyui-window" title=""
			data-options="modal:true,closed:true,iconCls:'icon-add'"
			style="width: 500px; height: 200px; padding: 10px;">
		</div>
		<div id="releaseDlg" class="easyui-dialog" closed="true" resizable="true"></div>
		<script type="text/javascript">
			var toolbar = [];
			<shiro:hasPermission name="interface-add">
			toolbar.push({
				text:'新增',
				iconCls:'icon-add',
				handler:function(){
					interfaceManager.append("${param.systemId}");
				}
			});
			</shiro:hasPermission>
			<shiro:hasPermission name="interface-update">
			toolbar.push({
				text:'修改',
				iconCls:'icon-edit',
				handler:function(){
					var row = $("#tg").treegrid("getSelected");
					if(row){
						interfaceManager.edit(row.interfaceId,"${param.systemId}");
					}else{
						alert("请选择要修改的行");
					}
				}
			});
			</shiro:hasPermission>
			<shiro:hasPermission name="interface-delete">
			toolbar.push({
				text:'删除',
				iconCls:'icon-remove',
				handler:function(){
					if(!confirm("删除接口后，该接口与服务的调用关系也被删除，是否确定删除？")){
						return false;
					}
					var row = $("#tg").treegrid("getSelected");
					if(row){
						remove(row.interfaceId,row.interfaceName);
					}else{
						alert("请选择要删除的行");
					}
				}
			});
			</shiro:hasPermission>
			<shiro:hasPermission name="interface-headRelation">
			toolbar.push({
				text:'关联报文头',
				iconCls:'icon-save',
				handler:function(){
					var row = $("#tg").treegrid("getSelected");
					if(row){
						var interfaceId = row.interfaceId;
						uiinit.win({
							w:500,
							iconCls:'icon-add',
							title:"关联报文头",
							url : "/jsp/interface/header_relate.jsp?interfaceId="+interfaceId +"&systemId="+${param.systemId}
						});
					}else{
						alert("请选择要关联的行");
					}

				}
			});
			/*,{
			 text:'导入',
			 iconCls:'icon-save',
			 handler:function(){
			 //							var row = $("#tg").treegrid("getSelected");
			 //							if(row){
			 //								var interfaceId = row.interfaceId;
			 uiinit.win({
			 w:500,
			 iconCls:'icon-add',
			 title:"导入接口",
			 url : "/jsp/interface/interface_import.jsp?systemId=${param.systemId }"
			 });
			 //							}else{
			 //								alert("请选择要关联的行");
			 //							}

			 }
			 }*/
			</shiro:hasPermission>
			<shiro:hasPermission name="exportInterface-get">
			toolbar.push({
				text:'导出',
				iconCls:'icon-save',
				handler:function(){
					var row = $("#tg").treegrid("getSelected");
					var rows = $("#tg").datagrid("getSelections");
					var interfaceIds = "";
					for(var per in rows){
						if(per == rows.length-1){
							interfaceIds += rows[per].interfaceId;
						}else{
							interfaceIds += rows[per].interfaceId + ",";
						}

					}

					var systemId = ${param.systemId };
					if(row){
						var form=$("<form>");//定义一个form表单
						form.attr("style","display:none");
						form.attr("target","");
						form.attr("method","post");
						form.attr("action","/excelExporter/exportInterface");
						var input1=$("<input>");
						input1.attr("type","hidden");
						input1.attr("name","ids");
						input1.attr("value",interfaceIds);
						var input2=$("<input>");
						input2.attr("type","hidden");
						input2.attr("name","type");
						input2.attr("value","interface");
						var input3=$("<input>");
						input3.attr("type","hidden");
						input3.attr("name","systemId");
						input3.attr("value",systemId);


						$("body").append(form);//将表单放置在web中
						form.append(input1);
						form.append(input2);
						form.append(input3);

						form.submit();//表单提交

					}else{
						alert("请选择要关联的行");
					}

				}
			});
			</shiro:hasPermission>
			<shiro:hasPermission name="version-get">
			toolbar.push({
				text: '历史版本',
				iconCls: 'icon-qxfp',
				handler: function () {
					var row = $("#tg").datagrid("getSelected");
					if(row){
						var urlPath =  '/jsp/interface/interface_history.jsp?interfaceId='+row.interfaceId;
						var hisContent = ' <iframe scrolling="auto" frameborder="0"  src="' + urlPath + '" style="width:100%;height:100%;"></iframe>'

						parent.$('#mainContentTabs').tabs('add', {
							title: '接口发布历史',
							content: hisContent,
							closable: true
						});
					}else{
						alert("请先选一个接口");
					}

				}
			});
			</shiro:hasPermission>
			<shiro:hasPermission name="interface-release">
			toolbar.push({
				text: '发布',
				iconCls: 'icon-save',
				handler: function () {
					var row = $("#tg").datagrid("getSelected");
					if(row){
						var versionCode="";
						if(row.version != null){
							versionCode=row.version.code;
						}
						var urlPath = "/jsp/interface/interface_release.jsp?interfaceId="+row.interfaceId+"&interfaceName="+encodeURI(encodeURI(row.interfaceName))+
								"&versionCode="+versionCode;
						$('#releaseDlg').dialog({
							title: '版本发布',
							width: 500,
							left:150,
							top:50,
							closed: false,
							cache: false,
							href: urlPath,
							modal: true
						});
					}else{
						alert("请先选一个接口");
					}

					//interfaceManager.release();
				}
			});
			</shiro:hasPermission>
			toolbar.push({
				text: '关联的服务场景',
				iconCls: 'icon-cfp',
				handler: function () {
					var row = $("#tg").datagrid("getSelected");
					if(row){
						//判断接口是否有服务调用
						$.ajax({
							type: "get",
							async: false,
							contentType: "application/json; charset=utf-8",
							url: "/serviceLink/contionOperation",
							dataType: "json",
							data: {"interfaceId": row.interfaceId},
							success: function (data) {
								if(data){
									var urlPath = "/jsp/sysadmin/relate_operation.jsp?interfaceId="+row.interfaceId;
									$('#releaseDlg').dialog({
										title: '关联的服务场景',
										width: 500,
										left:150,
										top:50,
										closed: false,
										cache: false,
										href: urlPath,
										modal: true
									});
								}else{
									alert("该接口没有被调用!");
								}
							}
						});

					}
					else{
						alert("请选中一行数据!");
					}
				}
			});


		 $(document).ready(function(){ 
			  $('#tg').datagrid({ 
	        title:'基本信息维护',
	        iconCls:'icon-edit',//图标 
	        width: '100%',
	        height: '500px',
	        method:'post',
	        collapsible: true,
	        url:'/interface/getInterface/${param.systemId }',
	        singleSelect:true,//是否单选
	        pagination:true,//分页控件 
	        pageSize: 15,//每页显示的记录条数，默认为10
		    pageList: [15,20,30],//可以设置每页记录条数的列表
	        rownumbers:true,//行号
	        toolbar: toolbar,
			onDblClickRow : dbClick
	   		});  

			var p = $('#tg').treegrid('getPager');  
			
			$(p).pagination({ 
	       		
		        beforePageText: '第',//页数文本框前显示的汉字 
		        afterPageText: '页    共 {pages} 页', 
		        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	    	});

	      $('#protocolIdSearch').combobox({
				url:'/system/getProtocolAll?query=y',
				method:'get',
				mode:'remote',
				valueField:'id',
				textField:'text'
			});

		 $('#headIdSearch').combobox({
				url:'/interface/getHeadAll?query=y',
				method:'get',
				mode:'remote',
				valueField:'id',
				textField:'text'
			});
		 })
		 var url = '/jsp/interface/interface_define.jsp';
		 var dbClick = function(index,field,value){
			 var title = "";
			 title = field.interfaceName+"("+field.interfaceId+")";
			 var mainTabs = parent.$('#mainContentTabs');
			 if (mainTabs.tabs('exists', title)) {
				 mainTabs.tabs('select', title);
			 } else {
				 var content = '<iframe scrolling="auto" frameborder="0"  src="'+url+'?interfaceId='+field.interfaceId+'" style="width:100%;height:98%;"></iframe>';
				 mainTabs.tabs('add', {
					 title: title,
					 content: content,
					 closable: true
				 });
			 }
		 }
		 
		 function searchData(){
		 
		  	 var ecode = $("#ecode").val();
		 	 var interfaceName = $("#interfaceName").val();
		 	 var desc = $("#remarkSearch").val();
		 	 var status = $("#statusSearch").combobox('getValue');
			 var interfaceTag = $("#interfaceTag").textbox('getValue');
		 	 var queryParams = $('#tg').datagrid('options').queryParams;
             queryParams.ecode = ecode;
             queryParams.interfaceName = interfaceName;
             queryParams.desc = encodeURI(desc);
			 queryParams.interfaceTag = encodeURI(interfaceTag);
             queryParams.status = status;
             queryParams.protocolId = $("#protocolIdSearch").combobox('getValue');
             queryParams.headId = $("#headIdSearch").combobox('getValue');
             $('#tg').datagrid('options').queryParams = queryParams;//传递值
             $("#tg").datagrid('reload');//重新加载table  
		 }

		 var formatter = {
			 interfaceState: function (value, row, index) {
				 if (value == 0) {
					 return "<font color='green'>投产</font>";
				 }
				 if (value == 1) {
					 return "<font color='red'>废弃</font>";
				 }
			 },
			 version: function(value, row, index){
				 try {
					 return row.version.code
				 } catch (exception) {
				 }
			 }
		 };

		 function remove(interfaceId,title){
                 if (!confirm("删除接口后，该接口与服务的调用关系也被删除，是否确定删除？")) {
                     return;
                 }

				treeObj = parent.$('.msinterfacetree');
                tabObj = parent.$('#mainContentTabs');

             	$.ajax({
                     type: "post",
                     contentType: "application/json; charset=utf-8",
                     //测试中出现#&等特殊符号，没法删掉
                     //url: "/interface/delete/"+sId,
                     url: "/interface/delete2",
                     dataType: "json",
                     data:JSON.stringify(interfaceId),
                     success: function(result) {
                         if(result){
                         	$("#tg").datagrid("reload");
                            tabObj.tabs("close",title);
                             var node = treeObj.tree("getSelected");
                             var systemNode =  treeObj.tree("getParent",node.target);
                             treeObj.tree('options').url = "/interface/getLeftTree/subInterfaceTree/system/" + systemNode.id;
                             treeObj.tree("reload", node.target);

                         }
                     }
                 });

             }
		</script>

	</body>
</html>