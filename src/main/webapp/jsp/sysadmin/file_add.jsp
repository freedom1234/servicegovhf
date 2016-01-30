<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%
	String systemId = request.getParameter("systemId");
	systemId = (systemId == null) ? "" : systemId;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<form id="fileform"  action="/fileManager/addfile" method="post" enctype="multipart/form-data" class="formui">
	<input type="hidden" id="isAll" name="isAll"/>
	<table border="0" cellspacing="0" cellpadding="0">

		<tr>
			<th>
				文件<br/><br/>
			</th>
			<td>
				<input id="file" type="file"  class="easyui-file" name="file"  style="width:175px"><br/><br/>
			</td>
		</tr>
		<tr>
			<th>
				系统<br/><br/>
			</th>
			<td>
				<select class="easyui-combobox" id="systemId" name="systemId"  style="width:155px" panelHeight="200px" data-options="editable:false, required:true">
				</select>
			</td>
		</tr>

		<tr>
			<th>
				文件备注
			</th>
			<td>
				<input class="easyui-textbox" data-options="multiline:true" style="height:60px;width:275px" type="text" name="fileDesc" id="fileDesc">
			</td>
		</tr>
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

<script type="text/javascript">
	var systemId="<%=systemId%>";
	if(systemId!= "" && systemId != "null" && isAll == "0"){
		$('#isAll').attr("value", "0");
		$(document).ready(function(){
			$('#systemId').combobox({
				url:'/system/getSystemAll',
				method:'get',
				value:systemId,
				valueField:'id',
				textField:'text'
			});
			$('#systemId').combobox("select", systemId);
		});

	}else{
		$(document).ready(function(){
			$('#systemId').combobox({
				url:'/system/getSystemAll',
				method:'get',
				mode:'remote',
				valueField:'id',
				textField:'text'
			});
		});
	}

	function save(){
		if("" == $("#file").val()){
			alert("请选择一个文件！")
			return false;
		}
		if (!$("#fileform" ).form('validate')) {
			alert("必须选择一个系统!");
			return false;
		}
		var processId = parent.parent.PROCESS_INFO.processId;
		if(processId){
			$("#fileform").attr("action", "/fileManager/addfile/" +processId);
		}
		$("#fileform").submit();
//		var urlPath = $('.msinterfacetree').tree('options').url;
//		$('.msinterfacetree').tree('options').url = "/interface/getLeftTree/subFileTree/system/" + systemId;
//		$('.msinterfacetree').tree("reload", selectNode.target);
//		$('.msinterfacetree').tree('options').url = urlPath;
//		$('#w').window('close');
	}
</script>


