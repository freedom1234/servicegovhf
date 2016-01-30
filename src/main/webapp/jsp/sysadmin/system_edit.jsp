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
<form class="formui">
	<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td colspan="3" style="text-align:center" >
				<a href="#" class="easyui-linkbutton" iconCls="icon-cancel"
				   onClick="$('#w').window('close')">取消</a>&nbsp;&nbsp;<a href="#"
																		  class="easyui-linkbutton" onclick="save()" iconCls="icon-save">保存</a>
			</td>
		</tr>
		<tr>
			<th>
				系统ID
			</th>
			<td>
				<input class="easyui-textbox" type="text" id="systemIdText" value="${system.systemId}" disabled>
			</td>
		</tr>
		<tr>
			<th>
				系统简称
			</th>
			<td>
				<input class="easyui-textbox" type="text" id="systemAbText" value="${system.systemAb}"  data-options="required:true, validType:['englishB']">
			</td>
		</tr>
		<tr>
			<th>
				系统中文名称
			</th>
			<td>
				<input class="easyui-textbox" type="text" id="systemChineseNameText" value="${system.systemChineseName}" data-options="required:true, validType:['chineseB']">
			</td>
		</tr>
		<tr style="display:none">
			<th>
				系统功能描述
			</th>
			<td>
				<input class="easyui-textbox" type="text" id="systemDescText" value="${system.systemDesc}">
			</td>
		</tr>
		<!--<tr>
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
				<input class="easyui-textbox" type="text" id="workRangeText" value="${system.workRange}">
			</td>
		</tr>
		<tr>
			<th>
				系统描述
			</th>
			<td>
				<input class="easyui-textbox" type="text" id="featureDescText" value="${system.featureDesc}">
			</td>
		</tr>
		<tr>
			<th>
				联系人一
			</th>
			<td>
				<input class="easyui-textbox" type="text" id="principal1Text" value="${system.principal1}">
			</td>
		</tr>
		<tr style="display: none">
			<th>
				联系人一详细
			</th>
			<td>
				<input class="easyui-textbox" type="text" id="principalDetail1" value="${system.principalDetail1}">
			</td>
		</tr>
		<tr>
			<th>
				联系人二
			</th>
			<td>
				<input class="easyui-textbox" type="text" id="principal2Text" value="${system.principal2}">
			</td>
		</tr>
		<tr style="display: none">
			<th>
				联系人一
			</th>
			<td>
				<input class="easyui-textbox" type="text" id="principalDetail2" value="${system.principalDetail2}">
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript" src="/plugin/validate.js"></script>
<script type="text/javascript">
	$(document).ready(function (){
		$('#protocolId').combobox({
			url:'/system/getProtocolAll',
			method:'get',
			mode:'remote',
			valueField:'id',
			textField:'text',
			onLoadSuccess:function(){

			}
		});
    });

	function save(){

            var systemId = $("#systemIdText").val();
    		var systemAb = $("#systemAbText").val();
    		if(systemAb==null || systemAb == ''){
				alert("请填写系统简称");
				return;
			}
			var flag = true;
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
    		var systemChineseName = $("#systemChineseNameText").val();
    		//var protocolId = $("#protocolIdText").combobox('getValue');
    		var principal1 = $("#principal1Text").val();
    		var principal2 = $("#principal2Text").val();
    		var workRange = $("#workRangeText").val();
    		var featureDesc = $("#featureDescText").val();
    		var systemDesc = $("#systemDescText").val();
    		var principalDetail1 = $("#principalDetail1").val();
    		var principalDetail2 = $("#principalDetail2").val();
    		var data = {};

    		data.systemId = systemId;
    		data.systemAb = systemAb;
    		data.systemChineseName = systemChineseName;
    		data.principal1 = principal1;
    		data.principal2 = principal2;
    		data.workRange = workRange;
    		data.featureDesc = featureDesc;
    		data.systemDesc = principalDetail2;
    		data.principalDetail1 = principalDetail1;
    		data.principalDetail2 = principalDetail2;

    		//var protocolData = {};

    		//protocolData.protocolId = protocolId;
    		//protocolData.systemId = systemId;
    		//data.systemProtocol = protocolData;

    		sysManager.addSystem(data,function(result){
    			if(result){
    				$('#w').window('close');
    				//alert(parent.parent.$('.msinterfacetree'));
    				//alert($('.msinterfacetree'));
    				parent.parent.$('.msinterfacetree').tree("reload");
					$('#tg').datagrid("reload");
    			}else{
    				alert("保存失败");
    			}
    		});


    	}

</script>


