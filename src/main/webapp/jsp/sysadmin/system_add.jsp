<%@ page contentType="text/html; charset=utf-8" language="java"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<style>
	<!--
	table tr th{
		text-align:right
	}
	table tr td{
		padding-left:15px;
		text-align:left
	}
	-->
</style>

<form class="formui" id="form1">
	<table border="0" cellspacing="0" cellpadding="0" style="width:100%; text-align:center">
		<tr>
			<th>
				系统ID
			</th>
			<td>
				<input class="easyui-textbox" type="text" id="systemIdText" data-options="required:true, validType:['intOrFloat']">
			</td>
		</tr>
		<tr>
			<th>
				系统简称
			</th>
			<td>
				<input class="easyui-textbox" type="text" id="systemAbText" data-options="required:true, validType:['englishB']">
			</td>
		</tr>
		<tr>
			<th>
				系统中文名称
			</th>
			<td>
				<input class="easyui-textbox" type="text" id="systemChineseNameText" data-options="required:true, validType:['chineseB']">
			</td>
		</tr>
		<tr style="display:none">
			<th>
				系统功能描述
			</th>
			<td>
				<input class="easyui-textbox" type="text" id="systemDescText">
			</td>
		</tr>
		<!--
		<tr>
			<th>
				系统协议
			</th>
			<td>
				<select class="easyui-combobox" id="protocolIdText" style="width:155px" panelHeight="auto" data-options="editable:false">
				</select>
			</td>
		</tr>
		-->
		<tr>
			<th>
				工作范围
			</th>
			<td>
				<input class="easyui-textbox" type="text" id="workRangeText">
			</td>
		</tr>
		<tr>
			<th>
				系统描述
			</th>
			<td>
				<input class="easyui-textbox" type="text" id="featureDescText">
			</td>
		</tr>
		<tr>
			<th>
				联系人一
			</th>
			<td>
				<input class="easyui-textbox" type="text" id="principal1Text">
			</td>
		</tr>
		<tr style="display: none">
			<th>
				联系人一详细
			</th>
			<td>
				<input class="easyui-textbox" type="text" id="principalDetail1">
			</td>
		</tr>

		<tr>
			<th>
				联系人二
			</th>
			<td>
				<input class="easyui-textbox" type="text" id="principal2Text">
			</td>
		</tr>
		<tr style="display: none">
			<th>
				联系人二详细
			</th>
			<td>
				<input class="easyui-textbox" type="text" id="principalDetail2">
			</td>
		</tr>
		<tr>
			<td colspan="3" style="text-align:center" >
				<a href="#" class="easyui-linkbutton" iconCls="icon-cancel"
				   onClick="$('#w').window('close')">取消</a>&nbsp;&nbsp;<a href="#"
																		  class="easyui-linkbutton" onclick="save()" iconCls="icon-save">保存</a>
			</td>
		</tr>

	</table>
</form>
<script type="text/javascript" src="/plugin/validate.js"></script>
<script type="text/javascript">
	$(document).ready(function (){

		    $('#protocolIdText').combobox({
                url:'/system/getProtocolAll',
                method:'get',
                mode:'remote',
                valueField:'id',
                textField:'text'
            });
	});


	function save(){
		if(!$("#form1").form('validate')){
			return false;
		}
        var systemId = $("#systemIdText").val();
        if(systemId==null || systemId == ''){
			alert("请填写系统ID");
			return;
		}
		var systemAb = $("#systemAbText").val();
		if(systemAb==null || systemAb == ''){
			alert("请填写系统简称");
			return;
		}
    	var systemChineseName = $("#systemChineseNameText").val();
    	var flag = true;
		 $.ajax({
			 type: "GET",
			 async:false,
			 url: "/system/systemIdCheck/"+systemId+"?_t=" + new Date().getTime(),
			 dataType: "json",
			 success: function(data){
				if(data){
					alert("系统ID已存在");
					flag = false;;
				}
			 }
		});

		if(!flag){
			return;
		}


		 $.ajax({
			 type: "GET",
			 async:false,
			 url: "/system/systemAbcheck/"+systemAb+"/"+systemId,
			 dataType: "json",
			 success: function(data){
				if(data){
					alert("系统简称已存在");
					flag = false;;
				}
			 }
		});

		if(!flag){
			return;
		}

		/*var protocolId = $("#protocolIdText").combobox('getValue');
		if(protocolId==null || protocolId == ''){
			alert("请选择协议类型");
			return;
		}
		*/
		var systemDesc = $("#systemDescText").val();
		var principal1 = $("#principal1Text").val();
		var principalDetail1 = $("#principalDetail1").val();
		var principal2 = $("#principal2Text").val();
		var principalDetail2 = $("#principalDetail2").val();
		var workRange = $("#workRangeText").val();
		var featureDesc = $("#featureDescText").val();
		var data = {};

		data.systemId = systemId;
		data.systemAb = systemAb;
		data.systemChineseName = systemChineseName;
		data.principal1 = principal1;
		data.principal2 = principal2;
		data.workRange = workRange;
		data.featureDesc = featureDesc;
		data.systemDesc = systemDesc;
		data.principalDetail1 = principalDetail1;
		data.principalDetail2 = principalDetail2;

		//var protocolData = {};

		//protocolData.protocolId = protocolId;
		//protocolData.systemId = systemId;
		//data.systemProtocol = protocolData;

		sysManager.addSystem(data,function(result){
			if(result){
				$('#w').window('close');
				parent.parent.$('.msinterfacetree').tree("reload");
				$('#tg').datagrid("reload");
			}else{
				alert("保存失败");
			}
		});


	}

</script>


