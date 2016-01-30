<%@ page contentType="text/html; charset=utf-8" language="java"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<form class="formui">
	<table border="0" cellspacing="0" cellpadding="0">

		<tr>
			<th>
				协议名称
			</th>
			<td>
				<input class="easyui-textbox" type="text" id="protocolNameText" required="true">
			</td>
		</tr>
		<tr>
			<th>
				通讯协议
			</th>
			<td>
				<input class="easyui-textbox" type="text" id="commuProtocol">
			</td>
		</tr>
		<tr>
			<th>
				报文格式
			</th>
			<td>
				<input class="easyui-textbox" type="text" id="msgTypeText">
			</td>
		</tr>
		<tr>
			<th>
				协议编码
			</th>
			<td>
				<input class="easyui-textbox" type="text" id="encodingText">
			</td>
		</tr>
		<tr>
			<th>
				超时时间
			</th>
			<td>
				<input class="easyui-textbox" type="text" id="timeoutText">
			</td>
		</tr>
		<tr>
			<th>
				加密
			</th>
			<td>
				<input class="easyui-textbox" type="text" id="isEncrypt">
				<%--<input class="easyui-combobox" id="isEncrypt"--%>
						<%--data-options="--%>
						<%--valueField:'value',--%>
						<%--textField:'label',--%>
						<%--data:[{label:'加密', value:'1'}, {label:'不加密', value:'0'}]--%>
						<%--"--%>
					   <%--value="0"--%>
						<%-->--%>
			</td>
		</tr>
		<tr>
			<th>
				同步
			</th>
			<td>
				<input class="easyui-textbox" type="text" id="isSync">
				<%--<input class="easyui-combobox" id="isSync"--%>
					   <%--data-options="--%>
						<%--valueField:'value',--%>
						<%--textField:'label',--%>
						<%--data:[{label:'同步', value:'1'}, {label:'不同步', value:'0'}]--%>
						<%--"--%>
					   <%--value="0"--%>
						<%-->--%>
			</td>
		</tr>
		<tr>
			<th>
				链接
			</th>
			<td>
				<input class="easyui-textbox" type="text" id="isLongCon">
				<%--<input class="easyui-combobox" id="isLongCon"--%>
					   <%--data-options="--%>
						<%--valueField:'value',--%>
						<%--textField:'label',--%>
						<%--data:[{label:'长链接', value:'1'}, {label:'短链接', value:'0'}]--%>
						<%--"--%>
					   <%--value="0"--%>
						<%-->--%>
			</td>
		</tr>
		<%--<tr>
			<th>
				错误代码
			</th>
			<td>
				<input class="easyui-textbox" type="text" id="errorCodeText">
			</td>
		</tr>

        <tr>
			<th>
				成功代码
			</th>
			<td>
				<input class="easyui-textbox" type="text" id="succCodeText">
			</td>
		</tr>--%>
		<tr>
			<th>
				备注说明
			</th>
			<td>
				<input class="easyui-textbox" type="text" id="remarkText">
			</td>
		</tr>
		<tr>
			<th>
				报文模板
			</th>
			<td>
				<input class="easyui-textbox" type="text" id="templateContent">
			</td>
		</tr>
		<tr>
			<th>
				生成类
			</th>
			<td>
				<select class="easyui-combobox" id="generator" style="width:173px" panelHeight="auto"
						data-options="
						url:'/generator/getAll',
						method:'get',
						mode:'remote',
						valueField:'id',
						textField:'name'
						">
				</select>
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
	function save(){

        var protocolName = $("#protocolNameText").val();
		var msgType = $("#msgTypeText").val();
		var encoding = $("#encodingText").val();
		var timeout = $("#timeoutText").val();
//		var errorCode = $("#errorCodeText").val();
		var remark = $("#remarkText").val();

//		var succCode = $("#succCodeText").val();
		var templateContent = $("#templateContent").val();
		var generatorId = $("#generator").combobox('getValue');
		var data = {};

		data.protocolName = protocolName;
		data.msgType = msgType;
		data.encoding = encoding;
		data.timeout = timeout;
//		data.errorCode = errorCode;
		data.remark = remark;
//		data.succCode = succCode;
		//data.msgTemplateId = msgTemplateId;
		data.generatorId = generatorId;
		data.commuProtocol = $("#commuProtocol").val();
		data.isEncrypt = $("#isEncrypt").val();
		data.isSync = $("#isSync").val();
		data.isLongCon = $("#isLongCon").val();
		var msgTemplate = {};

		msgTemplate.templateContent = templateContent;
		data.msgTemplate = msgTemplate;
		sysManager.addProtocol(data,function(result){
			if(result){
				var systemId;
				var selectNode = $('.msinterfacetree').tree("getSelected");
				if(selectNode){
					var systemNode =  $('.msinterfacetree').tree("getParent",selectNode.target);
					systemId = systemNode.id;
					if(systemId){
						$.ajax({
							type: "GET",
							contentType: "application/json; charset=utf-8",
							url: "/system/protocolRelate/"+systemId+"/"+result.protocolId,
							dataType: "json",
							success: function(result) {
								if(true){
									alert("关联成功");
									$('#w').window('close');
								}else{

								}
								$('#w').window('close');
								$('#tg').datagrid("reload");
								var uuid = ""+new Date().getTime();
								$('.msinterfacetree').tree('append', {
									parent: (selectNode?selectNode.target:null),
									data: [{
										text: 'new item1'
									},{
										text: 'new item2'
									}]
								});
								var urlPath = $('.msinterfacetree').tree('options').url;
								$('.msinterfacetree').tree('options').url = "/interface/getLeftTree/subProtocolTree/system/" + systemId;
								$('.msinterfacetree').tree("reload", selectNode.target);
								$('.msinterfacetree').tree('options').url = urlPath;
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
				}

			}else{
				alert("保存失败");
			}
		});


	}

</script>


