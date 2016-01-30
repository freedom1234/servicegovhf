<%@ page contentType="text/html; charset=utf-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<form class="formui">
    <table border="0" cellspacing="0" cellpadding="0">

        <tr>
            <th>
                ESB标识
            </th>
            <td>
                <input class="easyui-textbox" type="text" id="esbAccessPattern">
            </td>
        </tr>
        <tr>
            <th>
                条件位
            </th>
            <td>
                <input class="easyui-textbox" type="text" id="condition">
            </td>
        </tr>
        <tr>
            <td>
                &nbsp;
            </td>
            <td class="win-bbar">
                <a href="#" class="easyui-linkbutton" iconCls="icon-cancel"
                   onClick="$('#w').window('close')">取消</a><a href="#"
                                                              class="easyui-linkbutton" onclick="save()"
                                                              iconCls="icon-save">保存</a>
            </td>
        </tr>
    </table>
</form>

<script type="text/javascript">

    function save() {
        var serviceLink = {};
        serviceLink.serviceInvokeId = invokeId;
        serviceLink.esbAccessPattern = $("#esbAccessPattern").val();
        serviceLink.condition = $("#condition").val();
        $.ajax({
            type: "POST",
            url: "/serviceLink/modify/",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(serviceLink),
            dataType: "json",
            success: function (data) {
                if (data) {
                    alert("交易链路节点修改成功");
                    $('#w').window('close');
                    $("#invokeLinkeTable").datagrid("reload");
                }
            },
            complete: function (responce) {
                var resText = responce.responseText;
                if(resText.toString().indexOf("没有操作权限") > 0){
                    alert("没有权限！");
                    //window.location.href = "/jsp/403.jsp";
                }
            }

        });
    }

</script>


