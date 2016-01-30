/*
 * 兼容ie浏览器，给数组添加indexOf方法
 */
if (!Array.prototype.indexOf)
{
  Array.prototype.indexOf = function(elt /*, from*/)
  {
    var len = this.length >>> 0;

    var from = Number(arguments[1]) || 0;
    from = (from < 0)
         ? Math.ceil(from)
         : Math.floor(from);
    if (from < 0)
      from += len;

    for (; from < len; from++)
    {
      if (from in this &&
          this[from] === elt)
        return from;
    }
    return -1;
  };
}
/*
 * 常用格式转换器
 */
var Common = {

	    //EasyUI用DataGrid用日期格式化
	    TimeFormatter: function (value, rec, index) {
	        if (value == undefined) {
	            return "";
	        }
	        var time = new Date();
	        time.setTime(value.time);
	        
	        var JsonDateValue = new Date(value.time);

	        var text = JsonDateValue.getFullYear()+"-"+(JsonDateValue.getMonth()+1)
	        	+"-"+JsonDateValue.getDate()+" "+JsonDateValue.getHours()+":"+JsonDateValue.getMinutes()+":"+JsonDateValue.getSeconds();
	        return text;
	    },
	    
	    ImageFormatter: function(value,row,index){
				return '<img width="50px" alt="暂无图片" heithg="30px" src="'+getRootPath()+'/'+value+'" />';
			}
	    
	};

/*
 * 图片上传方法，图片上传到服务器doc的image目录下
 */
function uploadFile(file,urlId,valueId, docSubpath){
	$.ajaxFileUpload({
		url : getRootPath()+'/admin/util.do?method=uploadImage&fileName='+file.name,
		secureuri : false,
		fileElementId : file.id,
		dataType : 'json',
		// 参数
		data : {
			"confirm" : "yes",
			"docSubpath" : undefined == docSubpath ? "image" : docSubpath,
			"uploadType" : "click"
		},
		success : function(data, status) {
			alert(data.callfunction);
			var url = data.urlArray[0].replace('&amp;','&');
			getObject(urlId).src=url;
			getObject(valueId).value=data.imagePath;
			//document.getElementById(id).value='${ctx}'+data.urlArray[0];
		},
		error : function(data, status, e) {
			alert(e);
		}
	});
}

//已经选择的内容
var contentIds = new Array();
var innerContentIds = new Array();

var tempOperateConfId;

//内容布局预览
function previewContent(){
	$('#dlg').dialog({
		title : '预览',
		width : 700,
		height : 400,
		closed : false,
		cache : false,
		href : getRootPath()+'/admin/util/contentPreview.jsp',
		modal : true
	});
	$("#dlg").panel("move",{top:$(document).scrollTop() + ($(window).height()-400) * 0.5});
}
function addPreviewJsp(table){
	var maxRow = 0;
	var maxCol = 0;
	//计算行列数
	for(var i=0; i<contentIds.length; i++){
		var row = parseInt($("#mRow"+contentIds[i]).val());
		var col = parseInt($("#mCol"+contentIds[i]).val());
		var rowSpan = parseInt($("#mRowSpan"+contentIds[i]).val());
		var colSpan = parseInt($("#mColSpan"+contentIds[i]).val());
		if(maxRow < row+rowSpan){
			maxRow = row+rowSpan-1;
		}
		if(maxCol < col+colSpan){
			maxCol = col+colSpan-1;
		}
	}
	for(var r=1; r<=maxRow; r++){
		//添加行
		var trObj = $("<tr id='tr"+r+"'></tr>");
		trObj.appendTo($("#"+table));
		for(var c=1; c<=maxCol; c++){
			//添加列
			var tdObj = $("<td  id='tr"+r+"td"+c+"'></td>");
			tdObj.appendTo(trObj);
		}
	}
	
	for(var i=0; i<contentIds.length; i++){
		var src = $("#img"+contentIds[i]).attr("src");
		var row = parseInt($("#mRow"+contentIds[i]).val());
		var col = parseInt($("#mCol"+contentIds[i]).val());
		var rowSpan = parseInt($("#mRowSpan"+contentIds[i]).val());
		var colSpan = parseInt($("#mColSpan"+contentIds[i]).val());
		var width = parseInt($("#width"+contentIds[i]).val());
		var height = parseInt($("#height"+contentIds[i]).val());
		
		var tdObj = $("#tr"+row+"td"+col);
		for(var rs=1; rs<=rowSpan; rs++){
			var rn=row+rowSpan-rs;
			for(var cs=1; cs<=colSpan; cs++){
				//合并单元格
				var cn=col+colSpan-cs;
				if(rn*cn>row*col){
					$("#tr"+rn+"td"+cn).remove();					
				}
			}
			tdObj.attr("colSpan",colSpan);
		}
		tdObj.attr("rowSpan",rowSpan);	
		//tdObj.attr("width",width);
		//tdObj.attr("height",height);
		
		//创建图片
		var imgObj = $("<img id='iimg"+i+ "'  style='' width="+width+" height="+height+" alt='图片显示错误' src='"+src+"'/>");
		imgObj.appendTo(tdObj);
		
	}
	//获取表格宽高
	mSumWidth = $("#"+table).width();
	mSumHeight = $("#"+table).height();
	//计算缩放系数
	var p=1;
	if(mSumWidth > 680){
		p=(mSumWidth/680)*p;
	}
	if(mSumHeight>480){
		p=(mSumHeight/480)*p;
	}
	for(var i=0; i<contentIds.length; i++){
		var imgObj= $("#iimg"+i);
		var newWidth = imgObj.attr("width")/p;
		var newHeight = imgObj.attr("height")/p;
		imgObj.attr("width",newWidth);
		imgObj.attr("height",newHeight);
		
	}
	
	$.parser.parse($("#"+table));

}

//将当前页面的content添加到测试环境中
function previewTest(){
	 $.ajax({
         type: "GET",
         url:  getRootPath()+"/admin/util.do?method=configTest",
         dataType: "html",
         data: $('form').serialize(),
         success: function(data){
        	 alert(data.toString());
            }
	 	});
}

//添加入内容记录方法
function addContent(items){
	$.each(items, function(index, item){
		//判断选择的内容是否已经存在
		if(contentIds.indexOf(item.id+"") == -1){
			var trObj=$("<tr style='text-align:center'></tr>");
			var tdObj0 = $("<td><img id='img"+item.id+"' width=27 height=17 src='"+getRootPath()+item.posterUrl+"'/></td>");
			var tdObj1 = $("<td>"+item.name+"</td>");
			var tdObj2 = $("<td>"+item.typeName+"</td>");
			var tdObj3 = $("<td>"+item.code+"</td>");
			var tdObj4 = $("<td><input id='mRow"+item.id+"' type='text' style='width:30px' name='mRow' class='easyui-numberbox' onChange='alert(1)'/></td>");
			var tdObj5 = $("<td><input id='mCol"+item.id+"' type='text' style='width:30px' name='mCol' class='easyui-numberbox'/></td>");
			var tdObj6 = $("<td><input id='mRowSpan"+item.id+"' type='text' style='width:30px' name='mRowSpan'class='easyui-numberbox'/></td>");
			var tdObj7 = $("<td><input id='mColSpan"+item.id+"' type='text' style='width:30px' name='mColSpan'class='easyui-numberbox'/></td>");
			var tdObj8 = $("<td><input id='width"+item.id+"' type='text' style='width:30px' name='width'class='easyui-numberbox' /></td>");
			var tdObj9 = $("<td><input id='height"+item.id+"' type='text' style='width:30px' name='height'class='easyui-numberbox' /></td>");
			var tdObj10 = $("<td><img title='删除内容' src='"+getRootPath()+"/common/jquery-easyui-1.4/themes/icons/edit_remove.png'></td>");
			
			tdObj10.click(function(){
				if(confirm("确定要删除该内容吗？")){
					trObj.remove();
					contentIds.splice(contentIds.indexOf(item.id+""),1);
					$("#contentIds").attr("value",contentIds);
				};	
			});
			tdObj0.appendTo(trObj);
			tdObj1.appendTo(trObj);
			tdObj2.appendTo(trObj);
			tdObj3.appendTo(trObj);
			tdObj4.appendTo(trObj);
			tdObj5.appendTo(trObj);
			tdObj6.appendTo(trObj);
			tdObj7.appendTo(trObj);
			tdObj8.appendTo(trObj);
			tdObj9.appendTo(trObj);
			tdObj10.appendTo(trObj);
			trObj.appendTo($("#contentValues"));
			$.parser.parse(trObj);
			
			contentIds.push(item.id+"");
		}
		
	});
	$("#contentIds").attr("value",contentIds);
	$(".easyui-numberbox").numberbox({
		 onChange:function(newValue,oldValue){
			 $(".preview tbody").empty();
			 addPreviewJsp("preview2"); 
		 }
	});
}
//添加入内容记录方法
//默认推荐位列表内容不允许重复，但每个推荐位配置内容允许重复
function addSectionContent(items){
	$.each(items, function(index, item){
		//判断选择的内容是否已经存在
		if(contentIds.indexOf(item.id+"") == -1){
			var trObj=$("<tr style='text-align:center'></tr>");
			var innerTrObj=$("<tr style='text-align:center'></tr>");
			
			var tdObj0 = $("<td><img id='img"+item.id+"' width=27 height=17 src='"+getRootPath()+item.posterUrl+"'/></td>");
			var tdObj1 = $("<td>"+item.name+"</td>");
			var tdObj2 = $("<td>"+item.typeName+"</td>");
			var tdObj3 = $("<td>"+item.code+"</td>");
			var tdObj4 = $("<td><input id='mRow"+item.id+"' type='text' style='width:30px' name='mRow' class='easyui-numberbox' onChange='alert(1)'/></td>");
			var tdObj5 = $("<td><input id='mCol"+item.id+"' type='text' style='width:30px' name='mCol' class='easyui-numberbox'/></td>");
			var tdObj6 = $("<td><input id='mRowSpan"+item.id+"' type='text' style='width:30px' name='mRowSpan'class='easyui-numberbox'/></td>");
			var tdObj7 = $("<td><input id='mColSpan"+item.id+"' type='text' style='width:30px' name='mColSpan'class='easyui-numberbox'/></td>");
			var tdObj8 = $("<td><input id='width"+item.id+"' type='text' style='width:30px' name='width'class='easyui-numberbox' /></td>");
			var tdObj9 = $("<td><input id='height"+item.id+"' type='text' style='width:30px' name='height'class='easyui-numberbox' /></td>");
			var tdObj10 = $("<td><img title='删除内容' src='"+getRootPath()+"/common/jquery-easyui-1.4/themes/icons/edit_remove.png'></td>");
			var tdObj11 = $("<td align=center><div class='arrow'></div></td>");
			
			tdObj10.click(function(){
				if(confirm("确定要删除该内容吗？")){
					trObj.remove();
					innerTrObj.remove();
					contentIds.splice(contentIds.indexOf(item.id+""),1);
					$("#contentIds").attr("value",contentIds);
				};	
			});
			
			tdObj11.click(function(){
				innerTrObj.toggle();
		        $(this).find(".arrow").toggleClass("up");
		    });
			
			tdObj0.appendTo(trObj);
			tdObj1.appendTo(trObj);
			tdObj2.appendTo(trObj);
			tdObj3.appendTo(trObj);
			tdObj4.appendTo(trObj);
			tdObj5.appendTo(trObj);
			tdObj6.appendTo(trObj);
			tdObj7.appendTo(trObj);
			tdObj8.appendTo(trObj);
			tdObj9.appendTo(trObj);
			tdObj11.appendTo(trObj);
			tdObj10.appendTo(trObj);
			
			trObj.appendTo($("#contentValues"));
			
			
			innerTrObj.css("display","none");
			innerTrObj.appendTo($("#contentValues"));
			
			var innerTd = $("<td colspan='12'> </td>");
			innerTd.appendTo(innerTrObj);
			
			var innerTable = $("<table id='innerTable" + item.id + "' width='100%' height='16px'> </table> ")
			innerTable.appendTo(innerTd);
			
			var iTh1 =$("<th width='30'><B>海报</B></th>");
			var iTh2 =$("<th width='80''><B>内容名称</B></th>");
			var iTh3 =$("<th width='30'><B>类型</B></th>");
			var iTh4 =$("<th width='30'><B>编码</B></th>");
			var iTh5 =$("<th width='60'><B>运营配置</B></th>");
			var iTh6 =$("<th width='60'><img title='添加内容' src='"+getRootPath()+"/common/jquery-easyui-1.4/themes/icons/add.png'></th>");
			
			
			
			iTh6.click(function(){
				popDialog('dlg','选择内容', getRootPath()+'/admin/util.do?method=contentSelect&type=content&isSection=step2&innerTable=innerTable'+item.id);
			});
			
			iTh1.appendTo(innerTable);
			iTh2.appendTo(innerTable);
			iTh3.appendTo(innerTable);
			iTh4.appendTo(innerTable);
			iTh5.appendTo(innerTable);
			iTh6.appendTo(innerTable);
			
			$.parser.parse(innerTrObj);
			$.parser.parse($("#contentValues"));
			
			contentIds.push(item.id+"");
		}
		
	});
	$("#contentIds").attr("value",contentIds);
	$(".easyui-numberbox").numberbox({
		 onChange:function(newValue,oldValue){
			 $(".preview tbody").empty();
			 addPreviewJsp("preview2"); 
		 }
	});
}

//添加入内容记录方法
function addSectionContent2(innerTable, items){
	$.each(items, function(index, item){
		//判断选择的内容是否已经存在
		//if(contentIds.indexOf(item.id+"") == -1){
			var rowId = new Date().getTime()+ "_" + index;
			var trObj=$("<tr id='" + rowId + "' style='text-align:center'></tr>");
			var tdObj0 = $("<td><img  width=27 height=17 src='"+getRootPath()+item.posterUrl+"'/></td>");
			var tdObj1 = $("<td>"+item.name+"</td>");
			var tdObj2 = $("<td>"+item.typeName+"</td>");
			var tdObj3 = $("<td>"+item.code+"</td>");
			var tdObj4 = $("<td><img id='img_" + rowId + "' title='运营配置' src='"+getRootPath()+"/common/jquery-easyui-1.4/themes/icons/edit.png'></td>");
			
			tdObj4.click(function(){
				var inited = $("#operateConf_" + rowId).attr("value");
				if (!inited || typeof(inited)=="undefined"){ //添加操作
					popDialog('dlg','添加运营配置', getRootPath()+'/admin/client/operateConf.do?method=simpleAdd&type=4&status=1&rowId=' + rowId);
				}else{
					popDialog('dlg','修改运营配置', getRootPath()+'/admin/client/operateConf.do?method=simpleEdit&id=' +inited);
				}
				
			});
			
			var hiddenContent = $("<input type='hidden' name='content_" +innerTable+ "'>");
			hiddenContent.attr("value", item.id);
			var hiddenOperateConf =  $("<input type='hidden' name='operateConf_" +innerTable+ "' id='operateConf_" +rowId+ "'>");
			
			
			var tdObj10 = $("<td><img title='删除内容' src='"+getRootPath()+"/common/jquery-easyui-1.4/themes/icons/remove.png'></td>");
			
			tdObj10.click(function(){
				if(confirm("确定要删除该内容吗？")){
					trObj.remove();
//					contentIds.splice(contentIds.indexOf(item.id+""),1);
//					$("#contentIds").attr("value",contentIds);
				};	
			});
			
			
			hiddenContent.appendTo(trObj);
			hiddenOperateConf.appendTo(trObj);
			
			tdObj0.appendTo(trObj);
			tdObj1.appendTo(trObj);
			tdObj2.appendTo(trObj);
			tdObj3.appendTo(trObj);
			tdObj4.appendTo(trObj);
			tdObj10.appendTo(trObj);
			trObj.appendTo($("#"+innerTable));
			$.parser.parse(trObj);
		//}
		
	});
//	$("#contentIds").attr("value",contentIds);
}

//弹出对话框
function popDialog(dlgId,title,url){
	$('#'+dlgId).dialog({
		title : title,
		width : 700,
		height : 500,
		closed : false,
		cache : false,
		href : url,
		modal : true
	});
	$("#"+dlgId).panel("move",{top:$(document).scrollTop() + ($(window).height()-400) * 0.5});
}

//JSON日期转字符串
function timeFormatter(value){
	var JsonDateValue = new Date(value.time);

	var text = JsonDateValue.getFullYear()+"-"+(JsonDateValue.getMonth()+1)
		+"-"+JsonDateValue.getDate()+" "+JsonDateValue.getHours()+":"+JsonDateValue.getMinutes()+":"+JsonDateValue.getSeconds();
	return text;
}

//js获取项目根路径，如： http://localhost:8083/uimcardprj
function getRootPath(){
    //获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
    var curWwwPath=window.document.location.href;
    //获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
    var pathName=window.document.location.pathname;
    var pos=curWwwPath.indexOf(pathName);
    //获取主机地址，如： http://localhost:8083
    var localhostPaht=curWwwPath.substring(0,pos);
    //获取带"/"的项目名，如：/uimcardprj
    var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
    return(localhostPaht+projectName+"/");
}

//全国运营选中事件
function showArea(isAllCity, subArea){
	if($("#"+isAllCity).attr("value") == 0){
		$("#"+isAllCity).attr("value", 1);
		document.getElementById(subArea).style.display="";
	}
	else{
		$("#"+isAllCity).attr("value", 0);
		document.getElementById(subArea).style.display="none";
	}
}
//地区组件加载
function areaReady(id){
	$("#"+id).combotree({
		url: getRootPath()+'/admin/client/areaInfo.do?method=treeData',
		method:'get',
	 	line:true,
	 	multiple:true,
	 	onBeforeExpand:function(node,param){ 
		$("#"+id).combotree("tree").tree("options").url  = getRootPath()+'/admin/client/areaInfo.do?method=treeData&fatherId='+node.id;                
		}
	});
}
//终端展示选择
function showTerminal(isAllTerminal, terminal){
	
	if($("#"+isAllTerminal).attr("value") == 0){
		$("#"+isAllTerminal).attr("value", 1);
		document.getElementById(terminal).style.display="";
	}
	else{
		$("#"+isAllTerminal).attr("value", 0);
		document.getElementById(terminal).style.display="none";
	}
	
}
//终端加载
function terminalReady(id){
	 $("#"+id).combogrid({
	        panelWidth:360,
	        idField:'terminalID',
	        textField:'brand',
	        multiple:true,
	        url: getRootPath()+'/admin/client/terminal.do?method=contentJson',
	        columns:[[
	        {field:'terminalID',checkbox:true,width:20},
	        {field:'brand',title:'品牌',width:100},
	        {field:'model',title:'型号',width:100},
	        {field:'manufacture',title:'生产厂家',width:120}
	        ]]
	        });
}

//刷新缓存

function refresh(memKey){
	 if(confirm("确定要情况缓存数据吗？")){
		 $.ajax({
	         type: "GET",
	         url: getRootPath()+ "/admin/util.do?method=refreshMem",
	         data: { "memKey" : memKey},
	         success: function(data){
	        	 alert(data.result);
	            }
		 	});
	 }
}
// 根据绑定类型和绑定内容ID获取绑定列表
function contentBand( cbId, cbType){
	var result;
	 $.ajax({
         type: "GET",
         async: false,
         url: getRootPath()+ "/admin/util.do?method=contentBanding",
         data: { "cbId" : cbId, "cbType" : cbType },
         success: function(data){
        	 result = data;
            }
	 	});
	 return result;
}
