<fieldset>
  <legend>导入元数据文档(Excel)</legend>
  <form id="uploadimg-form"  action="/resourceImport/import/process" method="post" enctype="multipart/form-data">
    <input type="file" title="选择文件" name="file" id="file"/>&nbsp;<input id="fileBtn" type="submit" class="btn" value="文件上传"/>
  </form>
</fieldset>
<script type="text/javascript">
  $("#uploadimg-form").attr("action","/resourceImport/import/process/" + processId);
</script>