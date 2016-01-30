<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
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

<body>
<form id="searchForm">
    <fieldset>
        <legend>条件搜索</legend>
        <table border="0" cellspacing="0" cellpadding="0">
            <tr>
                <th>接口代码</th>
                <td><input name="interfaceId" id="interfaceId" class="easyui-textbox" style="width:150px"/>
                </td>
                <th>接口名称</th>
                <td><input name="interfaceName" id="interfaceName" class="easyui-textbox" style="width:150px"/>
                </td>
                </th>
                <td></td>
            </tr>
            <tr>
                <th>提供者</th>
                <td><input name="providerId" id="providerId" class="easyui-combobox" style="width:150px"
                           data-options="
                 method:'get',
                 url:'/system/getSystemAll',
                 textField:'chineseName',
                 valueField:'id',
                 onChange:function(newValue, oldValue){
							this.value=newValue;
				    }
                 "
                        >
                </td>
                <th>消费者</th>
                <td><input name="consumerId" id="consumerId" class="easyui-combobox" style="width:150px"
                           data-options="
                 method:'get',
                 url:'/system/getSystemAll',
                 textField:'chineseName',
                 valueField:'id',
                 onChange:function(newValue, oldValue){
							this.value=newValue;
				    }
                 "
                        >
                </td>
                <th>

                </th>
                <td></td>
                <th>

                </th>
                <td></td>
                <th style="width:200px">
                    <a href="#" id="saveTagBtn" onclick="query()" class="easyui-linkbutton" iconCls="icon-search"
                       style="margin-left:1em">查询</a>
                    <a href="#" id="clean" onclick="$('#searchForm').form('clear');" class="easyui-linkbutton"
                       iconCls="icon-clear" style="margin-left:1em">清空</a>
                </th>
                <td></td>

            </tr>
        </table>


    </fieldset>
</form>
<div style="width:100%">
    <table id="resultList" class="easyui-datagrid"
           data-options="
			rownumbers:true,
			singleSelect:false,
			fitColumns:false,
			method:'get',toolbar:toolbar,
			pagination:true,
				"
           style="height:370px; width:100%;">
        <thead>
        <tr>
            <th data-options="field:'',checkbox:true,width:50"></th>
            <th data-options="field:'interfaceId',width:80">服务代码</th>
            <th data-options="field:'interfaceName',width:100">服务名称</th>
            <th data-options="field:'consumers',width:150">消费者</th>
            <th data-options="field:'providers',width:150">提供者</th>
        </tr>
        </thead>
    </table>
</div>
<div id="w" class="easyui-window" title=""
     data-options="modal:true,closed:true,iconCls:'icon-add'"
     style="width:500px;height:200px;padding:10px;"></div>
<div id="opDialog" class="easyui-dialog"
     style="width:400px;height:280px;padding:10px 20px" closed="true"
     resizable="true"></div>
</body>
<script type="text/javascript">
    $(document).ready(function () {
        $("#resultList").datagrid({url: "/operation/query"});
        query();
    });
    var toolbar = [

    ];
    function query() {
        var params = {
            "interfaceId": $("#interfaceId").textbox("getValue"),
            "interfaceName": encodeURI($("#interfaceName").textbox("getText")),
            "providerId": $("#providerId").combobox("getValue"),
            "consumerId": $("#consumerId").combobox("getValue")
        }
        $("#resultList").datagrid('options').queryParams = params;
        changePageList()
        $("#resultList").datagrid('reload');
    }
    function changePageList() {
        var p = $("#resultList").datagrid('getPager');
        var total = $(p).pagination("options").total;
        if (total < 100) {
            total = 100;
        }
        $(p).pagination({
            pageList: [10, 20, 50, total]
        });

    }
</script>

</body>
</html>

