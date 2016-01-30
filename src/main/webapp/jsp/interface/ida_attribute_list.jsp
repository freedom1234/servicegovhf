<%@ page language="java" import="java.util.*, java.net.URLDecoder" pageEncoding="utf-8"%>
<%

    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
    <base href="<%=basePath%>">
</head>

<body>
<table class="easyui-datagrid" id="attrList" style=" width:auto;"
       data-options="
				rownumbers: true,
				fitColumns: true,
				checkbox:true,
				url: '/idaAttribute/getByIdaId?idaId=${param.idaId}&t='+ new Date().getTime(),
				method: 'get',
				toolbar:[
				{
                text: '删除',
                iconCls: 'icon-remove',
                handler: deleteSDAAttr
              }
				]
				"
        >
    <thead>
    <tr>
        <th data-options="field:'id',checkbox:true,width:50"></th>
        <th data-options="field:'type',width:140, formatter:attrFormatter.type">属性</th>
        <th data-options="field:'value',width:140,align:'left'">值</th>
    </tr>
    </thead>
</table>
<script type="text/javascript">
    var attrFormatter = {
        type: function (value, row, index) {
            try {
                if(row.type == "0"){
                    return "固定值";
                }
                if(row.type == "1"){
                    return "表达式";
                }
            } catch (exception) {
            }
        }
    };
    function deleteSDAAttr(){
        var rows = $("#attrList").datagrid("getChecked");
        if(rows != null && rows.length > 0){
            $.messager.confirm("删除IDA属性","确定要删除选中的属性吗？",function(r){
                if(r){
                    var attrIds = new Array();
                    for(var i = 0; i < rows.length; i++){
                        attrIds.push(rows[i].id);
                    }
                    var path = $("#tg").treegrid('options').url;
                    $.ajax({
                        type: "post",
                        async: false,
                        contentType : "application/json;charset=utf-8",
                        url: "/idaAttribute/deletes?_t="+ new Date().getTime(),
                        data: JSON.stringify(attrIds),
                        dataType: "json",
                        success: function (data) {
                            if (data) {
                                $("#attrList").datagrid("reload");
                                $("#tg").treegrid({url:path+"&_t="+ new Date().getDate});
                            }
                        }
                    });
                }
            })
        }

    }
</script>
</body>
</html>
