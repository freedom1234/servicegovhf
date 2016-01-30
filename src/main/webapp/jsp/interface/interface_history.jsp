<%@ page language="java" pageEncoding="utf-8" %>
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
    <link rel="stylesheet" type="text/css" href="/resources/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="/resources/themes/icon.css">
    <link href="/resources/css/ui.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/resources/js/jquery.min.js"></script>
    <script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="/resources/js/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="/js/version/versionManager.js"></script>
    <script type="text/javascript">
        var formatter = {
            interfaceState: function (value, row, index) {
                if (value == 0) {
                    return "<font color='green'>投产</font>";
                }
                if (value == 1) {
                    return "<font color='red'>废弃</font>";
                }
            },
            versionHis:function(value, row, index){
                try {
                    return row.versionHis.code
                } catch (exception) {
                }
            },
            versionHisDesc:function(value, row, index){
                try {
                    return row.versionHis.versionDesc
                } catch (exception) {
                }
            }
        };
        function operation(value, row, index){
            var s = '<a onclick="idaHis(\'' + row.autoId + '\')" class="easyui-linkbutton l-btn l-btn-small" href="javascript:void(0)" id="cancelbtn'+value+'">' +
                    '<span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">idaHis</span></span></a>&nbsp;&nbsp;'+
                    '<a onclick="comparePage(\'' + row.versionHisId + '\')" class="easyui-linkbutton l-btn l-btn-small" href="javascript:void(0)"  id="comparebtn'+value+'">' +
                    '<span class="l-btn-text">版本对比</span>&nbsp;</span></span></a>';
            return s;
        }
        function idaHis(value){
            $('#historyDlg').dialog({
                title: '接口定义信息',
                width: 600,
                height: 400,
                left:'150px',
                top:'50px',
                closed: false,
                cache: false,
                href: '/jsp/interface/interface_idaHis.jsp?interfaceHisId=' + value,
                modal: true
            });
        }
        //弹出对比页面
        function comparePage(autoId){
            $.ajax({
                type: "get",
                async: false,
                url: "/versionHis/judgeVersionPre?autoId="+autoId,
                dataType: "json",
                success: function (data) {
                    if(data.autoId != null){
                        var urlPath = "/jsp/version/idaComparePage.jsp?autoId1="+autoId+"&type=1&autoId2="+data.autoId+"&versionId="+ data.id;
                        $("#historyDlg").dialog({
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
                        alert("该版本为初始版本!");
                    }
                }
            });

        }
    </script>
    </head>
<body>

<table id="interfaceHisList" class="easyui-datagrid" data-options="
			rownumbers:true,
			singleSelect:true,
			fitColumns:true,
			url:'/interfaceHis/history/${param.interfaceId}',
			method:'get',
			pagination:true,
				pageSize:50"
       style="height:470px; width:100%;">
    <thead>
    <tr>
        <th data-options="field:'autoId',checkbox:true"> </th>
        <th data-options="field:'ecode',width:'10%'">
            交易码
        </th>
        <th data-options="field:'interfaceName',width:'15%'">
            交易名称
        </th>
        <th data-options="field:'versionId',width:'10%'" formatter='formatter.versionHis'>
            版本号
        </th>
        <th data-options="field:'desc',width:'10%'" formatter='formatter.versionHisDesc'>
            版本说明
        </th>
        <th data-options="field:'optDate',width:'15%',align:'center'">
            更新时间
        </th>
        <th data-options="field:'optUser',width:'10%'">
            更新用户
        </th>
        <th data-options="field:' ',width:'20%', formatter:operation">
            操作
        </th>

    </tr>
    </thead>
</table>
<div id="historyDlg" class="easyui-dialog" closed="true"
     resizable="true"></div>
</body>
</body>
</html>