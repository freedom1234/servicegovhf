<%@ page contentType="text/html; charset=utf-8" language="java"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<form id="uploadimg-form"  action="/excelHelper/interfaceimport" method="post" enctype="multipart/form-data">
  <input type="file" title="选择文件" name="file" id="file"/><br /><br />
  <br /><br />
  <input id="fileBtn" type="submit" class="btn" value="文件上传"/><br /><br />
  <tr style="display:none">
    <td>
      <input style="display:none" type="text" id="systemId" name="systemId" value="${param.systemId }">
    </td>
  </tr>

</form>

<script type="text/javascript">
  $(document).ready(function (){

  })

</script>


