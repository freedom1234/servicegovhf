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

    <title>版本对比</title>
</head>
<body>
    <input id="versionId" value="${param.versionId}" type="hidden">
<table>
    <tr>
        <td>选择版本1</td>
        <td><input type="text" id="version1"
                   class="easyui-combobox"
                   data-options="
                       panelHeight:'300px',
						url:'/version/getVersions?versionId=${param.versionId}',
				 		 method:'get',
				 		 valueField: 'autoId',
				 		 textField: 'code',
				 		 value:'${param.autoId1}',
				 		 onChange:function(newValue, oldValue){
							this.value=newValue;
						}
					"
                /></td>
        <td>选择版本2</td>
        <td><input type="text" id="version2"
                   class="easyui-combobox"
                   data-options="
                       panelHeight:'300px',
						url:'/version/getVersions?versionId=${param.versionId}',
				 		 method:'get',
				 		 valueField: 'autoId',
				 		 textField: 'code',
				 		 value:'${param.autoId2}',
				 		 onChange:function(newValue, oldValue){
							this.value=newValue;
						}
					"
                /></td>
        <td>
            <a href="javascript:void(0)" id="saveTagBtn" onclick="userManager.compare()" class="easyui-linkbutton" iconCls="icon-search" style="margin-left:1em" >对比</a>
        </td>
        <td>
            <table >
                <tr>
                    <td style="width:20px;background-color:green"></td>
                    <td>添加</td>
                    <td style="width:20px;background-color:yellow"></td>
                    <td>修改</td>
                    <td style="width:20px;background-color:red"></td>
                    <td>删除</td>
                </tr>
            </table>
        </td>
    </tr>

</table>
<table class="easyui-treegrid" id="sdaHisTree" title="SDA对比" style=" width:100%; height:auto"
       data-options="
				rownumbers: true,
				animate: true,
				collapsible: true,
				fitColumns: false,
				method : 'get',
				url: '/version/getOperationDiff?versionId=${param.versionId}&autoId1=${param.autoId1}&autoId2=${param.autoId2}&type=${param.type}&_t='+ new Date().getTime(),
				idField: 'id',
				treeField: 'text',
				 rowStyler:function(row, index){
				              if(row.color !=''){
				                return 'background-color:'+row.color+';font-weight:bold;';
				              }
                        }
               "
        >
    <thead>
    <tr>
        <th data-options="field:'   '," colspan="9">版本1</th>
        <th data-options="field:' ',width:20" ></th>
        <th data-options="field:'structName1 '," colspan="9">版本2</th>
    </tr>
    <tr>
        <th data-options="field:'text',width:200" >字段名</th>
        <th data-options="field:'structAlias1',width:100" >别名</th>
        <th data-options="field:'metadataId1',width:100" >元数据ID</th>
        <th data-options="field:'seq1',width:50" >排序</th>
        <th data-options="field:'type1',width:50" >类型</th>
        <th data-options="field:'length1',width:50" >长度</th>
        <th data-options="field:'required1',width:50" >约束</th>
        <th data-options="field:'desc1',width:50" >描述</th>
        <th data-options="field:'remark1',width:50" >备注</th>
        <th data-options="field:' ',width:20" ></th>
        <th data-options="field:'structName2',width:120" >字段名</th>
        <th data-options="field:'structAlias2',width:100" >别名</th>
        <th data-options="field:'metadataId2',width:100" >元数据ID</th>
        <th data-options="field:'seq2',width:50" >排序</th>
        <th data-options="field:'type2',width:50" >类型</th>
        <th data-options="field:'length2',width:50" >长度</th>
        <th data-options="field:'required2',width:50" >约束</th>
        <th data-options="field:'desc2',width:50" >描述</th>
        <th data-options="field:'remark2',width:50" >备注</th>

    </tr>
    </thead>
</table>
</body>
</html>