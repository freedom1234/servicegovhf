<%--
  Created by IntelliJ IDEA.
  User: wang
  Date: 2015/9/2
  Time: 14:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" pageEncoding="utf-8" %>
<div style="text-align:center">
  <div >
      <a href="javascript:void(0)" onclick="$('#dlg').dialog('close');" class="easyui-linkbutton" iconCls="icon-cancel">取消</a>&nbsp;&nbsp;
      <a href="javascript:void(0)" onclick="auditSave('${param.type}')" class="easyui-linkbutton" iconCls="icon-save">确定</a>
  </div>
  <div style="margin-top:20px">
    <textarea id="auditRemark"  style="width:300px;height:140px;" />
  </div>

</div>
