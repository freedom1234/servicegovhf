<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>My JSP 'MyJsp.jsp' starting page</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <!--
    <link rel="stylesheet" type="text/css" href="styles.css">
    -->

</head>


<body>

<form class="formui" id="metadataForm" action="/metadata/add" method="post">
    <table border="0" cellspacing="0" cellpadding="0">
        <tr>
            <th>元数据名称</th>
            <td><input class="easyui-textbox" type="text" name="metadataId"
                       data-options="required:true, validType:['unique','englishB']"></td>
        </tr>
        <tr>
            <th>中文名称</th>
            <td><input class="easyui-textbox" type="text" name="chineseName" data-options="required:true, validType:['uniqueName','chineseB']"></td>
        </tr>
        <tr  style="display:none;">
            <th>别名</th>
            <td><input class="easyui-textbox" type="text" name="metadataAlias" data-options="validType:['chineseB']"></td>
        </tr>
        <tr>
            <th>英文全称</th>
            <td><input class="easyui-textbox" type="text" name="metadataName"></td>
        </tr>
        <tr>
            <th>类别词</th>
            <td><input type="text" name="categoryWordId" id="categoryWordId"
                       class="easyui-combobox"
                       data-options="
                       panelHeight:'300px',
						url:'/metadata/categoryWord',
				 		 method:'get',
				 		 valueField: 'esglisgAb',
				 		 textField: 'chineseWord',
				 		 onChange:function(newValue, oldValue){
							this.value=newValue;
						}
					"
                    /></td>
        </tr>
        <tr style="display:none;">
            <th>业务定义</th>
            <td><input class="easyui-textbox" type="text" name="bussDefine"></td>
        </tr>
        <tr style="display:none;">
            <th>业务规则</th>
            <td><input class="easyui-textbox" type="text" name="bussRule"></td>
        </tr>
	       <tr>
            				<th>类型</th>
            				<td><input class="easyui-textbox" data-options="required:true" type="text" name="type"></td>
            </tr>

        <tr>
            <th>长度</th>
            <td><input class="easyui-textbox" type="text" name="length"></td>
        </tr>
        <tr>
            <th>精度</th>
            <td><input class="easyui-textbox" type="text" name="scale"></td>
        </tr>
        <tr>
                    <th>数据项分类</th>
                    <td><input class="easyui-textbox" type="text" name="dataCategory"></td>
                </tr>
        <tr style="display:none;">
            <th>数据来源</th>
            <td><input class="easyui-textbox" type="text" name="dataSource"></td>
        </tr>
        <tr>
            <th>状态</th>
            <td>
                <input
                        name = "status"
                        class="easyui-combobox"
                        value="正式"
                        data-options="valueField: 'value',textField: 'label',editable:false,
						data: [{label: '正式',value: '正式'}
							]"
                        />
            </td>
        </tr>
        <tr>
            <th>备注</th>
            <td><input class="easyui-textbox" type="remark" name="length"></td>
        </tr>
        <tr style="display:none;">
            <th>任务id</th>
            <td><input class="easyui-textbox" disabled="disabled" type="text" name="processId" id="taskIdInput"></td>
        </tr>
        <tr>
            <td colspan="2" style="text-align:center">
                <a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onClick="$('#w').window('close')">取消</a>
                <a href="#" onclick="save('metadataForm')" class="easyui-linkbutton" iconCls="icon-save">保存</a>
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
                    type: "get",
                    async: false,
                    url: "/metadata/uniqueValid",
                    dataType: "json",
                    data: {"metadataId": value},
                    success: function (data) {
                        result = data;
                    }
                });
                return result;
            },
            message: '元数据名称已存在'
        },
        uniqueName: {
            validator: function (value, param) {
                var result;
                $.ajax({
                    type: "get",
                    async: false,
                    url: "/metadata/uniqueChineseNameValid",
                    dataType: "json",
                    data: {"chineseName": encodeURI(encodeURI(value))},
                    success: function (data) {
                        result = data;
                    }
                });
                return result;
            },
            message: '元数据中文名称已存在'
        }
    });
    if(typeof(processId) != 'undefined'){
        $('#taskIdInput').val(processId);
    }
</script>

</body>
</html>



