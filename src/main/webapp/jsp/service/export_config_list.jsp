<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>批量导出配置</title>

    <link rel="stylesheet" type="text/css"
          href="/resources/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="/resources/themes/icon.css">
    <link href="/resources/css/ui.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/resources/js/jquery.min.js"></script>
    <script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
    <!--
    <link rel="stylesheet" type="text/css" href="styles.css">
    -->

</head>


<body>
<form class="easyui-form" id="exportForm" >
    <table id="choosedList" class="easyui-datagrid"
           data-options="
                rownumbers:true,
			      singleSelect:false,
			      fitColumns:true,
                toolbar:[{
                    iconCls: 'icon-excel-export',
                    text:'导出配置',
                     handler: exportBath
                },
                {
                    text:'（默认导出标准XML配置，鼠标右键选择其他选项)'
                }
                ],
                method:'get',
                onLoadSuccess:function(row){//当表格成功加载时执行
                    $(this).datagrid('selectAll');
                },
                onRowContextMenu:onRowContextMenu,
                onDblClickCell:onDblClickCell
                "
           style="width:100%;">
        <thead>
        <tr>
            <th data-options="field:'', checkbox:true" rowspan="2"></th>
            <th data-options="field:'serviceId',width:100" rowspan="2">服务代码</th>
            <th data-options="field:'operationId',width:80" rowspan="2">场景代码</th>
            <th data-options="field:'operationName',width:100" rowspan="2">场景名称</th>
            <th data-options="hidden:true" colspan="4"></th>
            <th colspan="3">调用方</th>
            <th colspan="3">提供方</th>

        </tr>
        <tr>
            <th data-options="field:'providerServiceInvokeId',hidden:true">提供者关系Id</th>
            <th data-options="field:'consumerServiceInvokeId',hidden:true">消费者关系ID</th>
            <th data-options="field:'conGeneratorId',hidden:true">生成器ID</th>
            <th data-options="field:'proGeneratorId',hidden:true">生成器ID</th>

            <th data-options="field:'consumerName',width:100">调用方</th>
            <th data-options="field:'conInterfaceName',width:100">接口</th>
            <%--<th data-options="field:'conIsStandard',width:50,formatter:formatter.standard, editor:standardEditor">标准</th>--%>
            <th data-options="field:'conGeneratorName',width:100,editor:conGeneratorEditor">生成器</th>

            <th data-options="field:'providerName',width:100">提供方</th>
            <th data-options="field:'proInterfaceName',width:100">接口</th>
            <%--<th data-options="field:'proIsStandard',width:50,formatter:formatter.standard">标准</th>--%>
            <th data-options="field:'proGeneratorName',width:100,editor:proGeneratorEditor">生成器</th>


    </table>
</form>
<div id="mm" class="easyui-menu" style="width:120px;">
    <div onclick="chooseUnStandard()" data-options="iconCls:'icon-edit'">选择其他标准</div>
</div>
<script type="text/javascript">
    var formatter={
        standard : function (value, row, index) {
            if(value == '0'){
                return '是';
            }
            if(value == '1'){
                return '否';
            }
        }
    }
//    var standardEditor = {
//        type:'combobox',
//        options:{
//            required:true,
//            valueField:'id',
//            textField:'text',
//            data:[{
//                id: '1',
//                text: '否'
//            },{
//                id: '0',
//                text: '是'
//            }],
//            onSelect:function(record){
//                var row = $('#choosedList').datagrid('getSelected');
//                var index =  $('#choosedList').datagrid('getRowIndex', row);
//                var ed = $('#choosedList').datagrid('getEditor', {index:index,field:'conGeneratorName'});
//                $(ed.target).combobox('setValue',null);
//                if(record.id == '1'){
//                    var url = '/generator/getAllList?type';
//                    $(ed.target).combobox({
//                        valueField: 'id',
//                        textField: 'name',
//                        url: url,
//                        onSelect:function(record){
//                            row.interfaceIdOrProtocolIdPro = record.id;
//                        }
//                    })
//                }
//                if(record.id == '0'){
//                    var data = [{
//                        id: 'xml',
//                        text: 'xml'
//                    },{
//                        id: 'soap',
//                        text: 'soap'
//                    }];
//                    $(ed.target).combobox('loadData',data);
//                }
//            }
//        }
//    }
    var conGeneratorEditor = {
        type:'combobox',
        options: {
            required:true,
            valueField: 'name',
            textField: 'name',
            method : 'GET',
            url: "/generator/getAll",
            onSelect:function(record){
                var row = $("#choosedList").datagrid("getSelected");
                var index = $("#choosedList").datagrid("getRowIndex", row);
                $("#choosedList").datagrid("endEdit", index);
                $("#choosedList").datagrid("updateRow",{
                    index : index,
                    row:{
                        conGeneratorId:record.id
                    }
                })
            }
        }
    }
    var proGeneratorEditor = {
        type:'combobox',
        options: {
            required:true,
            valueField: 'name',
            textField: 'name',
            method : 'GET',
            url: "/generator/getAll",
            onSelect:function(record){
                var row = $("#choosedList").datagrid("getSelected");
                var index = $("#choosedList").datagrid("getRowIndex", row);
                $("#choosedList").datagrid("endEdit", index);
                $("#choosedList").datagrid("updateRow",{
                    index : index,
                    row:{
                        proGeneratorId:record.id
                    }
                })
            }
        }
    }
    function onRowContextMenu(e, rowIndex, rowData) {//表格鼠标右击事件
        e.preventDefault(); //阻止浏览器捕获右键事件
        $(this).datagrid("clearSelections"); //取消所有选中项
        $(this).datagrid("selectRow", rowIndex); //根据索引选中该行
        $('#mm').menu('show', {
            left: e.pageX,
            top: e.pageY
        });
    }
    function chooseUnStandard(){//选择非标导出方法
        var row = $("#choosedList").datagrid("getSelected");
        var index = $("#choosedList").datagrid("getRowIndex", row);
        $("#choosedList").datagrid("beginEdit", index);
    }
    function exportBath(){
        if(!$("#exportForm").form('validate')){
            alert("请完善数据！");
            return false;
        }
        var rows = $("#choosedList").datagrid('getChecked');
        if(null != rows && 0 < rows.length){
            var form=$("<form>");//定义一个form表单
            form.attr("style","display:none");
            form.attr("target","");
            form.attr("method","post");
            form.attr("action","/export/exportBatch");
            var fields = ["consumerServiceInvokeId", "providerServiceInvokeId", "conGeneratorId", "proGeneratorId"];
            for(var i=0; i < rows.length; i++){
                var index = $("#choosedList").datagrid('getRowIndex', rows[i]);
                $("#choosedList").datagrid('endEdit', index);
                for(var j=0; j < fields.length; j++){
                    var input1=$("<input>");
                    input1.attr("type","hidden");
                    input1.attr("name","list["+i+"]."+fields[j]);
                    input1.attr("value",rows[i][fields[j]]);
                    form.append(input1);
                }
            }

            $("body").append(form);//将表单放置在web中
            form.submit();//表单提交
        }else{
            alert("没有选中数据！");
        }

    }


</script>
</body>
</html>