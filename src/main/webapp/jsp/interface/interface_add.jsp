<%@ page contentType="text/html; charset=utf-8" language="java"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<form class="formui" id="form">
	<table border="0" cellspacing="0" cellpadding="0">

		<tr>
			<th>
				接口ID
			</th>
			<td>
				<input class="easyui-textbox" type="text" id="interfaceIdText"  data-options="required:true">
			</td>
		</tr>
		<tr>
			<th>
				交易名称
			</th>
			<td>
				<input class="easyui-textbox" type="text" id="interfaceNameText" data-options="required:true">
			</td>
		</tr>
		<tr>
			<th>
				交易码
			</th>
			<td>
				<input class="easyui-textbox" type="text" id="ecodeText" data-options="">
			</td>
		</tr>
		<tr>
			<th>
				状态
			</th>
			<td>
				<select id="status" class="easyui-combobox"  panelHeight="auto" name="status" style="width: 155px"  data-options="editable:false">
					<option value="">全部</option>
					<option value="0">投产</option>
					<option value="1">废弃</option>
				</select>
			</td>
		</tr>
		<tr>
			<th>
				调用关系
			</th>
			<td>
				<select id="type" class="easyui-combobox"  panelHeight="auto" name="type" style="width: 155px"  data-options="editable:false">
					<option value="0">提供方</option>
					<%--台州只有提供方--%>
					<option value="1">消费方</option>
				</select>
			</td>
		</tr>
		<tr>
			<th>
				是否标准接口
			</th>
			<td>
				<select id="isStandard" class="easyui-combobox"  panelHeight="auto" name="isStandard" style="width: 155px"  data-options="editable:false">
					<option value="0">是</option>
					<option value="1">否</option>
				</select>
			</td>
		</tr>
		<tr>
			<th>
				接口协议
			</th>
			<td>
				<select class="easyui-combobox" id="protocolId" style="width:155px" panelHeight="auto" data-options="editable:false">
				</select>
			</td>
		</tr>
		<tr>
			<th>
				接口功能描述
			</th>
			<td>
				<input class="easyui-textbox" type="text" id="desc">
			</td>
		</tr>
		<%--<tr>
			<th>
				备注说明
			</th>
			<td>
				<input class="easyui-textbox" type="text" id="remark">
			</td>
		</tr>--%>


		<tr>
			<td>
				&nbsp;
			</td>
			<td class="win-bbar">
				<a href="#" class="easyui-linkbutton" iconCls="icon-cancel"
					onClick="$('#w').window('close')">取消</a><a href="#"
					class="easyui-linkbutton" onclick="save()" iconCls="icon-save">保存</a>
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript" src="/plugin/validate.js"></script>
<script type="text/javascript">
	$(document).ready(function (){
		var systemId ="";
		try {
			var selectNode = $('.msinterfacetree').tree("getSelected");
			systemId = selectNode.id;

			var node = $('.msinterfacetree').tree("getParent",selectNode.target);

			if(node && node.id!='root'){
				 systemId = node.id;
			}

		} catch (e) {
			systemId = "${param.systemId}";
		}

		$('#protocolId').combobox({
			url:'/system/getProtocolAll?systemId='+systemId,
			method:'get',
			mode:'remote',
			valueField:'id',
			textField:'text'
		});
    	});

	function save(){

		if(!$("#form").form('validate')){
			alert("请输入正确数据");
			return false;
		}
		var interfaceId = $("#interfaceIdText").val();
		 if(interfaceId==null || interfaceId == ''){
			alert("接口ID不能为空");
			return;
		 }
		 var ecode = $("#ecodeText").val();
		 var interfaceName = $("#interfaceNameText").val();
		 /*if(ecode==null || ecode == ''){
			alert("交易码不能为空");
			return;
		 }*/
		 if(interfaceName==null || interfaceName == ''){
			alert("交易名称不能为空");
			return;
		 }


		var flag = true;
		 $.ajax({
			 type: "GET",
			 url: "/interface/check/"+interfaceId,
			 async:false,
			 dataType: "json",
			 success: function(data){
			 	if(data){
			 		alert("接口ID已存在");
			 		flag = false;
			 		return;
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
		if(!flag){
			return;
		}
		var desc = $("#desc").val();
		var remark = $("#remark").val();
		var status = $("#status").combobox('getValue');
		var type = $("#type").combobox('getValue');
		var isStandard =  $("#isStandard").combobox('getValue');
		var systemId ="";
		var treeObj =$('.msinterfacetree') ;

		try { 
			var selectNode = $('.msinterfacetree').tree("getSelected");
			var node = $('.msinterfacetree').tree("getParent",selectNode.target);
			if(node && node.id!='root'){
				 systemId = node.id;
				var systemNode = $('.msinterfacetree').tree("getParent",node.target);
				 if(systemNode && systemNode.id !='root'){
					 systemId = systemNode.id;
				 }
			}

		} catch (e) { 
			systemId = "${param.systemId}";
			treeObj = parent.$('.msinterfacetree');
		}
		var data = {};
		data.ecode = ecode;
		data.interfaceName = interfaceName;
		data.desc = desc;
		data.remark = remark;
		data.status = status;
		data.interfaceId = interfaceId;

		var invokes = [];
		var serviceInvoke = {};
		serviceInvoke.systemId = systemId;
		var protocolId = $("#protocolId").combobox('getValue');

		serviceInvoke.protocolId = protocolId;
		serviceInvoke.interfaceId = interfaceId;
		serviceInvoke.type = type;
		serviceInvoke.isStandard = isStandard;
		invokes.push(serviceInvoke);
		data.serviceInvoke = invokes;
		interfaceManager.add(data,"add",function(result){
			if(result){
				$('#w').window('close');
				//TODO selectNode 是null
				var selectNode = treeObj.tree("getSelected");
				if(selectNode.id.indexOf('_interface') < 0 ){//如果选择节点是某个接口节点
					selectNode = $('.msinterfacetree').tree("getParent",selectNode.target);
				}
				treeObj.tree('append', {
									parent: (selectNode?selectNode.target:null),
									data: [{
										text: 'new item1'
									}]
								});
				var urlPath = treeObj.tree('options').url;
				treeObj.tree('options').url = "/interface/getLeftTree/subInterfaceTree/system/" + systemId;
				treeObj.tree("reload", selectNode.target);
				treeObj.tree('options').url = urlPath;
				$("#tg").datagrid("reload");
				//var mainTabs = parent.$('#mainContentTabs');
				//var ifName = "interface_"+interfaceId;
				//mainTabs.tabs("getTab","接口("+systemId+")").tg.datagrid("reload");
			}else{
				alert("保存失败");
			}
        });
	}

</script>


