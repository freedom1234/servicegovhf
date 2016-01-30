<%@ page contentType="text/html; charset=utf-8" language="java"
		 import="java.sql.*" errorPage=""%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>列表页</title>
	<link rel="stylesheet" type="text/css"
		  href="/resources/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css"
		  href="/resources/themes/icon.css">
	<link href="/resources/css/css.css" rel="stylesheet" type="text/css">
</head>

<body>
<fieldset>
	<legend>导入元数据文档(Excel)</legend>
	<form id="uploadimg-form"  action="/resourceImport/import" method="post" enctype="multipart/form-data">
    <input type="file" title="选择文件" name="file" id="file"/>&nbsp;<input id="fileBtn" type="submit" class="btn" value="文件上传"/>  
    </form>
</fieldset>
<fieldset>
	<legend>导出元数据文档(Xml)</legend>
	<input name="元数据" type="button" value="元数据(Metadata.xml)"  id="metadata"/>
</fieldset>
  <script type="text/javascript"
		src="/assets/exporImport/js/exporImportManager.js"></script>
	<script type="text/javascript" src="/resources/js/jquery.min.js"></script>
	<script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="/resources/js/jquery.fileDownload.js"></script>
<script type="text/javascript">
	$(function() {
		$('#metadata').click(function(){
			 $.fileDownload("/exportMetadata/export", {
            	});
		});
	});
</script>  
</body>
</html>