<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
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
    <script type="text/javascript" src="/resources/js/jquery.min.js"></script>
    <script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
    </head>
<body>
<form id="searchForm">
    <div class="win-bbar" style="text-align:center"><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
                                                       onClick="javascript:$('#dlg').dialog('close');">取消</a><a href="javascript:void(0)" id="saveBtn"
                                                                                  onclick="saveAdd('${param.optType}')"
                                                                                  class="easyui-linkbutton"
                                                                                  iconCls="icon-save">确定</a></div>
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
						url:'/metadata/categoryWord',
				 		 method:'get',
				 		 valueField: 'englishWord',
				 		 textField: 'chineseWord',
				 		 onChange:function(newValue, oldValue){
							this.value=newValue;
						}
					"></td>
            <td align="right">
                <shiro:hasPermission name="metadata-get">
                    <a href="javascript:void(0)" onclick="queryMetadata()" id="queryMetadataBtn" class="easyui-linkbutton" iconCls="icon-search">搜索</a>
                    <a href="#" id="clean" onclick="$('#searchForm').form('clear');" class="easyui-linkbutton" iconCls="icon-clear" style="margin-left:1em" >清空</a>
                </shiro:hasPermission>
            </td>
        </tr>
    </table>
</fieldset>
</form>
<table id="metadataList" title="元数据列表"
        style="height:620px; width:100%;">
    <thead>
    <tr>
        <th data-options="field:'',checkbox:true"  width="5%"></th>
        <th data-options="field:'metadataId'" width="15%">元数据名称</th>
        <th data-options="field:'chineseName'" width="15%">中文名称</th>
        <th data-options="field:'metadataName'" width="20%">英文全称</th>
        <th data-options="field:'categoryChineseWord'" width="12%">类别词</th>
        <th data-options="field:'type'" width="12%">类型</th>
        <th data-options="field:'length', hidden:true" >长度</th>
        <th data-options="field:'scale', hidden:true" >精度</th>
        <th data-options="field:'status'" width="10%">状态</th>
    </tr>
    </thead>
</table>

<script type="text/javascript">
    $(function(){
        $("#metadataList").datagrid({
            rownumbers:true,
            singleSelect:true,
            url:'/metadata/query',
            method:'get',
            pagination:true,
            pageSize:20,
            pageList: [20,30,50],
            fitColumns:true,
            onLoadError: function (responce) {
                var resText = responce.responseText;
                if(resText.toString().indexOf("没有操作权限") > 0){
//                    alert("没有权限！");
                    window.location.href = "/jsp/403.jsp";
                }
            }
        });

        $("#categoryWordId").combobox({
            panelHeight:'130px',
            url:'/metadata/categoryWord',
            method:'get',
            valueField: 'englishWord',
            textField: 'chineseWord',
            onChange:function(newValue, oldValue){
                this.value=newValue;
            }
        });
    });

    function queryMetadata() {
        var queryMetadataCallBack = function queryMetadataCallBack(data){
            $('#metadataList').datagrid('loadData', data);
        };
        var params = {
            "metadataId" : $("#metadataId").textbox("getValue"),
            "chineseName" : encodeURI($("#chineseName").textbox("getValue")),
            "categoryWordId" : $("#categoryWordId").combobox("getValue"),
        };
        $("#metadataList").datagrid('options').queryParams = params;
        $("#metadataList").datagrid('reload');
    };
    function saveAdd(optType){
        if('insertBef' == optType){
            insertBef();
        }
        if('append' == optType){
            append();
        }
        if('insertAft' == optType){
            insertAft()
        }
    }
    function insertBef(){
        var node = $('#tg').treegrid('getSelected');
        var row =  $('#metadataList').datagrid('getSelected');
        editingId = ""+new Date().getTime();
        var typeStr = row.type;
        if(row.length){
            typeStr += "(" + row.length;
            if(row.scale){
                typeStr += "," + row.scale;
            }
            typeStr += ")";
        }
        newIds.push(editingId);
        $('#tg').treegrid('insert', {
            before: node.id,
            data: {
                id : editingId,
                text:row.metadataId,
                parentId : node.parentId,
                append1: row.chineseName,
                append2: typeStr,
                append3 : node.append3.substring(0,node.append3.lastIndexOf('/')) + "/"+row.metadataId,
                append4: row.metadataId,
                attributes:getSeq(node.id),
            }
        });
        $('#tg').treegrid('reloadFooter');
        $('#tg').treegrid('select', editingId);
        $('#dlg').dialog("close");
    }
    function append(){
        var row =  $('#metadataList').datagrid('getSelected');
        if(row){
            var uuid = "" + new Date().getTime();
            var node = $('#tg').treegrid('getSelected');
            var typeStr = row.type;
            if(row.length){
                typeStr += "(" + row.length;
                if(row.scale){
                    typeStr += "," + row.scale;
                }
                typeStr += ")";
            }
            $('#tg').treegrid('append',{
                parent: node.id,
                data: [{
                    id: uuid,
                    text:row.metadataId,
                    parentId:node.id,
                    append1: row.chineseName,
                    append2: typeStr,
                    append3: node.append3 + '/' + row.metadataId,
                    append4: row.metadataId,
                    attributes:getSeq(node.id)
                }]
            });
            editingId = uuid;
            newIds.push(uuid);
            $('#tg').treegrid('reloadFooter');
            $('#tg').treegrid('select', uuid);
            $('#dlg').dialog("close");
        }else{
            alert('请选中一行数据!');
        }
    }
    function insertAft(){
        var node = $('#tg').treegrid('getSelected');
        var row =  $('#metadataList').datagrid('getSelected');
        editingId = ""+new Date().getTime();
        var typeStr = row.type;
        if(row.length){
            typeStr += "(" + row.length;
            if(row.scale){
                typeStr += "," + row.scale;
            }
            typeStr += ")";
        }
        newIds.push(editingId);
        $('#tg').treegrid('insert', {
            after: node.id,
            data: {
                id : editingId,
                text:row.metadataId,
                parentId : node.parentId,
                append1: row.chineseName,
                append2: typeStr,
                append3 : node.append3.substring(0,node.append3.lastIndexOf('/')) + "/"+row.metadataId,
                append4: row.metadataId,
                attributes:getSeq(node.id)+1,
            }
        });
        $('#tg').treegrid('reloadFooter');
        $('#tg').treegrid('select', editingId);
        $('#dlg').dialog("close");
    }
    function getSeq(nodeId){
        var children = $('#tg').treegrid('getChildren', nodeId);
        if(null != children && children.length > 0){
            return children[children.length -1].attributes + 1;
        }else{
            return 0;
        }
    }
</script>
</body>
</html>