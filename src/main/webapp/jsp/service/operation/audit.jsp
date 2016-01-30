<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <base href="<%=basePath%>">

    <title>sda详细信息</title>
    <link rel="stylesheet" type="text/css"
          href="/resources/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="/resources/themes/icon.css">
    <link href="/resources/css/ui.css" rel="stylesheet" type="text/css">

    <script type="text/javascript" src="resources/js/jquery.min.js"></script>
    <script type="text/javascript" src="resources/js/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="/jsp/service/operation/operation.js"></script>
    <script type="text/javascript" src="/js/version/versionManager.js"></script>
</head>
<body>
<fieldset>
    <legend>基本信息</legend>
    <table border="0" cellspacing="0" cellpadding="0">
        <tr>
            <th>服务</th>
            <td>
                <input class="easyui-textbox" id="serviceId" value="${service.serviceName }"/>&nbsp;&nbsp;
                <a iconcls="icon-search" class="easyui-linkbutton l-btn l-btn-small" onclick="choseService('dlg')"
                   href="javascript:void(0)">选择服务</a>
            </td>
        </tr>
    </table>


</fieldset>

<fieldset>
    <legend>未审核场景列表</legend>
    <table id="operationAuditList" class="easyui-datagrid"
           data-options="
				rownumbers:true,
				singleSelect:false,
				url:'/operation/getAudits/${service.serviceId }',
				method:'get',
				pagination:true,
				toolbar:'#tb',
				pageSize:10"
           style="height:370px; width:auto;">
        <thead>
        <tr>
            <th data-options="field:'operationId',checkbox:true"></th>
            <th data-options="field:'operationName'" width="100">场景名称</th>
            <th data-options="field:'operationDesc'" width="460">功能描述</th>
            <th data-options="field:'version'" formatter='ff.version' width="60">版本号</th>
            <th data-options="field:'optDate'" width="130">更新时间</th>
            <th data-options="field:'optUser'" width="90">更新用户</th>
            <th data-options="field:' ',formatter:formatConsole" width="100">操作</th>
        </tr>
        </thead>
    </table>
    <div id="tb" style="padding:5px;height:auto">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td><a href="javascript:void(0)" onclick="auditPage(2);" class="easyui-linkbutton" iconCls="icon-cancel"
                       plain="true">审核不通过</a>&nbsp;&nbsp;
                    <a href="javascript:void(0)" onclick="auditPage(1);" class="easyui-linkbutton" iconCls="icon-ok"
                       plain="true">审核通过</a>
                </td>
            </tr>
        </table>
    </div>
</fieldset>


</div>
<div id="dlg" class="easyui-dialog"
     style="width:400px;height:280px;padding:10px 20px" closed="true"
     resizable="true"></div>

</body>
<script type="text/javascript">
    function auditPage(type) {
        var checkedItems = $('#operationAuditList').datagrid('getChecked');
        if (checkedItems != null && checkedItems.length > 0) {
            var text = type == 2 ? "不通过" : "通过";
            $('#dlg').dialog({
                title: '审核意见-' + text,
                width: 500,
                height: 300,
                left:300,
                closed: false,
                cache: false,
                href: '/jsp/service/operation/audit_remark.jsp?type=' + type,
                modal: true
            });
        } else {
            alert("没有选中数据！");
        }

    }

    function auditSave(type) {
        var checkedItems = $('#operationAuditList').datagrid('getChecked');
        if (checkedItems != null && checkedItems.length > 0) {
            var ids = [];
            $.each(checkedItems, function (index, item) {
                ids.push("" + item.operationId + "," + item.serviceId);
            });
            var auditRemark = encodeURI(encodeURI($("#auditRemark").val()));
            if (type == 2 && auditRemark == "") {
                alert("必须输入不通过原因!");
                return false;
            }
            var processId = parent.PROCESS_INFO.processId;
            var url = "/operation/auditSave?state=" + type + "&auditRemark=" + auditRemark;
            if (processId) {
                url = "/operation/auditSave/" + processId + "?state=" + type + "&auditRemark=" + auditRemark;
            }
            $.ajax({
                type: "post",
                async: false,
                contentType: "application/json; charset=utf-8",
                url: url,
                dataType: "json",
                data: JSON.stringify(ids),
                success: function (data) {
                    $("#dlg").dialog("close");
                    alert("操作成功");
                    $('#operationAuditList').datagrid('reload');
                }
            });
        }
        //如果有任务在执行，则更新任务的状态
        parent.PROCESS_INFO.approved = false;
    }
//操作按钮
	function formatConsole(value, row, index){
	    var versionId = "";
	     try {
                versionId = row.version.id;
         } catch (exception) {
         }
		var s = '<a iconcls="icon-search" onclick="comparePage(\'' + versionId + '\')" style="margin-top:1px;margin-bottom:1px;margin-left:5px;" class="easyui-linkbutton l-btn l-btn-small" href="javascript:void(0)" group="" id="cancelbtn'+value+'"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">版本对比</span><span class="l-btn-icon icon-search">&nbsp;</span></span></a>';
		return s;

	}

//弹出对比页面
		function comparePage(versionId){
			$.ajax({
				type: "get",
				async: false,
				url: "/versionHis/judgeVersionHis?versionId="+versionId,
				dataType: "json",
				success: function (data) {
					if(data.autoId != null){
						var urlPath = "/jsp/version/sdaComparePage.jsp?versionId="+  versionId + "&autoId1=&type=0&autoId2="+data.autoId;
						$("#dlg").dialog({
                                title: '版本对比',
                                left:'50px',
                                width: 1000,
                                height:'auto',
                                closed: false,
                                cache: false,
                                href: urlPath,
                                modal: true
                            });
					}else{
						alert("没有历史版本可以对比!");
					}
				}
			});

		}
</script>
</html>
