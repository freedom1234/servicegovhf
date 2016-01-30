<%@ page language="java" import="java.util.*, java.net.URLDecoder" pageEncoding="utf-8"%>
<html>
<head>
    <title></title>
</head>

<body>
<fieldset>
  <table width="100%">
    <tr>
      <td width="100%">
        接口名称：<%=URLDecoder.decode(request.getParameter("interfaceName"), "utf-8") %>
      </td>
    </tr>
    <tr>
      <td>
        版本号：${param.versionCode }
      </td>

    </tr>
    <tr>
      <td width="100%">
        发布说明：<input id="versionDesc" type="text" class="easyui-textbox" style="width:200px, height:200" >
      </td>
    </tr>
    <tr>
      <td width="100%">
        <div class="win-bbar" style="text-align:center"><a href="#" class="easyui-linkbutton"  iconCls="icon-cancel" onClick="$('#releaseDlg').dialog('close');">取消</a><a href="#" onclick="interfaceManager.release('${param.interfaceId}',$('#versionDesc').textbox('getValue'));" class="easyui-linkbutton"  iconCls="icon-save">保存</a></div>
      </td>
    </tr>
  </table>
</fieldset>
</body>
</html>
