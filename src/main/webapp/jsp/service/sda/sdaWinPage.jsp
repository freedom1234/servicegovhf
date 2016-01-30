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
</head>
<body>
<div id="sdaContextMenu" class="easyui-menu" style="width:120px;">
    <shiro:hasPermission name="sda-add">
        <div onclick="insert('insertBef')" data-options="iconCls:'icon-add'">在上方插入</div>
        <div onclick="insert('append')" data-options="iconCls:'icon-add'">插入子节点</div>
        <div onclick="insert('insertAft')" data-options="iconCls:'icon-add'">在下方插入</div>
    </shiro:hasPermission>
    <shiro:hasPermission name="sda-update">
        <div onclick="editIt()" data-options="iconCls:'icon-edit'">编辑</div>
    </shiro:hasPermission>
    <shiro:hasPermission name="sda-delete">
        <div onclick="removeIt()" data-options="iconCls:'icon-remove'">删除</div>
    </shiro:hasPermission>
    <%--<shiro:hasPermission name="sda-update">--%>
    <%--<div onclick="editIt()" data-options="iconCls:'icon-edit'">移动</div>--%>
    <%--</shiro:hasPermission>--%>
</div>
<form id="sdaForm">
    <table class="easyui-treegrid" id="tg" style=" width:auto;"
           data-options="
				rownumbers: true,
				animate: true,
				fitColumns: false,
				url: '/sda/sdaTree?serviceId=${param.serviceId }&operationId=${param.operationId }&t='+ new Date().getTime(),
				method: 'get',
				idField: 'id',
				treeField: 'text',
                onDblClickCell:onDblClickCell,
                onContextMenu:showContextMenu"
            >
        <thead>
        <tr>
            <th data-options="field:'id', checkbox:true">字段名</th>
            <th data-options="field:'text',width:200">字段名</th>
            <th data-options="field:'append1',width:120,align:'left'">字段别名</th>
            <th data-options="field:'append2',width:80">类型/长度</th>
            <th data-options="field:'append3', hidden:true">xpath</th>
            <%--<th field="append4" width="80" editor="{type:'combobox', options:{required:true, method:'get', data: getMetadataJson(), valueField:'metadataId',textField:'metadataId'}}">元数据</th>--%>
            <th field="append4" width="120" editor="{type:'combobox', options:{required:true}}">元数据</th>
            <th field="append5" width="80"
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
            <th data-options="field:'append6',width:150,editor:'text'">备注</th>
        </tr>
        </thead>
    </table>
</form>
<script type="text/javascript" src="/plugin/validate.js"></script>
<div id="dlg" class="easyui-dialog" closed="true" resizable="true"></div>
<script type="text/javascript">
    var editingId;
    var newIds = [];
    var delIds = [];
    function showContextMenu(e, row) {
        e.preventDefault();
        $(this).treegrid('select', row.id);
        $('#sdaContextMenu').menu('show', {
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
            editingId = node.id;
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

    function getSeq(nodeId){
        var children = $('#tg').treegrid('getChildren', nodeId);
        if(null != children && children.length > 0){
            return children[children.length -1].attributes + 1;
        }else{
            return 0;
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

                    node.serviceId = "${param.serviceId }";
                    node.operationId = "${param.operationId }";

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
                        t.treegrid({url: '/sda/sdaTree?serviceId=${param.serviceId }&operationId=${param.operationId }&t=' + new Date().getTime()});
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
            t.treegrid({url: '/sda/sdaTree?serviceId=${param.serviceId }&operationId=${param.operationId }&t=' + new Date().getTime()});
        }
        return result;
    }
    //点击确定按钮保存
    function addRelate(){
        if(undefined != editingId){
           var result = saveSDA();//保存sda信息
            if(!result){
                return false;
            }
        }
        var idanode = $('#mappingdatagrid').treegrid('getSelected');
        var sdanode = $('#tg').treegrid('getSelected');
        if(sdanode){
            changeIds.push(editingRowId);
            $('#mappingdatagrid').treegrid('update',{
                id: idanode.id,
                row: {
                    sdastructAlias: sdanode.append1,
                    sdametadataId: sdanode.append4,
                    sdarequired: sdanode.append5,
                    sdaconstraint: sdanode.append7,
                    sdaremark: sdanode.append6,
                    sdaxpath: sdanode.append3,
                    sdaid: sdanode.id
                }
            });
            $("#sdaDlg").dialog("close");
        }else{
            alert("没有选中元素!");
        }

    }
    function cancel() {
        if (editingId != undefined) {
            $('#tg').treegrid('cancelEdit', editingId);
            editingId = undefined;
        }
        $("#sdaDlg").dialog("close");
    }
    //弹出元数据选择界面(根据元数据新增操作)
    function insert(optType) {
        var node = $('#tg').treegrid('getSelected');
        if (node.text == "root" || node.parentId == null) {
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
            modal: true,
            top:$(document).scrollTop() + ($(window).height()-250) * 0.4
        });
    }
    //弹出元数据选择界面（编辑操作）
    function showMetadata(optType) {
        var urlPath = "/jsp/metadata/metadata_choose2.jsp?optType=" + optType;
        $('#dlg').dialog({
            title: '元数据',
            width: 770,
            left: 100,
            top:$(document).scrollTop() + ($(window).height()-250) * 0.4,
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
    $('#sdaFilter').searchbox({
        searcher: function (value, name) {
            if("" == value || null == value){
                $("#tg").treegrid({url:'/sda/sdaTree?serviceId=${param.serviceId }&operationId=${param.operationId }&_t=' + new Date().getTime()})
            }else{
                $.ajax({
                    type: "post",
                    async: false,
                    url: "/sda/querySDATree",
                    dataType: "json",
                    data: {"keyword" : value,
                        "serviceId" : "${param.serviceId }",
                        "operationId" : "${param.operationId }"
                    },
                    success: function (data) {
                        if(data){
                            $("#tg").treegrid("loadData", data);
                        }else{
                            $("#tg").treegrid("loadData", "");
                        }

                    }
                });
            }
        }
    });
</script>
</body>
</html>

