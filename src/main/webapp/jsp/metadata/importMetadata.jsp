<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<fieldset>
    <legend>导入元数据文档(Excel)</legend>
    <form id="uploadimg-form" action="/resourceImport/import" method="post" enctype="multipart/form-data"  onsubmit="uploading()">
        <input type="file" title="选择文件" name="file" id="file"/>&nbsp;<input id="fileBtn" type="submit" class="btn"
                                                                            value="文件上传"/>
    </form>
</fieldset>
<script type="text/javascript">
    function uploading(){
        $.messager.progress({
            title:'请稍后',
            msg:'正在上传文件...'
        });
    }
</script>
<%--<script type="text/javascript">--%>
    <%--$("#fileBtn").click(function(){--%>
        <%--$("#file").fileupload({--%>
            <%--url: "/resourceImport/import",//文件上传地址，当然也可以直接写在input的data-url属性内--%>
<%--//        formData: {param1: "p1", param2: "p2"},//如果需要额外添加参数可以在这里添加--%>
            <%--done: function (e, result) {--%>
            <%--}--%>
        <%--});--%>
    <%--});--%>
<%--</script>--%>