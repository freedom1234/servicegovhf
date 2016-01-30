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
        <th data-options="field:'optUser'" width="7%">操作用户</th>
        <th data-options="field:'optDate'" width="8%">操作时间</th>
        <th data-options="field:'remark'" width="7%">备注</th>
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
<div id="opDialog" class="easyui-dialog"
     style="width:700px;height:280px;padding:10px 20px" closed="true"
     resizable="true"></div>
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
            url:'/metadata/query',
            method:'get',
            toolbar:toolbar,
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
    var toolbar = [];
    <shiro:hasPermission name="metadata-add">
    toolbar.push({
        text: '新增',
        iconCls: 'icon-add',
        handler: function () {
            uiinit.win({
                top:"20px",
                left:"150px",
                w: 500,
                iconCls: 'icon-add',
                title: "新增元数据",
                url: "/assets/metadata/add.jsp"
            });
        }
    });
    </shiro:hasPermission>
    <shiro:hasPermission name="metadata-update">
    toolbar.push({
        text: '修改',
        iconCls: 'icon-edit',
        handler: function () {
            var checkedItems = $('#metadataList').datagrid('getChecked');
            var checkedItem;
            if (checkedItems != null && checkedItems.length > 0) {
                if (checkedItems.length > 1) {
                    alert("请只选中一行要修改的数据！");
                    return false;
                }
                else {
                    checkedItem = checkedItems[0];
                    uiinit.win({
                        w: 500,
                        top:"20px",
                        left:"150px",
                        iconCls: 'icon-edit',
                        title: "修改元数据",
                        url: "/metadata/editPage?metadataId=" + checkedItem.metadataId
                    });
                }
            }
            else {
                alert("请选中要修改的数据！");
            }
        }
    });
    </shiro:hasPermission>
    <shiro:hasPermission name="metadata-delete">
    toolbar.push({
        text: '删除',
        iconCls: 'icon-remove',
        handler: function () {
            deleteObj();
        }
    });
    </shiro:hasPermission>
    <shiro:hasPermission name="metadata-get">
    toolbar.push({
        text: '关联服务场景',
        iconCls: 'icon-cfp',
        handler: function () {
            var checkedItems = $('#metadataList').datagrid('getChecked');
            if (checkedItems != null && checkedItems.length > 0) {
                if (checkedItems.length > 1) {
                    alert("请只选中一行元数据！");
                    return false;
                }
                else{
                    $.ajax({
                        type: "get",
                        async: false,
                        url: "/operation/judgeByMetadataId/"+checkedItems[0].metadataId,
                        dataType: "json",
                        success: function (data) {
                            if(data){
                                uiinit.win({
                                    w: 800,
                                    iconCls: 'icon-cfp',
                                    title: "关联服务场景",
                                    url: "/jsp/service/operation/list.jsp?metadataId="+checkedItems[0].metadataId + "&_t=" + new Date().getTime()
                                });
                            }
                            else{
                                alert("元数据["+checkedItems[0].chineseName+"]没有关联任何服务场景");
                            }
                        }
                    });
                }
            }
            else {
                alert("请先选中一个元数据！");
            }
        }
    });
    </shiro:hasPermission>
    <shiro:hasPermission name="metadata-get">
    toolbar.push({
        text: '过时数据',
        iconCls: 'icon-save',
        handler: function () {
            var content = '<iframe scrolling="auto" frameborder="0"  src="/jsp/metadata/metdata_his.jsp?_t=' + new Date().getTime() + '" style="width:100%;height:98%;"></iframe>';
            parent.$('#mainContentTabs').tabs('add', {
                title: "过时元数据",
                content: content,
                closable: true,
                fit: true
            });
        }
    });
    </shiro:hasPermission>
    <%--<shiro:hasPermission name="importMetadata-update">--%>
    <%--toolbar.push({--%>
        <%--text: '导入',--%>
        <%--iconCls: 'icon-cfp',--%>
        <%--handler: function () {--%>
            <%--uiinit.win({--%>
                <%--w: 500,--%>
                <%--iconCls: 'icon-cfp',--%>
                <%--title: "导入元数据",--%>
                <%--url: "/jsp/metadata/importMetadata.jsp"--%>
            <%--});--%>
        <%--}--%>
    <%--});--%>
    <%--</shiro:hasPermission>--%>
    <shiro:hasPermission name="exportXML-get">
    toolbar.push({
        text: '导出XML',
        iconCls: 'icon-save',
        handler: function () {
            $(function() {
                $.fileDownload("/metadata/export", {
                });
            });
        }
    });
    </shiro:hasPermission>
    <shiro:hasPermission name=" exportMetadataExcel-get">
    toolbar.push({
        text: '导出EXCEL',
        iconCls: 'icon-save',
        handler: function () {
            $(function() {
                $.fileDownload("/resourceExporter/export", {
                });
            });
        }
    });
    </shiro:hasPermission>
//    toolbar.push({
//        text: '发布版本',
//        iconCls: 'icon-save',
//        handler: function () {
//            var urlPath = "/metadataVersion/releasePage";
//            $('#opDialog').dialog({
//                title: '版本发布',
//                width: 500,
//                left:200,
//                closed: false,
//                cache: false,
//                href: urlPath,
//                modal: true
//            });
//        }
//    });
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