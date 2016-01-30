<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
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
			<script type="text/javascript" src="/resources/js/jquery.min.js"></script>
	<script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
	<script type="text/javascript">
            function exportServiceView(){
				var t = $('#categoryId').combotree('tree');	// get the tree object
                var node = t.tree('getSelected');		// get selected node
				var type;
            	if(node == null || node == ''){
            		alert("请选择服务！");
            		return false;
            	}
            	var id = node.id;
            	if(node.domId == '_easyui_tree_1'){
            		type = "root";
            	}
            	if(node.domId == '_easyui_tree_2'){
                  	type = "serviceCategory1";
                }
                if(node.domId == '_easyui_tree_3'){
                    type = "serviceCategory1";
                    var parent = t.tree('getParent', node.target);
                    id = parent.id;
                }
                if(node.domId == '_easyui_tree_4'){
                    type = "serviceCategory1";
                    parent = t.tree('getParent', node.target);
					id = t.tree('getParent', parent.target).id;
                }

				var form=$("<form>");//定义一个form表单
				form.attr("style","display:none");
				form.attr("target","");
				form.attr("method","post");
				form.attr("action","/excelExporter/exportServiceView");
				var input1=$("<input>");
				input1.attr("type","hidden");
				input1.attr("name","categoryId");
				input1.attr("value",id);

				var input2=$("<input>");
				input2.attr("type","hidden");
				input2.attr("name","type");
				input2.attr("value",type);

				$("body").append(form);//将表单放置在web中
				form.append(input1);
				form.append(input2);

				form.submit();//表单提交

            }
	</script>
</head>

<body>
	<fieldset>
		<legend>服务视图导出</legend>
		<form id="baseForm">
			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<th>服务：</th>
					<td>
                        <input type="text" name="categoryId" id="categoryId" style="width:400px"
                                               class="easyui-combotree"
                                               data-options="
                                               panelHeight:'400px',
                        						url:'/service/getTree',
                        				 		 method:'get',
                        				 		 valueField: 'categoryId',
                        				 		 textField: 'categoryName',
                        				 		 onChange:function(newValue, oldValue){
                        							this.value=newValue;
                        						}
                        					"
                                            />
					</td>
					<td>
						<shiro:hasPermission name="categoryWord-add">
						<a href="javascript:void(0)" onclick="exportServiceView()" class="easyui-linkbutton" plain="true"
						iconCls="icon-dc">导出</a>
						</shiro:hasPermission>
					</td>
				</tr>
		</table>
		</form>
	</fieldset>
</body>
</html>