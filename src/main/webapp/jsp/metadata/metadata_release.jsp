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
                <a href="#" class="easyui-linkbutton"  iconCls="icon-cancel" onClick="$('#opDialog').dialog('close');">取消</a>
                <a href="#" onclick="releaseMetadata()" class="easyui-linkbutton"  iconCls="icon-save">保存</a>
            </td>
        </tr>
        <tr>
            <td width="20%">
                版本号：
            </td>
            <td>
                <input id="versionNo" type="text" class="easyui-textbox" style="width:200px" >
            </td>
        </tr>
        <tr>
            <td width="20%">
                当前版本：
            </td>
            <td>
                <input id="lastVersionNo" value="${lastVersionNo}" type="text" class="easyui-textbox" style="width:200px" editable="false">
            </td>
        </tr>
        <tr>
            <td width="20%">
                发布说明：
            </td>
            <td>
                <input id="versionDesc" type="text" class="easyui-textbox" style="width:200px; height:50px" >
            </td>
        </tr>
    </table>
</fieldset>
<script type="text/javascript">
    function releaseMetadata(){
        var versionNo = $("#versionNo").val();
        var lastVersionNo = $("#lastVersionNo").val();
        if(versionNo.localeCompare(lastVersionNo) <= 0){
            alert("新版本号必须比[" + lastVersionNo + "]大！");
            return false;
        }else{
            var versionDesc = $("#versionDesc").val();
            $.ajax({
                type: "post",
                async: false,
                url: "/metadataVersion/release",
                data:{
                    "versionNo" : versionNo,
                    "versionDesc" : versionDesc
                },
                dataType: "json",
                success: function (data) {
                    if (data) {
                        alert("发布成功！");
                        $('#opDialog').dialog('close');
                    }
                }
            });
        }
    }
</script>
</body>
</html>
