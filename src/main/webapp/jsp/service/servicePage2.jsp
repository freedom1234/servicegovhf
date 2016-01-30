<%--用于'元数据-关联服务场景，接口-关联服务场景双击操作--%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
    <base href="<%=basePath%>">

    <title>服务页面</title>

    <link rel="stylesheet" type="text/css" href="/resources/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="/resources/themes/icon.css">
    <link href="/resources/css/ui.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/resources/js/jquery.min.js"></script>
    <script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="/resources/js/ui.js"></script>
</head>

<body style="margin:0px;">

<div class="easyui-tabs" id="subtab" name="subtab" data-options="tools:'#tab-tools'" style="width:100%">
    <div title="服务场景" style="padding:0px;">
        <iframe id="operationInfo" name="serviceInfo" scrolling="auto" frameborder="0"
                src="/operation/editPage?serviceId=${param.serviceId}&operationId=${param.operationId}"
                style="width:100%;height:99%;"></iframe>
    </div>
    <div title="服务SDA" style="padding:0px;">
    </div>
    <div title="服务SLA" style="padding:0px;">
    </div>
    <%--<div title="服务OLA" style="padding:0px;">
    </div>--%>
    <div title="服务接口映射" style="padding:0px;">
    </div>
    <%--<div title="服务检索" style="padding:0px;">
    </div>--%>
</div>

<script type="text/javascript">

    $('#subtab').tabs({
        border: false,
        border: false,
        width: "auto",
        height: $("body").height(),
        onSelect: function (title, index) {
            if(index == 1){
//                $("#serviceInfo").$("#operationList").datagrid("reload");
                var urlPath = "/sda/sdaPage?serviceId=${param.serviceId}&operationId=${param.operationId}&_t" + new Date().getTime();
                var currTab = $('#subtab').tabs('getSelected');
                $('#subtab').tabs('update', {
                    tab: currTab,
                    options: {
                        content: ' <iframe id="serviceInfo" name="serviceInfo" scrolling="auto" frameborder="0"  src="' + urlPath + '"  style="width:100%;height:99%;"></iframe>'
                    }
                });
            }
            if (index == 2) {
                var urlPath = "/sla/slaPage?serviceId=${param.serviceId}&operationId=${param.operationId}&t="+ new Date().getTime();
                var currTab = $('#subtab').tabs('getSelected');
                $('#subtab').tabs('update', {
                    tab: currTab,
                    options: {
                        content: ' <iframe scrolling="auto" frameborder="0"  src="' + urlPath + '"  style="width:100%;height:99%;"></iframe>',
                        fit:true
                    }
                });
            }

            if (index == 3) {
                var urlPath = "/operation/interfacePage?serviceId=${param.serviceId}&operationId=${param.operationId}&_t" + new Date().getTime();
                var currTab = $('#subtab').tabs('getSelected');
                $('#subtab').tabs('update', {
                    tab: currTab,
                    options: {
                        content: ' <iframe scrolling="auto" frameborder="0"  src="' + encodeURI(encodeURI(urlPath)) + '"  style="width:100%;height:99%;"></iframe>',
                        fit:true
                    }
                });
            }
        }

    });
//页面拖动宽度自适应
    var tabUrl = "/operation/editPage?serviceId=${param.serviceId}&operationId=${param.operationId}";
    var tabItem = $('#subtab').tabs('getTab','服务场景');
    $('#subtab').tabs('update', {
        tab: tabItem,
        options: {
            content: ' <iframe id="serviceInfo" name="serviceInfo"  scrolling="auto" frameborder="0"  src="' + encodeURI(encodeURI(tabUrl)) + '"  style="width:100%;height:99%;"></iframe>',
            fit:true
        }
    });
</script>
</body>
</html>
