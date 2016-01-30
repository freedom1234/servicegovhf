<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <base href="<%=basePath%>">

    <title>sda信息</title>

    <link rel="stylesheet" type="text/css" href="/resources/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="/resources/themes/icon.css">
    <link href="/resources/css/ui.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/resources/js/jquery.min.js"></script>
    <script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="/resources/js/treegrid-dnd.js"></script>

    <script type="text/javascript" src="/resources/js/ui.js"></script>
    <script type="text/javascript" src="/js/version/versionManager.js"></script>
    <script type="text/javascript" src="/resources/js/datagrid-detailview.js"></script>
</head>
<body>
<div id="mm" class="easyui-menu" style="width:120px;">
    <shiro:hasPermission name="sda-add">
        <%--<div onclick="append()" data-options="iconCls:'icon-add'">新增</div>--%>
        <div onclick="insert('insertBef')" data-options="iconCls:'icon-add'">在上方插入</div>
        <div onclick="insert('append')" data-options="iconCls:'icon-add'">插入子节点</div>
        <div onclick="insert('insertAft')" data-options="iconCls:'icon-add'">在下方插入</div>
    </shiro:hasPermission>
    <shiro:hasPermission name="sda-update">
        <div onclick="editIt()" data-options="iconCls:'icon-edit'">编辑</div>
        <%--<div onclick="addLevel()" data-options="iconCls:'icon-edit'">升级</div>--%>
    </shiro:hasPermission>
    <shiro:hasPermission name="sda-delete">
        <div onclick="removeIt()" data-options="iconCls:'icon-remove'">删除</div>
    </shiro:hasPermission>
    <%--<shiro:hasPermission name="sda-update">--%>
        <%--<div onclick="editIt()" data-options="iconCls:'icon-edit'">移动</div>--%>
    <%--</shiro:hasPermission>--%>
    <shiro:hasPermission name="sda-update">
        <div onclick="appendAttribute()" data-options="iconCls:'icon-edit'">添加属性</div>
    </shiro:hasPermission>
</div>
<fieldset>
    <legend>基础信息</legend>
    <table border="0" cellspacing="0" cellpadding="0">
        <tr style="width:100%;">
            <th>
                <nobr>服务代码</nobr>
            </th>
            <td><input class="easyui-textbox" disabled type="text" name="serviceId" value="${service.serviceId }"
                       style="width:100px"></td>
            <th>
                <nobr>服务名称</nobr>
            </th>
            <td><input class="easyui-textbox" disabled type="text" name="serviceName" value="${service.serviceName }"
                       style="width:250px"></td>
            <th>
                <nobr>场景号</nobr>
            </th>
            <td><input class="easyui-textbox" disabled type="text" name="operationId" value="${operation.operationId }"
                       style="width:50px"></td>
            <th>
                <nobr>场景名称</nobr>
            </th>
            <td><input class="easyui-textbox" disabled type="text" name="operationName"
                       value="${operation.operationName }" style="width:250px"></td>
            <th>
                <nobr>版本</nobr>
            </th>
            <td><input class="easyui-textbox" disabled type="text" name="operationName"
                       value="${operation.version.code }" style="width:50px"></td>
        </tr>

    </table>


</fieldset>
<form id="sdaForm">
    <table title="sda" class="easyui-treegrid" id="tg" style=" width:auto;"
           data-options="
				iconCls: 'icon-ok',
				rownumbers: true,
				animate: true,
				collapsible: true,
				fitColumns: true,
				url: '/sda/sdaTree?serviceId=${service.serviceId }&operationId=${operation.operationId }&t='+ new Date().getTime(),
				method: 'get',
				idField: 'id',
				treeField: 'text',
                toolbar:'#tb',
                onDblClickCell:onDblClickCell,
                onContextMenu:onContextMenu"
            >
        <thead>
        <tr>
            <th data-options="field:'text',width:140">字段名</th>
            <th data-options="field:'append1',width:60,align:'left'">字段别名</th>
            <th data-options="field:'append2',width:50">类型/长度</th>
            <th data-options="field:'append3',width:60, hidden:true">xpath</th>
            <%--<th field="append4" width="80" editor="{type:'combobox', options:{required:true, method:'get', data: getMetadataJson(), valueField:'metadataId',textField:'metadataId'}}">元数据</th>--%>
            <th field="append4" width="80" editor="{type:'combobox', options:{required:true}}">元数据</th>
            <th field="append5" width="40"
                editor="{type:'combobox',options:{url:'/jsp/service/sda/combobox_data.json',valueField:'id',textField:'text'}}">
                是否必输
            </th>
            <!--
               <th data-options="field:'append6',width:80,formatter:formatConsole">备注</th>
               -->
            <%--<th field ="append7" width="80" editor="{type:'combobox',options:{url:'/serviceHead/queryByHeadIds?headIds=${operation.headId}', method:'get', valueField:'headId',textField:'headName'}}">约束条件</th>--%>
            <th field="append7" width="80"
                <%--editor="{type:'combobox',options:{data:[{headId:'sys_head', headName:'sys_head'}, {headId:'app_head', headName:'app_head'}], method:'get', valueField:'headId',textField:'headName'}}">--%>
                editor="{type:'combobox',options:{url:'/serviceHead/queryAll', method:'get', valueField:'headId',textField:'headName'}}">
                约束条件
            </th>
            <th data-options="field:'append6',width:80,editor:'text'">备注</th>
            <th data-options="field:'append10',width:30,formatter:attrFormat">属性</th>
        </tr>
        </thead>
    </table>
    <div id="tb" style="padding:5px;height:auto">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td>
                    <shiro:hasPermission name="sda-update">
                        <a href="javascript:void(0)" onclick="moveUp()" class="easyui-linkbutton" iconCls="icon-up"
                           plain="true">上移</a>&nbsp;&nbsp;
                        <a href="javascript:void(0)" onclick="moveDown()" class="easyui-linkbutton" iconCls="icon-down"
                           plain="true">下移</a>&nbsp;&nbsp;
                        <!--
                        <a href="javascript:void(0)" onclick="addNode()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a>&nbsp;&nbsp;
                        -->
                        <a href="javascript:void(0)" onclick="saveSDA()" class="easyui-linkbutton" iconCls="icon-save"
                           plain="true">保存</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="sda-get">
                        <a href="javascript:void(0)" onclick="comparePage()" class="easyui-linkbutton"
                           iconCls="icon-save" plain="true">版本对比</a>
                    </shiro:hasPermission>
                </td>
                <td align="right"></td>
            </tr>
        </table>
    </div>
</form>
<script type="text/javascript" src="/plugin/validate.js"></script>
<div id="dlg" class="easyui-dialog" closed="true" resizable="true"></div>
<script type="text/javascript">
    var editingId;
    var newIds = [];
    var delIds = [];
    function onContextMenu(e, row) {
        e.preventDefault();
        $(this).treegrid('select', row.id);
        $('#mm').menu('show', {
            left: e.pageX,
            top: e.pageY
        });
    }
    function removeIt() {
        var node = $('#tg').treegrid('getSelected');
        if (node.text == "root" || node.text == "response" || node.text == "request") {
            alert("请选择其他节点！");
            return false;
        }
        if (node) {

            delIds.push(node.id);
            $('#tg').treegrid('remove', node.id);
        }
    }
    function editIt() {
        var row = $('#tg').treegrid('getSelected');
        if (row.text == "root" || row.text == "response" || row.text == "request") {
            alert("请选择其他节点！");
            return false;
        }

        if (row) {
            editingId = row.id
            var contains = false;//判断之前是否已经将此节点加入newIds
            for (var i = 0; i < newIds.length; i++) {
                if (editingId == newIds[i]) {
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                newIds.push(editingId);
            }
            $('#tg').treegrid('beginEdit', editingId);
            var ed = $('#tg').treegrid('getEditor',
                    {id: editingId, field: 'append4'});
            $(ed.target).combobox({
                onShowPanel: function () {
                    $('#tg').treegrid('select', row.id);
                    showMetadata('edit');
                    $(ed.target).combobox('hidePanel');
                }
            });
            $(ed.target).combobox('setValue', row.append4);
        }
    }

    function saveSDA() {
        if (!confirm("确定保存吗？")) {
            return;
        }
        if (!$("#sdaForm").form('validate')) {
            return false;
        }
        var t = $('#tg');
        if (editingId != undefined) {
            var editNodes = [];
            for (var i = 0; i < newIds.length; i++) {
                var editNode = t.treegrid('find', newIds[i]);
                if (editNode != null) {
                    t.treegrid('endEdit', editNode.id);
                    var node = {};
                    node.id = editNode.id;
                    node.structName = editNode.text;
                    node.parentId = editNode.parentId;

                    node.serviceId = "${service.serviceId }";
                    node.operationId = "${operation.operationId }";

                    node.structAlias = editNode.append1;
                    node.type = editNode.append2;
//						node.length = editNode.append3;
                    node.metadataId = editNode.append4;
                    node.required = editNode.append5;
                    node.remark = editNode.append6;
                    node.constraint = editNode.append7;
                    node.seq = editNode.attributes;
                    node.xpath = editNode.append3;

                    editNodes.push(node);
                }
            }

            editingId = undefined;
            var result = false;
            $.ajax({
                type: "post",
                async: false,
                contentType: "application/json; charset=utf-8",
                url: "/sda/saveSDA",
                dataType: "json",
                data: JSON.stringify(editNodes),
                success: function (data) {
                    if (data) {
                        newIds = [];
                        result = true;
                        //t.treegrid('reload');
                    } else {
                        result = false;
                        alert("只有服务定义状态和修订状态能进行修改");
                        t.treegrid({url: '/sda/sdaTree?serviceId=${service.serviceId }&operationId=${operation.operationId }&t=' + new Date().getTime()});
                        return false;
                    }
                }
            });
        }
        if (delIds.length > 0) {
            $.ajax({
                type: "post",
                async: false,
                contentType: "application/json; charset=utf-8",
                url: "/sda/deleteSDA",
                dataType: "json",
                data: JSON.stringify(delIds),
                success: function (data) {
                    if (data) {
                        delIds = [];
                        result = true
                    }
                }
            });
        }
        if (result) {
            alert("保存成功");
            t.treegrid({url: '/sda/sdaTree?serviceId=${service.serviceId }&operationId=${operation.operationId }&t=' + new Date().getTime()});
        }
    }
    function cancel() {
        if (editingId != undefined) {
            $('#tg').treegrid('cancelEdit', editingId);
            editingId = undefined;
        }
    }
    function formatConsole(value) {


        var s = '<a iconcls="icon-close" onclick="cancel()" style="display:none;margin-top:5px;margin-bottom:5px;margin-left:5px;" class="easyui-linkbutton l-btn l-btn-small" href="javascript:void(0)" group="" id="cancelbtn' + value + '"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">取消</span><span class="l-btn-icon icon-cancel">&nbsp;</span></span></a>';
        s += '<a iconcls="icon-ok" onclick="saveSDA()" style="display:none;margin-top:5px;margin-bottom:5px;margin-left:5px;" class="easyui-linkbutton l-btn l-btn-small" href="javascript:void(0)" group="" id="okbtn' + value + '"><span class="l-btn-left l-btn-icon-left"><span class="l-btn-text">保存</span><span class="l-btn-icon icon-ok">&nbsp;</span></span></a>';
        return s;

    }
    function attrFormat(value,row,index) {
        if(value == "true") {
            var s = '<img src="/resources/themes/icons/edit_add.png" onclick="showAttr()"/> ';
            return s;
        }else{
            return '';
        }
    }
    //节点上移
    function moveUp() {
        var node = $('#tg').treegrid('getSelected');

        if (node != null) {
            //判断是否是第一个节点
            var parentNode = $('#tg').treegrid('getParent', node.id);
            var brothers = $('#tg').treegrid('getChildren', parentNode.id);
            if (node.id == brothers[0].id) {
                alert("已经在顶部！");
                return false;
            }
            $.ajax({
                type: "get",
                url: "/sda/moveUp?_t=" + new Date().getTime(),
                dataType: "json",
                data: {"id": node.id},
                success: function (data) {
                    if (data) {
                        $('#tg').treegrid({url: '/sda/sdaTree?serviceId=${service.serviceId }&operationId=${operation.operationId }&t=' + new Date().getTime()});
                    }
                }
            });
        }

    }

    function moveDown() {
        var node = $('#tg').treegrid('getSelected');
        if (node != null) {
            //判断是否是第一个节点
            var parentNode = $('#tg').treegrid('getParent', node.id);
            var brothers = $('#tg').treegrid('getChildren', parentNode.id);
            if (node.id == brothers[brothers.length - 1].id) {
                alert("已经在底部！");
                return false;
            }
            $.ajax({
                type: "get",
                url: "/sda/moveDown?_t=" + new Date().getTime(),
                dataType: "json",
                data: {"id": node.id},
                success: function (data) {
                    if (data) {
                        $('#tg').treegrid({url: '/sda/sdaTree?serviceId=${service.serviceId }&operationId=${operation.operationId }&t=' + new Date().getTime()});

                    }
                }
            });
        }

    }
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
    //跳转到对比页面
    function comparePage() {
        $.ajax({
            type: "get",
            async: false,
            url: "/versionHis/judgeVersionHis?versionId=${operation.versionId}",
            dataType: "json",
            success: function (data) {
                if (data.autoId != null) {
                    var urlPath = "/jsp/version/sdaComparePage.jsp?autoId1=&type=0&autoId2=" + data.autoId + "&versionId=${operation.versionId }";
                    $("#dlg").dialog({
                        title: '版本对比',
                        left: '50px',
                        width: 1000,
                        height: 'auto',
                        closed: false,
                        cache: false,
                        href: urlPath,
                        modal: true
                    });
                } else {
                    alert("没有历史版本可以对比!");
                }
            }
        });

    }
    //弹出元数据选择界面(根据元数据新增操作)
    function insert(optType) {
        var node = $('#tg').treegrid('getSelected');
        if (node.text == "root" || ((node.text == "request" || node.text == "response") && "append" != optType)) {
            alert("请选择其他节点！");
            return false;
        }
        var urlPath = "/jsp/metadata/metadata_choose.jsp?optType=" + optType;
        $('#dlg').dialog({
            title: '元数据',
            width: 770,
            left: 100,
            closed: false,
            href: urlPath,
            modal: true
        });
    }
    //弹出元数据选择界面（新增、编辑操作）
    function showMetadata(optType) {
        var urlPath = "/jsp/metadata/metadata_choose2.jsp?optType=" + optType;
        $('#dlg').dialog({
            title: '元数据',
            width: 770,
            left: 100,
            top:$(document).scrollTop() + ($(window).height()-250) * 0.5,
            closed: false,
            href: urlPath,
            modal: true
        });
    }
    //双击单元格弹出
    function onDblClickCell(field, row){
        var texts = '<div style="word-wrap:break-word" >'+row[field]+'</div>';
        $.messager.show({
            title:'详细',
            msg:texts,
            showType:'show',
            height:'auto'
        });
    }
    function addLevel(){
        var node = $("#tg").treegrid("getSelected");
        var parent = $("#tg").treegrid("getParent", node.id);
        if(parent.text == "root" || parent.text == "response" || parent.text == "request"){
            alert("已经到达顶层，不能够继续升级!");
            return false
        }
        alert(parent.parentId)
        $("#tg").treegrid("update", {
            id: node.id,
            row: {
                text:'1234',
                parentId: parent.parentId
            }
        });
        $("#tg").treegrid("refresh", parent.parentId);
        $("#tg").treegrid("select", node.id);
    }
    //添加属性
    function appendAttribute(){
        var row = $('#tg').treegrid('getSelected');
        var urlPath = "/jsp/service/sda/sdaAttributeAdd.jsp?sdaId=" + row.id;
        $('#dlg').dialog({
            title: '添加属性',
            width: 770,
            left: 100,
            closed: false,
            href: urlPath,
            modal: true
        });
    }
    //展示属性子表
    function showAttr(){
        var row = $('#tg').treegrid('getSelected');
        var urlPath = "/jsp/service/sda/sdaAttributeList.jsp?sdaId=" + row.id;
        $('#dlg').dialog({
            title: row.text + '属性列表',
            width: 770,
            left: 100,
            closed: false,
            href: urlPath,
            modal: true
        });
    }
</script>
</body>
</html>
