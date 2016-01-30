<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
    <meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>列表页</title>
    <link rel="stylesheet" type="text/css" href="/resources/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="/resources/themes/icon.css">
    <link href="/resources/css/ui.css" rel="stylesheet" type="text/css">
</head>

<body>
<form id="searchForm">
<fieldset>
    <legend>条件过滤</legend>
    <table border="0" cellspacing="0" cellpadding="0">
        <tr>
            <th><nobr>类别词中文名称</nobr></th>
            <td>
                <input class="easyui-textbox" id="chineseWord"/>
            </td>
           <%--<th>类别词英文名称</th>--%>
            <td style="display: none">
                <input class="easyui-textbox" type="text" id="englishWord">
            </td>
            <th><nobr>类别词英文</nobr></th>
            <td>
                <input class="easyui-textbox" id="esglisgAb"/>
            </td>
            <th><nobr>备注</nobr></th>
            <td>
                <input class="easyui-textbox" id="remark"/>
            </td>
            <td>
                <shiro:hasPermission name="categoryWord-get">
                <a href="#" id="search" class="easyui-linkbutton" iconCls="icon-search" style="margin-left:1em"><nobr>查询</nobr></a>
                <a href="#" id="clean" onclick="$('#searchForm').form('clear');" class="easyui-linkbutton" iconCls="icon-clear" style="margin-left:1em" >清空</a>
                </shiro:hasPermission>
            </td>
        </tr>
    </table>

</fieldset>
</form>
<table id="tt" style="height:620px; width:100%;"
       title="所有类别词">
    <thead>
    <tr>
        <th field="chineseWord" width="150" editor="{type:'validatebox',options:{required:true,validType:['chineseB']}}">中文名称</th>
        <th field="englishWord" width="100" editor="text">英文全称</th>
        <th field="esglisgAb" width="150" align="left" editor="{type:'textbox',options:{required:true,validType:['englishB']}}">英文缩写</th>
        <!-- <th field="esglisgab" width="100" align="right" editor="{type:'numberbox',options:{precision:1}}">类别词英文缩写</th> -->
        <th field="remark" width="150" align="left" editor="text">备注</th>
        <th field="optUser" width="150" editor="text">修订人</th>
        <th field="optDate" width="150" editor="text">修订时间</th>
    </tr>
    </thead>
</table>
<div id="w" class="easyui-window" title="" data-options="modal:true,closed:true,iconCls:'icon-add'"
     style="width:500px;height:200px;padding:10px;">

</div>
<script type="text/javascript" src="/resources/js/jquery.min.js"></script>
<script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/resources/js/jquery.edatagrid.js"></script>
<script type="text/javascript" src="/resources/js/ui.js"></script>
<script type="text/javascript" src="/assets/categoryWord/js/categoryWordManager.js"></script>
<script type="text/javascript" src="/plugin/validate.js"></script>
<script type="text/javascript">
    var toolbar = [];
    <shiro:hasPermission name="categoryWord-add">
    toolbar.push({
        text: '新增',
        iconCls: 'icon-add',
        handler: function () {
            //TODO 还在改
//        $('#tt').edatagrid('addRow');
            uiinit.win({
                top:"20px",
                left:"150px",
                w: 500,
                iconCls: 'icon-add',
                title: "新增类别词",
                url: "/jsp/categoryWord/add.jsp"
            });
        }
    });
    </shiro:hasPermission>
    <shiro:hasPermission name="categoryWord-delete">
    toolbar.push({
        text: '删除',
        iconCls: 'icon-remove',
        handler: function () {
            /*var row = $('#tt').edatagrid('getSelected');
             var rowIndex = $('#tt').edatagrid('getRowIndex', row);
             $('#tt').edatagrid('deleteRow', rowIndex);*/
            if (!confirm("确定要删除该类别词吗？")) {
                return;
            }
            var row = $('#tt').edatagrid('getSelected');
            categoryWordManager.deleteCategoryWord2(row,function(result){
                if(result){
                    alert("删除成功");
                    $("#tt").datagrid('reload');
                }else{
                    alert("不能删除，有元数据关联");
                }
            })
        }
    });
    </shiro:hasPermission>
    <shiro:hasPermission name="categoryWord-update">
    toolbar.push({
        text: '保存',
                iconCls: 'icon-save',
                handler: function () {
            for (var per in editedRows) {
                $("#tt").datagrid('endEdit', editedRows[per]);
                if(!$("#tt").datagrid('validateRow',editedRows[per])){
                    alert("请输入必输项");
                    return false;
                }
            }

            var editData1 = $("#tt").datagrid('getChanges','inserted');
            var editData2 = $("#tt").datagrid('getChanges','updated');
            var deleteData = $("#tt").datagrid('getChanges', 'deleted');
            categoryWordManager.saveCategoryWord(editData1,'1', function (result) {
                if (result) {
                    $('#tt').datagrid('reload');
                }else{
                    alert("英文名称或中文名称不能重复");
                    $('#tt').datagrid('reload');
                }
            });
            categoryWordManager.saveCategoryWord(editData2,'2', function (result) {
                if (result) {
                    $('#tt').datagrid('reload');
                }else{
                    alert("英文名称或中文名称不能重复");
                    $('#tt').datagrid('reload');
                }
            });
            if (deleteData.length > 0) {
                categoryWordManager.deleteCategoryWord(deleteData, function (result) {
                    if (result) {
                        $('#tt').datagrid('reload');
                    }else{
                        alert("不能删除，有元数据关联");
                    }
                })
            }
            editedRows = [];
        }
    });
    </shiro:hasPermission>
    var editedRows = [];
    $(function () {
        $.extend($.fn.validatebox.defaults.rules, {
            unique: {
                validator: function (value, param) {
                    var result;
                    $.ajax({
                        type: "get",
                        async: false,
                        url: "/categoryWord/uniqueValid",
                        dataType: "json",
                        data: {"esglisgAb": value},
                        success: function (data) {
                            result = data;
                        }
                    });
                    return result;
                },
                message: '已存在相同类别词英文'
            }
        });
        $('#tt').edatagrid({
            rownumbers: true,
            singleSelect: true,
            url: '/categoryWord/getAll',
            method: 'get',
            toolbar: toolbar,
            pagination: true,
            pageSize: 20,
            pageList: [20,30,50],
            onBeginEdit: function (index, row) {
                editedRows.push(index);
            },
            onLoadError: function (responce) {
                var resText = responce.responseText;
                if(resText.toString().indexOf("没有操作权限") > 0){
                    window.location.href = "/jsp/403.jsp";
                }
            }
        });
        $("#search").click(function(){
            var queryParams = $('#tt').datagrid('options').queryParams;
            queryParams.englishWord = $("#englishWord").textbox("getValue");
            queryParams.chineseWord = encodeURI($("#chineseWord").textbox("getValue"));
            queryParams.esglisgAb = $("#esglisgAb").textbox("getValue");
            queryParams.remark = encodeURI($("#remark").textbox("getValue"));
            if (queryParams.englishWord || queryParams.chineseWord || queryParams.esglisgAb || queryParams.remark) {
                $("#tt").datagrid('options').queryParams = queryParams;//传递值
                $("#tt").datagrid('reload');//重新加载table
//                categoryWordManager.query(param, function (result) {
//                    $('#tt').edatagrid('loadData', result);
//                });
            } else {
                $("#tt").datagrid('reload');
                //alert("请输入查询条件");
            }
        });

    });
</script>

</body>
</html>