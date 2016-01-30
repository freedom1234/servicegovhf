<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!doctype html>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>交易链路</title>
    <meta http-equiv="content-type" content="text/html;charset=utf-8"/>
    <link rel="stylesheet" href="/plugin/jsPlumb/css/jsplumb.css">
    <link rel="stylesheet" href="demo.css">
    <link href="/newui/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link href="/newui/dist/css/AdminLTE.min.css" rel="stylesheet" type="text/css"/>
    <link href="/newui/dist/css/skins/_all-skins.min.css" rel="stylesheet" type="text/css"/>
    <link href="/newui/plugins/select2/select2.min.css" rel="stylesheet" type="text/css"/>
</head>
<body data-demo-id="statemachine" data-library="dom">

<div id="userId" style="display: none"><shiro:principal/></div>
<div>
    <%--<div class="form-group">--%>
        <%--<table>--%>
            <%--<tr>--%>
                <%--<td style="padding:0.5em"><label>选择接口</label></td>--%>
                <%--<td style="padding:0.5em"><select class="form-control select2" style="width:50em" multiple="multiple"--%>
                                                  <%--data-placeholder="选择接口"></select></td>--%>
                <%--<td style="padding:0.5em">--%>
                    <%--<button id="add" class="btn btn-block btn-primary">添加</button>--%>
                <%--</td>--%>
                <%--<td style="padding:0.5em">--%>
                    <%--<button id="save" class="btn btn-block btn-primary">保存</button>--%>
                <%--</td>--%>
            <%--</tr>--%>
        <%--</table>--%>
    <%--</div>--%>
    <!-- demo -->
    <div class="demo statemachine-demo" id="statemachine-demo" style="background-color: aliceblue;">

    </div>
    <!-- /demo -->
</div>
<!-- JS -->
<!-- support lib for bezier stuff -->
<script src="/plugin/jsPlumb/lib/jsBezier-0.7.js"></script>
<!-- event adapter -->
<script src="/plugin/jsPlumb/lib/mottle-0.6.js"></script>
<!-- geometry functions -->
<script src="/plugin/jsPlumb/lib/biltong-0.2.js"></script>
<!-- drag -->
<script src="/plugin/jsPlumb/lib/katavorio-0.6.js"></script>
<!-- jsplumb util -->
<script src="/plugin/jsPlumb/src/util.js"></script>
<script src="/plugin/jsPlumb/src/browser-util.js"></script>
<!-- main jsplumb engine -->
<script src="/plugin/jsPlumb/src/jsPlumb.js"></script>
<!-- base DOM adapter -->
<script src="/plugin/jsPlumb/src/dom-adapter.js"></script>
<script src="/plugin/jsPlumb/src/overlay-component.js"></script>
<!-- endpoint -->
<script src="/plugin/jsPlumb/src/endpoint.js"></script>
<!-- connection -->
<script src="/plugin/jsPlumb/src/connection.js"></script>
<!-- anchors -->
<script src="/plugin/jsPlumb/src/anchors.js"></script>
<!-- connectors, endpoint and overlays  -->
<script src="/plugin/jsPlumb/src/defaults.js"></script>
<!-- bezier connectors -->
<script src="/plugin/jsPlumb/src/connectors-bezier.js"></script>
<!-- state machine connectors -->
<script src="/plugin/jsPlumb/src/connectors-statemachine.js"></script>
<!-- flowchart connectors -->
<script src="/plugin/jsPlumb/src/connectors-flowchart.js"></script>
<%--<script src="/plugin/jsPlumb/src/connector-editors.js"></script>--%>
<!-- SVG renderer -->
<script src="/plugin/jsPlumb/src/renderers-svg.js"></script>

<!-- vml renderer -->
<script src="/plugin/jsPlumb/src/renderers-vml.js"></script>

<!-- common adapter -->
<script src="/plugin/jsPlumb/src/base-library-adapter.js"></script>
<!-- no library jsPlumb adapter -->
<script src="/plugin/jsPlumb/src/dom.jsPlumb.js"></script>
<!-- /JS -->

<link rel="stylesheet" href="/assets/serviceLink2.0/app.css">
<link href="/plugin/font-awesome.css" rel="stylesheet">

<!--  demo code -->
<!--<script src="demo.js"></script>-->
<script type="text/javascript" src="/resources/js/jquery.min.js"></script>
<script type="text/javascript" src="/newui/bootstrap/js/bootstrap.min.js"></script>
<script src="/newui/plugins/select2/select2.full.min.js" type="text/javascript"></script>
<script src="/assets/serviceLink/serviceLinkManager.js" type="text/javascript"></script>
<script type="text/javascript">
    var userId = $("#userId").text();
    var sourceId = "<%=request.getParameter("sourceId")%>";
    var context = "";
    $(function () {


        /**
         * 初始化图表
         * @param row
         */
        var initDiagram = function initDiagram() {
            serviceLinkManager.getGraphData(sourceId, initConnections);
        };


        /**
         * 初始化块链接数据
         * @param result
         */
        var initConnections = function initConnections(result) {

            var nodes = result.nodes;
            var edges = result.edges;
            if(nodes){
                for(var i = 0; i< nodes.length; i++){
                    constructBlock(nodes[i]);
                }
            }

            document.getElementById("statemachine-demo").innerHTML = context;

            jsPlumb.ready(function () {
                // setup some defaults for jsPlumb.
                instance = jsPlumb.getInstance({
                    Endpoint: ["Dot", {radius: 2}],
                    HoverPaintStyle: {strokeStyle: "#5c96bc", lineWidth: 2},
                    ConnectionOverlays: [
                        ["Arrow", {
                            location: 1,
                            id: "arrow",
                            length: 5,
                            foldback: 0.3
                        }],
                        ["Label", {id: "label", cssClass: "aLabel"}]
                    ],
                    Container: "statemachine-demo"
                });
                window.jsp = instance;
                var windows = jsPlumb.getSelector(".statemachine-demo .table");
                instance.draggable(windows);

                instance.bind("connection", function (info) {
//                    info.connection.getOverlay("label").setLabel("");
                });
                instance.batch(function () {
                    instance.makeSource(windows, {
                        filter: ".ep",
                        anchor: "Continuous",
                        connector: ["Flowchart", {cornerRadius: 2}],
                        connectorStyle: {
                            strokeStyle: "#f76258",
                            lineWidth: 2,
                            outlineColor: "transparent",
                            outlineWidth: 4
                        },
                        onMaxConnections: function (info, e) {
                            alert("Maximum connections (" + info.maxConnections + ") reached");
                        }
                    });
                    instance.makeTarget(windows, {
                        dropOptions: {hoverClass: "dragHover"},
                        anchor: "Continuous",
                        allowLoopback: true
                    });
                    for (var i = 0; i < edges.length; i++) {
                        instance.connect({source: edges[i].source, target: edges[i].target});
                    }
                });
                jsPlumb.fire("jsPlumbDemoLoaded", instance);
            });
        };

        /**
         * 添加块的方法
         * @param row
         */
        var constructBlock = function constructBlock(row) {
            var columns = row.columns;
            context += '<div class=\"table node\" id="'+row.id+'" style="left:'+row.left+'px;top:'+row.top+'px;">'
                    + '     <div class=\"name\">'
                    + '         <span>'+row.name+'</span>'
                    + '         <div class="ep" style="display:none"></div>'
                    + '     </div>'
                    + '     <ul class=\"table-columns\">';
            for(var i = 0; i < columns.length; i ++){
                var icons = columns[i].icons;
                context += '<li class="table-column table-column-type-varchar">';
                if(columns[i].id){
                    context +=  '<div><span>'+columns[i].id+'</span></div>';
                }
                if(icons&&icons.length > 0){
                    context += '<div><span>';
                    for(var j = 0; j < icons.length; j++){
                        context +=  '<i style="margin-left:1em;" class="fa fa-'+icons[j].icon+'"></i>';
                    }
                    context += '</span></div>';
                }
                context +=  '</li>';
            }
            context    += '     </ul>'
                + ' </div>';

//            context += '<div class="w" style="background-color:' + backgroundColor + '" id="' + invokeId + '" type="0" ondblclick="dblEvent(event)">' + contextName
//            + '<div ></div>'
//            + '<div>'
//            + '系统ID: ' + systemId + '<br />'
//            + '服务场景: ' + serviceOperation + '<br />'
//            + '节点类型:' + type
//            + '</div>'
//            + '</div>';
        };

        initDiagram();
    });

</script>
</body>
</html>
