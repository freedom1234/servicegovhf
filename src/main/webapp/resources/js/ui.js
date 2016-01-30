var uiinit = {
		
		
		win : function(arg){
			var h = '<div id="w" ></div>';
			$(".window").remove();
			$("body").append(h);
			$('#w').window({
				top:arg.top,
				left:arg.left,
				width:arg.w,
				height:arg.h,
				title:arg.title,
				iconCls:arg.iconCls,
				draggable:true,
				modal:true
			});
			$('#w').window('refresh', arg.url);
		},
		selectex : function(oldDataUi,newDataUi){
			var oldData = ""
				
				$('#'+oldDataUi+' option:selected').each(function(){
					$("#"+oldDataUi+" option[value="+$(this).val()+"]").remove();
					$("#"+newDataUi).append("<option value='"+$(this).val()+"'>"+this.text+"</option>");
					
				});
		},
		subtab : {
			
			add : function(arg){
				$('#subtab').tabs('a	dd',{
					title:arg.title,
					content:arg.content,
					closable:true
				});	
			},
			remove : function(id){
				
			}
		}
		
	}
