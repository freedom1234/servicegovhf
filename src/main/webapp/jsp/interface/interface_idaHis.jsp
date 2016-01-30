<%@ page contentType="text/html; charset=utf-8" language="java" %>
<table  id="idaHisTg"
       class="easyui-treegrid"
       data-options="
				rownumbers: false,
				animate: true,
				collapsible: true,
				fitColumns: true,
				method: 'get',
				url:'/idaHis/idaHisTree?interfaceHisId=${param.interfaceHisId }',
				idField: 'id',
				treeField: 'structName',
                singleSelect:true
			">
  <thead>
  <tr>
    <th
            data-options="field:'structName',width:120,align:'left'">
      字段名称
    </th>
    <th
            data-options="field:'structAlias',width:90,align:'left'">
      字段别名
    </th>
    <th data-options="field:'type',width:80">
      类型
    </th>
    <th data-options="field:'length',width:80">
      长度
    </th>
    <%--<th data-options="field:'metadataId',width:100,editor:'text'">
        元数据ID
    </th>--%>
    <%--<th data-options="field:'scale',width:100,editor:'text'">
        精度
    </th>--%>
    <th data-options="field:'required',width:50">
      是否必须
    </th>
    <th data-options="field:'remark',width:100">
      备注
    </th>
    <th data-options="field:'seq',width:50,hidden:true">
      排序
    </th>
  </tr>
  </thead>
</table>
