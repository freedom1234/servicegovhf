<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript">
	var parentId;
	$(function(){
		var node = $('.mxservicetree').tree('getSelected');
		if(node.serviceCategory !=null){
			parentId = node.serviceCategory.categoryId;
		}
		if(node.service != null){
			parentId = node.service.categoryId;
		}
	});
	function toSave(){
		var service = {};
		service.serviceName = $('#serviceName').val();
		service.desc = $('#discription').val();
		service.categoryId = parentId;
		service.version = $('#version').val();
		service.state = $('#state').val();
		serviceManager.addService(service,function(result){
			$('#w').window('close');
			$('.mxservicetree').tree('reload');
		});
	}
</script>
<form class="formui">
<table border="0" cellspacing="0" cellpadding="0">

  <tr>
    <th>服务名</th>
    <td><input class="easyui-textbox" type="text" id="serviceName" ></td>
  </tr>
  <tr>
    <th>描述</th>
    <td><input class="easyui-textbox" type="text" id="discription" ></td>
  </tr>
  <tr style="display:none">
    <th>服务分类</th>
    <td><input class="easyui-textbox" type="text" id="serviceCategory" ></td>
  </tr>
<tr>
    <th>version</th>
    <td><input class="easyui-textbox" type="text" id="version" ></td>
  </tr>
  <tr>
    <th>state</th>
    <td><input class="easyui-textbox" type="text" id="state" ></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td class="win-bbar"><a href="#" class="easyui-linkbutton"  iconCls="icon-cancel" onClick="$('#w').window('close')">取消</a><a href="#" class="easyui-linkbutton" onClick="toSave()" iconCls="icon-save">保存</a></td>
  </tr>
</table>
</form>