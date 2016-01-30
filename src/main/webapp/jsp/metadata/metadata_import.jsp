<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
    <title>数据字典导入</title>
    <link rel="stylesheet" type="text/css" href="/resources/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="/resources/themes/icon.css">
    <link href="/resources/css/ui.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/resources/js/jquery.min.js"></script>
    <script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="/resources/js/treegrid-dnd.js"></script>

    <script type="text/javascript" src="/resources/js/ui.js"></script>
    <script type="text/javascript" src="/js/version/versionManager.js"></script>

</head>
<body>
<fieldset>
    <legend>导入Excel</legend>
    <form id="uploadimg-form"  action="/resourceImport/import" method="post" enctype="multipart/form-data" onsubmit="uploading()">
    <shiro:hasPermission name="importExcel-update">
        <input type="file" title="选择文件" name="file" id="file"/>
        <br /><br />
        <%--<select name="select">--%>
            <%--<option value="Y">覆盖</option>--%>
            <%--<option value="N">不覆盖</option>--%>
        <%--</select><br /><br />--%>
        <input id="fileBtn" type="submit" class="btn" value="文件上传"/><br /><br />
    </shiro:hasPermission>
    </form>
</fieldset>
   <%-- <div class="container">
        <h3>导入Excel</h3>
        <form id="uploadimg-form"  action="/excelHelper/fieldimport" method="post" enctype="multipart/form-data">
            <input type="file" title="选择文件" name="file" id="file"/><br /><br />
            <select name="select">
                <option value="Y">覆盖</option>
                <option value="N">不覆盖</option>
            </select><br /><br />
            <input id="fileBtn" type="submit" class="btn" value="文件上传"/><br /><br />
        </form>

    </div>--%>
    <table id="tt"  style="height:390px; width:100%;" title="导入日志">
        <thead>
        <tr>
            <th data-options="field:'',checkbox:true"></th>
            <th data-options="field:'type'" width="20%" align="left">日志类型</th>
            <th data-options="field:'detail'" width="50%">日志描述</th>
            <th data-options="field:'time'" width="20%">日志日期</th>
        </tr>
        </thead>
    </table>


   
</body>
<script type="text/javascript">
    $(document).ready(function () {
        var msg = "${message}";
        if(msg){
            alert(msg);
        }
        $('#tt').datagrid({
            rownumbers:true,
            singleSelect:false,
            url: '/log/getAll',
            method: 'get',
            pagination: true,
            pageSize: 10,
            toolbar:toolbar,
            onDblClickCell:onDblClickCell
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
        },
        {
            text: '清空',
            iconCls: 'icon-remove',
            handler: function () {
                deleteAll();
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
    function deleteAll(){
        $.ajax({
            type: "POST",
            async: false,
            url: "/log/deleteAll",
            dataType: "json",
            success: function (data) {
                alert("操作成功");
                $('#tt').datagrid('reload');
            }
        });
    }
    function uploading(){
        $.messager.progress({
            title:'请稍后',
            msg:'正在上传文件...'
        });
    }
    //双击单元格弹出
    function onDblClickCell(rowIndex, field,value){
        var texts = '<div style="word-wrap:break-word" >'+value+'</div>';
        $.messager.show({
            title:'详细',
            msg:texts,
            showType:'show',
            height:'auto'
        });
    }
</script>
</html>
