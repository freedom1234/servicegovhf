<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>列表页</title>
    <link rel="stylesheet" type="text/css"
          href="/resources/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="/resources/themes/icon.css">
    <link href="/resources/css/ui.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/resources/js/jquery.min.js"></script>
    <script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
</head>

<body style="margin:0px;">

<div class="easyui-tabs" id="reuseRateTab" name="subtab" data-options="tools:'#tab-tools'">
    <div title="系统复用率" style="padding:0px;">

    </div>

    <div title="服务复用率" style="padding:0px;">

    </div>
</div>
    <script type="text/javascript">
        //页面拖动宽度自适应
        $('#reuseRateTab').tabs({
            border: false,
            border: false,
            width: "auto",
            height: $("body").height()
        });
        var tabUrl = "/jsp/statistics/system_reuserate.jsp";
        var tabItem = $('#reuseRateTab').tabs('getTab','系统复用率');
        $('#reuseRateTab').tabs('update', {
            tab: tabItem,
            options: {
                content: ' <iframe id="serviceInfo"  scrolling="auto" frameborder="0"  src="' + encodeURI(encodeURI(tabUrl)) + '"  style="width:100%;height:99%;"></iframe>',
                fit:true
            }
        });
        var k = 1;
        $('#reuseRateTab').tabs({
            border: false,
            border: false,
            width: "auto",
            height: $("body").height(),
            onSelect: function (title, index) {
                if (index == 1 && k == 0) {
                    var currTab = $('#reuseRateTab').tabs('getSelected');
                    var urlPath= '/jsp/statistics/service_reuserate.jsp';
                    $('#reuseRateTab').tabs('update', {
                        tab: currTab,
                        options: {
                            content: ' <iframe scrolling="auto" frameborder="0"  src="' + urlPath + '"  style="width:100%;height:100%;"></iframe>',
                            fit:true
                        }
                    });
                }
            }

        });
        k = 0;
    </script>
</body>
</html>

