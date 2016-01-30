<%@ page language="java" pageEncoding="utf-8" %>
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
<%--<fieldset>
    <legend>
        条件搜索
    </legend>
    <table border="0" cellspacing="0" cellpadding="0">
        <tr>
            <th>
                协议名称
            </th>
            <td>
                <input class="easyui-textbox" type="text" id="protocolName">
            </td>

            <th>
                消息类型
            </th>
            <td>
                <input class="easyui-textbox" type="text" id="msgType">
            </td>
            <th>
                协议编码
            </th>
            <td>
                <input class="easyui-textbox" type="text" id="encoding">
            </td>
            <th>
                备注
            </th>
            <td>
                <input class="easyui-textbox" type="text" id="remark">
            </td>
            <td align="right">
                <a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="searchData();">搜索</a>
            </td>
        </tr>
    </table>


</fieldset>--%>
<table id="tg" style="height: 300px; width: 100%;" data-options="pageSize:5">
    <thead>
    <tr>
        <th data-options="field:'protocolName',width:'12%'">
            协议名称
        </th>
        <th data-options="field:'commuProtocol',width:'12%'">
            通讯协议
        </th>
        <th data-options="field:'msgType',width:'8%'">
            报文类型
        </th>
        <th data-options="field:'encoding',width:'8%'">
            协议编码
        </th>
        <th data-options="field:'timeout',width:'8%',align:'center'">
            超时时间
        </th>
        <th data-options="field:'isEncrypt',width:'8%'">
            加密
        </th>
        <th data-options="field:'isSync',width:'8%'">
            同步
        </th>
        <th data-options="field:'isLongCon',width:'8%'">
            链接
        </th>
        <th data-options="field:'remark',align:'right',width:'10%'">
            备注
        </th>
        <th data-options="field:'generatorId',width:'20%',align:'right'" formatter='formatter.generator'>
            生成类
        </th>
        <th data-options="field:'protocolId',align:'right',hidden:true">
            protocolId
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
    /*{
     text: '新增',
     iconCls: 'icon-add',
     handler: function () {
     sysManager.addProtocolPage();
     }
     },*/
    <shiro:hasPermission name="protocol-update">
    toolbar.push({
        text: '修改',
        iconCls: 'icon-edit',
        handler: function () {
            var node = $('#tg').datagrid("getSelected");
            if (node) {
                uiinit.win({
                    w: 500,
                    iconCls: 'icon-add',
                    title: "编辑协议",
                    url: "/protocol/edit/" + node.protocolId
                });
            } else {
                alert("请选择要修改的行");
            }
        }
    });
    </shiro:hasPermission>
    <%--<shiro:hasPermission name="protocol-delete">--%>
    <%--toolbar.push({--%>
        <%--text: '删除',--%>
        <%--iconCls: 'icon-remove',--%>
        <%--handler: function () {--%>
            <%--var node = $('#tg').datagrid("getSelected");--%>
            <%--if (node) {--%>
                <%--if (!confirm("确定要删除选中的记录吗？")) {--%>
                    <%--return;--%>
                <%--}--%>
                <%--$.ajax({--%>
                    <%--type: "GET",--%>
                    <%--contentType: "application/json; charset=utf-8",--%>
                    <%--url: "/protocol/delete/" + node.protocolId,--%>
                    <%--dataType: "json",--%>
                    <%--success: function (result) {--%>
                        <%--$('#tg').datagrid("reload");--%>
                    <%--},--%>
                    <%--complete: function (responce) {--%>
                        <%--var resText = responce.responseText;--%>
                        <%--if(resText.toString().indexOf("没有操作权限") > 0){--%>
                            <%--alert("没有权限！");--%>
                            <%--//window.location.href = "/jsp/403.jsp";--%>
                        <%--}--%>
                    <%--}--%>
                <%--});--%>
            <%--} else {--%>
                <%--alert("请选择要删除的行");--%>
            <%--}--%>
        <%--}--%>

    <%--});--%>
    <%--</shiro:hasPermission>--%>

    $(document).ready(function () {
        var href = window.location.href;
        var params = href.split("&");
        var protocol = params[0].split("=")[1];
        var url = "/protocol/getAll";
        if(protocol){
            url = "/protocol/get/" + protocol;
        }
        $('#tg').datagrid({
            title: '协议基本信息维护',
            iconCls: 'icon-edit',//图标
            width: '100%',
            height: '520px',
            collapsible: true,
            method: 'post',
            url: url,
            singleSelect: true,//是否单选
//            pagination: true,//分页控件
//            pageSize: 5,//每页显示的记录条数，默认为10
//            pageList: [5, 10, 15, 20],//可以设置每页记录条数的列表
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
    })

    function searchData() {

        var protocolName = $("#protocolName").val();
        var encoding = $("#encoding").val();
        var msgType = $("#msgType").val();
        var remark = $("#remark").val();

        var queryParams = $('#tg').datagrid('options').queryParams;
        queryParams.protocolName = protocolName;
        queryParams.encoding = encoding;
        queryParams.msgType = msgType;
        queryParams.remark = remark;
        $('#tg').datagrid('options').queryParams = queryParams;//传递值
        $("#tg").datagrid('reload');//重新加载table
    }
    var formatter = {
        generator: function (value, row, index) {
            try {
                return row.generator.name
            } catch (exception) {
            }
        }
    };
</script>

</body>
</html>