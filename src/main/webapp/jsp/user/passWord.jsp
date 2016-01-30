<%@ page contentType="text/html; charset=utf-8" language="java"
		 import="java.sql.*" errorPage=""%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>列表页</title>
</head>

<body>
<fieldset>
	<table width="99%" border="0" cellspacing="0" cellpadding="0" id="passWord">
  <tr>
    <th>旧密码</th>
    <td><input name="oldPW" class="easyui-textbox" type="password" id="oldPW" /><font color="#FF0000">*</font></td>
  </tr>
<tr>
    <th>新密码</th>
    <td><input name="newPW" class="easyui-textbox" type="password" id="newPW" /><font color="#FF0000">*</font></td>
  </tr>
  <tr>
    <th>确认密码</th>
    <td><input name="confirmPW" class="easyui-textbox" type="password" id="confirmPW" /><font color="#FF0000">*</font></td>
  </tr>
</table>
</fieldset>
    <div style="margin-top:10px;"></div>
<div align="center"><a class="easyui-linkbutton" id="saveBtn">保存</a>&nbsp;&nbsp;<a class="easyui-linkbutton" id="close" onclick="$('#w').window('close');">关闭</a>
</div>
<script type="text/javascript">
var pw="${user.password}";
var userI="${user.id}";
$(function() {
		$('#saveBtn').click(function(){
			var str1 = $('#oldPW').val();
			var str2 = $('#newPW').val();
			var str3 = $('#confirmPW').val();
			var parent=/^[u4E00-u9FA5]+$/;
  			if(!parent.test(str2))
  			{
  				alert("密码不能使用中文");
  				return false;
  			}
// 			alert(str1+"旧"+str2+"新"+str3+"确"+pw+"原")	
			if(str1!=pw){
				alert("输入的旧密码有误！");
				return false;
			}
			if(str2!=str3){
				alert("两次输入的新密码不一致！");
				return false;
			}
			if(str2==pw){
				alert("新密码不能与旧密码一样！");
				return false;
			}
// 			var data = {};	
// 			data.id =userI;
// 			data.password=str2;
   			userManager.passWord(userI,str2,function(result) {
   				if (result) {
   					alert("保存成功");
   					} else {
 					alert("保存失败");
   				}
   				});
 				$('#w').window('close');
		});
});
</script>
</body>
</html>
