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
    <div title="服务基本信息" style="padding:0px;">
        <iframe id="serviceInfo" name="serviceInfo" scrolling="auto" frameborder="0"
                src="/service/serviceGrid?serviceId=<%=request.getParameter("serviceId") %>"
                style="width:100%;height:99%;"></iframe>
    </div>
    <div title="服务场景" style="padding:0px;">
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
    function closeTab(title) {
        $('#subtab').tabs('close', title);
    }
    var k = 1;
    var j = 1;
    var m = 1;
    var n = 1;
    var q = 1;
    var p = 1;
    $('#subtab').tabs({
        border: false,
        border: false,
        width: "auto",
        height: $("body").height(),
        onSelect: function (title, index) {
            if(index == 0){
//                $("#serviceInfo").$("#operationList").datagrid("reload");
                var urlPath = "/service/serviceGrid?serviceId=<%=request.getParameter("serviceId") %>&_t" + new Date().getTime();
                var currTab = $('#subtab').tabs('getSelected');
                $('#subtab').tabs('update', {
                    tab: currTab,
                    options: {
                        content: ' <iframe id="serviceInfo" name="serviceInfo" scrolling="auto" frameborder="0"  src="' + urlPath + '"  style="width:100%;height:99%;"></iframe>'
                    }
                });
            }
            if (index == 1 && k == 0) {
                var opId = serviceInfo.getSelected();
                if (opId != null) {
                    var urlPath = "/operation/editPage?serviceId=${param.serviceId}&operationId=" + opId+"&_t" + new Date().getTime();
                                var currTab = $('#subtab').tabs('getSelected');
                                $('#subtab').tabs('update', {
                                    tab: currTab,
                                    options: {
                                        content: ' <iframe scrolling="auto" frameborder="0"  src="' + urlPath + '"  style="width:100%;height:99%;"></iframe>',
                                        fit:true
                                    }
                                });
                }else {
                                     alert("请选则一个场景！");
                                     $('#subtab').tabs('select', '服务基本信息');
                                 }
            }
            if (index == 2 && j == 0) {
                var opId = serviceInfo.getSelected();
                if (opId != null) {
                    var urlPath = "/sda/sdaPage?serviceId=<%=request.getParameter("serviceId") %>&operationId=" + opId+"&t="+ new Date().getTime();
                    var currTab = $('#subtab').tabs('getSelected');
                    $('#subtab').tabs('update', {
                        tab: currTab,
                        options: {
                            content: ' <iframe scrolling="auto" frameborder="0"  src="' + urlPath + '"  style="width:100%;height:99%;"></iframe>',
                            fit:true
                        }
                    });
                } else {
                    alert("请选则一个场景！");
                    $('#subtab').tabs('select', '服务基本信息');
                }
            }

            if (index == 3 && m == 0) {
                var opId = serviceInfo.getSelected();
                if (opId != null) {
                    var urlPath = "/sla/slaPage?serviceId=<%=request.getParameter("serviceId") %>&operationId=" + opId+"&_t" + new Date().getTime();
                    var currTab = $('#subtab').tabs('getSelected');
                    $('#subtab').tabs('update', {
                        tab: currTab,
                        options: {
                            content: ' <iframe scrolling="auto" frameborder="0"  src="' + encodeURI(encodeURI(urlPath)) + '"  style="width:100%;height:99%;"></iframe>',
                            fit:true
                        }
                    });
                } else {
                    alert("请选则一个场景！");
                    $('#subtab').tabs('select', '服务基本信息');
                }
            }
            /*if (index == 4  && k == 0) {
                var opId = serviceInfo.getSelected();
                if (opId != null) {
                    var urlPath = "/ola/olaPage?serviceId=<%=request.getParameter("serviceId") %>&operationId=" + opId+"&_t" + new Date().getTime();
                    var currTab = $('#subtab').tabs('getSelected');
                    $('#subtab').tabs('update', {
                        tab: currTab,
                        options: {
                            content: ' <iframe scrolling="auto" frameborder="0"  src="' + encodeURI(encodeURI(urlPath)) + '"  style="width:100%;height:100%;"></iframe>'
                        }
                    });
                } else {
                    alert("请选则一个场景！");
                    $('#subtab').tabs('select', '服务基本信息');
                }
            }*/
            if (index == 4  && q == 0) {
                var opId = serviceInfo.getSelected();
                if (opId != null) {
                    var urlPath = "/operation/interfacePage?serviceId=${param.serviceId}&operationId=" + opId+"&_t" + new Date().getTime();
                    var currTab = $('#subtab').tabs('getSelected');
                    $('#subtab').tabs('update', {
                        tab: currTab,
                        options: {
                            content: ' <iframe scrolling="auto" frameborder="0"  src="' + encodeURI(encodeURI(urlPath)) + '"  style="width:100%;height:99%;"></iframe>',
                            fit:true
                        }
                    });
                } else {
                    alert("请选则一个场景！");
                    $('#subtab').tabs('select', '服务基本信息');
                }
            }
           /* if (index == 6  && p == 0) {
                var currTab = $('#subtab').tabs('getSelected');
                var urlPath = "jsp/service/search.jsp?_t" + new Date().getTime();
                $('#subtab').tabs('update', {
                    tab: currTab,
                    options: {
                        content: ' <iframe scrolling="auto" frameborder="0"  src="' + encodeURI(encodeURI(urlPath)) + '"  style="width:100%;height:100%;"></iframe>'
                    }
                });
            }*/
            //if(title+' is selected');

        }

    });
    k = 0;
    j = 0;
    m = 0;
    n = 0;
    q = 0;
    p = 0;
//页面拖动宽度自适应
    var tabUrl = "/service/serviceGrid?serviceId=<%=request.getParameter("serviceId") %>";
    var tabItem = $('#subtab').tabs('getTab','服务基本信息');
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
