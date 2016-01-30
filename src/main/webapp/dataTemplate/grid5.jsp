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
<fieldset>
    <form id="searchForm">
    <legend>条件搜索</legend>
    <table border="0" cellspacing="0" cellpadding="0">
        <tr>
            <th>词汇中文名称</th>
            <td><input class="easyui-textbox" type="text" name="ChineseWord" id="ChineseWord">
            </td>

            <th>词汇英文名称</th>
            <td><input class="easyui-textbox" type="text" name="EnglishWord" id="EnglishWord">
            </td>
            <th>词汇英文缩写</th>
            <td><input class="easyui-textbox" type="text" name="WordAb" id="WordAb">
            </td>
            <td align="right"><a href="#" class="easyui-linkbutton"
                                 iconCls="icon-search" id="search">搜索单词</a>
                <a href="#" id="clean" onclick="$('#searchForm').form('clear');" class="easyui-linkbutton" iconCls="icon-clear" style="margin-left:1em" >清空</a>
            </td>
        </tr>
    </table>
        </form>
</fieldset>

<table id="tt" style="height:370px; width:auto;" title="所有单词">
    <thead>
    <tr>
        <th field="id" width="100" hidden="true">ID</th>
        <th field="englishWord" width="100" editor="text">单词名称</th>
        <th field="wordAb" width="100" align="right" editor="text">单词缩写</th>
        <th field="chineseWord" width="100" align="right" editor="text">单词中文</th>
        <th field="remark" width="150" align="right" editor="text">单词备注</th>
        <th field="optUser" width="100" editor="text">操作用户</th>
        <th field="optDate" width="100" editor="text">操作日期</th>
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
<script type="text/javascript"
        src="/assets/englishWord/js/englishWordManager.js"></script>
<script type="text/javascript">
    $(function () {
        var editedRows = [];
        var toolbar = [
            {
                text: '新增',
                iconCls: 'icon-add',
                handler: function () {
                    $('#tt').edatagrid('addRow');
                }
            },
            {
                text: '删除',
                iconCls: 'icon-remove',
                handler: function () {
                    var row = $('#tt').edatagrid('getSelected');
                    var rowIndex = $('#tt').edatagrid('getRowIndex', row);
                    $('#tt').edatagrid('deleteRow', rowIndex);
                }
            },
            {
                text: ' 保存',
                iconCls: 'icon-save',
                handler: function () {

                    for (var per in editedRows) {
                        $("#tt").datagrid('endEdit', editedRows[per]);
                    }
                    var deletedDatas = $('#tt').edatagrid('getChanges',
                            'deleted');
                    var addDatas = $('#tt').edatagrid('getChanges',
                            'inserted');
                    var updatedDatas = $('#tt').edatagrid('getChanges',
                            'updated');
                    for (var i = 0; i < addDatas.length; i++) {
                        var addData = addDatas[i];
                        var data = {};
                        if (addData) {
                            data.id = addData.id;
                            data.englishWord = addData.englishWord;
                            data.wordAb = addData.wordAb;
                            data.chineseWord = addData.chineseWord;
                            data.optUser = addData.optUser;
                            data.optDate = addData.optDate;
                            data.remark = addData.remark;
                            englishWordManager.add(data, function (result) {
                                if (result) {
                                    alert("保存成功");
                                } else {
                                    alert("保存失败");
                                }
                            });
                        }
                    }
                    for (var j = 0; j < deletedDatas.length; j++) {
                        var deleteData = deletedDatas[j];
                        englishWordManager.deleteById(deleteData.id,
                                function (result) {
                                    if (result) {
                                        alert("删除成功");
                                    } else {
                                        alert("删除失败");
                                    }
                                });
                    }
                    for (var k = 0; k < updatedDatas.length; k++) {
                        var updatedData = updatedDatas[k];
                        var data = {};
                        data.id = updatedData.id;
                        data.englishWord = updatedData.englishWord;
                        data.wordAb = updatedData.wordAb;
                        data.chineseWord = updatedData.chineseWord;
                        data.optUser = updatedData.optUser;
                        data.optDate = updatedData.optDate;
                        data.remark = updatedData.remark;
                        englishWordManager.modify(data, function (result) {
                            if (result) {
                                alert("修改成功");
                            } else {
                                alert("修改失败");
                            }
                        });
                    }
                }
            }];

        $('#tt').edatagrid({
            rownumbers: true,
            singleSelect: true,
            url: '/englishWord/getAll',
            method: 'get',
            toolbar: toolbar,
            pagination: true,
            pageSize: 10,
            onBeginEdit: function (index, row) {
                editedRows.push(index);
            }
        });

        $('#tt').edatagrid({
            autoSave: false
        });
        $('#search').click(function () {
            var param = {
                "englishWord": $("#EnglishWord").textbox("getValue"),
                "chineseWord": $("#ChineseWord").textbox("getValue"),
                "wordAb": $("#WordAb").textbox("getValue")
            };
            if(param.englishWord || param.chineseWord || param.wordAb){
                englishWordManager.query(param, function (result) {
                    $('#tt').edatagrid('loadData', result);
                });
            }else{
                $("#tt").datagrid('reload');
            }
        });
    });

</script>

</body>
</html>