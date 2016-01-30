<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv ="X-UA-Compatible" content ="IE=edge" >
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>列表页</title>
<link rel="stylesheet" type="text/css" href="/resources/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="/resources/themes/icon.css">
<link href="/resources/css/ui.css" rel="stylesheet" type="text/css">
</head>

<body  style="margin:0px;">

<div class="easyui-tabs" id="subtab"  >
<div title="服务基本信息" style="padding:0px;">
    <iframe scrolling="auto" frameborder="0"  src="/dataTemplate/serviceadmin/grid.html"  style="width:100%;height:100%;"></iframe>
</div>
<%if(request.getParameter("active")!=null){%>

<div title="服务场景" style="padding:0px;">
    
</div>
<div title="服务接口SDO" style="padding:0px;">
    
</div>
<div title="服务SLA" style="padding:0px;">
    
</div>
<div title="服务OLA" style="padding:0px;">
    
</div>
<div title="服务接口映射" style="padding:0px;">
    
</div>
<%}%>
</div>

<script type="text/javascript" src="/resources/js/jquery.min.js"></script> 
<script type="text/javascript" src="/resources/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/resources/js/ui.js"></script>
<script type="text/javascript">

function showTabs(){
	
	$('#subtab').tabs('select', '服务场景');
}
<%if(request.getParameter("active")!=null){%>
	$(function(){
		showTabs();	
	})
<%}%>

	var k = 0;
	var j = 0;
	var m =0;
	var n =0;
	var q = 0;
	$('#subtab').tabs({
    border:false,
	border:false,
	width:"auto",
	height:$("body").height(),
    onSelect:function(title,index){
		
        if(index==1&&k==0){
			k++;
			var currTab =	$('#subtab').tabs('getSelected');
			$('#subtab').tabs('update', {
			  tab : currTab,
			  
			  options : {
			   content : ' <iframe scrolling="auto" frameborder="0"  src="/dataTemplate/serviceadmin/fwcj.html"  style="width:100%;height:100%;"></iframe>'
			  }
			 });	
		}
		
		 if(index==2&&j==0){
			j++;
			var currTab =	$('#subtab').tabs('getSelected');
			$('#subtab').tabs('update', {
			  tab : currTab,
			  
			  options : {
			   content : ' <iframe scrolling="auto" frameborder="0"  src="/dataTemplate/serviceadmin/grid2.html"  style="width:100%;height:100%;"></iframe>'
			  }
			 });	
		}
		
		 if(index==3&&m==0){
			m++;
			var currTab =	$('#subtab').tabs('getSelected');
			$('#subtab').tabs('update', {
			  tab : currTab,
			  
			  options : {
			   content : ' <iframe scrolling="auto" frameborder="0"  src="/dataTemplate/serviceadmin/grid3.html"  style="width:100%;height:100%;"></iframe>'
			  }
			 });	
		}
		 if(index==4&&n==0){
			n++;
			var currTab =	$('#subtab').tabs('getSelected');
			$('#subtab').tabs('update', {
			  tab : currTab,
			  
			  options : {
			   content : ' <iframe scrolling="auto" frameborder="0"  src="/dataTemplate/serviceadmin/grid4.html"  style="width:100%;height:100%;"></iframe>'
			  }
			 });	
		}
		 if(index==5&&q==0){
			q++;
			var currTab =	$('#subtab').tabs('getSelected');
			$('#subtab').tabs('update', {
			  tab : currTab,
			  
			  options : {
			   content : ' <iframe scrolling="auto" frameborder="0"  src="/dataTemplate/serviceadmin/grid5.html"  style="width:100%;height:100%;"></iframe>'
			  }
			 });	
		}
		//if(title+' is selected');
    
    }
});
</script>
</body>
</html>