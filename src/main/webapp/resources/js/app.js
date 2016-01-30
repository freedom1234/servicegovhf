var PROCESS_INFO = {};
var LOAD_URL = {
    LEFTMENU: '/dataTemplate/leftMenu.jsp',
    CHECKRULE: '/dataTemplate/checkRule.jsp',
    SYSADMINUI: '/dataTemplate/sysadmin/sla.html',
    SYSADMINUIEDIT: '/dataTemplate/sysadmin/grid2.html',
    PUBLICHEADER: '/jsp/sysadmin/interface_header.jsp',
    INTERFACELIST: '/jsp/interface/interface_list.jsp',
    INTERFACEDEFINE: '/jsp/interface/interface_define.jsp',
    SYSTEMMANGER: '/jsp/sysadmin/system_manager.jsp',
    SYSTEMINDEX: '/jsp/sysadmin/system_index.jsp',
    SERVICEUI: '/dataTemplate/serviceadmin/index.html',
    SERVICEUI2: '/dataTemplate/serviceadmin/fwcjmx.html',
    SERVICEUI_LW: '/jsp/service/servicePage.jsp',
    USERADD: '/jsp/user/useradd.jsp',
    SEARCH: '/jsp/service/search.jsp'
};
var mainContentIndex = 0;
var maxIndex = 1;
var SYSMENU = {
    init: function () {
        SYSMENU.initHeaderMenu();
    },
    initHeaderMenu: function () {//初始化头部菜单
        $("#nav").find("a").click(function () {//点击更新左侧菜单
            $("#nav").find("a").removeClass("current");
            $(this).addClass("current");
            var mid = $(this).attr("mid");
            $("#west-menu").load(LOAD_URL.LEFTMENU, 'mid=' + mid, function () {
                $('#mxsysadmintreefilter').searchbox({
                    searcher: function (value, name) {
                        $('.mslinktree').tree('doFilter', value);
                    },
                    prompt: '请输入关键词'
                });
                $('#servicetreefilter').searchbox({
                    "searcher": function (value, name) {
                        $('.mxservicetree').tree('doFilter', value);
                    },
                    prompt: '请输入服务名'
                });
                $('#mxinterfacetreefilter').searchbox({
                    searcher: function (value, name) {
                        $('.msinterfacetree').tree('doFilter', value);
                    },
                    prompt: '请输入关键词'
                });
                $('#mxinterfaceheadtreefilter').searchbox({
                    searcher: function (value, name) {
                        $('.mxsysadmintree').tree('doFilter', value);
                    },
                    prompt: '请输入关键词'
                });

                var tab = $('#mainContentTabs').tabs('getSelected');
                var index = $('#mainContentTabs').tabs('getTabIndex', tab);

                //tabs菜单  mainContentTabs
                $("#mainContentTabs").tabs({
                    onContextMenu: function (e, title, index) {
                        e.preventDefault();
                        mainContentIndex = index;
                        var tabs = $("#mainContentTabs").tabs('tabs');
                        maxIndex = tabs.length;
                        $('#mm-mxmaintabs').menu('show', {
                            left: e.pageX,
                            top: e.pageY
                        });
                    }
                });
                $('#mainContentTabs').tabs('select', index);

                //报文管理
                $('.mxsysadmintree').tree({
                    onContextMenu: function (e, node) {
                        /*if(node.id=='root'){
                         return;
                         }*/
                        e.preventDefault();
                        $(this).tree('select', node.target);
                        if (typeof(node.children) != 'undefined') {//编辑接口
                            $('#mm-mxsysadmintree').menu('show', {
                                left: e.pageX,
                                top: e.pageY
                            });
                        }
                    },
                    onClick: function (node) {
                        if (node.id == 'root') {
                            return;
                        }
                        if (typeof(node.children) == 'undefined') {//编辑接口
                            var url = LOAD_URL.SYSADMINUIEDIT;
                            var mid = node.id;
                            var title = node.text;
                            //公共报文头信息管理
                            if (mid == 1) {
                                url = LOAD_URL.PUBLICHEADER;
                            }

                            if ($('#mainContentTabs').tabs('exists', title)) {
                                $('#mainContentTabs').tabs('select', title);
                            } else {
                                var content = '<iframe scrolling="yes" frameborder="0"  src="' + url + '" style="width:100%;height:98%;"></iframe>';
                                $('#mainContentTabs').tabs('add', {
                                    title: title,
                                    content: content,
                                    closable: true,
                                    fit: true
                                });
                            }
                        }

                        else {//基本信息
                            var mid = node.id;
                            var title = node.text;
                            var node = $('.mxsysadmintree').tree("getSelected");
                            if ($('#mainContentTabs').tabs('exists', title)) {
                                $('#mainContentTabs').tabs('select', title);
                            } else {//LOAD_URL.SYSADMINUI+
                                var content = '<iframe scrolling="yes" frameborder="0"  src="' + LOAD_URL.PUBLICHEADER + '?headId=' + node.id + '" style="width:100%;height:98%;"></iframe>';
                                $('#mainContentTabs').tabs('add', {
                                    title: title,
                                    content: content,
                                    closable: true,
                                    fit: true
                                });
                            }
                        }
                    }
                });

                //接口管理
                var loadFlag = false;
                $('.msinterfacetree').tree({
                    onContextMenu: function (e, node) {
                        e.preventDefault();
                        $(this).tree('select', node.target);
                        if (typeof(node.children) != 'undefined') {//编辑接口
                            if (node.click == 'system') {
                                $('#mm-mxsystemtree').menu('show', {
                                    left: e.pageX,
                                    top: e.pageY
                                });
                            } else if (node.click == 'disable') {

                                $('#mm-mxsystemtree1').menu('show', {
                                    left: e.pageX,
                                    top: e.pageY
                                });
                                //右键报文头节点
                            } else if (node.click == "heads") {
                                $('#mm-mxsysadmintree').menu('show', {
                                    left: e.pageX,
                                    top: e.pageY
                                });
                                //右键协议节点
                            } else if (node.click == "protocols") {

                                $('#mm-mxprotocols').menu('show', {
                                    left: e.pageX,
                                    top: e.pageY
                                });
                                //右键具体报文头节点
                            } else if (node.click == "head") {
                                $('#mm-syshead').menu('show', {
                                    left: e.pageX,
                                    top: e.pageY
                                });
                                //邮件具体协议节点
                            } else if (node.click == "protocol") {
                                $('#mm-mxprotocol').menu('show', {
                                    left: e.pageX,
                                    top: e.pageY
                                });
                            } else if (node.click == "files") {
                                $("#mm-mxfiles").menu("show", {
                                    left: e.pageX,
                                    top: e.pageY
                                });
                            } else if (node.click == "file") {
                                $("#mm-mxfile").menu("show", {
                                    left: e.pageX,
                                    top: e.pageY
                                });
                            } else {
                                $('#mm-mxinterfacetree1').menu('show', {
                                    left: e.pageX,
                                    top: e.pageY
                                });
                            }
                        }
                    },
                    onClick: function (node) {
                        var systemNode = $('.msinterfacetree').tree("getParent", node.target);
                        if (node.click == 'system') {
                            var mid = node.id;
                            //var title = node.text;
                            var title = "系统";
                            if ($('#mainContentTabs').tabs('exists', title)) {
                                $('#mainContentTabs').tabs('select', title);
                            } else {

                                var content = '<iframe name="serviceFrame" id="serviceFrame" scrolling="auto"  frameborder="0"  src="' + LOAD_URL.SYSTEMINDEX + '?_t=' + new Date().getTime() + '" style="width:100%;height:97%;"></iframe>';
                                $('#mainContentTabs').tabs('add', {
                                    title: title,
                                    content: content,
                                    closable: true,
                                    fit: true
                                });
                            }
                        } else if (node.click == "interfaces") {
                            var mid = systemNode.id;
                            var title = node.text + '(' + mid + ')';
                            if ($('#mainContentTabs').tabs('exists', title)) {
                                $('#mainContentTabs').tabs('select', title);
                            } else {//SYSADMINUIEDIT
                                var content = '<iframe scrolling="auto" frameborder="0"  src="' + LOAD_URL.INTERFACELIST + '?systemId=' + mid + '&_t=' + new Date().getTime() + '" style="width:100%;height:98%;"></iframe>';
                                $('#mainContentTabs').tabs('add', {
                                    title: title,
                                    content: content,
                                    closable: true,
                                    fit: true
                                });
                            }

                        } else if (node.click == 'disable') {
                            var mid = systemNode.id;
                            var title = node.text;
                            if ($('#mainContentTabs').tabs('exists', title)) {
                                $('#mainContentTabs').tabs('select', title);
                            } else {//SYSADMINUIEDIT
                                var content = '<iframe scrolling="auto" frameborder="0"  src="' + LOAD_URL.INTERFACELIST + '?systemId=' + mid + '&_t=' + new Date().getTime() + '" style="width:100%;height:98%;"></iframe>';
                                $('#mainContentTabs').tabs('add', {
                                    title: title,
                                    content: content,
                                    closable: true,
                                    fit: true
                                });
                            }

                        } else if (node.click == "head") {
                            var mid = systemNode.id;
                            var title = node.text;
                            if ($('#mainContentTabs').tabs('exists', title)) {
                                $('#mainContentTabs').tabs('select', title);
                            } else {//LOAD_URL.SYSADMINUI+
                                var content = '<iframe scrolling="auto" frameborder="0"  src="' + LOAD_URL.PUBLICHEADER + '?headId=' + node.id + '&_t=' + new Date().getTime() + '" style="width:100%;height:98%;"></iframe>';

                                $('#mainContentTabs').tabs('add', {
                                    title: title,
                                    content: content,
                                    closable: true,
                                    fit: true
                                });
                            }

                        } else if (node.click == "protocol") {
                            var mid = node.id;
                            var title = node.text;
                            if ($('#mainContentTabs').tabs('exists', title)) {
                                $('#mainContentTabs').tabs('select', title);
                            } else {
                                var content = '<iframe scrolling="auto" frameborder="0"  src="/jsp/sysadmin/system_protocol.jsp?protocolId=' + mid + '&_t=' + new Date().getTime() + '"  style="width:100%;height:98%;"></iframe>';
                                $('#mainContentTabs').tabs('add', {
                                    title: title,
                                    content: content,
                                    closable: true,
                                    fit: true
                                });
                            }
                        } else if (node.click == "protocols") {

                        } else if (node.click == "heads") {

                        } else if (node.click == "files") {
                            var title = "系统" + systemNode.id + "需求文件";
                            if ($('#mainContentTabs').tabs('exists', title)) {
                                $('#mainContentTabs').tabs('select', title);
                            } else {
                                var content = '<iframe scrolling="auto" frameborder="0"  src="/jsp/sysadmin/file_list.jsp?isAll=0&systemId=' + systemNode.id + '&_t=' + new Date().getTime() + '"  style="width:100%;height:98%;"></iframe>';
                                $('#mainContentTabs').tabs('add', {
                                    title: title,
                                    content: content,
                                    closable: true,
                                    fit: true
                                });
                            }
                        } else if (node.click == "file") {

                        } else {
                            var mid = node.id;
                            var title = node.text;
                            if ($('#mainContentTabs').tabs('exists', title)) {
                                $('#mainContentTabs').tabs('select', title);
                            } else {//SYSADMINUIEDIT
                                var content = '<iframe scrolling="auto" frameborder="0"  src="' + LOAD_URL.INTERFACEDEFINE + '?interfaceId=' + mid + '&_t=' + new Date().getTime() + '" style="width:100%;height:98%;"></iframe>';
                                $('#mainContentTabs').tabs('add', {
                                    title: title,
                                    content: content,
                                    closable: true,
                                    fit: true
                                });
                            }
                        }
                    },
                    "onBeforeExpand": function (node) {
                        if (node.children == null && node.click == 'system') {
                            $.ajax({
                                type: "get",
                                async: false,
                                url: "/interface/getLeftTree/subtree/system/" + node.id + "?_t=" + new Date().getTime(),
                                dataType: "json",
                                success: function (result) {
                                    $('.msinterfacetree').tree('append', {
                                        parent: (node ? node.target : null),
                                        data: result
                                    });
                                }

                            });
                        }
                    }
                });

                $('.msinterfacetree').tree('collapseAll');

                $('.mslinktree').tree({
                    onClick: function (node) {
                        if (typeof(node.children) == 'undefined') {
                            var mid = node.id;
                            var title = node.text + "交易链路";
                            if ($('#mainContentTabs').tabs('exists', title)) {
                                $('#mainContentTabs').tabs('select', title);
                            } else {
                                var content = '<iframe scrolling="auto" frameborder="0"  src="/jsp/serviceLink/dom.html' + '?_t=' + new Date().getTime() + '" style="width:100%;height:98%;"></iframe>';
                                $('#mainContentTabs').tabs('add', {
                                    title: title,
                                    content: content,
                                    closable: true,
                                    fit: true
                                });
                            }
                        } else {
                            var mid = node.id;
                            var title = node.text + "交易链路";
                            if ($('#mainContentTabs').tabs('exists', title)) {
                                $('#mainContentTabs').tabs('select', title);
                            } else {
                                var content = '<iframe scrolling="auto" frameborder="0"  src="/jsp/serviceLink/index.jsp?systemId=' + node.id + '&_t=' + new Date().getTime() + '" style="width:100%;height:98%;"></iframe>';
                                $('#mainContentTabs').tabs('add', {
                                    title: title,
                                    content: content,
                                    closable: true,
                                    fit: true
                                });
                            }
                        }
                    }
                });

                $('.mxservicetree').tree({
                    onContextMenu: function (e, node) {
                        e.preventDefault();
                        $(this).tree('select', node.target);
                        if (typeof(node.children) != 'undefined') {
                            $('#mm-mxservicetree').menu('show', {
                                left: e.pageX,
                                top: e.pageY
                            });
                        }
                    },
                    onClick: function (node) {
                        if (node.type == 'root') {
                            return;
                            var title = "服务检索";
                            var content = '<iframe scrolling="auto"  name="searchFrame" id="searchFrame" frameborder="0"  src="' + LOAD_URL.SEARCH + '?_t=' + new Date().getTime() + '" style="width:100%;height:98%;"></iframe>';
                            if ($('#mainContentTabs').tabs('exists', title)) {
                                $('#mainContentTabs').tabs('select', title);
                            } else {
                                $('#mainContentTabs').tabs('add', {
                                    title: title,
                                    content: content,
                                    closable: true,
                                    fit: true
                                });
                            }
                        } else if (node.type == 'service') {//打开服务场景
                            if ($("#serviceFrame" + node.id).size() == 0) {//如果没有打开基本信息，则新创建基本信息
                                var mid = node.id;
                                var title = node.text;
                                if ($('#mainContentTabs').tabs('exists', title)) {
                                    $('#mainContentTabs').tabs('select', title);
                                } else {
                                    var content = '<iframe scrolling="auto"  name="serviceFrame' + node.id + '" id="serviceFrame' + node.id + '" frameborder="0"  src="' + LOAD_URL.SERVICEUI_LW + "?serviceId=" + node.id + '&_t=' + new Date().getTime() + '" style="width:100%;height:97%;"></iframe>';
                                    $('#mainContentTabs').tabs('add', {
                                        title: title,
                                        content: content,
                                        closable: true,
                                        fit: true
                                    });
                                }
                            } else {
                                var mid = node.id;
                                var title = node.text;
                                if ($('#mainContentTabs').tabs('exists', title)) {
                                    $('#mainContentTabs').tabs('select', title);
                                }
                            }
                        }
                    }
                });
                SYSTABMENU.init();
            });
        });
    },
    changeLeftMenu: function (mid) {
        $("#west-menu").load(LOAD_URL.LEFTMENU, 'mid=' + mid, function () {
            $('#mxsysadmintreefilter').searchbox({
                searcher: function (value, name) {
                    alert(value + "," + name);
                },
                prompt: '请输入关键词'
            });
            $('#servicetreefilter').searchbox({
                "searcher": function (value, name) {
                    $('.mxservicetree').tree('doFilter', value);
                },
                prompt: '请输入服务名'
            });
            $('#mxinterfacetreefilter').searchbox({
                searcher: function (value, name) {
                    $('.msinterfacetree').tree('doFilter', value);
                },
                prompt: '请输入关键词'
            });
            $('#mxinterfaceheadtreefilter').searchbox({
                searcher: function (value, name) {
                    $('.mxsysadmintree').tree('doFilter', value);
                },
                prompt: '请输入关键词'
            });

            //报文管理
            $('.mxsysadmintree').tree({
                onContextMenu: function (e, node) {
                    /*if(node.id=='root'){
                     return;
                     }*/
                    e.preventDefault();
                    $(this).tree('select', node.target);
                    if (typeof(node.children) != 'undefined') {//编辑接口
                        $('#mm-mxsysadmintree').menu('show', {
                            left: e.pageX,
                            top: e.pageY
                        });
                    }
                },
                onClick: function (node) {
                    if (node.id == 'root') {
                        return;
                    }
                    if (typeof(node.children) == 'undefined') {//编辑接口
                        var url = LOAD_URL.SYSADMINUIEDIT;
                        var mid = node.id;
                        var title = node.text;
                        //公共报文头信息管理
                        if (mid == 1) {
                            url = LOAD_URL.PUBLICHEADER;
                        }

                        if ($('#mainContentTabs').tabs('exists', title)) {
                            $('#mainContentTabs').tabs('select', title);
                        } else {
                            var content = '<iframe scrolling="auto" frameborder="0"  src="' + url + '?_t=' + new Date().getTime() + '" style="width:100%;height:98%;"></iframe>';
                            $('#mainContentTabs').tabs('add', {
                                title: title,
                                content: content,
                                closable: true,
                                fit: true
                            });
                        }
                    }

                    else {//基本信息
                        var mid = node.id;
                        var title = node.text;
                        var node = $('.mxsysadmintree').tree("getSelected");
                        if ($('#mainContentTabs').tabs('exists', title)) {
                            $('#mainContentTabs').tabs('select', title);
                        } else {//LOAD_URL.SYSADMINUI+
                            var content = '<iframe scrolling="auto" frameborder="0"  src="' + LOAD_URL.PUBLICHEADER + '?headId=' + node.id + '&_t=' + new Date().getTime() + '" style="width:100%;height:98%;"></iframe>';
                            $('#mainContentTabs').tabs('add', {
                                title: title,
                                content: content,
                                closable: true,
                                fit: true
                            });
                        }
                    }
                }
            });

            //接口管理
            var loadFlag = false;
            $('.msinterfacetree').tree({
                onContextMenu: function (e, node) {
                    e.preventDefault();
                    $(this).tree('select', node.target);
                    if (typeof(node.children) != 'undefined') {//编辑接口
                        if (node.click == 'system') {
                            $('#mm-mxsystemtree').menu('show', {
                                left: e.pageX,
                                top: e.pageY
                            });
                        } else if (node.click == 'disable') {

                            $('#mm-mxsystemtree1').menu('show', {
                                left: e.pageX,
                                top: e.pageY
                            });
                            //右键报文头节点
                        } else if (node.click == "heads") {
                            $('#mm-mxsysadmintree').menu('show', {
                                left: e.pageX,
                                top: e.pageY
                            });
                            //右键协议节点
                        } else if (node.click == "protocols") {

                            $('#mm-mxprotocols').menu('show', {
                                left: e.pageX,
                                top: e.pageY
                            });
                            //右键具体报文头节点
                        } else if (node.click == "head") {
                            $('#mm-syshead').menu('show', {
                                left: e.pageX,
                                top: e.pageY
                            });
                            //邮件具体协议节点
                        } else if (node.click == "protocol") {
                            $('#mm-mxprotocol').menu('show', {
                                left: e.pageX,
                                top: e.pageY
                            });
                        } else if (node.click == "files") {
                            $("#mm-mxfiles").menu("show", {
                                left: e.pageX,
                                top: e.pageY
                            });
                        } else if (node.click == "file") {
                            $("#mm-mxfile").menu("show", {
                                left: e.pageX,
                                top: e.pageY
                            });
                        } else {
                            $('#mm-mxinterfacetree1').menu('show', {
                                left: e.pageX,
                                top: e.pageY
                            });
                        }
                    }
                },
                onClick: function (node) {
                    if (node.click == 'system') {
                        var mid = node.id;
                        var title = node.text;
                        if ($('#mainContentTabs').tabs('exists', title)) {
                            $('#mainContentTabs').tabs('select', title);
                        } else {
                            var content = '<iframe scrolling="auto" frameborder="0"  src="' + LOAD_URL.SYSTEMINDEX + '?_t=' + new Date().getTime() + '" style="width:100%;height:98%;"></iframe>';
                            $('#mainContentTabs').tabs('add', {
                                title: title,
                                content: content,
                                closable: true,
                                fit: true
                            });
                        }
                    } else if (node.click == "interfaces") {
                        var mid = node.id;
                        var title = node.text;
                        if ($('#mainContentTabs').tabs('exists', title)) {
                            $('#mainContentTabs').tabs('select', title);
                        } else {//SYSADMINUIEDIT
                            var content = '<iframe scrolling="auto" frameborder="0"  src="' + LOAD_URL.INTERFACELIST + '?systemId=' + mid + '&_t=' + new Date().getTime() + '" style="width:100%;height:98%;"></iframe>';
                            $('#mainContentTabs').tabs('add', {
                                title: title,
                                content: content,
                                closable: true
                            });
                        }

                    } else if (node.click == 'disable') {
                        var mid = node.id;
                        var title = node.text;
                        if ($('#mainContentTabs').tabs('exists', title)) {
                            $('#mainContentTabs').tabs('select', title);
                        } else {//SYSADMINUIEDIT
                            var content = '<iframe scrolling="auto" frameborder="0"  src="' + LOAD_URL.INTERFACELIST + '?systemId=' + mid + '&_t=' + new Date().getTime() + '" style="width:100%;height:98%;"></iframe>';
                            $('#mainContentTabs').tabs('add', {
                                title: title,
                                content: content,
                                closable: true,
                                fit: true
                            });
                        }

                    } else if (node.click == "head") {
                        var mid = node.id;
                        var title = node.text;
                        if ($('#mainContentTabs').tabs('exists', title)) {
                            $('#mainContentTabs').tabs('select', title);
                        } else {//LOAD_URL.SYSADMINUI+
                            var content = '<iframe scrolling="auto" frameborder="0"  src="' + LOAD_URL.PUBLICHEADER + '?headId=' + node.id + '&_t=' + new Date().getTime() + '" style="width:100%;height:98%;"></iframe>';
                            $('#mainContentTabs').tabs('add', {
                                title: title,
                                content: content,
                                closable: true,
                                fit: true
                            });
                        }

                    } else if (node.click == "protocol") {
                        var mid = node.id;
                        var title = node.text;
                        if ($('#mainContentTabs').tabs('exists', title)) {
                            $('#mainContentTabs').tabs('select', title);
                        } else {
                            var content = '<iframe scrolling="auto" frameborder="0"  src="/jsp/sysadmin/system_protocol.jsp?protocolId=' + mid + '&_t=' + new Date().getTime() + '"  style="width:100%;height:98%;"></iframe>';
                            $('#mainContentTabs').tabs('add', {
                                title: title,
                                content: content,
                                closable: true,
                                fit: true
                            });
                        }
                    } else if (node.click == "protocols") {

                    } else if (node.click == "heads") {

                    } else {
                        var mid = node.id;
                        var title = node.text;
                        if ($('#mainContentTabs').tabs('exists', title)) {
                            $('#mainContentTabs').tabs('select', title);
                        } else {//SYSADMINUIEDIT
                            var content = '<iframe scrolling="auto" frameborder="0"  src="' + LOAD_URL.INTERFACEDEFINE + '?interfaceId=' + mid + '&_t=' + new Date().getTime() + '" style="width:100%;height:98%;"></iframe>';
                            $('#mainContentTabs').tabs('add', {
                                title: title,
                                content: content,
                                closable: true,
                                fit: true
                            });
                        }
                    }
                }
            });

            $('.msinterfacetree').tree('collapseAll');

            $('.mslinktree').tree({
                onClick: function (node) {
                    if (typeof(node.children) == 'undefined') {
                        var mid = node.id;
                        var title = node.text + "交易链路";
                        if ($('#mainContentTabs').tabs('exists', title)) {
                            $('#mainContentTabs').tabs('select', title);
                        } else {
                            var content = '<iframe scrolling="yes" frameborder="0"  src="/jsp/serviceLink/dom.html" style="width:100%;height:98%;"></iframe>';
                            $('#mainContentTabs').tabs('add', {
                                title: title,
                                content: content,
                                closable: true,
                                fit: true
                            });
                        }
                    } else {
                        var mid = node.id;
                        var title = node.text + "交易链路";
                        if ($('#mainContentTabs').tabs('exists', title)) {
                            $('#mainContentTabs').tabs('select', title);
                        } else {
                            var content = '<iframe scrolling="auto" frameborder="0"  src="/jsp/serviceLink/index.jsp?systemId=' + node.id + '&_t=' + new Date().getTime() + '" style="width:100%;height:98%;"></iframe>';
                            $('#mainContentTabs').tabs('add', {
                                title: title,
                                content: content,
                                closable: true,
                            });
                        }
                    }
                }
            });

            $('.mxservicetree').tree({
                onContextMenu: function (e, node) {
                    e.preventDefault();
                    $(this).tree('select', node.target);
                    if (typeof(node.children) != 'undefined') {
                        $('#mm-mxservicetree').menu('show', {
                            left: e.pageX,
                            top: e.pageY
                        });
                    }
                },
                onClick: function (node) {
                    if (node.type == 'service') {//打开服务场景
                        if ($("#serviceFrame" + node.id).size() == 0) {//如果没有打开基本信息，则新创建基本信息
                            var mid = node.id;
                            var title = node.text;
                            if ($('#mainContentTabs').tabs('exists', title)) {
                                $('#mainContentTabs').tabs('select', title);
                            } else {
                                var content = '<iframe scrolling="auto"  name="serviceFrame' + node.id + '" id="serviceFrame' + node.id + '" frameborder="0"  src="' + LOAD_URL.SERVICEUI_LW + "?serviceId=" + node.id + '&_t=' + new Date().getTime() + '" style="width:100%;height:98%;"></iframe>';
                                $('#mainContentTabs').tabs('add', {
                                    title: title,
                                    content: content,
                                    closable: true
                                });
                            }
                        } else {
                            var mid = node.id;
                            var title = node.text;
                            if ($('#mainContentTabs').tabs('exists', title)) {
                                $('#mainContentTabs').tabs('select', title);
                            }
                        }
                    }
                }
            });
            SYSTABMENU.init();
        });
    },
    changeLeftMenuWithCallBack: function (mid, callBack) {

        $("#west-menu").load(LOAD_URL.LEFTMENU, 'mid=' + mid, function () {
            var flag = false;
            $('#mxsysadmintreefilter').searchbox({
                searcher: function (value, name) {
                    alert(value + "," + name);
                },
                prompt: '请输入关键词'
            });
            $('#servicetreefilter').searchbox({
                "searcher": function (value, name) {
                    $('.mxservicetree').tree('doFilter', value);
                },
                prompt: '请输入服务名'
            });
            $('#mxinterfacetreefilter').searchbox({
                searcher: function (value, name) {
                    $('.msinterfacetree').tree('doFilter', value);
                },
                prompt: '请输入关键词'
            });
            $('#mxinterfaceheadtreefilter').searchbox({
                searcher: function (value, name) {
                    $('.mxsysadmintree').tree('doFilter', value);
                },
                prompt: '请输入关键词'
            });

            //报文管理
            $('.mxsysadmintree').tree({
                onContextMenu: function (e, node) {
                    /*if(node.id=='root'){
                     return;
                     }*/
                    e.preventDefault();
                    $(this).tree('select', node.target);
                    if (typeof(node.children) != 'undefined') {//编辑接口
                        $('#mm-mxsysadmintree').menu('show', {
                            left: e.pageX,
                            top: e.pageY
                        });
                    }
                },
                onClick: function (node) {
                    if (node.id == 'root') {
                        return;
                    }
                    if (typeof(node.children) == 'undefined') {//编辑接口
                        var url = LOAD_URL.SYSADMINUIEDIT;
                        var mid = node.id;
                        var title = node.text;
                        //公共报文头信息管理
                        if (mid == 1) {
                            url = LOAD_URL.PUBLICHEADER;
                        }

                        if ($('#mainContentTabs').tabs('exists', title)) {
                            $('#mainContentTabs').tabs('select', title);
                        } else {
                            var content = '<iframe scrolling="auto" frameborder="0"  src="' + url + '?_t=' + new Date().getTime() + '" style="width:100%;height:98%;"></iframe>';
                            $('#mainContentTabs').tabs('add', {
                                title: title,
                                content: content,
                                closable: true,
                                fit: true
                            });
                        }
                    }

                    else {//基本信息
                        var mid = node.id;
                        var title = node.text;
                        var node = $('.mxsysadmintree').tree("getSelected");
                        if ($('#mainContentTabs').tabs('exists', title)) {
                            $('#mainContentTabs').tabs('select', title);
                        } else {//LOAD_URL.SYSADMINUI+
                            var content = '<iframe scrolling="auto" frameborder="0"  src="' + LOAD_URL.PUBLICHEADER + '?headId=' + node.id + '&_t=' + new Date().getTime() + '" style="width:100%;height:98%;"></iframe>';
                            $('#mainContentTabs').tabs('add', {
                                title: title,
                                content: content,
                                closable: true,
                                fit: true
                            });
                        }
                    }
                }
            });

            //接口管理
            $('.msinterfacetree').tree({
                    onContextMenu: function (e, node) {
                        e.preventDefault();
                        $(this).tree('select', node.target);
                        if (typeof(node.children) != 'undefined') {//编辑接口
                            if (node.click == 'system') {
                                $('#mm-mxsystemtree').menu('show', {
                                    left: e.pageX,
                                    top: e.pageY
                                });
                            } else if (node.click == 'disable') {

                                $('#mm-mxsystemtree1').menu('show', {
                                    left: e.pageX,
                                    top: e.pageY
                                });
                                //右键报文头节点
                            } else if (node.click == "heads") {
                                $('#mm-mxsysadmintree').menu('show', {
                                    left: e.pageX,
                                    top: e.pageY
                                });
                                //右键协议节点
                            } else if (node.click == "protocols") {

                                $('#mm-mxprotocols').menu('show', {
                                    left: e.pageX,
                                    top: e.pageY
                                });
                                //右键具体报文头节点
                            } else if (node.click == "head") {
                                $('#mm-syshead').menu('show', {
                                    left: e.pageX,
                                    top: e.pageY
                                });
                                //邮件具体协议节点
                            } else if (node.click == "protocol") {
                                $('#mm-mxprotocol').menu('show', {
                                    left: e.pageX,
                                    top: e.pageY
                                });
                            } else if (node.click == "files") {
                                $("#mm-mxfiles").menu("show", {
                                    left: e.pageX,
                                    top: e.pageY
                                });
                            } else if (node.click == "file") {
                                $("#mm-mxfile").menu("show", {
                                    left: e.pageX,
                                    top: e.pageY
                                });
                            } else {
                                $('#mm-mxinterfacetree1').menu('show', {
                                    left: e.pageX,
                                    top: e.pageY
                                });
                            }
                        }
                    },
                    onClick: function (node) {
                        if (node.click == 'system') {
                            var mid = node.id;
                            var title = node.text;
                            if ($('#mainContentTabs').tabs('exists', title)) {
                                $('#mainContentTabs').tabs('select', title);
                            } else {

                                var content = '<iframe scrolling="auto" frameborder="0"  src="' + LOAD_URL.SYSTEMINDEX + '?_t=' + new Date().getTime() + '" style="width:100%;height:98%;"></iframe>';
                                $('#mainContentTabs').tabs('add', {
                                    title: title,
                                    content: content,
                                    closable: true,
                                    fit: true
                                });
                            }
                        } else if (node.click == "interfaces") {
                            var mid = node.id;
                            var title = node.text;
                            if ($('#mainContentTabs').tabs('exists', title)) {
                                $('#mainContentTabs').tabs('select', title);
                            } else {//SYSADMINUIEDIT
                                var content = '<iframe scrolling="auto" frameborder="0"  src="' + LOAD_URL.INTERFACELIST + '?systemId=' + mid + '&_t=' + new Date().getTime() + '" style="width:100%;height:98%;"></iframe>';
                                $('#mainContentTabs').tabs('add', {
                                    title: title,
                                    content: content,
                                    closable: true,
                                    fit: true
                                });
                            }

                        } else if (node.click == 'disable') {
                            var mid = node.id;
                            var title = node.text;
                            if ($('#mainContentTabs').tabs('exists', title)) {
                                $('#mainContentTabs').tabs('select', title);
                            } else {//SYSADMINUIEDIT
                                var content = '<iframe scrolling="auto" frameborder="0"  src="' + LOAD_URL.INTERFACELIST + '?systemId=' + mid + '&_t=' + new Date().getTime() + '" style="width:100%;height:98%;"></iframe>';
                                $('#mainContentTabs').tabs('add', {
                                    title: title,
                                    content: content,
                                    closable: true,
                                    fit: true
                                });
                            }

                        } else if (node.click == "head") {
                            var mid = node.id;
                            var title = node.text;
                            if ($('#mainContentTabs').tabs('exists', title)) {
                                $('#mainContentTabs').tabs('select', title);
                            } else {//LOAD_URL.SYSADMINUI+
                                var content = '<iframe scrolling="auto" frameborder="0"  src="' + LOAD_URL.PUBLICHEADER + '?headId=' + node.id + '&_t=' + new Date().getTime() + '" style="width:100%;height:98%;"></iframe>';
                                $('#mainContentTabs').tabs('add', {
                                    title: title,
                                    content: content,
                                    closable: true,
                                    fit: true
                                });
                            }

                        } else if (node.click == "protocol") {
                            var mid = node.id;
                            var title = node.text;
                            if ($('#mainContentTabs').tabs('exists', title)) {
                                $('#mainContentTabs').tabs('select', title);
                            } else {
                                var content = '<iframe scrolling="auto" frameborder="0"  src="/jsp/sysadmin/system_protocol.jsp?protocolId=' + mid + '&_t=' + new Date().getTime() + '"  style="width:100%;height:98%;"></iframe>';
                                $('#mainContentTabs').tabs('add', {
                                    title: title,
                                    content: content,
                                    closable: true,
                                    fit: true
                                });
                            }
                        } else if (node.click == "protocols") {

                        } else if (node.click == "heads") {

                        } else if (node.click == "files") {

                        } else {
                            var mid = node.id;
                            var title = node.text;
                            if ($('#mainContentTabs').tabs('exists', title)) {
                                $('#mainContentTabs').tabs('select', title);
                            } else {//SYSADMINUIEDIT
                                var content = '<iframe scrolling="auto" frameborder="0"  src="' + LOAD_URL.INTERFACEDEFINE + '?interfaceId=' + mid + '&_t=' + new Date().getTime() + '" style="width:100%;height:98%;"></iframe>';
                                $('#mainContentTabs').tabs('add', {
                                    title: title,
                                    content: content,
                                    closable: true,
                                    fit: true
                                });
                            }
                        }
                    },
                    "onLoadSuccess": function () {
                        flag = true;
                    }
                }
            );


            $('.msinterfacetree').tree('collapseAll');

            $('.mslinktree').tree({
                onClick: function (node) {
                    if (typeof(node.children) == 'undefined') {
                        var mid = node.id;
                        var title = node.text + "交易链路";
                        if ($('#mainContentTabs').tabs('exists', title)) {
                            $('#mainContentTabs').tabs('select', title);
                        } else {
                            var content = '<iframe scrolling="yes" frameborder="0"  src="/jsp/serviceLink/dom.html" style="width:100%;height:98%;"></iframe>';
                            $('#mainContentTabs').tabs('add', {
                                title: title,
                                content: content,
                                closable: true,
                                fit: true
                            });
                        }
                    } else {
                        var mid = node.id;
                        var title = node.text + "交易链路";
                        if ($('#mainContentTabs').tabs('exists', title)) {
                            $('#mainContentTabs').tabs('select', title);
                        } else {
                            var content = '<iframe scrolling="auto" frameborder="0"  src="/jsp/serviceLink/index.jsp?systemId=' + node.id + '&_t=' + new Date().getTime() + '" style="width:100%;height:98%;"></iframe>';
                            $('#mainContentTabs').tabs('add', {
                                title: title,
                                content: content,
                                closable: true,
                                fit: true
                            });
                        }
                    }
                }
            });

            $('.mxservicetree').tree({
                onContextMenu: function (e, node) {
                    e.preventDefault();
                    $(this).tree('select', node.target);
                    if (typeof(node.children) != 'undefined') {
                        $('#mm-mxservicetree').menu('show', {
                            left: e.pageX,
                            top: e.pageY
                        });
                    }
                },
                onClick: function (node) {
                    if (node.type == 'service') {//打开服务场景
                        if ($("#serviceFrame" + node.id).size() == 0) {//如果没有打开基本信息，则新创建基本信息
                            var mid = node.id;
                            var title = node.text;
                            if ($('#mainContentTabs').tabs('exists', title)) {
                                $('#mainContentTabs').tabs('select', title);
                            } else {
                                var content = '<iframe scrolling="auto"  name="serviceFrame' + node.id + '" id="serviceFrame' + node.id + '" frameborder="0"  src="' + LOAD_URL.SERVICEUI_LW + "?serviceId=" + node.id + '&_t=' + new Date().getTime() + '" style="width:100%;height:98%;"></iframe>';
                                $('#mainContentTabs').tabs('add', {
                                    title: title,
                                    content: content,
                                    closable: true,
                                    fit: true
                                });
                            }
                        } else {
                            var mid = node.id;
                            var title = node.text;
                            if ($('#mainContentTabs').tabs('exists', title)) {
                                $('#mainContentTabs').tabs('select', title);
                            }
                        }
                    }
                }
            });
            SYSTABMENU.init();
            var reloadTime = 100;
            while (!flag && reloadTime > 0) {
                sleep(10);
                reloadTime--;
            }
            callBack();
        });

    },
    reloadTreeByValue: function (key, value) {
        $('.' + key).tree("loadData", value);
    }
};
SYSMENU.init();
$("#nav").find("a").get(0).click();

var SYSTABMENU = {
    init: function () {
        $("#west-menu").find(".openable").click(function () {
            var mid = $(this).attr("mid");
            var title = $(this).text();
            if ($('#mainContentTabs').tabs('exists', title)) {
                $('#mainContentTabs').tabs('select', title);
            } else {
                SYSTABMENU.checkRule(mid);
            }
        });
    },
    checkRule: function (mid) {
        $.post(LOAD_URL.CHECKRULE, 'mid=' + mid + '&t=' + new Date().getTime(), function (result) {


            if (result.success) {
                var content = '<iframe scrolling="auto" frameborder="0"  src="' + result.url + '?_t=' + new Date().getTime() + '" style="width:100%;height:98%;"></iframe>';
                $('#mainContentTabs').tabs('add', {
                    title: result.title,
                    content: content,
                    closable: true
                });
            } else {
                alert("对不起您没有权限.");
            }
        }, "json");

    }
}

function appendSysapi() {
    uiinit.win({
        w: 500,
        iconCls: 'icon-add',
        title: "新增接口",
        url: "/dataTemplate/formTemplate/form.html"
    });
}

function editSysapi() {
    uiinit.win({
        w: 500,
        iconCls: 'icon-add',
        title: "编辑接口",
        url: "/dataTemplate/formTemplate/form.html"
    });
}
function removeSysapi() {
    var node = $('.mxsysadmintree').tree('getSelected');
    $('.mxsysadmintree').tree('remove', node.target);
}

function addTab(title, content) {
    $('#mainContentTabs').tabs('add', {
        title: title,
        content: content,
        closable: true
    });
}

function changeTaskName() {
    $("#taskName").text("当前任务:" + PROCESS_INFO.taskName + "(" + PROCESS_INFO.taskId + ")")
}

function sleep(d) {
    for (var t = Date.now(); Date.now() - t <= d;);
}

function SetWinHeight(obj) {
    var win = obj;
    if (document.getElementById) {
        if (win && !window.opera) {
            if (win.contentDocument && win.contentDocument.body.offsetHeight)
                win.height = win.contentDocument.body.offsetHeight;
            else if (win.Document && win.Document.body.scrollHeight)
                win.height = win.Document.body.scrollHeight;
        }
    }
}

function userPage(){
    $('#userDlg').dialog({
        title: '用户信息',
        width: '550px',
        left:'450px',
        height:'403px',
        closed: false,
        cache: false,
        href: '/user/getUserInfo',
        modal: true
    });
}

$(function () {
    $("#taskName").click(function () {
        uiinit.win({
            w: 500,
            iconCls: 'icon-cfp',
            title: "完成任务",
            url: "/jsp/task/completeTask.jsp"
        });
    });
    var content = '<iframe scrolling="auto" frameborder="0"  src="/jsp/service/search.jsp" style="width:100%;height:98%;"></iframe>';
    var title = "服务检索"
    $('#mainContentTabs').tabs('add', {
        title: title,
        content: content,
        closable: true
    });
    ////TODO TZB不用我的任务
    //var content = '<iframe scrolling="yes" frameborder="0"  src="/jsp/task/mytask.jsp" style="width:100%;height:98%;"></iframe>';
    //var title = "我的任务"
    //$('#mainContentTabs').tabs('add', {
    //    title: title,
    //    content: content
    //});




});


