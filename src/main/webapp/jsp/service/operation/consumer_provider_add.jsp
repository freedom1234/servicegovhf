<%@ page language="java" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<form class="formui">
    <div class="win-bbar" style="text-align:center"><a href="#" class="easyui-linkbutton" iconCls="icon-cancel"
                                                       onClick="$('#interfaceDlg').dialog('close')">取消</a><a href="#"
                                                                                                             <%--onclick="addInterfaceContent()"--%>
                                                                                                             onclick="addInterfaceInvoke()"
                                                                                                             class="easyui-linkbutton"
                                                                                                             iconCls="icon-save">确定</a></div>
    <fieldset>
        <legend>调用方</legend>
        <table border="0" cellspacing="0" cellpadding="0" style="width:auto;">
            <tr>
                <th align="center">已经应用</th>
                <td width="50" align="center">&nbsp;</td>
                <th align="center">应用系统列表</th>
            </tr>

            <tr>
                <th align="center"><select name="select2" id="consumer" size="10" multiple
                                           style="width:155px;height:160px" panelHeight="auto">
                </select></th>
                <td align="center" valign="middle"><a href="#" class="easyui-linkbutton" iconCls="icon-select-add"
                                                      onClick="selectex('consumer','systemList1','1')"></a><br>
                    <br>
                    <br>
                    <a href="#" class="easyui-linkbutton" iconCls="icon-select-remove"
                       onClick="chooseInterface('systemList1','consumer','1')"></a></td>
                <td align="center">
                    <select name="select" id="systemList1" size="10" multiple style="width:155px;height:160px"
                            panelHeight="auto"/>
                    </select>
                </td>
            </tr>
        </table>
    </fieldset>
    <div style="margin-top:10px;"></div>
    <fieldset>
        <legend>提供方</legend>
        <table border="0" cellspacing="0" cellpadding="0" style="width:auto;">
            <tr>
                <th align="center">已经应用</th>
                <td width="50" align="center">&nbsp;</td>
                <th align="center">应用系统列表</th>
            </tr>

            <tr>
                <th align="center"><select name="select2" id="provider" size="10" multiple
                                           style="width:155px;height:160px" panelHeight="auto">
                </select></th>
                <td align="center" valign="middle"><a href="#" class="easyui-linkbutton" iconCls="icon-select-add"
                                                      onClick="selectex('provider','systemList2','0')"></a><br>
                    <br>
                    <br>
                    <a href="#" class="easyui-linkbutton" iconCls="icon-select-remove"
                       onClick="chooseInterface('systemList2','provider','0')"></a></td>
                <td align="center"><select name="select" id="systemList2" size="10" multiple
                                           style="width:155px;height:160px" panelHeight="auto">

                </select></td>
            </tr>
        </table>
    </fieldset>
    <div id="opDlg" class="easyui-dialog"
         style="width:400px;height:280px;padding:10px 20px" closed="true"
         resizable="true"></div>
</form>
<script type="text/javascript">
    $(function(){
        consumerList = new Array();
        providerList = new Array();
    })
    loadSystem("systemList1", systemList, "systemId", "systemChineseName");
    loadSystem("systemList2", systemList, "systemId", "systemChineseName");

    function addInterfaceInvoke(){
        //保存调用关系
        var params = [consumerList, providerList];
        $.ajax({
            type: "POST",
            async: false,
            contentType: "application/json; charset=utf-8",
            url: "/serviceLink/addServiceLink2",
            dataType: "json",
            data : JSON.stringify(params),
            success: function (data) {
                alert("保存成功");
                //刷新数据
                $("#resultList").datagrid('reload');
                $('#interfaceDlg').dialog('close');
            }
        });
    }
</script>
