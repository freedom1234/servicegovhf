<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
    <base href="<%=basePath%>">
    
    <title>sda信息</title>
    
	<link rel="stylesheet" type="text/css" href="/resources/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="/resources/themes/icon.css">
<link href="/resources/css/ui.css" rel="stylesheet" type="text/css">
	 <script type="text/javascript" src="/resources/js/jquery.min.js"></script> 
        <script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="/resources/js/treegrid-dnd.js"></script>

<script type="text/javascript" src="/resources/js/ui.js"></script>
	  <script type="text/javascript" src="/js/version/versionManager.js"></script>
<script type="text/javascript">
var editingId;
var newIds = [];
var delIds = [];
		function onContextMenu(e,row){
			e.preventDefault();
			$(this).treegrid('select', row.id);
			$('#mm').menu('show',{
				left: e.pageX,
				top: e.pageY
			});
		}
		function removeIt(){
			var node = $('#tg').treegrid('getSelected');
			if(node.text == "root" && node.parentId == null){
				alert("请选择其他节点！");
				return false;
			}
			if (node){
				delIds.push(node.id);
				$('#tg').treegrid('remove', node.id);
			}
		}
		function editIt(){
				var row = $('#tg').treegrid('getSelected');
				if(row.text == "root" && row.parentId == null){
					alert("请选择其他节点！");
					return false;
				}
				
				if (row){
					editingId = row.id
					newIds.push(editingId);
					$('#tg').treegrid('beginEdit', editingId);
					
					$("#cancelbtn"+editingId).show();
					$("#okbtn"+editingId).show();
				}
		}
		
		function append(){
			var uuid = "" + new Date().getTime();
			var node = $('#tg').treegrid('getSelected');
			if(node.text == "root" && node.parentId == null){
				alert("请选择其他节点！");
				return false;
			}
			$('#tg').treegrid('append',{
				parent: node.id,
				data: [{
					id: uuid,
					parentId:node.id,
					append3:node.append3,  //初始路径为父路径
					append8:"${head.headId}"
				}]
			});
			editingId = uuid;
			newIds.push(uuid);
			$('#tg').treegrid('reloadFooter');
//			$('#tg').treegrid('beginEdit', uuid);
			$('#tg').treegrid('select',uuid);
			var tempNode = $('#tg').treegrid('getSelected');
//			tempNode = node;
			$('#tg').treegrid('beginEdit', uuid);
//			tempNode.text =  "cs";
//			$('#tg').treegrid('endEdit', uuid);
//			$('#tg').treegrid('reload',{id:uuid, text:'mikel'});
//			{'name':'mikel'})
			var ed = $('#tg').treegrid('getEditor',
					{id:uuid,field:'append4'});
			$(ed.target).combobox({"onSelect":function(record){
				$('#tg').treegrid('select', uuid);
				comboboxSelect(record);
			}});
		}
		function saveSDA(){
			if (!confirm("确定保存吗？")) {
				return;
			}
			if (!$("#sdaForm").form('validate')) {
                return false;
            }
			if(newIds.length == 0 && delIds.length == 0){
				alert("没有修改数据!");
				return false;
			}
			var t = $('#tg');
			if (editingId != undefined){
				var editNodes = [];
				for(var i=0; i<newIds.length; i++){
					var editNode = t.treegrid('find', newIds[i]);
					t.treegrid('endEdit', editNode.id);
					var node = {};
					node.id = editNode.id;
					node.structName = editNode.text;
					node.parentId = editNode.parentId;
					
					node.serviceId = "${service.serviceId }";
					node.operationId = "${operation.operationId }";
					
					node.structAlias = editNode.append1;
					node.type = editNode.append2;
					//node.length = editNode.append3;
					node.metadataId = editNode.append4;
					node.required = editNode.append5;
					node.remark = editNode.append6;
					node.constraint = editNode.append7;
					node.seq = editNode.attributes;
					node.xpath = editNode.append3;
					node.headId = editNode.append8;

					editNodes.push(node);
				}
				
				editingId = undefined;
				var result = false;
				$.ajax({
			         type: "post",
			         async: false,
			         contentType:"application/json; charset=utf-8",
			         url: "/sda/commonSaveSDA",
			         dataType: "json",
			         data: JSON.stringify(editNodes),
			         success: function(data){
			        	 if(data){
							 result = true;
							 newIds = [];
			        	 }
			            }
				 });
			}
			if(delIds.length > 0){
				$.ajax({
			         type: "post",
			         async: false,
			         contentType:"application/json; charset=utf-8",
			         url: "/sda/deleteSDA",
			         dataType: "json",
			         data: JSON.stringify(delIds),
			         success: function(data){
			        	 if(data){
			        	 	result = true;
							 delIds = [];
						 }
			            }
				 });
			}
			if(result){
				alert("保存成功！");
				t.treegrid({url:'/sda/headSdaTree?headId=${head.headId }&t='+ new Date().getTime()});
			}else{
				alert("保存数据时发生错误,保存失败！");
			}

		}
		function cancel(){
			if (editingId != undefined){
				$('#tg').treegrid('cancelEdit', editingId);
				editingId = undefined;
			}
		}
		function formatConsole(value){
	    	
		    	
				var s = '<a iconcls="icon-close" onclick="cancel()" style="display:none;margin-top:5px;margin-bottom:5px;margin-left:5px;" class="easyui-linkbutton l-btn l-btn-small" href="javascript:void(0)" group="" id="cancelbtn'+value+'"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">取消</span><span class="l-btn-icon icon-cancel">&nbsp;</span></span></a>';
				 s += '<a iconcls="icon-ok" onclick="saveSDA()" style="display:none;margin-top:5px;margin-bottom:5px;margin-left:5px;" class="easyui-linkbutton l-btn l-btn-small" href="javascript:void(0)" group="" id="okbtn'+value+'"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">保存</span><span class="l-btn-icon icon-ok">&nbsp;</span></span></a>';
		    	return s;
	    	
		}
		//节点上移
		function moveUp(){
			var node = $('#tg').treegrid('getSelected');

			if(node != null){
				//判断是否是第一个节点
				var parentNode = $('#tg').treegrid('getParent', node.id);
				var brothers =  $('#tg').treegrid('getChildren', parentNode.id);
				if(node.id == brothers[0].id){
					alert("已经在顶部！");
					return false;
				}
				$.ajax({
					type:"get",
					url: "/sda/moveUp?_t=" + new Date().getTime(),
			        dataType: "json",
			        data: {"id": node.id},
			        success: function(data){
			        	 if(data){
							 $('#tg').treegrid({url:'/sda/headSdaTree?headId=${head.headId }&t='+ new Date().getTime()});
						 }
			            }
				});
			}

		}

		function moveDown(){
			var node = $('#tg').treegrid('getSelected');
			if(node != null){
				//判断是否是第一个节点
				var parentNode = $('#tg').treegrid('getParent', node.id);
				var brothers =  $('#tg').treegrid('getChildren', parentNode.id);
				if(node.id == brothers[brothers.length -1 ].id){
					alert("已经在底部！");
					return false;
				}
				$.ajax({
					type:"get",
					url: "/sda/moveDown?_t="+new Date().getTime(),
			        dataType: "json",
			        data: {"id": node.id},
			        success: function(data){
			        	 if(data){
							 $('#tg').treegrid({url:'/sda/headSdaTree?headId=${head.headId }&t='+ new Date().getTime()});

						 }
			            }
				});
			}

		}
		function selectTab(title, content) {
			var exsit = parent.$('#subtab').tabs('getTab', title);
			if (exsit == null) {
				parent.$('#subtab').tabs('add', {
					title: title,
					content: content
				});
			} else {
				parent.$('#subtab').tabs('update', {
					tab: exsit,
					options: {
						content: content
					}
				});
			}
		}
		//跳转到对比页面
		function comparePage(){
			$.ajax({
				type: "get",
				async: false,
				url: "/versionHis/judgeVersionHis?versionId=${operation.versionId}",
				dataType: "json",
				success: function (data) {
					if(data.autoId != null){
						var urlPath = "/jsp/version/sdaComparePage.jsp?autoId1=&type=0&autoId2="+data.autoId+"&versionId=${operation.versionId }";
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
						alert("没有历史版本可以对比!");
					}
				}
			});

		}
		$.extend($.fn.validatebox.defaults.rules, {
                        unique: {
                            validator: function (value, param) {
                               if(value != 'root' && value != 'request' && value != 'response'){
                               		return true;
                               }
                                return false;
                            },
                            message: '新建节点名称不能为“root、request、response”'
                        }
                    });
	//选择元数据自动更新其他数据
	function comboboxSelect(record){
		var node = $('#tg').treegrid('getSelected');
		$('#tg').treegrid('endEdit', node.id);
		var node2 = $('#tg').treegrid('getSelected');
		node2.text =  record.metadataId;
		node2.append1 = record.chineseName;
		node2.append2 = record.formula;
		node2.append3 = node.append3+"/"+record.metadataId;
		node2.append4 = record.metadataId;
		$('#tg').treegrid('refreshRow',node2.id);
	}

	//弹出元数据选择界面
	function appendByMetadata(){

	}
</script>
</head>
<body >
<div id="mm" class="easyui-menu" style="width:120px;">
	<shiro:hasPermission name="sda-add">
		<div onclick="append()" data-options="iconCls:'icon-add'">新增</div>
	</shiro:hasPermission>
	<shiro:hasPermission name="sda-update">
		<div onclick="editIt()" data-options="iconCls:'icon-edit'">编辑</div>
	</shiro:hasPermission>
	<shiro:hasPermission name="sda-delete">
		<div onclick="removeIt()" data-options="iconCls:'icon-remove'">删除</div>
	</shiro:hasPermission>
	</div>
<fieldset>
 <legend>报文头信息</legend>
<table border="0" cellspacing="0" cellpadding="0">
	<input type = "hidden" id = "headId" value="${head.headId}" />
  <tr style="width:100%;">
     <th><nobr>报文头名称</nobr></th>
    <td><input class="easyui-textbox" disabled type="text" name="serviceId" value="${head.headName }" style="width:100px"></td>
    <th><nobr>备注</nobr></th>
    <td><input class="easyui-textbox" disabled type="text" name="serviceName" value="${head.headRemark }"  style="width:250px"></td>
     <th><nobr>描述</nobr></th>
    <td> <input class="easyui-textbox"disabled  type="text" name="operationId" value="${head.headDesc }"   style="width:250px"></td>
  </tr>

</table>


</fieldset>
<form id="sdaForm">
	<table class="easyui-treegrid" id="tg" style=" width:auto;"
			data-options="
				iconCls: 'icon-ok',
				rownumbers: true,
				animate: true,
				collapsible: true,
				fitColumns: true,
				url: '/sda/headSdaTree?headId=${head.headId }&t='+ new Date().getTime(),
				method: 'get',
				idField: 'id',
				treeField: 'text',
                toolbar:'#tb',
                onContextMenu:onContextMenu"
                >
		<thead>
			<tr>
				<th data-options="field:'text',width:140" editor="{type:'textbox',options:{validType:['englishB']}}">字段名</th>
				<th data-options="field:'append1',width:60,align:'left'" editor="{type:'textbox'}">字段别名</th>
				<th data-options="field:'append2',width:50" editor="{type:'textbox'}">类型/长度</th>
				<th data-options="field:'append3',width:60,editor:'text', hidden:true">xpath</th>
				<th field="append4" width="80" editor="{type:'combobox', options:{required:true, editable:true, method:'get', url:'/metadata/getAll', valueField:'metadataId',textField:'metadataId'}}">元数据</th>
                <th field ="append5" width="40" editor="{type:'combobox',options:{url:'/jsp/service/sda/combobox_data.json',valueField:'id',textField:'text'}}">是否必输</th>
                <!--
               	<th data-options="field:'append6',width:80,formatter:formatConsole">备注</th>
               	-->
				<th field ="append7" width="80" editor="{type:'combobox',options:{url:'/jsp/service/sda/combobox_data2.json',valueField:'id',textField:'text'}}">约束条件</th>
               	<th data-options="field:'append6',width:80,editor:'text'">备注</th>
               	<th data-options="field:'append8',width:80, hidden:true">报文头</th>
			</tr>
		</thead>
	</table>
    <div id="tb" style="padding:5px;height:auto">
    	<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td>
		<shiro:hasPermission name="sda-update">
		<a href="javascript:void(0)" onclick="moveUp()" class="easyui-linkbutton" iconCls="icon-up" plain="true">上移</a>&nbsp;&nbsp;
    	<a href="javascript:void(0)" onclick="moveDown()" class="easyui-linkbutton" iconCls="icon-down" plain="true">下移</a>&nbsp;&nbsp;
	    <!--
	    <a href="javascript:void(0)" onclick="addNode()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a>&nbsp;&nbsp;
	    -->
	    <a href="javascript:void(0)" onclick="saveSDA()" class="easyui-linkbutton" iconCls="icon-save" plain="true">保存</a>
		</shiro:hasPermission>
		<!--
		<shiro:hasPermission name="sda-get">
	    <a href="javascript:void(0)" onclick="comparePage()" class="easyui-linkbutton" iconCls="icon-save" plain="true">版本对比</a>
	    -->
		</shiro:hasPermission>
    </td>
    <td align="right"></td>
  </tr>
</table>
</div>
</form>
<script type="text/javascript" src="/plugin/validate.js"></script>
<div id="dlg" class="easyui-dialog"
	 style="width:400px;height:280px;padding:10px 20px" closed="true"
	 resizable="true"></div>
  
  </body>
</html>
