<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>列表页</title>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/resources/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/resources/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/resources/css/ui.css">
</head>

<body style="margin:0px;">

<div class="easyui-tabs" id="subtab">
    <div title="交易链路信息查询" style="padding:0px;">

    </div>
</div>

<script type="text/javascript" src="<%=basePath%>/resources/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/resources/js/ui.js"></script>
<script type="text/javascript">
    //页面拖动宽度自适应
    $('#subtab').tabs({
        border: false,
        border: false,
        width: "auto",
        height: $("body").height()
    });
    var tabUrl = "/jsp/serviceLink/systemLinkInfo.jsp?systemId=<%=request.getParameter("systemId")%>";
    var tabItem = $('#subtab').tabs('getTab','交易链路信息查询');
    $('#subtab').tabs('update', {
        tab: tabItem,
        options: {
            content: ' <iframe id="serviceLink"  scrolling="auto" frameborder="0"  src="' + encodeURI(encodeURI(tabUrl)) + '"  style="width:100%;height:99%;"></iframe>',
            fit:true
        }
    });
    var k = 0;
    $('#subtab').tabs({
        border: false,
        width: "auto",
        height: $("body").height(),
        onSelect: function (title, index) {
            var systemId = "<%=request.getParameter("systemId")%>";
            if (index == 1 && k == 0) {
                k++;
                var currTab = $('#subtab').tabs('getSelected');
                var url = '<iframe scrolling="auto" frameborder="0"  src="/jsp/serviceLink/systemLinkInfo.jsp?systemId=';
                url += systemId;
                url += 'style="width:100%;height:100%;"';
                $('#subtab').tabs('update', {
                    tab: currTab,
                    options: {
                        content: url
                    }
                });
            }
        }
    });
</script>
</body>
</html>