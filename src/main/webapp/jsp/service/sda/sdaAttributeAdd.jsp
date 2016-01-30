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
<fieldset>
    <table width="100%">
        <tr>
            <td width="20%">

            </td>
            <td>
                <a href="javascript:void(0)" class="easyui-linkbutton"  iconCls="icon-cancel" onClick="$('#dlg').dialog('close');">取消</a>
                <a href="javascript:void(0)" onclick="saveAttr()" class="easyui-linkbutton"  iconCls="icon-save">保存</a>
            </td>
        </tr>
        <tr>
            <td width="20%">
                属性：
            </td>
            <td>
                <input type="text" id="attrType"
                       class="easyui-combobox"
                       data-options="
                 textField:'text',
                 valueField:'id',
                 value:'0',
                 data:[
                    {'id':'0','text':'固定值'},
                    {'id':'1','text':'表达式'}
                 ]
                 "
                 />
            </td>
        </tr>
        <tr>
            <td width="20%">
                值：
            </td>
            <td>
                <input id="sdaAttribute"  type="text" class="easyui-textbox" required="true" style="width:200px">
            </td>
        </tr>
    </table>
</fieldset>
<script type="text/javascript">
    function saveAttr(){
        var attributeValue = $("#sdaAttribute").textbox("getValue");
        if(attributeValue == null || attributeValue == ""){
            alert("请输入属性值！");
            return;
        }else{
            var attrType = $("#attrType").combobox("getValue");
            var path = $("#tg").treegrid('options').url;
            var sdaAttribute ={};
            sdaAttribute.id = new Date().getTime();
            sdaAttribute.type = attrType;
            sdaAttribute.value = attributeValue;
            sdaAttribute.sdaId = "${param.sdaId}";
            $.ajax({
                type: "post",
                async: false,
                contentType : "application/json;charset=utf-8",
                url: "/sdaAttribute/add?_t="+ new Date().getTime(),
                data: JSON.stringify(sdaAttribute),
                dataType: "json",
                success: function (data) {
                    if (data) {
                        alert("属性保存成功!");
                        $("#tg").treegrid({url:path+"&_t="+ new Date().getDate});
                        $('#dlg').dialog('close');
                        showAttr();
                    }
                }
            });
        }
    }
</script>
</body>
</html>
