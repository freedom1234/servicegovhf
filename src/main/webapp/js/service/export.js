var exportManager = {
     exportHandle : function(serviceId,operationId){

        var t1 =   $('#provider_isStandard').combobox('getValue');
        var t2 =   $('#consumer_isStandard').combobox('getValue');

        var i1 =   $('#provider_interfaceId').combobox('getValue');
        var i2 =   $('#consumer_interfaceId').combobox('getValue');

        var s1 = $('#provider_systemId').combobox('getValue');
        var s2 = $('#consumer_systemId').combobox('getValue');

        var p1 = $("#provider_protocol").combobox('getValue');
        var p2 = $("#consumer_protocol").combobox('getValue');
        var msg = ''
        if(t1 == '1'){
            if(i1 == ''){
                msg = "提供方";
            }
            t1 = false;
        }else{
            t1 = true;
        }

         if(t2 == '1'){
            if(i2 == ''){
                msg += "消费方";
            }
            t2 = false;
        }else{
            t2 = true;
        }

        if(s1 == ''){
            alert("请选择提供方系统");
            return;
        }

        if(s2 == ''){
            alert("请选择消费方系统");
             return;
        }

        if(msg!=''){
            alert("请选择"+msg +"接口");
            return;
        }

        if(t1){
            i1 = 'no';
        }
        if(t2){
            i2 = 'no';
        }
        var url ="/export/exportHandle/"+serviceId+"/"+operationId+"/"+s1+"/"+i1+"/"
                  +t1+"/"+s2+"/"+i2+"/"+t2+"/"+p1+"/"+p2;

            $.fileDownload(url,{
                failCallback: function (responseHtml, url) {
                    //TODO 怎么显示返回string？
                    var str = responseHtml;
                    if(str.indexOf("提供方接口未关联协议，导出失败") > 0){
                        alert("提供方接口未关联协议，导出失败");
                    }else if(str.indexOf("消费方接口未关联协议，导出失败") > 0){
                        alert("消费方接口未关联协议，导出失败");
                    }else if(str.indexOf("消费方接口协议报文生成类实例化失败,导出失败") > 0){
                        alert("消费方接口协议报文生成类实例化失败,导出失败");
                    }else if(str.indexOf("消费方接口协议报文生成类构造方法是不可访问,导出失败") > 0){
                        alert("消费方接口协议报文生成类构造方法是不可访问,导出失败");
                    }else if(str.indexOf("消费方接口协议报文生成类未找到，导出失败") > 0){
                        alert("消费方接口协议报文生成类未找到，导出失败");
                    }else if(str.indexOf("提供方接口协议报文生成类实例化失败,导出失败") > 0){
                        alert("提供方接口协议报文生成类实例化失败,导出失败");
                    }else if(str.indexOf("提供方接口协议报文生成类构造方法是不可访问,导出失败") > 0){
                        alert("提供方接口协议报文生成类构造方法是不可访问,导出失败");
                    }else if(str.indexOf("提供方接口协议报文生成类未找到，导出失败") > 0){
                        alert("提供方接口协议报文生成类未找到，导出失败");
                    }

                },
                prepareCallback: function (url) {
                    $('#w').window('close');
                 }

            });

     },
     selectStandardProvider : function(){
          var t =   $('#provider_isStandard').combobox('getValue');
          if(t == 0){
            $("#providerTr").hide();

              $("#providerprotocolTR").show();
          }else{
            $("#providerTr").show();
              $("#providerprotocolTR").hide();

          }
     },
       selectStandardConsumer : function(){
            var t =   $('#consumer_isStandard').combobox('getValue');
            if(t == 0){
              $("#consumerTr").hide();
             $("#consumerprotocolTR").show();
            }else{
              $("#consumerTr").show();

               $("#consumerprotocolTR").hide();
            }
       }

}