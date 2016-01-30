<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: vincentfxz
  Date: 15/6/30
  Time: 17:31
  To change this template use File | Settings | File Templates.
--%>
<!--服务管理页面，服务树的右键菜单-->
<div id="mm-mxservicetree" class="easyui-menu" style="width: 120px;">
  <shiro:hasPermission name="service-add">
    <div id="serviceTreeAddBtn" data-options="iconCls:'icon-add'">新增</div>
  </shiro:hasPermission>
  <shiro:hasPermission name="service-update">
    <div id="serviceTreeEditBtn" data-options="iconCls:'icon-edit'">编辑</div>
  </shiro:hasPermission>
  <shiro:hasPermission name="service-delete">
    <div id="serviceTreeDeleteBtn" data-options="iconCls:'icon-remove'">删除</div>
  </shiro:hasPermission>
</div>
