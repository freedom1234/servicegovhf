<%@ page contentType="text/html; charset=utf-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<form id="headForm" class="formui">
    <table border="0" cellspacing="0" cellpadding="0">

        <tr>
            <th>
                报文头名称
            </th>
            <td>
                <input class="easyui-textbox" type="text" id="headName" data-options="required:true,validType:['unique','chineseB']">
            </td>
        </tr>
        <tr>
            <th>
                报文头备注
            </th>
            <td>
                <input class="easyui-textbox" type="text" id="headRemark" data-options="validType:['chineseB']">
            </td>
        </tr>
        <tr>
            <th>
                报文头描述
            </th>
            <td>
                <input class="easyui-textbox" type="text" id="headDesc" data-options="validType:['chineseB']">
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
<script type="text/javascript" src="/plugin/validate.js"></script>
<script type="text/javascript">
    $.extend($.fn.validatebox.defaults.rules, {
        unique: {
            validator: function (value, param) {
                var result;
                $.ajax({
                    type: "post",
                    async: false,
                    url: "/interfaceHead/uniqueValid",
                    dataType: "json",
                    data: {"headName": value},
                    success: function (data) {
                        result = data;
                    }
                });
                return result;
            },
            message: '已存在相同报文头名称'
        }
    });
    function save() {
        if (!$("#headForm").form('validate')) {
            alert("请先完善基础信息!");
            return false;
        }
        //var headId = $("#headId").val();
        var headName = $("#headName").val();
        var headRemark = $("#headRemark").val();
        var headDesc = $("#headDesc").val();

        var data = {};

        var systemId = "";
        var treeObj = $('.msinterfacetree');
        try {
            var selectNode = $('.msinterfacetree').tree("getSelected");
            systemId = selectNode.id;

            var node = $('.msinterfacetree').tree("getParent", selectNode.target);
            if (node && node.id != 'root') {
                systemId = node.id;
            }

        } catch (e) {
            systemId = "${param.systemId}";
            treeObj = parent.$('.msinterfacetree');
        }
        //data.headId = headId;
        data.headName = headName;
        data.headRemark = headRemark;
        data.headDesc = headDesc;
        data.systemId = systemId;

        sysManager.add(data, function (result) {
            if (result) {
                var selectNode = $('.msinterfacetree').tree("getSelected");
                $('.msinterfacetree').tree('append', {
									parent: (selectNode?selectNode.target:null),
									data: [{
										text: 'new item1'
									}]
								});
				var urlPath = $('.msinterfacetree').tree('options').url;
                $('.msinterfacetree').tree('options').url = "/interface/getLeftTree/subHeadTree/system/" + systemId;
                $('.msinterfacetree').tree("reload", selectNode.target);
                $('.msinterfacetree').tree('options').url = urlPath;
                $('#w').window('close');
            } else {
                alert("保存失败");
            }
        });
    }

</script>


