/**
 * Created by vincentfxz on 15/7/3.
 */
var href = window.location.href;
var params = href.split("&");
serviceId = params[0].split("=")[1];
operationId = params[1].split("=")[1];
var slaUrl = "/sla/getAll/" + serviceId + "/" + operationId;
var slatoolbar = [{
    text: '新增',
    iconCls: 'icon-add',
    handler: function () {
        $('#sla').edatagrid('addRow');
    }
}, {
    text: '删除',
    iconCls: 'icon-remove',
    handler: function () {
        var row = $('#sla').edatagrid('getSelected');
        var rowIndex = $('#sla').edatagrid('getRowIndex', row);
        $('#sla').edatagrid('deleteRow', rowIndex);
    }
}, {
    text: ' 保存',
    iconCls: 'icon-save',
    handler: function () {
        var addDatas = $('#sla').edatagrid('getData').rows;
        var datas = [];
        for (var i = 0; i < addDatas.length; i++) {
            var addData = addDatas[i];
            var data = {};
            data.slaName = addData.slaName;
            data.slaValue = addData.slaValue;
            data.slaDesc = addData.slaDesc;
            data.slaRemark = addData.slaRemark;
            data.serviceId = serviceId;
            data.operationId = operationId;
            datas.push(data);
        }
        var param = serviceId + "/" + operationId;
        slaManager.deleteByAll(param, function (result) {
            if (result) {
                slaManager.addList(datas, function (result) {
                    if (result) {
                        alert("保存成功");
                    } else {
                        alert("保存失败");
                    }
                });
            } else {
                alert("保存失败");
            }
        });
    }
}, {
    text: 'SLA模板',
    iconCls: 'icon-qxfp',
    handler: function () {
        uiinit.win({
            w: 900,
            iconCls: 'icon-add',
            title: "SLA模块",
            url: "/jsp/service/sla/slaTemplateEdit.html"
        });
    }
}];
$(function () {
    $('#sla').edatagrid({
        autoSave: false,
        saveUrl: '/',
        updateUrl: '/',
        destroyUrl: '/'
    });

});