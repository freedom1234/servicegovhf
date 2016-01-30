<%@ page contentType="text/html; charset=utf-8" language="java"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>列表页</title>
    <link rel="stylesheet" type="text/css" href="/resources/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="/resources/themes/icon.css">
    <link href="/resources/css/ui.css" rel="stylesheet" type="text/css">
</head>
<body class="easyui-layout">
<div class="easyui-tabs" id="subtab" data-options="region:'center',border:false" style="width:100%;height:100%;">
    <div title="公共代码" style="padding:0px;">
        <iframe scrolling="auto" frameborder="0"  src="/jsp/SGEnum/commonEnum.jsp" style="width:100%;height:100%;"></iframe>
    </div>
</div>

<script type="text/javascript" src="/resources/js/jquery.min.js"></script>
<script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/resources/js/ui.js"></script>
<script type="text/javascript">
    var processId = "${param.processId}";
    var taskId = "${param.taskId}";
</script>
</body>
</html>