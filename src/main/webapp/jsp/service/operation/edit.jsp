<%@ page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
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
    <link href="/plugin/aehlke-tag-it/css/jquery.tagit.css" rel="stylesheet" type="text/css">
    <link href="/plugin/aehlke-tag-it/css/tagit.ui-zendesk.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/resources/js/jquery.min.js"></script>
    <script src="/newui/plugins/jQueryUI/jquery-ui.js" type="text/javascript" charset="utf-8"></script>
    <script src="/plugin/aehlke-tag-it/js/tag-it.js" type="text/javascript" charset="utf-8"></script>
    <script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="/resources/js/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="/resources/js/ui.js"></script>


    <script type="text/javascript" src="/assets/tag/tagManager.js"></script>
    <script type="text/javascript" src="/jsp/service/operation/operation.js"></script>
    <script type="text/javascript">
        var serviceId;
        var operationId;
        var state;
        $(function(){
            state = "${operation.state }";
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
            serviceId = $("#serviceId").attr("value");
            operationId = $("#operationId").textbox("getValue");
            tagManager.getTagForOperation(serviceId,operationId,initTags);
        })
        var toolbar = [];
        <shiro:hasPermission name="invoke-add">
        toolbar.push({
            text: '新增',
            iconCls: 'icon-add',
            handler: function () {
                if (!$("#operationForm").form('validate')) {
                    alert("请先完善基础信息!");
                    return false;
                }
                if( state != 0 &&  state != 7){
                    alert("只有服务定义和修订状态的场景才能修改");
                    return false;
                }
                $('#interfaceDlg').dialog({
                    title: '添加消费者-提供者关系',
                    width: 500,
                    height: 500,
                    closed: false,
                    cache: false,
                    href: '/jsp/service/operation/consumer_provider_add.jsp',
                    modal: true
                });
            }
        });

        </shiro:hasPermission>

        <shiro:hasPermission name="invoke-delete">
        toolbar.push({
            text: '删除',
            iconCls: 'icon-remove',
            handler: function () {
                if( state != 0 &&  state != 7){
                    alert("只有服务定义和修订状态的场景才能修改");
                    return false;
                }
//                $("#resultList").datagrid('get',{total:0,rows:[]});
                var row = $("#resultList").datagrid('getSelected');
                var index =  $("#resultList").datagrid('getRowIndex',row);
//                <!--
//                $.ajax({
//                    type: "POST",
//                    async: false,
//                    contentType: "application/json; charset=utf-8",
//                    url: "/serviceLink/deleteInvoke",
//                    dataType: "json",
//                    data : JSON.stringify(row),
//                    success: function (data) {
//                        $("#resultList").datagrid('deleteRow',index);
//                        alert("删除成功");
//                    }
//                });
//                -->
                $.ajax({
                    type: "POST",
                    async: false,
                    contentType: "application/json; charset=utf-8",
                    url: "/serviceLink/deleteInvoke2?id="+row.id,
                    dataType: "json",
                    success: function (data) {
                        $("#resultList").datagrid('deleteRow',index);
                        alert("删除成功");
                    }
                });
//                invokeList = new Array();
            }
        });
        </shiro:hasPermission>
        var systemList = ${systemList};
        var consumerList = new Array();
        var providerList = new Array();
        var invokeList = ${invokeList};
        /*在接口选完服务提供者消费者关系点击确定执行*/
        function addInterfaceContent(){
            var consumerNames = "";
            var consumerNameIds = "";
            var standardConsumerNames = "";
            var standardConsumerIds = "";
            for(var i = 0; i < consumerList.length; i++){
                var csi = consumerList[i];
                if(i > 0){
                    consumerNames += ", ";
                    consumerNameIds += ", ";
                }
                consumerNames += csi.systemChineseName;
                consumerNameIds += csi.systemId;
                if(csi.interfaceId == ""){
                    if(i > 0){
                        standardConsumerNames += ",";
                        standardConsumerIds += ",";
                    }
                    standardConsumerNames += csi.systemChineseName;
                    standardConsumerIds += csi.systemId;
                }
                invokeList.push(csi);
            }
            var standardProviderNames = "";
            var standardProviderIds = "";
            for(var i = 0; i < providerList.length; i++){
                var psi = providerList[i];
                if(psi.interfaceId == ""){
                    if(i > 0){
                        standardProviderNames += ", ";
                        standardProviderIds += ", ";
                    }
                    standardProviderNames += psi.systemChineseName;
                    standardProviderIds += psi.systemId;
                }
                invokeList.push(psi);
            }
            for(var i = 0; i < providerList.length; i++){
                var psi = providerList[i];
                if(psi.interfaceId != ""){//如果提供者是非标准接口
                    //添加一条记录
                    var row =  genderRow(consumerNames, consumerNameIds, psi.systemChineseName, psi.systemId, psi.interfaceName, psi.interfaceId);
                    $("#resultList").datagrid("appendRow", row);
                }else{//如果是标准接口
                    for(var j = 0; j < consumerList.length; j++){
                        var csi = consumerList[j];
                        if(csi.interfaceId != ""){ //如果消费者是非标接口
                            var row = genderRow(csi.systemChineseName, csi.systemId, standardProviderNames, standardProviderIds, csi.interfaceName, csi.interfaceId);
                            $("#resultList").datagrid("appendRow", row);
                        }
                    }
                }
            }
            if(standardConsumerIds !="" && standardProviderIds !=""){
                var row = genderRow(standardConsumerNames, standardConsumerIds, standardProviderNames, standardProviderIds, "", "");
                $("#resultList").datagrid("appendRow", row);
            }
            consumerList = new Array();
            providerList = new Array();
            $("#interfaceDlg").dialog("close");
        }
        function genderRow(consumerNames, consumerIds, providerNames, providerIds, interfaceName, interfaceId){
            var row = {};
            row.consumers= consumerNames;
            row.consumerIds= consumerIds;
            row.providers= providerNames;
            row.providerIds= providerIds;
            row.interfaceName= interfaceName;
            row.interfaceId= interfaceId;
            return row;
        }
        function consumerStyle(value, row, index) {
            if(row.conIsStandard == '99'){
                return 'background-color:#d9d2e9;color:white';
            }
        }
        function providerStyle(value, row, index) {
            if(row.proIsStandard == '99'){
                return 'background-color:#d9d2e9;color:white';
            }
        }
    </script>

</head>

<body>
<form class="formui" id="operationForm">
    <div class="win-bbar" style="text-align:center">
        <shiro:hasPermission name="operation-update">
            <a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onClick="clean()">取消</a>
            <a href="#" onclick="save('operationForm',1)" class="easyui-linkbutton" iconCls="icon-save">保存</a>
        </shiro:hasPermission>
    </div>
    <div class="easyui-panel" title="基本信息" style="width:100%;height:auto;padding:10px;">
        <input type="hidden" name="versionId" value="${operation.versionId }" />
        <input type="hidden" name="deleted" value="${operation.deleted }" />
        <input type="hidden" name="state" value="${operation.state }" />
        <table border="0" cellspacing="0" cellpadding="0">
            <tr>
                <th>服务代码</th>
                <td><input id="serviceId" name="serviceId" class="easyui-textbox" readonly="true" type="text" value="${service.serviceId }"/></td>

                <th>服务名称</th>
                <td><input class="easyui-textbox" readonly="true" type="text" name="serviceName"
                           value="${service.serviceName }"/>
                </td>
            </tr>
            <tr>
                <th>场景号</th>
                <td><input id="operationId" name="operationId" class="easyui-textbox" type="text" readonly="true" value="${operation.operationId}"
                           data-options="required:true, validType:['unique','english']"/></td>
                <th>场景名称</th>
                <td><input id="operationName" name="operationName" value="${operation.operationName}" class="easyui-textbox" type="text"
                           data-options="required:true"/></td>
            </tr>
            <tr>
                <th>功能描述</th>
                <td colspan="3"><input id="operationDesc" name="operationDesc" value="${operation.operationDesc}" class="easyui-textbox" style="width:100%"
                                       type="text"/></td>
            </tr>
            <tr>
                <%--<th>场景关键词</th>
                <td><input class="easyui-textbox" disabled="disabled" type="text" name=""/></td>--%>

                <th>状态</th>
                <td>
                    <input
                              class="easyui-combobox"
                              value="${operation.state }"
                              data-options="readonly:true, valueField: 'value',textField: 'label',
						data: [{label: '待审核',value: '0'},
						{label: '审核通过',value: '1'},
						{label: '审核未通过',value: '2'},
						{label: '已发布',value: '3'},
						{label: '已上线',value: '4'},
						{label: '已下线',value: '5'},
						{label: '待审核',value: '6'},
						{label: '修订',value: '7'},
						{label: '下线',value: '8'}
							]"
                            />
                </td>
            </tr>
            <tr>
                <th>使用范围</th>
                <td><input class="easyui-textbox" value="${operation.range}" type="text" name="range"/></td>

                <th>备注</th>
                <td><input id="operationRemark" name="operationRemark"  value="${operation.operationRemark}" class="easyui-textbox" type="text"/>
                </td>
            </tr>
            <tr>
                <th>场景关键字:</th>
                <td >
                    <ul id="tags"></ul>
                </td>
                <%--<th>
                    <a href="#" id="saveTagBtn" class="easyui-linkbutton" iconCls="icon-save" style="margin-left:1em">保存</a>
                </th>--%>
            </tr>
            <tr>
                <th>服务头:</th>
                <td >
                    <input
                            name = "headId"
                            class="easyui-combobox"
                            value="${operation.headId}"
                            data-options="
                       panelHeight:'200px',
                       multiple:true,
						url:'/serviceHead/queryAll',
				 		 method:'get',
				 		 valueField: 'headId',
				 		 textField: 'headName'
					"/>
                </td>
                <th></th>
                <td >
                </td>
            </tr>
        </table>


    </div>


</form>
<div  class="easyui-panel" title="调用方-提供方列表" style="width:100%;height:auto;padding:10px;">
    <table id="resultList" class="easyui-datagrid"
           data-options="
			rownumbers:true,
			singleSelect:true,
			fitColumns:false,
			url:'/operation/getInvokeMapping2?serviceId=${operation.serviceId}&operationId=${operation.operationId}',
			method:'get',toolbar:toolbar,
			pagination:true,
				"
           style="height:400px; width:100%;">
        <thead>
        <tr>
            <th ></th>
            <th colspan="5">调用方</th>
            <th colspan="5">提供方</th>
        </tr>
        <tr>
            <th data-options="field:'id', width:50, checkbox:true"></th>
            <th data-options="field:'conName',width:120 ">系统名称</th>
            <th data-options="field:'conId',width:70">系统编码</th>
            <th data-options="field:'conInterName',width:120,styler:consumerStyle">接口名称</th>
            <th data-options="field:'conInterId',width:120,styler:consumerStyle">接口代码</th>
            <th data-options="field:'conIsStandard',width:70,styler:consumerStyle " formatter='ff.isStandardText'>是否标准</th>
            <th data-options="field:'proName',width:120">系统名称</th>
            <th data-options="field:'proId',width:70">系统编码</th>
            <th data-options="field:'proInterName',width:120,styler:providerStyle ">接口名称</th>
            <th data-options="field:'proInterId',width:120,styler:providerStyle ">接口代码</th>
            <th data-options="field:'proIsStandard',width:70,styler:providerStyle " formatter='ff.isStandardText'>是否标准</th>

        </tr>
        </thead>
    </table>
</div>
<div style="margin-top:10px;"></div>
<div id="interfaceDlg" class="easyui-dialog"
     style="width:400px;height:280px;padding:10px 20px" closed="true"
     resizable="true"></div>
</body>
</html>
