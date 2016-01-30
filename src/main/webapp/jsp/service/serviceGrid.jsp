<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>列表页</title>
    <link rel="stylesheet" type="text/css"
          href="/resources/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="/resources/themes/icon.css">
    <link href="/plugin/aehlke-tag-it/css/jquery.tagit.css" rel="stylesheet" type="text/css">
    <link href="/plugin/aehlke-tag-it/css/tagit.ui-zendesk.css" rel="stylesheet" type="text/css">
    <link href="/resources/css/ui.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/resources/js/jquery.min.js"></script>
    <script src="/newui/plugins/jQueryUI/jquery-ui.js" type="text/javascript" charset="utf-8"></script>
    <script src="/plugin/aehlke-tag-it/js/tag-it.js" type="text/javascript" charset="utf-8"></script>
    <script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="/resources/js/ui.js"></script>
    <script type="text/javascript" src="/js/service/export.js"></script>
    <script type="text/javascript" src="/resources/js/jquery.fileDownload.js"></script>
    <script type="text/javascript" src="/assets/tag/tagManager.js"></script>
<style type="text/css">
    <!--
    ul#tags li {
        font-size: 1.4em;
        font-weight: bold;
    }


    -->
</style>
</head>

<body>
<fieldset>
    <legend>条件搜索</legend>
    <table border="0" cellspacing="0" cellpadding="0" >
        <tr>
            <th><nobr>服务代码</nobr></th>
            <td><input class="easyui-textbox" disabled="true"
                       type="text" name="serviceId" value="${entity.serviceId }">
            </td>
            <th><nobr>服务名称</nobr></th>
            <td><input class="easyui-textbox" disabled="true"
                       type="text" name="serviceName" value="${entity.serviceName }">
            </td>
            <th><nobr>服务备注</nobr></th>
            <td><input class="easyui-textbox" disabled="true"
                       type="text" name="remark" value="${entity.remark }">
            </td>
        </tr>
        <tr>
            <th><nobr>服务功能描述</nobr></th>
            <td colspan="7"><input class="easyui-textbox" disabled="true" style="width:100%" type="text" name="desc" value="${entity.desc }"></td>
        </tr>
        <tr>
            <th><nobr>服务标签</nobr></th>
            <td><nobr>
                <ul id="tags"></ul>
                </nobr>
            </td>

            <th>
                <shiro:hasPermission name="service-update">
                <nobr>
                <a href="#" id="saveTagBtn" class="easyui-linkbutton" iconCls="icon-save" style="margin-left:1em">保存</a>
                </nobr>
                </shiro:hasPermission>
            </th>
            <td>&nbsp;</td>
        </tr>
    </table>


</fieldset>
<table id="operationList" class="easyui-datagrid" title="场景列表"
       data-options="
			rownumbers:true,
			singleSelect:false,
			<%--fitColumns:true,--%>
			url:'/operation/query?serviceId=${entity.serviceId }',
			method:'get',
			toolbar:toolbar,
			onDblClickCell:onDblClickCell,
			pagination:true,
				pageSize:10"
       style="height:370px; width:100%;">
    <thead>
    <tr>
        <th data-options="field:'',checkbox:true,width:50"></th>
        <th data-options="field:'operationId',width:60">场景代码</th>
        <th data-options="field:'operationName'">场景名称</th>
        <th data-options="field:'operationDesc',width:300">场景功能描述</th>
        <th data-options="field:'consumers',width:150">消费者</th>
        <th data-options="field:'providers',width:150">提供者</th>
        <th data-options="field:'version'" >版本号</th>
        <th data-options="field:'versionRemark', width:80" >版本说明</th>
        <th data-options="field:'optState'"  formatter='formatter.operationState'>状态</th>
        <th data-options="field:'optDate',width:120">更新时间</th>
        <th data-options="field:'optUser', width:80">更新用户</th>
    </thead>
</table>
<div id="w" class="easyui-window" title=""
     data-options="modal:true,closed:true,iconCls:'icon-add'"
     style="width:500px;height:200px;padding:10px;"></div>
<div id="opDialog" class="easyui-dialog"
     style="width:400px;height:280px;padding:10px 20px" closed="true"
     resizable="true"></div>
</body>
<script type="text/javascript">
    /*$(function(){
        $('.descText') textarea.textbox-text {
            white-space: pre-wrap;
        }
    });*/

    var formatter = {
        operationState: function (value, row, index) {
            if (value == 0) {
                return "<font color='green'>服务定义</font>";
            }
            if (value == 1) {
                return "<font color='green'>审核通过</font>";
            }
            if (value == 2) {
                return "<font color='red'>审核未通过</font>";
            }
            if (value == 3) {
                return "<font color='green'>已发布</font>";
            }
            if (value == 4) {
                return "<font color='green'>已上线</font>";
            }
            if (value == 5) {
                return "<font color='red'>已下线</font>";
            }
            if (value == 6) {
                return "<font color='red'>待审核</font>";
            }
            if (value == 7) {
                return "<font color='red'>修订</font>";
            }
            if (value == 8) {
                return "<font color='red'>已下线</font>";
            }
            if (value == 9) {
                return "<font color='red'>已废弃</font>";
            }
        },
        version: function (value, row, index) {
            try {
                return row.version.code
            } catch (exception) {
            }
        }
    };
    function selectTab(title, content) {
        var exsit = parent.$('#subtab').tabs('getTab', title);
        if (exsit == null) {
            parent.$('#subtab').tabs('add', {
                title: title,
                content: content
            });
        } else {
            parent.$('#subtab').tabs('update', {
                tab: exsit,
                options: {
                    content: content
                }
            });
        }
    }

    function reloadData() {
        $("#operationList").datagrid('reload');
    }

    function getSelected() {
        var checkedItems = $("#operationList").datagrid('getChecked');
        if (checkedItems != null && checkedItems.length > 0) {
            return checkedItems[0].operationId;
        }
        return null;
    }

    var toolbar = [];
    <shiro:hasPermission name="operation-add">
    toolbar.push({
        text: '新增',
        iconCls: 'icon-add',
        handler: function () {
            var opeAddContent = ' <iframe scrolling="auto" frameborder="0"  src="/operation/addPage/${entity.serviceId }"  style="width:100%;height:100%;"></iframe>'
            selectTab('服务场景', opeAddContent);
            //var sdaAddContent = ' <iframe scrolling="auto" frameborder="0"  src="/sda/sdaPPage?serviceId=${entity.serviceId }"  style="width:100%;height:100%;"></iframe>'
            //selectTab('服务接口SDO', '');

            //selectTab('服务SLA', '');
            //selectTab('服务OLA', '');
            //selectTab('服务接口隐射', '');
            parent.k++;
            parent.$('#subtab').tabs('select', '服务场景');

        }
    });
    </shiro:hasPermission>
    <shiro:hasPermission name="operation-update">
    toolbar.push({
        text: '修改',
        iconCls: 'icon-edit',
        handler: function () {
            var checkedItems = $('#operationList').datagrid('getChecked');
            var checkedItem;
            if (checkedItems != null && checkedItems.length > 0) {
                if (checkedItems.length > 1) {
                    alert("请只选中一行要修改的数据！");
                    return false;
                }
                else {
                    if( checkedItems[0].optState != 0 &&  checkedItems[0].optState != 7){
                        alert("只有服务定义和修订状态的场景才能修改");
                        return false;
                    }

                    var urlPath = "/operation/editPage?serviceId=${entity.serviceId }&operationId=" + checkedItems[0].operationId;
                    var opeEditContent = ' <iframe scrolling="auto" frameborder="0"  src="' + encodeURI(encodeURI(urlPath)) + '" style="width:100%;height:100%;"></iframe>'
                    selectTab('服务场景', opeEditContent);
                    parent.k++;

                    parent.$('#subtab').tabs('select', '服务场景');
                }
            }
            else {
                alert("请选中要修改的数据！");
            }

        }
    });
    </shiro:hasPermission>
    <shiro:hasPermission name="operation-delete">
    toolbar.push({
        text: '删除',
        iconCls: 'icon-remove',
        handler: function () {
            var flag = true;
            var checkedItems = $('#operationList').datagrid('getChecked');
            $.each(checkedItems, function (index, item) {
                if(item.optState == 3 && item.optState != 4){
                    alert("已上线和已发布的场景不能删除！");
                    flag = false;
                }
            });
            if(flag == false) return;
            if (checkedItems != null && checkedItems.length > 0) {
                if (confirm("确定要删除已选中的" + checkedItems.length + "项吗？一旦删除无法恢复！")) {
                    var ids = [];
                    $.each(checkedItems, function (index, item) {
                        var operationPK = {};
                        operationPK.serviceId = "${entity.serviceId }";
                        operationPK.operationId = item.operationId;
                        ids.push(operationPK);
                    });
                    $.ajax({
                        type: "post",
                        async: false,
                        contentType: "application/json; charset=utf-8",
                        url: "/operation/deletes",
                        dataType: "json",
                        data: JSON.stringify(ids),
                        success: function (data) {
                            if(data){
                                alert("操作成功");
                            }else{
                                alert("已上线和发布的场景不能删除");
                            }

                            $('#operationList').datagrid('reload');
                        }
                    });
                }
            } else {
                alert("没有选中项！");
            }
        }
    });
    </shiro:hasPermission>
    <shiro:hasPermission name="version-get">
    toolbar.push({
        text: '历史版本',
        iconCls: 'icon-qxfp',
        handler: function () {
            var checkedItems = $('#operationList').datagrid('getChecked');
            if (checkedItems != null && checkedItems.length > 0) {
                if(checkedItems.length > 1){
                    alert('请只选中一个要查看的场景！');
                    return;
                }
                var urlPath = "/operationHis/hisPage?serviceId=${entity.serviceId }";
                urlPath += "&operationId=" + checkedItems[0].operationId;
                var opeHisContent = ' <iframe scrolling="auto" frameborder="0"  src="' + encodeURI(encodeURI(urlPath)) + '" style="width:100%;height:100%;"></iframe>'

                parent.parent.addTab('历史场景', opeHisContent);
            }else{
                alert('没有选中数据哦！');
            }

        }
    });
    </shiro:hasPermission>
    <shiro:hasPermission name="version-add">
    toolbar.push({
        text: '发布版本',
        iconCls: 'icon-qxfp',
        handler: function () {
            var checkedItems = $('#operationList').datagrid('getChecked');
            if (checkedItems != null && checkedItems.length > 0) {
                if (checkedItems.length > 1) {
                    alert("请只选中一个要发布的场景！");
                    return false;
                }
                else {
                    if (checkedItems[0].optState == "1") {
                        var urlPath = "/jsp/service/operation/release.jsp?operationName=" + encodeURI(encodeURI(checkedItems[0].operationName)) + "&versionCode=" + encodeURI(checkedItems[0].version) + "&operationId=" + encodeURI(checkedItems[0].operationId);
                        $('#opDialog').dialog({
                            title: '版本发布',
                            width: 500,
                            closed: false,
                            cache: false,
                            href: urlPath,
                            modal: true
                        });
                    }
                    else {
                        alert("请先通过审核再发布!")
                        return false;
                    }

                }
            }
            else {
                alert("请选中要发布的场景！");
            }
        }
    });
    </shiro:hasPermission>
    <shiro:hasPermission name="operation-commit">
    toolbar.push({
        text: '提交审核',
        iconCls: 'icon-audit',
        handler: function(){
            var url = "";
            var items = $('#operationList').datagrid('getSelections');
            if (items != null && items.length > 0) {
                for(var i = 0; i < items.length; i++){
                    if(items[i].optState != 0 && items[i].optState != 7){
                        alert("只有服务定义状态和修订状态的服务能提交审核");
                        return;
                    }
                    if (!confirm("确定要提交审核吗？")) {
                        return;
                    }
                    $.ajax({
                        "type": "POST",
                        "async": false,
                        "contentType": "application/json; charset=utf-8",
                        "url": "/operation/submitToAudit",
                        "data": JSON.stringify(items),
                        "dataType": "json",
                        "success": function (result) {
                            if(result){
                                $('#operationList').datagrid('reload');
                            }
                        }
                    });
                }
            }else{
                alert('没有选中数据哦！');
            }
        }
    });
    </shiro:hasPermission>
    <shiro:hasPermission name="version-check">
    toolbar.push({
        text: '审核',
        iconCls: 'icon-audit',
        handler: function () {
            var urlPath = "/operation/auditPage?serviceId=${entity.serviceId }";
            var items = $('#operationList').datagrid('getData').rows;
            var flag = false;
            if (items != null && items.length > 0) {
                for(var i = 0; i < items.length; i++){
                    if(items[i].optState == 6){
                        flag = true;
                        break;
                    }
                }
            }
            if(!flag){
                alert("该服务下没有要审核的数据！");
                return false;
            }
            var opeAuditContent = ' <iframe scrolling="auto" frameborder="0"  src="' + urlPath + '" style="width:100%;height:98%;"></iframe>'

            parent.parent.$('#mainContentTabs').tabs('add', {
                title: '服务场景审核',
                content: opeAuditContent,
                closable: true
            });
        }
    });
    </shiro:hasPermission>
    <shiro:hasPermission name="operation-revise">
    toolbar.push({
        text: '修订',
        iconCls: 'icon-audit',
        handler: function () {
            //审核通过，已上线，已发布 已废弃可以变为修订状态
            var url = "";
            var items = $('#operationList').datagrid('getSelections');
            if (items != null && items.length > 0) {
                for(var i = 0; i < items.length; i++){
                    if(items[i].optState == 1 || items[i].optState == 2 || items[i].optState == 3 || items[i].optState == 4|| items[i].optState == 9){

                    }else{
                        alert("审核通过，审核不通过，已上线，已发布，已废弃状态的服务才能进行修订");
                        return;
                    }
                }

                if (!confirm("确定要修订吗？")) {
                    return;
                }
                $.ajax({
                    "type": "POST",
                    "async": false,
                    "contentType": "application/json; charset=utf-8",
                    "url": "/operation/revise",
                    "data": JSON.stringify(items),
                    "dataType": "json",
                    "success": function (result) {
                        if(result){
                            $('#operationList').datagrid('reload');
                        }
                    }
                });
            }else{
                alert('没有选中数据哦！');
            };
        }
    });
    </shiro:hasPermission>
    <shiro:hasPermission name="operation-quit">
    toolbar.push({
        text: '下线',
        iconCls: 'icon-audit',
        handler: function () {
            //已上线，已发布 可以变为下线状态
            var url = "";
            var items = $('#operationList').datagrid('getSelections');
            if (items != null && items.length > 0) {
                for(var i = 0; i < items.length; i++){
                    if( items[i].optState == 4){

                    }else{
                        alert("已上线状态的服务才能下线");
                        return;
                    }
                }
                if (!confirm("确定要下线吗？")) {
                    return;
                }
                $.ajax({
                    "type": "POST",
                    "async": false,
                    "contentType": "application/json; charset=utf-8",
                    "url": "/operation/quit",
                    "data": JSON.stringify(items),
                    "dataType": "json",
                    "success": function (result) {
                        if(result){
                            $('#operationList').datagrid('reload');
                        }
                    }
                });
            }else{
                alert('没有选中数据哦！');
            }
        }
    });
    </shiro:hasPermission>

//    废弃
    toolbar.push({
        text: '废弃',
        iconCls: 'icon-audit',
        handler: function () {
            var url = "";
            var items = $('#operationList').datagrid('getChecked');
            if (items != null && items.length > 0) {
                if (confirm("确定要废弃已选中的" + items.length + "项吗？")) {
                    $.ajax({
                        "type": "POST",
                        "async": false,
                        "contentType": "application/json; charset=utf-8",
                        "url": "/operation/drop",
                        "data": JSON.stringify(items),
                        "dataType": "json",
                        "success": function (result) {
                            if(result){
                                alert("操作成功");
                                $('#operationList').datagrid('reload');
                            }else{
                                alert("操作失败");
                            }
                        }
                    });
                }
            }else{
                alert("没用选中数据哦！")
            }
        }
    });

    <shiro:hasPermission name="excelExport-get">
    toolbar.push({
        text:'导出EXCEL',
        iconCls:'icon-excel-export',
        handler: function () {
            var checkedItems = $('#operationList').datagrid('getChecked');
            if (checkedItems != null && checkedItems.length > 0) {
                var form=$("<form>");//定义一个form表单
                form.attr("style","display:none");
                form.attr("target","");
                form.attr("method","post");
                form.attr("action","/excelExporter/exportOperation");

                for(var i=0; i < checkedItems.length; i++){
                    var input1=$("<input>");
                    input1.attr("type","hidden");
                    input1.attr("name","pks["+i+"].serviceId");
                    input1.attr("value",checkedItems[i].serviceId);
                    var input2=$("<input>");
                    input2.attr("type","hidden");
                    input2.attr("name","pks["+i+"].operationId");
                    input2.attr("value",checkedItems[i].operationId);

                    form.append(input1);
                    form.append(input2);
                }

                $("body").append(form);//将表单放置在web中
                form.submit();//表单提交
            }
            else{
                alert("没有选中数据！");
            }
        }
    });
    </shiro:hasPermission>
    <shiro:hasPermission name="exportConfig-get">
    <%--toolbar.push({--%>
        <%--text: '导出配置文件',--%>
        <%--iconCls: 'icon-qxfp',--%>
        <%--handler: function () {--%>

            <%--var checkedItems = $('#operationList').datagrid('getChecked');--%>
            <%--if (checkedItems != null && checkedItems.length > 0) {--%>
                <%--if (checkedItems.length > 1) {--%>
                    <%--alert("请只选中一个要导出的场景！");--%>
                    <%--return false;--%>
                <%--} else {--%>
                    <%--var urlPath = "/jsp/service/export_config.jsp?serviceId=${entity.serviceId }&operationId=" + checkedItems[0].operationId;--%>
                    <%--uiinit.win({--%>
                        <%--w: 500,--%>
                        <%--iconCls: 'icon-add',--%>
                        <%--title: "配置导出",--%>
                        <%--modal: true,--%>
                        <%--url:urlPath--%>
                    <%--});--%>

                <%--}--%>
            <%--} else {--%>
                <%--alert("请选中要导出的场景！");--%>
            <%--}--%>

        <%--}--%>
    <%--});--%>
    toolbar.push({
        text:'导出配置&nbsp;&nbsp;&nbsp;',
        iconCls:'icon-excel-export',
        handler: function () {
            var checkedItems = $('#operationList').datagrid('getChecked');
            if (checkedItems != null && checkedItems.length > 0) {
                $.ajax({
                    "type": "POST",
                    "async": false,
                    "contentType": "application/json; charset=utf-8",
                    "url": "/export/getConfigVo",
                    "data": JSON.stringify(checkedItems),
                    "dataType": "json",
                    "success": function (result) {
                        if(result && result.length > 0){
                            configResult = result;
                            $('#opDialog').dialog({
                                title: '导出配置',
                                width: 1000,
                                left:50,
                                closed: false,
                                cache: false,
                                href: "/jsp/service/export_config_list.jsp",
                                modal: true,
                                onLoad:function(){
                                    $("#choosedList").datagrid("loadData", configResult);
                                }
                            });
                        }else{
                            alert("没有可导出的配置！");
                        }
                    }
                });

            }
            else{
                alert("没有选中数据！");
            }
        }
    })
    </shiro:hasPermission>

    //版本发布
    function releaseOp(desc, operationId) {
        $('#opDialog').dialog('close');
        var urlPath = "/operation/release?serviceId=${entity.serviceId }&operationId=" + operationId + "&versionDesc=" + desc;
        var opeReleaseContent = ' <iframe scrolling="auto" frameborder="0"  src="' + encodeURI(encodeURI(urlPath)) + '" style="width:100%;height:100%;"></iframe>'
        selectTab('服务场景', opeReleaseContent);
        parent.k++;
        parent.$('#subtab').tabs('select', '服务场景');

    }

    $(function () {
        var serviceId = "${entity.serviceId}";
        /**
         *  初始化接口标签
         * @param result
         */
        var initTags = function initTags(result){
            for(var i = 0; i < result.length; i++){
                var tag = result[i];
                $("#tags").append("<li>" + tag.tagName + "</li>");
            }
            $("#tags").tagit();

        };
        tagManager.getTagForService(serviceId,initTags);

        $("#saveTagBtn").click(function () {
            var tagNames = $("#tags").tagit("assignedTags");
            var tags = [];
            for(var i = 0; i < tagNames.length; i++){
                 var tagName = tagNames[i];
                 var tagToAdd = {};
                 tagToAdd.tagName = tagName;
                 tags.push(tagToAdd);
            }
            tagManager.addTagForService(serviceId, tags, function (){
                alert("标签保存成功");
            });
        });

    });

    function onDblClickCell(rowIndex, field,value){
        var texts = '<div style="word-wrap:break-word" >'+value+'</div>';
        $.messager.show({
            title:'详细',
            msg:texts,
            showType:'show',
            height:'auto'
        });
    }
</script>

</body>
</html>

