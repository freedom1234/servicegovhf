<%@ page contentType="text/html; charset=utf-8" language="java"
         errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>列表页</title>
    <link rel="stylesheet" type="text/css"
          href="/resources/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css"
          href="/resources/themes/icon.css">
    <link href="/resources/css/css.css" rel="stylesheet" type="text/css">
</head>

<body>
<form id="searchForm">
<fieldset>
    <legend>条件搜索</legend>
    <table border="0" cellspacing="0" cellpadding="0">
        <tr>
            <th><nobr>角色代码</nobr></th>
            <td><input class="easyui-textbox" type="text" name="Id" id="Id">
            </td>
            <th><nobr>角色名称</nobr></th>
            <td><input class="easyui-textbox" type="text" name="Name" id="Name">
            </td>
            <th><nobr>角色描述</nobr></th>
            <td><input class="easyui-textbox" type="text" name="Remark" id="Remark">
            </td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td align="right"><a href="#" class="easyui-linkbutton"
                                 iconCls="icon-search" id="search">搜索角色</a>
                <a href="#" id="clean" onclick="$('#searchForm').form('clear');" class="easyui-linkbutton" iconCls="icon-clear" style="margin-left:1em" >清空</a>
            </td>
        </tr>
    </table>


</fieldset>
</form>
<table id="tt" style="height:500px; width:auto;" title="所有角色">
    <thead>
    <tr>
        <th data-options="field:'role',checkbox:true"></th>
        <th field="id" width="130px" type="text" align="center">角色代码</th>
        <th field="name" width="130px" align="center">角色名称</th>
        <th field="remark" width="130px" align="center">角色描述</th>
    </tr>
    </thead>
</table>

<div id="w" class="easyui-window" title=""
     data-options="modal:true,closed:true,iconCls:'icon-add'"
     style="width:500px;height:200px;padding:10px;"></div>
<script type="text/javascript" src="/resources/js/jquery.min.js"></script>
<script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/resources/js/jquery.edatagrid.js"></script>
<script type="text/javascript" src="/resources/js/ui.js"></script>
<script type="text/javascript" src="/plugin/json/json2.js"></script>
<script type="text/javascript" src="/js/role/roleManager.js"></script>
<script type="text/javascript">

    var toolbar = [{
        text: '新增',
        iconCls: 'icon-add',
        handler: function () {
            uiinit.win({
                w: 370,
                iconCls: 'icon-add',
                title: "新增角色",
                url: "roleAdd.jsp"
            })
        }
    },{
			text : '修改',
			iconCls : 'icon-edit',
			handler : function() {
			var row = $('#tt').edatagrid('getSelected');
			var checkedItems = $('#tt').edatagrid('getChecked');
            if (checkedItems != null && checkedItems.length > 0) {
				 uiinit.win({
                    w: 370,
                    iconCls: 'icon-edit',
                    title: "修改 角色",
                    url: "/role/getById/" + row.id
                })
			  }else {
                alert("请选中要修改的数据！");
             }
		   }
		},
        {
			text : '删除',
			iconCls : 'icon-remove',
			handler : function() {
			var row = $('#tt').edatagrid('getSelected');
			var rowIndex = $('#tt').edatagrid('getRowIndex', row);
			var checkedItems = $('#tt').edatagrid('getChecked');
            if (checkedItems != null && checkedItems.length > 0) {
            	roleManager.relation(row.id, function (result) {
            	  if (result) {
                        alert("该角色正在被使用，无法删除！");
                    } else {
                       roleManager.deleteById(row.id, function (result) {
                       if (result) {
                        alert("删除成功");
                        $('#tt').edatagrid('deleteRow', rowIndex);
                       } else {
                        alert("删除失败");
                      }
                  });
                 }
                });
			  }else {
                alert("请选中要删除的数据！");
             }
		   }
		},
        {
            text: '权限分配',
            iconCls: 'icon-qxfp',
            handler: function () {
                var row = $('#tt').datagrid('getSelected');
             var checkedItems = $('#tt').edatagrid('getChecked');
            if (checkedItems != null && checkedItems.length > 0) {
				 uiinit.win({
                    w: 450,
                     h:580,
                     top:0,
                     left:250,
                    iconCls: 'icon-qxfp',
                    title: "权限分配",
//                     url: "permissionEdit3.jsp?id=" + row.id
                     url: "permissionEdit4.jsp?id=" + row.id
                })
			  }else {
                alert("请选中要分配权限的角色！");
             }
		   }
		}
    ];
    $(function () {
        $('#tt').datagrid({
            rownumbers: true,
            singleSelect: true,
            url: '/role/getAll',
            method: 'get',
            toolbar: toolbar,
            pagination: true,
            pageSize: 10,
            onLoadError: function (responce) {
                var resText = responce.responseText;
                if(resText.toString().indexOf("没有操作权限") > 0){
//                    alert("没有权限！");
                    window.location.href = "/jsp/403.jsp";
                }
            }
        });
        $('#search').click(function () {
        	var id=$('#Id').val();
			var name= $('#Name').val();
			var remark= $('#Remark').val();
			if(id==""&&name==""&&remark=="")
			{window.location.reload();}
			else{
            var param = {};
            param.id = $('#Id').val();
            param.name = $('#Name').val();
            param.remark = $('#Remark').val();
            roleManager.query(param, function(result){
                 $('#tt').edatagrid('loadData', result);
             });
            }
        });
    });

</script>

</body>
</html>