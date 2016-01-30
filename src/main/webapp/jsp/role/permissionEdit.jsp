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

<div style="margin-top:10px;"></div>
<div id="abc">

</div>
<div align="center"><a class="easyui-linkbutton" id="saveBtn">保存</a>&nbsp;&nbsp;<a class="easyui-linkbutton" id="close" onclick="$('#w').window('close');">关闭</a>
</div>
<script type="text/javascript"  src="/js/permission/permissionManager.js"></script>
<script type="text/javascript"  src="/js/rolePermissionRelation/rolePermissionRelationManager.js"></script>
<script type="text/javascript">
  var roleId="${param.id}";
  $(function() {
    permissionManager.getAll(function(result) {

      if (result) {
        $('#abc').append("<table width='99%' border='0' id='pTable'>");
        for(var per in result){

          $('#abc').append("<tr>");
          $('#abc').append("<th>"+per+"</th>");
          var checks=result[per].split(",");
          $('#abc').append("<td>");
          for(var i=0;i<checks.length;i++){
            $('#abc').append("<input type='checkbox' name='checkbox' value='"+checks[i].split('&')[0]+"'/>");
            $("#abc").append(checks[i].split('&')[1]);
          }
          $('#abc').append("</td>");
          $('#abc').append("</tr>");
        }
        $('#abc').append("</table>");
      }

      rolePermissionRelationManager.getById(roleId,function(result) {
        $('input[name="checkbox"]').each(function(){

                  var current = $(this).val();
                  for(var per in result){

                    if(current ==result[per]['permissionId']){
                      this.checked=true;
                    }
                  }
        }
        )

      });
    })
  });
  $('#saveBtn').click(function(){
    var datas = [];
    $('input[name="checkbox"]:checked').each(function(){
      var sfruit=$(this).val();
      var data = {};
      data.roleId = roleId;
      data.permissionId = sfruit;
      datas.push(data);
    });
    rolePermissionRelationManager.delet(roleId, function (result) {
      if (result) {
           rolePermissionRelationManager.modify(datas, function (result) {
      if (result) {
        alert("保存成功");
      } else {
        alert("保存失败");
      }
           });
    } else {
        alert("保存失败");
      }
    });
    $('#w').window('close');
  });
</script>
</body>
</html>
