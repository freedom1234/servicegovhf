<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>列表页</title>
    <link rel="stylesheet" type="text/css" href="/resources/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="/resources/themes/icon.css">
    <link href="/resources/css/ui.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/resources/js/jquery.min.js"></script>
    <script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="/resources/js/ui.js"></script>
</head>

<body>
<fieldset>
    <legend>条件搜索</legend>
    <table border="0" cellspacing="0" cellpadding="0">
        <tr>
            <%--<th>接口ID</th>--%>
            <th><nobr>交易码</nobr></th>
            <td><input class="easyui-textbox" type="text" name="interfaceId" id="interfaceId"></td>
            <th><nobr>交易名称</nobr></th>
            <td><input class="easyui-textbox" type="text" name="interfaceName" id="interfaceName"></td>
            <th><nobr>服务码</nobr></th>
            <td><input class="easyui-textbox" type="text" name="serviceId" id="serviceId"></td>
            <th><nobr>服务名称</nobr></th>
            <td><input class="easyui-textbox" type="text" name="serviceName" id="serviceName"></td>
        </tr>
        <tr>
            <%--<th>交易属性标识</th>
            <td><input class="easyui-textbox" type="text" name="attribute" id="attribute"></td>
            <th>节点状态</th>
            <td><input class="easyui-textbox" type="text" name="status" id="status"></td>
            <th>版本号</th>
            <td><input class="easyui-textbox" type="text" name="status" id=""></td>--%>
            <th></th>
            <td align="right"><a href="#" id="search" class="easyui-linkbutton" iconCls="icon-search">搜索</a></td>
        </tr>
    </table>


</fieldset>
<table title="交易节点" id="invokeLinkeTable" style="height:440px; width:100%;">
    <thead>
    <tr>
        <th data-options="field:'productid',checkbox:true"></th>
        <th colspan="8">交易属性</th>
        <th colspan="4">节点属性</th>
        <th colspan="3">节点依赖信息</th>

    </tr>
    <tr>
        <th data-options="field:'productid',checkbox:true"></th>
        <th data-options="field:'interfaceId'" width="10%">接口ID</th>
        <th data-options="field:'ecode'" width="10%">交易代码</th>
        <th data-options="field:'interfaceName'" width="15%">交易名称</th>
        <th data-options="field:'serviceId'" width="10%">服务码</th>
        <th data-options="field:'serviceName'" width="15%">服务名称</th>
        <th data-options="field:'operationId'" width="5%">场景码</th>
        <th data-options="field:'operationName'" width="15%">场景名称</th>
        <th data-options="field:'invokeType'" width="6%">调用类型</th>

        <th data-options="field:'nodeType'" formatter='formatter.nodeType' width="10%">节点类型</th>
        <th data-options="field:'location'" formatter='formatter.location' width="10%">交易属性标识</th>
        <th data-options="field:'bussCategory'" width="10%">节点业务分类</th>
        <th data-options="field:'status'" width="10%">节点状态</th>

        <th data-options="field:'esbAccessPattern'" formatter='formatter.esbAccessPattern' width="10%">ESB调用方式</th>
        <th data-options="field:'condition'" width="10%">条件位</th>
        <th data-options="field:'connectionDesc'" width="10%">条件信息</th>
    </tr>
    </thead>
</table>
<div id="w" class="easyui-window" title="" data-options="modal:true,closed:true,iconCls:'icon-add'"
     style="width:500px;height:200px;padding:10px;">

</div>
<script type="text/javascript">
    var invokeId;
    var formatter = {
        esbAccessPattern: function (value, row, index) {
            if (value == 0) {
                return "穿透模式，ESB不负责任何处理";
            }
            if (value == 1) {
                return "代理模式，ESB负责服务适配";
            }
            if (value == 2) {
                return "寻址模式，ESB提供寻址功能，仅负责寻址、监控";
            }
            if (value == 4) {
                return "组合模式，ESB提供服务的组合";
            }
        },
        location: function (value, row, index) {
            if (value == 1) {
                return "本系统发起";
            }
            if (value == 2) {
                return "上游系统调用";
            }
        },
        nodeType: function (value, row, index) {
            if (value == 0) {
                return "消费方节点";
            }
            if (value == 1) {
                return "提供方节点";
            }
            if (value == 2) {
                return "复合型节点";
            }
        }
    };
    $(function () {
        $('#invokeLinkeTable').datagrid({
            rownumbers:true,
            singleSelect:true,
            url:'/serviceLink/getServiceLinkNode/system/<%=request.getParameter("systemId") %>',
            method:'get',
            toolbar:toolbar,
            pagination:true,
            pageSize:13,
            pageList: [13,30,50],
            onLoadSuccess:function(data){
                $.each(data.rows, function (index, item) {
                    var invokeType = item.invokeType;
                    if (invokeType == '0') {
                        item.invokeType = '提供者';
                    } else if (invokeType == '1') {
                        item.invokeType = '消费者';
                    }
                });
            },
            onLoadError: function (responce) {
                var resText = responce.responseText;
                if (resText.toString().indexOf("没有操作权限") > 0) {
//                    alert("没有权限！");
                    window.location.href = "/jsp/403.jsp";
                }
            }
        });
    });
    $("#search").click(function () {
        var queryParams = $('#invokeLinkeTable').datagrid('options').queryParams;
        queryParams.interfaceId = $("#interfaceId").textbox("getValue");
        queryParams.interfaceName = encodeURI($("#interfaceName").textbox("getValue"));
        queryParams.serviceId = $("#serviceId").textbox("getValue");
        queryParams.serviceName = encodeURI($("#serviceName").textbox("getValue"));
        if (queryParams.englishWord || queryParams.chineseWord || queryParams.esglisgAb || queryParams.remark) {
            $("#invokeLinkeTable").datagrid('options').queryParams = queryParams;//传递值
            $("#invokeLinkeTable").datagrid('reload');//重新加载table

        } else {
            $("#invokeLinkeTable").datagrid('reload');
        }
    });
    function selectTab(title, content) {
        var exsit = parent.$('#subtab').tabs('getTab', title);
        if (exsit == null) {
            parent.$('#subtab').tabs('add', {
                title: title,
                content: content
            });
        } else {
            parent.$('#subtab').tabs('update', {
                tab: exsit,
                options: {
                    content: content
                }
            });
            parent.$('#subtab').tabs('select', title);
        }
    }

    var toolbar = [];
    <shiro:hasPermission name="link-get">
    toolbar.push({
                text: '预览',
                iconCls: 'icon-qxfp',
                handler: function () {
                    var checkedItems = $('#invokeLinkeTable').datagrid('getChecked');
                    var checkedItem;
                    if (checkedItems != null && checkedItems.length > 0) {
                        if (checkedItems.length > 1) {
                            alert("请选择一个节点进行预览！");
                            return false;
                        }
                        else {
                            checkedItem = checkedItems[0];
                            var oldIE;
                            var ieVersion = "undefined" != typeof navigator && /MSIE\s([\d.]+)/.test(navigator.userAgent) ? new Number(RegExp.$1) : -1, oldIE = ieVersion > -1 && 9 > ieVersion;
                            var content = '<iframe scrolling="auto" frameborder="0"  src="/jsp/serviceLink2.0/serviceLink.jsp?sourceId=' + checkedItem.id + '" style="width:100%;height:100%;"></iframe>';
                            if(oldIE){
                                content = '<iframe scrolling="auto" frameborder="0"  src="/jsp/serviceLink/previewLink.jsp?sourceId='+checkedItem.id+'" style="width:100%;height:100%;"></iframe>';
                            }
//                        //测试
//                        content= '<iframe scrolling="auto" frameborder="0"  src="/jsp/serviceLink/previewLink.jsp?sourceId='+checkedItem.id+'" style="width:100%;height:100%;"></iframe>';
                            selectTab('预览', content);
                            selectTab('预览', content);
                        }
                    }
                    else {
                        alert("请选中要预览的节点！");
                    }


                }
            },
            {
                text: '血缘分析',
                iconCls: 'icon-qxfp',
                handler: function () {
                    var checkedItems = $('#invokeLinkeTable').datagrid('getChecked');
                    var checkedItem;
                    if (checkedItems != null && checkedItems.length > 0) {
                        if (checkedItems.length > 1) {
                            alert("请选择一个节点进行预览！");
                            return false;
                        }
                        else {
                            checkedItem = checkedItems[0];
                            var content = '<iframe scrolling="auto" frameborder="0"  src="/jsp/serviceLink/parentLink.jsp?sourceId=' + checkedItem.id + '" style="width:100%;height:100%;"></iframe>';
                            selectTab('血缘分析', content);
                            selectTab('血缘分析', content);
                        }
                    }
                    else {
                        alert("请选中要预览的节点！");
                    }
                }
            });
    </shiro:hasPermission>
    <shiro:hasPermission name="link-update">
    toolbar.push({
        text: '节点维护',
        iconCls: 'icon-qxfp',
        handler: function () {
            var checkedItems = $('#invokeLinkeTable').datagrid('getChecked');
            var checkedItem;
            if (checkedItems != null && checkedItems.length > 0) {
                if (checkedItems.length > 1) {
                    alert("请选择一个节点进行预览！");
                    return false;
                }
                else {
                    checkedItem = checkedItems[0];
                    var content = '<iframe scrolling="auto" frameborder="0"  src="/serviceLink/nodeInfo/' + checkedItem.id + '" style="width:100%;height:100%;"></iframe>';
                    selectTab('节点维护', content);
                    selectTab('节点维护', content);
                }
            }
            else {
                alert("请选中要预览的节点！");
            }
        }
    });
    </shiro:hasPermission>

//        ,{
//            text: '编辑节点',
//            iconCls: 'icon-qxfp',
//            handler: function () {
//                var checkedItems = $('#invokeLinkeTable').datagrid('getChecked');
//                var checkedItem;
//                if (checkedItems != null && checkedItems.length > 0) {
//                    if (checkedItems.length > 1) {
//                        alert("请选择一个节点进行预览！");
//                        return false;
//                    }
//                    else {
//                        checkedItem = checkedItems[0];
//                        invokeId = checkedItem.id;
//                        uiinit.win({
//                            w:500,
//                            iconCls:'icon-add',
//                            title:"编辑",
//                            url : "/jsp/serviceLink/serviceLinkModifyDialog.jsp"
//                        });
//
//                    }
//                }
//                else {
//                    alert("请选中要预览的节点！");
//                }
//            }
//        }
</script>

</body>
</html>