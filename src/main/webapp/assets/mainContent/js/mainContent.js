$(function(){
    /**
     * 页签点击关闭左侧
     */
    $("#maintabsCloseLeft").click(function (){
        mainContentUIHelper.closeLeft();
    });
    /**
     * 页签点击关闭右侧
     */
    $("#maintabsCloseRight").click(function (){
        mainContentUIHelper.closeRight();
    });
    /**
     * 页签点击关闭其他
     */
    $("#maintabsCloseOhters").click(function (){
        mainContentUIHelper.closeOthers();
    });
    /**
     * 页签点击关闭所有
     */
    $("#maintabsCloseAll").click(function (){
        mainContentUIHelper.closeAll();
    });
})