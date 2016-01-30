<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<form class="formui">
    <table border="0" cellspacing="0" cellpadding="0">
        <tr>
            <th>分类编码</th>
            <td><input class="easyui-textbox" required="true" type="text" id="categoryId"></td>
        </tr>
        <tr>
            <th>服务分类名</th>
            <td><input class="easyui-textbox" required="true"  type="text" id="categoryName"></td>
        </tr>
        <tr>
            <th>描述</th>
            <td><input class="easyui-textbox" type="text" id="discription"></td>
        </tr>
        <tr style="display:none">
            <th>服务类父类</th>
            <td><input class="easyui-textbox" type="text" id="parentId"></td>
        </tr>
        <tr style="display:none;">
            <th>remark</th>
            <td><input class="easyui-textbox" type="text" id="remark"></td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td class="win-bbar">
                <a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onClick="$('#w').window('close')">取消</a>
                <a id="saveServiceCategoryBtn" href="#" class="easyui-linkbutton" iconCls="icon-save">保存</a>
            </td>
        </tr>
    </table>
</form>
<script>
    $(function () {
        var node;
        var saveServiceCategory = function saveServiceCategory() {
            if (!$(".formui").form('validate')) {
                alert("请完善数据!");
                return false;
            }
            var serviceCategory = {};
            var type = node.type;
            if (type == "root") {
                //新增服务大类
                serviceCategory.categoryId = $('#categoryId').val();
                serviceCategory.categoryName = $('#categoryName').val();
                serviceCategory.desc = $('#discription').val();
                serviceCategory.remark = $('#remark').val();
            } else {
                //新增服务小类
                var parentId = node.serviceCategory.categoryId;
                serviceCategory.categoryId = $('#categoryId').val();
                serviceCategory.categoryName = $('#categoryName').val();
                serviceCategory.desc = $('#discription').val();
                serviceCategory.parentId = parentId;
                serviceCategory.remark = $('#remark').val();
            }
            serviceManager.addServiceCategory(serviceCategory, function (result) {
                $('#w').window('close');
                $('.mxservicetree').tree('reload');
            });
        };
        $("#saveServiceCategoryBtn").click(saveServiceCategory);
        node = $('.mxservicetree').tree('getSelected');
        if(node.serviceCategory){
            $('#parentId').val(node.serviceCategory.parentId);
        }
    });
</script>