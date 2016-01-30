var mainContentUIHelper = {
    "closeLeft":function(){
        for(var i =0;i<mainContentIndex;i++){
            //$("#mainContentTabs").tabs('close',i);
            //每次关闭都刷新了
            $("#mainContentTabs").tabs('close',0);
        }
    },
    "closeRight":function(){
        for(var i =maxIndex;i>mainContentIndex;i--){
            //$("#mainContentTabs").tabs('close',i);
            //每次关闭都刷新了
            $("#mainContentTabs").tabs('close',mainContentIndex+1);
        }
    },
    "closeOthers":function(){
        //先关闭左侧，再关闭右侧
        for(var i =0;i<mainContentIndex;i++){
            $("#mainContentTabs").tabs('close',0);
        }
        for(var i =maxIndex;i>mainContentIndex;i--){
            $("#mainContentTabs").tabs('close',1);
        }
    },
    "closeAll":function(){
        for(var i =0;i<maxIndex;i++){
            $("#mainContentTabs").tabs('close',0);
        }
    }
}