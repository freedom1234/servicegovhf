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
<div align="center"><a class="easyui-linkbutton" id="saveBtn">保存</a>&nbsp;&nbsp;<a class="easyui-linkbutton" id="close" onclick="$('#w').window('close');">关闭</a>
</div>
<div id="abc">
  <table title="权限" id="tg" style=" width:400px;">
    <thead>
    <tr>
      <%--<th data-options="field:'check',checkbox:true" onclick="clickCheck()"></th>--%>
      <%--<th data-options="field:'check'" iconCls="icon-ok" onclick="clickCheck()"></th>
      <th data-options="field:'text',width:180"></th>--%>
    </tr>
    </thead>
  </table>
</div>

<script type="text/javascript"  src="/js/permission/permissionManager.js"></script>
<script type="text/javascript"  src="/js/rolePermissionRelation/rolePermissionRelationManager.js"></script>
<script type="text/javascript">
  var roleId="${param.id}";
  var rows;
  $(function(){
    $('#tg').treegrid({
      iconCls: 'icon-ok',
      rownumbers: true,
      animate: true,
//      lines:true,
      collapsible: true,
      fitColumns: true,
      singleSelect:false,
      url: '/permission/getPermissionTree/'+roleId +'?t=' + (new Date()).valueOf(),
      method: 'get',
      idField: 'id',
      treeField: 'text',
      columns:[[
        {field:'id',formatter:function(value,rowData,rowIndex){
          if(rowData.permissionState == 1){
            return "<input TYPE='checkbox' checked></INPUT>";
          }else{
            return "<input TYPE='checkbox'></INPUT>";
          }

        },width:5},
        {field:'text',title:'Persons',width:60,align:'left'}
      ]],
      onLoadSuccess:function(row, data){
        rows = data;
        parsePermissionData(rows);

      },
      onClickRow: function(row){
        if(row.type == "permission"){
          if(row.permissionState == "0"){
            row.permissionState = "1";
          }else{
            row.permissionState = "0";
          }
          $('#tg').treegrid("refresh",row.id);
        }else{
          //勾上或去掉子记录
          if(row.permissionState == "0"){
            pareseChildrenSelect(row);
            row.permissionState = "1";
          }else{
            pareseChildrenUnSelect(row);
            row.permissionState = "0";
          }
          $('#tg').treegrid("refresh",row.id);
        }
      }
    });
    $('#saveBtn').click(function(){
      $.ajax({
        type: "post",
        contentType : "application/json;charset=utf-8",
        async: false,
        url: "/permission/savePermission/"+roleId,
        dataType: "json",
        data: JSON.stringify(rows),
        success: function (data) {
          alert("保存成功");
          $('#w').window('close');
        }
      });
    });
  });
  var parsePermissionData = function(array){
    $.each(array, function (index, item) {
      if(item.type == "permission"){
        //把已经有的权限打勾
        if(item.permissionState == "1"){
          $('#tg').treegrid('select',item.id);
        }
      }else{
        //子集全打勾自身也打勾
        var flag = true;
        $.each(item.children,function (index2, item2){
          if(item2.permissionState == "0"){
            flag = false;
          }
        });
        if(flag){
          item.permissionState = "1";
          $('#tg').treegrid('select',item.id);
          $('#tg').treegrid("refresh",item.id);

        }
        parsePermissionData(item.children);
      }
    });
  }

  var pareseChildrenSelect = function(row){
    if(row.children.length > 0){
      $.each(row.children,function (index, item){
        $('#tg').treegrid('select',item.id);
        item.permissionState = "1";
        pareseChildrenSelect(item);
      })
    }
    $('#tg').treegrid("refresh",row.id);
  }

  var pareseChildrenUnSelect = function(row){
    if(row.children.length > 0){
      $.each(row.children,function (index, item){
        $('#tg').treegrid('unselect',item.id);
        item.permissionState = "0";
        pareseChildrenUnSelect(item);
      })
    }
    $('#tg').treegrid("refresh",row.id);
  }

</script>
</body>
</html>
