<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>列表页</title>
    <link rel="stylesheet" type="text/css"
          href="/resources/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="/resources/themes/icon.css">
    <link href="/resources/css/css.css" rel="stylesheet" type="text/css">

    <script type="text/javascript" src="/resources/js/jquery.min.js"></script>
    <script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="/jsp/service/operation/operation.js"></script>
    <script type="text/javascript" src="/js/ida/idaManager.js"></script>
</head>

<body>
<fieldset>
    <div>
        <table border="0" cellspacing="0" cellpadding="0" width="98%">
            <tr>
                <th>
                    <nobr>服务代码</nobr>
                </th>
                <td><input class="easyui-textbox" type="text" name="1" id="1"
                           value="${service.serviceId }" disabled="disabled">
                </td>
                <th>
                    <nobr>服务名称</nobr>
                </th>
                <td><input class="easyui-textbox" type="text" name="2" id="2"
                           value="${service.serviceName }" disabled="disabled">
                </td>
                <th>
                    <nobr>场景号</nobr>
                </th>
                <td><input class="easyui-textbox" type="text" name="3" id="3"
                           value="${operation.operationId }" disabled="disabled">
                </td>
                <th>
                    <nobr>场景名称</nobr>
                </th>
                <td><input class="easyui-textbox" type="text" name="4" id="4"
                           value="${operation.operationName }" disabled="disabled">
                </td>
            </tr>
        </table>
    </div>
    <div>
        <table id="invokeList" style="height:150px; width:98%;">
            <thead>
            <tr>
                <%--<th data-options="field:'invokeId',checkbox:true"></th>--%>
                <th data-options="field:'systemId', width:'10%'">系统id</th>
                <th data-options="field:'systemChineseName', width:'15%'">系统名称</th>
                <th data-options="field:'isStandard', width:'10%', styler:standardStyle"
                    formatter='ff.isStandardText'>标准
                </th>
                <th data-options="field:'interfaceId', width:'15%', styler:standardStyle">接口id</th>
                <th data-options="field:'interfaceName', width:'15%', styler:standardStyle">接口名称</th>
                <th data-options="field:'type', width:'10%'"
                    formatter='ff.typeText'>类型
                </th>
                <th data-options="field:'desc', width:'10%'">描述</th>
                <th data-options="field:'remark', width:'10%'">备注</th>
            </tr>
            </thead>
        </table>

    </div>
    <%--<div id="tb" style="padding:5px;height:auto">
            <a href="javascript:void(0);" onclick="relateInterface()" class="easyui-linkbutton" iconCls="icon-save" plain="true">关联映射结果</a>
    </div>--%>
</fieldset>
<div id="mm" class="easyui-menu" style="width:120px;">
<shiro:hasPermission name="ida-update">
    <div onclick="insertBef()" data-options="iconCls:'icon-edit'">在上方插入</div>
    <div onclick="insertAft()" data-options="iconCls:'icon-edit'">在下方插入</div>
    <div onclick="beginRelate()" data-options="iconCls:'icon-edit'">关联字段映射</div>
    <div onclick="delRelate()" data-options="iconCls:'icon-edit'">删除字段映射</div>
    </shiro:hasPermission>
</div>
<div>
    <table title="字段映射操作" id="mappingdatagrid"
           class="easyui-treegrid"
           width="98%"
           data-options="
            iconCls:'icon-edit',
            rownumbers: false,
            animate: true,
            method: 'get',
            idField: 'id',
            treeField: 'structName',
            singleSelect:true,
            toolbar:mappingToolbar,
            onDblClickCell:onDblClickCell,
            onContextMenu: onContextMenu
        ">
        <thead>
        <tr>
            <th colspan="6">
                <b>接口信息</b>
            </th>
            <th></th>
            <th colspan="4">
                <b>SDA信息</b>
            </th>
        </tr>
        <tr>
            <th data-options="field:'id',checkbox:true"></th>
            <th data-options="field:'structName',width:'170',align:'left'">
                字段名称
            </th>
            <th data-options="field:'structAlias',width:'140',align:'left'">
                字段别名
            </th>
            <th data-options="field:'type',width:'100'">
                类型
            </th>
            <th data-options="field:'remark',title:'IDA备注',width:'100'"></th>
            <th data-options="field:'',width:'10',styler:splitStyle">
            </th>
            <th data-options="field:'sdastructAlias',width:'120', editor:{type:'combobox'}">
                对应SDA
            </th>
            <th data-options="field:'sdametadataId',width:'100'">
                元数据
            </th>

            <th data-options="field:'sdarequired',width:'60'" >是否必输</th>
            <th data-options="field:'sdaconstraint',width:'90'">约束条件</th>
            <th data-options="field:'sdaremark',title:'SDA备注',width:'150'"></th>
            <th data-options="field:'sdaxpath',hidden:true"></th>
            <th data-options="field:'sdaid',hidden:true"></th>
            <th data-options="field:'interfaceId',hidden:true"></th>
        </tr>
        </thead>
    </table>
</div>
<div id="sdaDlg" class="easyui-dialog"  data-options="toolbar:'#sdaBar'" closed="true"  resizable="true"></div>
<div id="sdaBar" >
    <input class="easyui-searchbox" id="sdaFilter" style="width:300px">&nbsp;&nbsp;
    <a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onClick="cancel()">取消</a>
    <a href="#" class="easyui-linkbutton" iconCls="icon-save" onclick="addRelate()">确定</a>
</div>
<script type="text/javascript">
    var editingRowId;
    var changeIds =[];
    $(function () {
        var url = '/serviceLink/getInterfaceByOSS?operationId=${operation.operationId }&serviceId=${service.serviceId }';
        $("#invokeList").datagrid({
            rownumbers: true,
            singleSelect: true,
            fixed: false,
            url: url,
            method: 'get',
            pagination: true,
            striped: true,
            pageSize: 10,
            onClickRow: function () {
                relateInterface();
            }
        });
    });
    var mappingToolbar = [];
    <shiro:hasPermission name="ida-update">
    mappingToolbar.push({
        text: '保存',
        iconCls: 'icon-save',
        handler: function () {
            $.ajax({
                type: "get",
                async: false,
                contentType: "application/json; charset=utf-8",
                url: "/operation/updateable",
                dataType: "json",
                data: {"serviceId": "${service.serviceId }",
                    "operationId":"${operation.operationId}"
                },
                success: function (data) {
                    if(data){
                        if(undefined != editingRowId){
                            $("#mappingdatagrid").treegrid("endEdit", editingRowId);//结束上次编辑
                        }
                        if(changeIds.length > 0){
                            var editData = []
                            for(var i=0; i < changeIds.length; i++){
                                var node = $("#mappingdatagrid").treegrid("find", changeIds[i]);
                                editData.push(node);
                            }
                            idaManager.saveIdaMapping(editData, function (result) {
                                if (result) {
                                    alert("保存成功！");
                                    $('#mappingdatagrid').treegrid('reload');
                                }
                            });
                            changeIds = [];
                        }else{
                            alert("没有改变任何数据!");
                        }
                    }else{
                        alert("该状态下不能保存修改!");
                    }
                }
            });

        }
    });
    </shiro:hasPermission>
    //根据已选中的接口关系在“接口映射”区域更新接口信息
    function relateInterface() {
        var item = $('#invokeList').treegrid('getSelected');
        if (item != null) {
            if (item.interfaceId == null || item.interfaceId == '') {
                alert("该系统没有关联接口！");
                return false;
            }
            else {
                $('#mappingdatagrid').treegrid({
                    url: '/ida/getIdaMapping/' + item.interfaceId + '/${operation.serviceId}/${operation.operationId}?time=' + (new Date()).valueOf()
                });
            }
        }
    }
    //标准样式判断
    function standardStyle(value, row, index) {
        if (row.isStandard == '99') {
            return 'background-color:#d9d2e9;color:white';
        }

    }
    //分割列样式
    function splitStyle(value, row, index) {
        return 'background-color:#cc0033';
    }
    //右键菜单
    function onContextMenu(e, row) {
        e.preventDefault();
        if (row.structName == 'root' || row.structName == 'request' || row.structName == 'response') {
            $('#mappingdatagrid').treegrid('unselect', row.id);
            return;
        }
        $(this).treegrid('select', row.id);
        $('#mm').menu('show', {
            left: e.pageX,
            top: e.pageY
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
    //插入行
    function insertBef(){
        var node = $('#mappingdatagrid').treegrid('getSelected');
        editingRowId = ""+new Date().getTime();
        $('#mappingdatagrid').treegrid('insert', {
            before: node.id,
            data: {
                id : editingRowId,
                seq : '' +node.seq,
                _parentId : node._parentId,
                interfaceId:node.interfaceId,
                structName : ''
            }
        });
        $('#mappingdatagrid').treegrid('select', editingRowId);
    }
    //插入行
    function insertAft(){
        var node = $('#mappingdatagrid').treegrid('getSelected');
        editingRowId = ""+new Date().getTime();
        $('#mappingdatagrid').treegrid('insert', {
            after: node.id,
            data: {
                id : editingRowId,
                seq : '' +(node.seq+1),
                _parentId : node._parentId,
                interfaceId:node.interfaceId,
                structName : ''
            }
        });
        $('#mappingdatagrid').treegrid('select', editingRowId);
    }
    //删除映射关系
    function delRelate(){
        var node = $('#mappingdatagrid').treegrid('getSelected');
        $('#mappingdatagrid').treegrid('update',{
            id: node.id,
            row: {
                sdastructAlias: null,
                sdametadataId: null,
                sdarequired: null,
                sdaconstraint: null,
                sdaremark: null,
                sdaxpath: null,
                sdaid: null
            }
        });
        editingRowId = node.id;
        changeIds.push(editingRowId);
    }
    //开始编辑关联行
    function beginRelate(){
        if(undefined != editingRowId){
            $("#mappingdatagrid").treegrid("endEdit", editingRowId);//结束上次编辑
        }
        var node = $('#mappingdatagrid').treegrid('getSelected');
        if ("root" != node.structName && "response" != node.structName && "response" != node.structName) {
            var parent = $('#mappingdatagrid').treegrid('getParent', node.id);//查询当前ida的父节点
            if(null == parent.sdaid || "" == parent.sdaid || undefined == parent.sdaid){//根据父节点查询对应sda
                alert("请先关联父节点！");
                return false;
            }
            editingRowId = node.id;
            showSDA();

//            $('#mappingdatagrid').treegrid('beginEdit', editingRowId);
//            //填充SDA列编辑器
//            var ed2 = $('#mappingdatagrid').treegrid('getEditor', {id: editingRowId, field: 'sdastructAlias'});
//            $(ed2.target).combobox({
//                onShowPanel: function () {
//                    showSDA();
//                    $(ed2.target).combobox('hidePanel');
//                }
//            });
//            $(ed2.target).combobox('setValue', node.sdastructAlias);
        }
    }
    //弹出sda选择窗口
    function showSDA(){
        var selectData = $('#mappingdatagrid').treegrid('getSelected');
        var urlPath = "/jsp/service/sda/sdaWinPage.jsp?serviceId=${service.serviceId}&operationId=${operation.operationId}&idaId="+selectData.id;
        $("#sdaDlg").dialog({
            title: 'SDA选择',
            width: 870,
            height:500,
            left: 100,
            top:$(document).scrollTop() + ($(window).height()-250) * 0.3,
            closed: false,
            href: urlPath,
            modal: true
        })
    }
</script>
</body>
</html>
