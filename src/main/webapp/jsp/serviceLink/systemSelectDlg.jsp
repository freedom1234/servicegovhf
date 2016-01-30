<div style="margin-top:1em;margin-bottom: 1em;">
    <label style="margin-right: 1em;">选择系统:</label>
    <select class="easyui-combobox" id="systemId" name="systemId"
            style="width:155px" panelHeight="200px"
            data-options="editable:false">
    </select>
    <label style="margin-right: 1em;">服务码:</label>
    <input class="easyui-textbox" id="serviceIdSearch"/>
    <a href="#" id="selectSysBtn" class="easyui-linkbutton" iconCls="icon-save"
       style="margin-left:1em">查询</a>
    <a href="#" id="selectNodeBtn" class="easyui-linkbutton" iconCls="icon-save"
       style="margin-left:1em">选择节点</a>
</div>
<table title="相邻节点" id="nodeTable" style="height:370px; width:auto;">
    <thead>
    <tr>
        <th data-options="field:'productid',checkbox:true"></th>
        <th data-options="field:'interfaceId'">交易码</th>
        <th data-options="field:'interfaceName'">交易名称</th>
        <th data-options="field:'serviceId'">服务码</th>
        <th data-options="field:'serviceName'">服务名称</th>
        <th data-options="field:'operationId'">场景码</th>
        <th data-options="field:'operationName'">场景名称</th>
        <th data-options="field:'invokeType'">调用类型</th>
    </tr>
    </thead>
</table>
<%--<a href="#" id="saveNodeInfoBtn" class="easyui-linkbutton" iconCls="icon-save"
   style="margin-left:1em">保存</a>--%>
<script type="text/javascript">
    $(function () {

        var constructNodeTable = function constructNodeTable(systemId,serviceId) {
            $('#nodeTable').datagrid({
                rownumbers: true,
                singleSelect: true,
                url: "/serviceLink/getServiceLinkNode/system/" + systemId + "?serviceId=" + serviceId,
                method: 'get',
                pagination: true,
                pageSize: 10,
                onLoadSuccess: function (data) {
                    $.each(data.rows, function (index, item) {
                        var invokeType = item.invokeType;
                        if (invokeType == '0') {
                            item.invokeType = '提供者';
                        } else if (invokeType == '1') {
                            item.invokeType = '消费者';
                        }
                    });
                },
                onLoadError: function (responce) {
                    var resText = responce.responseText;
                    if (resText.toString().indexOf("没有操作权限") > 0) {
                        window.location.href = "/jsp/403.jsp";
                    }
                }
            });
        }

        $('#systemId').combobox({
            url: '/system/getSystemAll',
            method: 'get',
            mode: 'remote',
            valueField: 'id',
            textField: 'text'
        });
        $("#selectSysBtn").click(function () {
            constructNodeTable($("#systemId").combobox("getValue"),$("#serviceIdSearch").textbox("getValue"));
        });
        $("#selectNodeBtn").click(function () {
            var connections = [];
            var checkedItems = $('#nodeTable').datagrid('getChecked');
            var checkedItemNum = checkedItems.length;
            for (var i = 0; i < checkedItemNum; i++) {
                var connection = {};
                connection.sourceId = nodeId;
                connection.targetId = checkedItems[i].invokeId;
                connections.push(connection);
            }

            serviceLinkManager.saveConnections(connections, function () {
                alert("关联成功");
                $("#systemDlg").window('close');
                $("#invokeLinkeTable").datagrid("reload");

            });
        });
        constructNodeTable();
    });

</script>

