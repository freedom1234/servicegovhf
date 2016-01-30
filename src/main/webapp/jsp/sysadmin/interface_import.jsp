<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
  <title>原始接口导入</title>
  <link rel="stylesheet" type="text/css" href="/resources/themes/default/easyui.css">
  <link rel="stylesheet" type="text/css" href="/resources/themes/icon.css">
  <link href="/resources/css/ui.css" rel="stylesheet" type="text/css">

</head>
<body>
<fieldset>
  <legend>导入原始接口Excel</legend>
  <form id="uploadimg-form"  action="/excelHelper/interfaceimport/noSystem" method="post" enctype="multipart/form-data">
    <shiro:hasPermission name="importInterface-update">
    <input type="file" title="选择文件" name="file" id="file"/><br /><br />
    <br /><br />
    <input id="fileBtn" type="submit" class="btn" value="文件上传"/><br /><br />
    </shiro:hasPermission>
    <%--<tr style="display:none">
      <td>
        <input style="display:none" type="text" id="systemId" name="systemId">
      </td>
    </tr>--%>
  </form>
</fieldset>
<div class="container">
  <%--<&lt;%&ndash;h3>导入原始接口Excel</h3>
  <form id="uploadimg-form"  action="/excelHelper/interfaceimport/noSystem" method="post" enctype="multipart/form-data">
    <input type="file" title="选择文件" name="file" id="file"/><br /><br />
    <br /><br />
    <input id="fileBtn" type="submit" class="btn" value="文件上传"/><br /><br />
    &lt;%&ndash;<tr style="display:none">
      <td>
        <input style="display:none" type="text" id="systemId" name="systemId">
      </td>
    </tr>&ndash;%&gt;
  </form>&ndash;%&gt;--%>

</div>
<table id="tt" style="height:420px; width:auto;"
       title="导入日志">
  <thead>
  <tr>
    <th data-options="field:'',checkbox:true"></th>
    <th field="type" width="20%" align="left">日志类型</th>
    <th field="detail" width="60%">日志描述</th>
    <th field="time" width="20%">日志日期</th>
  </tr>
  </thead>
</table>
<script type="text/javascript" src="/resources/js/jquery.min.js"></script>
<script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
<script type="text/javascript">
  $(document).ready(function () {
    $('#tt').datagrid({
      rownumbers:true,
      singleSelect:false,
      url: '/log/getAll',
      method: 'get',
      pagination: true,
      pageSize: 10,
      toolbar:toolbar
    });
  })

  var toolbar = [
    <shiro:hasPermission name="importlog-delete">
    {
      text: '删除',
      iconCls: 'icon-remove',
      handler: function () {
        deleteObj();
      }
    }
    </shiro:hasPermission>
      ]

  function deleteObj(){
    var checkedItems = $('#tt').datagrid('getChecked');
    if(checkedItems != null && checkedItems.length > 0){
      if(confirm("确定要删除已选中的"+checkedItems.length+"项吗？一旦删除无法恢复！")){
        var logInfos = [];
        $.each(checkedItems, function(index, item) {
          logInfos.push(item.id);
        });
        var a = $.ajax({
          type: "POST",
          async: false,
          url: "/log/delete",
          dataType: "json",
          data: {"logInfos":logInfos.join(",")},
          success: function(data){
            alert("操作成功");
            $('#tt').datagrid('reload');
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
    }else{
      alert("没有选中项！");
    }
  }

</script>


</body>
</html>
