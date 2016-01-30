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
    <script type="text/javascript" src="/plugin/validate.js"></script>
    <script type="text/javascript">
        var isSave = 0;
        $(document).ready(function () {
            $.extend($.fn.validatebox.defaults.rules, {
                unique: {
                    validator: function (value, param) {
                        var result;
                        $.ajax({
                            type: "get",
                            async: false,
                            url: "/operation/uniqueValid",
                            dataType: "json",
                            data: {"operationId": value, "serviceId": "${service.serviceId }"},
                            success: function (data) {
                                result = data;
                            }
                        });
                        return result;
                    },
                    message: '已存在相同场景编号'
                }
            });
        });
        $(function(){
            /**
             *  初始化接口标签
             * @param result
             */
            $("#tags").tagit();
        });
        var toolbar = [{
            text: '新增',
            iconCls: 'icon-add',
            handler: function () {
                if (!$("#operationForm").form('validate')) {
                    alert("请先完善基础信息!");
                    return false;
                }
                if(isSave == 0){
                    alert("请先保存场景，再增加调用关系!");
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
            }},
            {
            text: '清空',
            iconCls: 'icon-remove',
            handler: function () {
                $("#resultList").datagrid('loadData',{total:0,rows:[]});
                invokeList = new Array();
            }}
        ];
        var systemList = ${systemList};
        var consumerList = new Array();
        var providerList = new Array();
        var invokeList = new Array();
        /*在接口选完服务提供者消费者关系点击确定执行*/
        function addInterfaceContent(){
            alert(0);
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
            row.consumerNames= consumerNames;
            row.consumerIds= consumerIds;
            row.providerNames= providerNames;
            row.providerIds= providerIds;
            row.interfaceName= interfaceName;
            row.interfaceId= interfaceId;
            return row;
        }
    </script>

</head>

<body>
<form class="formui" id="operationForm">
    <div class="win-bbar" style="text-align:center"><a href="#" class="easyui-linkbutton" iconCls="icon-cancel"
                                                       onClick="clean()">取消</a><a href="#" id="saveBtn"
                                                                                  onclick="saveAdd('operationForm',0)"
                                                                                  class="easyui-linkbutton"
                                                                                  iconCls="icon-save">保存</a></div>
    <div class="easyui-panel" title="基本信息" style="width:100%;height:auto;padding:10px;">
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
                <td><input id="operationId" name="operationId" class="easyui-textbox" type="text"
                           data-options="required:true, validType:['unique','englishB']"/></td>
                <th>场景名称</th>
                <td><input id="operationName" name="operationName" class="easyui-textbox" type="text"
                           data-options="required:true"/></td>
            </tr>
            <tr>
                <th>功能描述</th>
                <td colspan="3"><input id="operationDesc" name="operationDesc" class="easyui-textbox" style="width:100%"
                                       type="text"/></td>
            </tr>
            <tr>
                <th>状态</th>
                <td>服务定义
                    <input type="hidden" value="0" name="state"/>
                </td>
            </tr>
            <tr>
                <th>使用范围</th>
                <td><input class="easyui-textbox" type="text" name="range"/></td>

                <th>备注</th>
                <td><input id="operationRemark" name="operationRemark" class="easyui-textbox" type="text"/>
                </td>
            </tr>
            <tr>
                <th>场景关键字:</th>
                <td >
                    <ul id="tags"></ul>
                </td>
            </tr>

            <tr>
                <th>服务头:</th>
                <td >
                    <input
                            name = "headId"
                            class="easyui-combobox"
                            value="sys_head,app_head"
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
			method:'get',
			toolbar:toolbar
				"
           style="height:400px; width:100%;">
        <thead>
        <tr>
            <th ></th>
            <th colspan="4">调用方</th>
            <th colspan="4">提供方</th>
        </tr>
        <tr>
            <th data-options="field:'id', width:50, checkbox:true"></th>
            <th data-options="field:'conName',width:120">系统名称</th>
            <th data-options="field:'conId',width:120">系统编码</th>
            <th data-options="field:'conInterName',width:120">接口名称</th>
            <th data-options="field:'conInterId',width:120">接口代码</th>
            <th data-options="field:'proName',width:120">系统名称</th>
            <th data-options="field:'proId',width:120">系统编码</th>
            <th data-options="field:'proInterName',width:120">接口名称</th>
            <th data-options="field:'proInterId',width:120">接口代码</th>

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
