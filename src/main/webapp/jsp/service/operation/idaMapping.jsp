<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <title>列表页</title>
  <link rel="stylesheet" type="text/css"
        href="/resources/themes/default/easyui.css">
  <link rel="stylesheet" type="text/css" href="/resources/themes/icon.css">
  <link href="/resources/css/ui.css" rel="stylesheet" type="text/css">
</head>

<body>
<fieldset>
  <legend>接口信息</legend>
  <table border="0" cellspacing="0" cellpadding="0">
    <tr>
      <th>系统id</th>
      <td>
        <input class="easyui-text" readonly="true" value="${system.systemId}" style="width:140px" type="text" name="name" id="masterName"/>
      </td>
      <th>系统名称</th>
      <td>
        <input class="easyui-text" readonly="true" value="${system.systemChineseName}" style="width:140px" type="text" name="name" id="masterName"/>
      </td>
      <th>接口id</th>
      <td>
        <input class="easyui-text" readonly="true" value="${inter.interfaceId}" style="width:140px" type="text" name="name" id="masterName"/>
      </td>
      <th>接口名称</th>
      <td>
        <input class="easyui-text" readonly="true" value="${inter.interfaceName}" style="width:140px" type="text" name="name" id="slaveName"/>
      </td>
    </tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </table>
</fieldset>
<table title="ida映射" id="mappingdatagrid" style="height:530px; width:auto;">
  <thead>
  <tr>
    <th data-options="field:'productid',checkbox:true"></th>
    <th data-options="field:'structName',title:'字段名称',width:'15%'"></th>
    <th data-options="field:'structAlias',title:'字段别名',width:'12%'"></th>
    <th data-options="field:'type',title:'类型',width:'10%'"></th>
    <th data-options="field:'remark',title:'接口备注',width:'15%'"></th>
    <th data-options="field:'metadataId',title:'元数据ID',required : true,width:'12%',
        "></th>
    <th data-options="field:'sdastructAlias',title:'对应SDA',width:'13%',
    editor:{type:'combotree',
      options:{url:'/sda/sdaComboTree?serviceId=${service.serviceId}&operationId=${operation.operationId}',
      method : 'get',
      valueField : 'id',
      textField : 'text',
      panelHeight : '200px',
      onSelect:function(node){
          if(node.text == '根节点' || node.text == '请求报文体' || node.text == '响应报文体'){
            alert('请选择其他节点');
            var node2 = $('#mappingdatagrid').treegrid('getSelected');
            $('#mappingdatagrid').treegrid('endEdit', node2.id);
            return false;
          }else{
            this.value = node.id
          }
        }
      }
      }
    "></th>
    <th data-options="field:'sdaremark',title:'服务备注',width:'21%'"></th>
    <th data-options="field:'id', hidden:true"></th>
  </tr>
  </thead>
</table>
<div id="w" class="easyui-window" title=""
     data-options="modal:true,closed:true,iconCls:'icon-add'"
     style="width:500px;height:200px;padding:10px;"></div>

<script type="text/javascript" src="/resources/js/jquery.min.js"></script>
<script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/resources/js/ui.js"></script>
<script type="text/javascript" src="/js/ida/idaManager.js"></script>
<script type="text/javascript"
        src="/assets/enumManager/js/enumManager.js"></script>
<script type="text/javascript">
  var editedRows = [];
  var processId = parent.processId;
  var taskId = parent.taskId;
  $(function(){
    var url = "/ida/getIdaMapping/${inter.interfaceId}/${operation.serviceId}/${operation.operationId}?t=" + (new Date()).valueOf();
    $('#mappingdatagrid').treegrid({
      animate: true,
      fitColumns: true,
      rownumbers:true,
      singleSelect:false,
      collapsible:true,
      url:url,
      method:'get',
      toolbar:toolbar,
      onContextMenu:onContextMenu,
      idField: 'id',
      treeField: 'structName',
//      pagination:true,
      pageSize:10,
     /* columns:[[
        {field:'productid',checkbox:true},
        {field:'structName',title:'字段名称',width:'15%'},
        {field:'structAlias',title:'字段别名',width:'12%'},
        {field:'type',title:'类型',width:'10%'},
        {field:'remark',title:'接口备注',width:'15%'},
        {field:'metadataId',title:'元数据ID',required : true,width:'12%',
          editor:{
            type:'combobox',
            options:{
              url : '/ida/getSdaMapping/'+"${service.serviceId}" + "/" +"${operation.operationId}",
              method : 'get',
              valueField : 'metadataId',
              textField : 'metadataId'
//              panelHeight : '200px'
            }
          }
        },
        {field:'sdastructAlias',title:'SDA字段别名',width:'13%'},
        *//*{field:'sdatype',title:'SDA类型',width:'10%'},*//*
        {field:'sdaremark',title:'服务备注',width:'21%'}
      ]],*/
      onDblClickCell: function(index,field){
        $(this).treegrid('beginEdit', field.id);
          var ed = $(this).treegrid('getEditor', {id:field.id,field:field});
          $(this).treegrid('select', field.id);
//        $(ed.target).focus();
      },
      onBeginEdit : function(index,row){
        editedRows.push(index);
      },
      onLoadSuccess : function (data){

      },
      onLoadError: function (responce) {
          var resText = responce.responseText;
          if(resText.toString().indexOf("没有操作权限") > 0){
//                    alert("没有权限！");
              window.location.href = "/jsp/403.jsp";
          }
      }

    });
  });

  function onContextMenu(e, row) {

    e.preventDefault();


    $(this).treegrid('unselectAll');

    $(this).treegrid('select', row.id);

    if (row.structName == 'root') {
      $('#mappingdatagrid').treegrid('unselect', row.id);
      return;
    }

  }

  var toolbar = [];
  <shiro:hasPermission name="ida-delete">
  toolbar.push({
      text : '删除映射关系',
      iconCls : 'icon-remove',
      handler : function() {
          var selectData = $('#mappingdatagrid').treegrid('getSelections');
          if (selectData.length == 0) {
              alert("请先选择一条记录");
              return;
          }
          if(confirm('确定删除映射关系吗 ？')){
              idaManager.deleteIdaMapping(selectData, function(result){
                  if(result){
                      $('#mappingdatagrid').treegrid('reload');
                  }
              });
          }
      }
  });
  </shiro:hasPermission>
  <shiro:hasPermission name="ida-update">
  toolbar.push({
      text : '保存映射关系',
      iconCls : 'icon-remove',
      handler : function() {
          for ( var per in editedRows) {
              $("#mappingdatagrid").treegrid('endEdit', editedRows[per].id);
          }
          var editData = $("#mappingdatagrid").treegrid('getChanges');
          idaManager.saveIdaMapping(editData,function(result){
              if(result){
                  $('#mappingdatagrid').treegrid('reload');
              }
          });
          editedRows = [];
      }
  });
  </shiro:hasPermission>

</script>
</body>
</html>