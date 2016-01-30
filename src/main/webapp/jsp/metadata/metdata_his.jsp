<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>列表页</title>
    <link rel="stylesheet" type="text/css" href="/resources/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="/resources/themes/icon.css">
    <link href="/resources/css/css.css" rel="stylesheet" type="text/css">
</head>

<body>
<form id="searchForm">
<fieldset>
    <legend>条件搜索</legend>
    <table border="0" cellspacing="0" cellpadding="0" heigth="auto">
        <tr>
            <th><nobr>元数据名称</nobr></th>
            <td><input class="easyui-textbox" type="text" style="width:100px" name="metadataId" id="metadataId"></td>
            <th><nobr>中文名称</nobr></th>
            <td><input class="easyui-textbox" type="text" style="width:100px" name="chineseName" id="chineseName">
            </td>
           <%-- <th>英文名称</th>--%>
            <td style="display: none"><input class="easyui-textbox" type="text" style="width:100px" name="metadataName" id="metadataName">
            </td>
            <%--<th style="text-align:right">别名</th>--%>
            <td style="display:none"><input class="easyui-textbox" type="text" style="width:100px" name="metadataAlias" id="metadataAlias">
            </td>
            <th><nobr>类别词</nobr></th>
            <td><input type="text" name="categoryWordId" id="categoryWordId" style="width: 100px"
                       class="easyui-combobox"
                       data-options="
				 		 method:'get',
				 		 valueField: 'esglisgAb',
				 		 textField: 'chineseWord',
				 		 onChange:function(newValue, oldValue){
							this.value=newValue;
						}
					"></td>
        </tr>
        <tr>
            <th><nobr> 创建人</nobr></th>
            <td><input class="easyui-textbox" style="width:100px" type="text" name="optUser" id="optUser"></td>
            <th><nobr> 创建起始日期</nobr></th>
            <td><input class="easyui-datebox" style="width:100px" type="text" name="startDate" id="startDate"></td>
            <th><nobr> 创建结束日期</nobr></th>
            <td><input class="easyui-datebox" style="width:100px" type="text" name="endDate" id="endDate"></td>
            <td align="right">
                <shiro:hasPermission name="metadata-get">
                <a href="#" id="queryMetadataBtn" class="easyui-linkbutton" iconCls="icon-search">搜索</a>
                <a href="#" id="clean" onclick="$('#searchForm').form('clear');" class="easyui-linkbutton" iconCls="icon-clear" style="margin-left:1em" >清空</a>
                </shiro:hasPermission>
            </td>
        </tr>
    </table>
</fieldset>
</form>
<table id="metadataList" title="元数据管理"
        style="height:620px; width:98%;">
    <thead>
    <tr>
        <th data-options="field:'',checkbox:true"></th>
        <th data-options="field:'metadataId'" width="12%">元数据名称</th>
        <th data-options="field:'chineseName'" width="12%">中文名称</th>
        <th data-options="field:'metadataName'" width="14%">英文全称</th>
        <%--<th data-options="field:'metadataName'">英文名称</th>--%>
        <th data-options="field:'categoryChineseWord'" width="8%">类别词</th>
        <th data-options="field:'type'" width="5%">类型</th>
        <th data-options="field:'length'" width="4%">长度</th>
        <th data-options="field:'scale'" width="3%">精度</th>
        <th data-options="field:'dataCategory'" width="10%">数据项分类</th>
        <%--<th data-options="field:'buzzCategory'">业务项分类</th>
        <th data-options="field:'bussDefine'">业务定义</th>
        <th data-options="field:'bussRule'">业务规则</th>
        <th data-options="field:'dataSource'">数据来源</th>--%>

        <%--<th data-options="field:'version'">版本号</th>--%>
        <th data-options="field:'status'" width="5%">状态</th>
        <th data-options="field:'optUser'" width="7%">创建人</th>
        <th data-options="field:'optDate'" width="15%">创建时间</th>
        <!--
        <th data-options="field:'  '">审核人</th>
        <th data-options="field:'  '">审核时间</th>
        -->
    </tr>
    </thead>
</table>
<div id="w" class="easyui-window" title="" data-options="modal:true,closed:true,iconCls:'icon-add'"
     style="width:500px;height:200px;padding:10px;">

</div>
<script type="text/javascript" src="/resources/js/jquery.min.js"></script>
<script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/resources/js/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="/resources/js/ui.js"></script>
<script type="text/javascript" src="/assets/metadata/metadataManager.js"></script>
<script type="text/javascript" src="/assets/metadata/metadata.js"></script>
<scirpt type="text/javascript" src="/resources/js/jquery.fileupload.js"></scirpt>
<script type="text/javascript" src="/resources/js/jquery.fileDownload.js"></script>
<script type="text/javascript">
    $(function(){
        $("#metadataList").datagrid({
            rownumbers:true,
            singleSelect:false,
            url:'/metadataHis/query',
            method:'get',
            pagination:true,
            pageSize:20,
            pageList: [20,30,50],
            fitColumns:'false',
            onLoadError: function (responce) {
                var resText = responce.responseText;
                if(resText.toString().indexOf("没有操作权限") > 0){
//                    alert("没有权限！");
                    window.location.href = "/jsp/403.jsp";
                }
            }
        });
    });

</script>


<script type="text/javascript">
    $(function () {
        $(".datagrid-cell-group").width("auto");
        $("#categoryWordId").combobox({
            panelHeight:'130px',
            url:'/metadata/categoryWord',
            method:'get',
            valueField: 'esglisgAb',
            textField: 'chineseWord',
            onChange:function(newValue, oldValue){
                this.value=newValue;
            }
        });
    })
</script>
</body>
</html>