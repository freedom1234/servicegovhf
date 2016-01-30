<%@ page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
    String systemId = request.getParameter("systemId");
    String isAll = request.getParameter("isAll");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>文件管理</title>
    <link rel="stylesheet" type="text/css"
          href="/resources/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css"
          href="/resources/themes/icon.css">
    <link href="/resources/css/ui.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/resources/js/jquery.min.js"></script>
    <script type="text/javascript"
            src="/resources/js/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="/resources/js/ui.js"></script>
    <script type="text/javascript" src="/js/sysadmin/sysManager.js"></script>
</head>
<body>
<table id="tg" style="height:670px;width: 100%;" data-options="pageSize:5">
    <thead>
    <tr>
        <th data-options="field:'fileName',width:'15%'">
            文件名称
        </th>
        <th data-options="field:'systemName',width:'10%'">
            系统名称
        </th>
        <th data-options="field:'fileSize',width:'10%'">
            文件大小
        </th>
        <th data-options="field:'filePath',width:'30%',title:true">
            文件路径
        </th>
        <th data-options="field:'fileDesc',width:'25%'">
            文件描述
        </th>
        <th data-options="field:'fileId',width:'25%', formatter:downloadFormatter">
            下载
        </th>

    </tr>
    </thead>
</table>
<div id="w" class="easyui-window" title=""
     data-options="modal:true,closed:true,iconCls:'icon-add'"
     style="width: 500px; height: 200px; padding: 10px;">
</div>
<script type="text/javascript">
    var isAll = "<%=isAll%>";
    var systemId = "<%=systemId%>";
    var url = "/fileManager/getAll";
    var method = "post";
    if(systemId != null && systemId !="null" && isAll == "0"){
        url = "/fileManager/get/systemId/" + systemId;
        method = "post";
    }

    var toolbar = [];
    <shiro:hasPermission name="file-add">
    toolbar.push({
        text: '新增',
        iconCls: 'icon-add',
        handler: function () {
            uiinit.win({
                w: 500,
                iconCls: 'icon-add',
                title: "新增文件",
                url: "/jsp/sysadmin/file_add.jsp?systemId=" + systemId
            });
        }
    });
    </shiro:hasPermission>
    <shiro:hasPermission name="file-delete">
    toolbar.push({
        text: '删除',
        iconCls: 'icon-remove',
        handler: function () {
            var node = $('#tg').datagrid("getSelected");
            if (node) {
                if (!confirm("确定要删除选中的记录吗？")) {
                    return;
                }
                $.ajax({
                    type: "GET",
                    contentType: "application/json; charset=utf-8",
                    url: "/fileManager/delete/" + node.fileId,
                    dataType: "json",
                    success: function (result) {
                        $('#tg').datagrid("reload");
                    }
                });
            } else {
                alert("请选择要删除的行");
            }
        }
    });
    </shiro:hasPermission>

    $(document).ready(function () {
        $('#tg').datagrid({
            title: '文件管理',
            iconCls: 'icon-edit',//图标
            width: '100%',
            height: '600px',
            collapsible: true,
            method: method,
            url: url,
            singleSelect: true,//是否单选
            pagination: true,//分页控件
            pageSize: 10,//每页显示的记录条数，默认为10
            pageList: [10, 15, 20],//可以设置每页记录条数的列表
            rownumbers: false,//行号
            toolbar: toolbar,
            onLoadError: function (responce) {
                var resText = responce.responseText;
                if(resText.toString().indexOf("没有操作权限") > 0){
//                    alert("没有权限！");
                    window.location.href = "/jsp/403.jsp";
                }
            }
        });

        var p = $('#tg').datagrid('getPager');

        $(p).pagination({

            beforePageText: '第',//页数文本框前显示的汉字
            afterPageText: '页    共 {pages} 页',
            displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
        });

        $('#systemIdSearch').combobox({
            url: '/system/getSystemAll?query=y',
            method: 'get',
            mode: 'remote',
            valueField: 'id',
            textField: 'text'
        });
    })

    function searchData() {

        var fileName = $("#fileName").val();
        var fileDesc = $("#fileDesc").val();
        var systemId = $("#systemIdSearch").combobox('getValue');

        var queryParams = $('#tg').datagrid('options').queryParams;
        queryParams.fileName = fileName;
        queryParams.fileDesc = fileDesc;
        queryParams.systemId = systemId;
        $('#tg').datagrid('options').queryParams = queryParams;//传递值
        $("#tg").datagrid('reload');//重新加载table
    }

    function downloadFormatter(value) {
        var s = '<a iconcls="icon-save"  style="margin-top:5px;margin-bottom:5px;margin-left:5px;" class="easyui-linkbutton l-btn l-btn-small" href="/fileManager/download?fileId=' + value + '" group="" ><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">下载</span><span class="l-btn-icon icon-save">&nbsp;</span></span></a>';
        return s;
    }
</script>

</body>
</html>