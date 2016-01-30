/**
 * Created by vincentfxz on 15/6/24.
 */
$(function() {

    //初始化服务Grid的方法
    var initTable = function initTable(result) {
        //创建Grid
        $("#interfaceManagementTable").dataTable( {
            "aaData" : result,
            "aoColumns" : englishWordLayout,
            "aoColumnDefs" : [
                {
                    "sClass" : "center",
                    "aTargets" : [ 0, 1, 2, 3, 4, 5]
                },
                {
                    "bVisible" : false,
                    "aTargets" : [0]
                }
            ],
            "bJQueryUI": true,
            "bAutoWidth" : true,
            "oLanguage" : oLanguage,
            "fnDrawCallback" : function() {
                columnClickEventInit();
            }
        });
    };

    interfaceManager.getInterfaceManagementList(initTable);

    //初始化操作Grid的搜索框
    var initTableFooter = function initTableFooter() {
        $("#interfaceManagementTable tfoot input").keyup(
            function() {
                tables["interfaceManagementTable"].fnFilter(this.value, $(
                    "#interfaceManagementTable tfoot input").index(this));
            });
        $("#interfaceManagementTable tfoot input").each(function(i) {
            asInitVals[i] = this.value;
        });
        $("#interfaceManagementTable tfoot input").focus(function() {
            if (this.className == "search_init") {
                this.className = "";
                this.value = "";
            }
        });
        $("#interfaceManagementTable tfoot input").blur(
            function(i) {
                if (this.value == "") {
                    this.className = "search_init";
                    this.value = asInitVals[$("#interfaceManagementTable tfoot input")
                        .index(this)];
                }
            });
    };
    initTableFooter();
});