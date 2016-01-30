<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>系统管理</title>
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
<form id="searchForm">
<fieldset>
    <legend>
        条件搜索
    </legend>
    <table border="0" cellspacing="0" cellpadding="0" style="width:100%">
        <tr>
            <th width="100">
                系统ID
            </th>
            <td>
                <input class="easyui-textbox" type="text" id="systemId" width="100">
            </td>
            <th width="100">
                 中文名称
            </th>
            <td width="100">
                <input class="easyui-textbox" type="text" id="systemChineseName" width="100">
            </td>
            <th width="100">
                系统简称
            </th>
            <td width="100">
                <input class="easyui-textbox" type="text" id="systemAb" width="100">
            </td>
            <th width="100">
                联系人
            </th>
            <td>
                <input class="easyui-textbox" type="text" id="principal1" width="100">
            </td>
        </tr>

        <tr>
            <th>
                系统描述
            </th>
            <td>
                <input class="easyui-textbox" type="text" id="featureDesc">
            </td>
            <th>
                系统协议
            </th>
            <td>
                <select class="" id="protocolIdSearch" style="width:155px" panelHeight="auto"
                        data-options="editable:false">
                </select>
            </td>
            <td>
                &nbsp;
            </td>
            <td>
                &nbsp;
            </td>

            <td>
                &nbsp;
            </td>
            <td align="right">
                <shiro:hasPermission name="system-get">
                <a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="searchData();">搜索</a>
                <a href="#" id="clean" onclick="$('#searchForm').form('clear');" class="easyui-linkbutton" iconCls="icon-clear" style="margin-left:1em" >清空</a>
                </shiro:hasPermission>
            </td>
        </tr>
    </table>

</fieldset>
</form>
<table id="tg" style="width:100%">
    <thead>
    <tr>
        <th data-options="field:'systemId',width:'10%'">
            系统ID
        </th>
        <th data-options="field:'systemAb',width:'12%'">
            系统简称
        </th>
        <th data-options="field:'systemChineseName',width:'15%'">
            系统中文名称
        </th>
        <th data-options="field:'protocolName',width:'15%',align:'center'">
            系统协议
        </th>
        <th data-options="field:'principal1',width:'10%'">
            联系人
        </th>
        <th data-options="field:'featureDesc',align:'left',width:'20%'">
            系统描述
        </th>
        <th data-options="field:'workRange',width:'15%',align:'left'">
            工作范围
        </th>
    </tr>
    </thead>
</table>
<div id="w" class="easyui-window" title=""
     data-options="modal:true,closed:true,iconCls:'icon-add'"
     style="width: 500px; height: 200px; padding: 10px;">

</div>
<script type="text/javascript">
    var toolbar = [];
    <shiro:hasPermission name="system-add">
    toolbar.push({
        text: '新增',
        iconCls: 'icon-add',
        handler: function () {
            sysManager.addSystemPage();
        }
    });
    </shiro:hasPermission>
    <shiro:hasPermission name="system-update">
    toolbar.push({
        text: '修改',
        iconCls: 'icon-edit',
        handler: function () {
            var node = $('#tg').datagrid("getSelected");
            if (node) {
                uiinit.win({
                    w: 500,
                    iconCls: 'icon-add',
                    title: "编辑系统",
                    url: "/system/edit/" + node.systemId
                });
            } else {
                alert("请选择要修改的行");
            }
        }
    });
    </shiro:hasPermission>
    <shiro:hasPermission name="system-delete">
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
                    type: "POST",
                    contentType: "application/json; charset=utf-8",
                    //如果systemId包含#等特殊符号就会有问题
//                            url: "/system/delete/" + node.systemId,
                    url: "/system/delete2",
                    dataType: "json",
                    data: JSON.stringify(node.systemId),
                    success: function (result) {
                        if(result){
                            $('#tg').datagrid("reload");
                            parent.parent.$('.msinterfacetree').tree('reload');
                        }else{
                            alert("系统有下挂接口，无法删除");
                        }
                    },
                    complete: function (responce) {
                        var resText = responce.responseText;
                        if(resText.toString().indexOf("没有操作权限") > 0){
                            alert("没有权限！");
                            //window.location.href = "/jsp/403.jsp";
                        }
                    }
                });
            } else {
                alert("请选择要删除的行");
            }
        }
    });
    </shiro:hasPermission>
    <shiro:hasPermission name="file-get">
    toolbar.push({
        text: '文件管理',
        iconCls: 'icon-save',
        handler: function () {
            var node = $('#tg').datagrid("getSelected");
            if (parent.$('#sysContentTabs').tabs('exists', "文件管理")) {
                parent.$('#sysContentTabs').tabs('select', "文件管理");
            } else {
                var content = '<iframe scrolling="auto" frameborder="0"  src="jsp/sysadmin/file_list.jsp" style="width:100%;height:100%;"></iframe>';
                if (node && node["systemId"]) {
                    content = '<iframe scrolling="auto" frameborder="0"  src="jsp/sysadmin/file_list.jsp?systemId=' + node.systemId + '" style="width:100%;height:100%;"></iframe>';
                }
                parent.$('#sysContentTabs').tabs('add', {
                    title: "文件管理",
                    content: content,
                    closable: true
                });
            }
        }
    });
    </shiro:hasPermission>

    $(document).ready(function () {
        $('#tg').datagrid({
            title: '系统基本信息维护',
            iconCls: 'icon-edit',//图标
            width: '100%',
            height: '500px',
            collapsible: true,
            method: 'post',
            url: '/system/getAll',
            singleSelect: true,//是否单选
            pagination: true,//分页控件
            pageSize: 15,//每页显示的记录条数，默认为10
            pageList: [15, 20, 30],//可以设置每页记录条数的列表
            rownumbers: true,//行号
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


        $('#protocolIdSearch').combobox({
            url: '/system/getProtocolAll?query=y',
            method: 'get',
            mode: 'remote',
            valueField: 'id',
            textField: 'text'
        });
    })

    function searchData() {

        var systemId = $("#systemId").val();
        var systemAb = $("#systemAb").val();
        var systemChineseName = $("#systemChineseName").val();
        var principal1 = $("#principal1").val();
        var featureDesc = $("#featureDesc").val();
        var protocolId = $("#protocolIdSearch").combobox('getValue');

        var queryParams = $('#tg').datagrid('options').queryParams;
        queryParams.systemId = systemId;
        queryParams.systemChineseName = encodeURI(systemChineseName);
        queryParams.systemAb = systemAb;
        queryParams.principal1 = principal1;
        queryParams.featureDesc = featureDesc;
        queryParams.protocolId = protocolId;

        $('#tg').datagrid('options').queryParams = queryParams;//传递值
        $("#tg").datagrid('reload');//重新加载table
    }
</script>

</body>
</html>