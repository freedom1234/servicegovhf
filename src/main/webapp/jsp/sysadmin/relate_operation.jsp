<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
    <base href="<%=basePath%>">

    <title>My JSP 'sdaHisPage.jsp' starting page</title>
  </head>

  <body>
  	<table title="服务场景" class="easyui-treegrid" id="tg" style=" width:100%;"
    			data-options="
    				iconCls: 'icon-ok',
    				rownumbers: true,
    				animate: true,
    				collapsible: true,
    				fitColumns: true,
    				url: '/operation/getByInterfaceId/${param.interfaceId}',
    				method: 'get',
    				idField: 'id',
    				treeField: 'text',
    				nowrap:false,
    				onDblClickRow:servicePage
    				"
                    >
    		<thead>
    			<tr>
    				<th data-options="field:'text',width:500,editor:'text'">名称</th>
    				<th data-options="field:'append1',width:250,align:'left',editor:'text'" >描述</th>
    			</tr>
    		</thead>
    	</table>
  <script type="text/javascript">
	  function servicePage(row){
		  var serviceId = null;
		  var operationId = null;
		  if(row.append2 !=  null && row.append2 != ""){
			  serviceId = row.append2;
			  if(row.append3 != null && row.append3 != ""){
				  operationId = row.append3;
			  }
		  }
		  if(serviceId != null && operationId!= null){
			  var urlPath =  '/jsp/service/servicePage2.jsp?serviceId='+serviceId + "&operationId="+operationId+"&_t=" + new Date().getTime();
			  var content = ' <iframe scrolling="auto" frameborder="0"  src="' + urlPath + '" style="width:100%;height:100%;"></iframe>'

			  parent.parent.$('#mainContentTabs').tabs('add', {
				  title: '服务（'+ serviceId+")",
				  content: content,
				  closable: true
			  });
		  }

	  }
  </script>
  </body>
</html>
