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
    <div class="form-group" style="display: none">
        <table>
            <tr>
                <td style="padding:0.5em"><label>选择接口</label></td>
                <td style="padding:0.5em"><select class="form-control select2" style="width:20em" multiple="multiple"
                                                  data-placeholder="选择接口"></select></td>
                <td style="padding:0.5em">
                    <button id="add" class="btn btn-block btn-primary">添加</button>
                </td>
                <td style="padding:0.5em">
                    <button id="save" class="btn btn-block btn-primary">保存</button>
                </td>
            </tr>
        </table>
    </div>
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
<script src="/plugin/jsPlumb/src/connector-editors.js"></script>
<!-- SVG renderer -->
<script src="/plugin/jsPlumb/src/renderers-svg.js"></script>

<!-- vml renderer -->
<script src="/plugin/jsPlumb/src/renderers-vml.js"></script>

<!-- common adapter -->
<script src="/plugin/jsPlumb/src/base-library-adapter.js"></script>
<!-- no library jsPlumb adapter -->
<script src="/plugin/jsPlumb/src/dom.jsPlumb.js"></script>
<!-- /JS -->

<!--  demo code -->
<!--<script src="demo.js"></script>-->
<script type="text/javascript" src="/resources/js/jquery.min.js"></script>
<script src="/newui/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script src="/newui/plugins/select2/select2.full.min.js" type="text/javascript"></script>
<script src="/assets/serviceLink/serviceLinkManager.js" type="text/javascript"></script>
<script type="text/javascript">
    var userId = $("#userId").text();
    var containBlock = function containBlock(blocks, obj) {

        var i = blocks.length;
        while (i--) {
            if (blocks[i].blockId == obj.blockId) {
                return true;
            }
        }
        return false;

    };

    var getBlock = function getBlock(blocks, id) {
        var i = blocks.length;
        while (i--) {
            if (blocks[i].blockId == id) {
                return blocks[i];
            }
        }
        return false;
    };

    var sourceId = "<%=request.getParameter("sourceId")%>";
    var startId = "<%=request.getParameter("sourceId")%>";
    var data = {};


    var context = "";
    var connections = [];
    var connectionsToDel = [];
    var blocks = [];
    var initPosX = 100;
    var initPosY = 300;

    $(function () {
        var instance;


        /**
         * 初始化接口下拉框的方法
         * @param row
         */
        var initComboBox = function (result) {
            for (var i = 0; i < result.length; i++) {
                data[result[i].invokeId] = result[i];
                $(".select2").append('<option value="' + result[i].invokeId + '" >接口:' + result[i].interfaceId + '服务:' + result[i].serviceId + '</option>');
            }
            $(".select2").select2();
            //在初始化完成数据框之后初始化图标表
            initDiagram();
        };
        serviceLinkManager.getAll(initComboBox);

        /**
         * 初始化图表
         * @param row
         */
        var initDiagram = function initDiagram() {
            serviceLinkManager.getParentConnectionsBySourceId(sourceId, initConnections);
        };

        /**
         * 添加块的方法
         * @param row
         */
        var constructBlock = function constructBlock(row) {
            var interfaceId = row.interfaceId;
            var interfaceName = row.interfaceName;
            var serviceId = row.serviceId;
            var systemId = row.systemName;
            var invokeId = row.invokeId;
            var operationId = row.operationId;
            var type = row.type;
            if (type == "0") {
                type = "提供方";
            } else if (type == "1") {
                type = "消费方";
            }

            var backgroundColor = "white";

            if (null != interfaceId) {
                var contextName = interfaceId + interfaceName;
            } else {
                var contextName = serviceId + operationId;
                backgroundColor = "antiquewhite";
            }

            if(startId == invokeId){
                backgroundColor = "plum"
            }

            var serviceOperation = "";
            if (serviceId != null && operationId != null) {
                serviceOperation = serviceId + operationId;
            }
            context += '<div class="w" style="background-color:' + backgroundColor + '" id="' + invokeId + '" type="0" ondblclick="dblEvent(event)">' + contextName
            + '<div class="ep"></div>'
            + '<div>'
            + '系统ID: ' + systemId + '<br />'
            + '服务场景: ' + serviceOperation + '<br />'
            + '节点类型:' + type
            + '</div>'
            + '</div>';
        };
        /**
         * 初始化块链接数据
         * @param result
         */
        var initConnections = function initConnections(result) {


            if (result.length == 0) {
                var sourceId = "<%=request.getParameter("sourceId")%>";
                var sourceBlock = {
                    blockId: sourceId,
                    positionX: initPosX,
                    positionY: initPosY
                };
                var sourceRow = data[sourceId];
                constructBlock(sourceRow, sourceBlock);
            }

            var prenode ="";
            var levelX = [];
            var level = 0;

            for (var i = 0; i < result.length; i++) {
                connections.push({
                    connectionId: result[i].sourceId + "-" + result[i].targetId,
                    sourceId: result[i].sourceId,
                    targetId: result[i].targetId
                });
                var targetId = result[i].targetId;
                var targetBlock = {
                    blockId: targetId,
                    positionX: initPosX,
                    positionY: initPosY
                };
                if (targetId == startId) {
                    targetBlock = {
                        blockId: targetId,
                        positionX: 50,
                        positionY: 150,
                        level:1
                    };
                }
                if (!containBlock(blocks, targetBlock)) {
                    var targetRow = data[targetId];
                    constructBlock(targetRow, targetBlock);
                    blocks.push(targetBlock);
                    levelX[1] = 50;
                    initPosY += 150;
                    prenode = startId;
                    level = 1;
                }

                var sourceId = result[i].sourceId;
                var targetBlock = getBlock(blocks, targetId);

                if(!levelX[targetBlock.level +1]){
                    levelX[targetBlock.level +1] = 50;
                }

                var sourceBlock = {
                    blockId: sourceId,
                    positionX: levelX[targetBlock.level +1],
                    positionY: targetBlock.positionY + 150,
                    level : targetBlock.level +1
                };
                if (!containBlock(blocks, sourceBlock)) {
                    var sourceRow = data[sourceId];
                    constructBlock(sourceRow, sourceBlock);
                    blocks.push(sourceBlock);
                    levelX[targetBlock.level +1] = levelX[targetBlock.level +1] + 300;
                }

            }
            document.getElementById("statemachine-demo").innerHTML = context;
            for (var i = 0; i < blocks.length; i++) {
                $("#" + blocks[i].blockId).css("left", blocks[i].positionX);
                $("#" + blocks[i].blockId).css("top", blocks[i].positionY);
            }
            jsPlumb.ready(function () {
                // setup some defaults for jsPlumb.
                instance = jsPlumb.getInstance({
                    Endpoint: ["Dot", {radius: 2}],
                    HoverPaintStyle: {strokeStyle: "#1e8141", lineWidth: 1},
                    ConnectionOverlays: [
                        ["Arrow", {
                            location: 1,
                            id: "arrow",
                            length: 5,
                            foldback: 0.9
                        }],
                        ["Label", {id: "label", cssClass: "aLabel"}]
                    ],
                    Container: "statemachine-demo"
                });
                window.jsp = instance;
                var windows = jsPlumb.getSelector(".statemachine-demo .w");
                instance.draggable(windows);
//                instance.bind("click", function (c) {
//                    connectionsToDel.push({
//                        sourceId: c.sourceId,
//                        sourceType: "",
//                        targetId: c.targetId,
//                        targetType: ""
//                    });
//                    instance.detach(c);
//                });
                instance.bind("connection", function (info) {
//                    info.connection.getOverlay("label").setLabel("");
                });
                instance.batch(function () {
                    instance.makeSource(windows, {
                        filter: ".ep",
                        anchor: "Continuous",
                        connector: ["Flowchart",  {cornerRadius: 2}],
                        connectorStyle: {
                            strokeStyle: "#5c96bc",
                            lineWidth: 1,
                            outlineColor: "transparent",
                            outlineWidth: 2
                        },
                        maxConnections: 5,
                        onMaxConnections: function (info, e) {
                            alert("Maximum connections (" + info.maxConnections + ") reached");
                        }
                    });
                    instance.makeTarget(windows, {
                        dropOptions: {hoverClass: "dragHover"},
                        anchor: "Continuous",
                        allowLoopback: true
                    });
                    for (var i = 0; i < connections.length; i++) {
                        instance.connect({source: connections[i].sourceId, target: connections[i].targetId});
                    }
                    connections = [];
                });
                jsPlumb.fire("jsPlumbDemoLoaded", instance);
            });
        };

        /**
         * 添加按钮的事件
         */
        $("#add").click(function () {
            if (instance) {
                $.each(instance.getConnections(), function (idx, connection) {
                    connections.push({
                        connectionId: connection.id,
                        sourceId: connection.sourceId,
                        targetId: connection.targetId
                    });
                });
                $("#statemachine-demo .w").each(function (idx, elem) {
                    var $elem = $(elem);
                    blocks.push({
                        blockId: $elem.attr('id'),
                        positionX: parseInt($elem.css("left"), 10),
                        positionY: parseInt($elem.css("top"), 10)
                    });
                });
            }
            var addInterfaceIds = $(".select2").val();
            for (var i = 0; i < addInterfaceIds.length; i++) {
                var row = data[addInterfaceIds[i]];
                constructBlock(row);
            }
            document.getElementById("statemachine-demo").innerHTML = context;
            for (var i = 0; i < blocks.length; i++) {
                $("#" + blocks[i].blockId).css("left", blocks[i].positionX);
                $("#" + blocks[i].blockId).css("top", blocks[i].positionY);
            }
            jsPlumb.ready(function () {
                // setup some defaults for jsPlumb.
                instance = jsPlumb.getInstance({
                    Endpoint: ["Dot", {radius: 2}],
                    HoverPaintStyle: {strokeStyle: "#1e8141", lineWidth: 2},
                    ConnectionOverlays: [
                        ["Arrow", {
                            location: 1,
                            id: "arrow",
                            length: 5,
                            foldback: 0.3
                        }],
                        ["Label", {label: "FOO", id: "label", cssClass: "aLabel"}]
                    ],
                    Container: "statemachine-demo"
                });
                window.jsp = instance;
                var windows = jsPlumb.getSelector(".statemachine-demo .w");
                // initialise draggable elements.
                instance.draggable(windows);
                // bind a click listener to each connection; the connection is deleted. you could of course
                // just do this: jsPlumb.bind("click", jsPlumb.detach), but I wanted to make it clear what was
                // happening.
                instance.bind("click", function (c) {
                    connectionsToDel.push({
                        sourceId: c.sourceId,
                        sourceType: "",
                        targetId: c.targetId,
                        targetType: ""
                    });
                    instance.detach(c);
                });
                // bind a connection listener. note that the parameter passed to this function contains more than
                // just the new connection - see the documentation for a full list of what is included in 'info'.
                // this listener sets the connection's internal
                // id as the label overlay's text.
                instance.bind("connection", function (info) {
                    info.connection.getOverlay("label").setLabel("调用");
                });
                // suspend drawing and initialise.
                instance.batch(function () {
                    instance.makeSource(windows, {
                        filter: ".ep",
                        anchor: "Continuous",
                        connector: ["StateMachine", {curviness: 20}],
                        connectorStyle: {
                            strokeStyle: "#5c96bc",
                            lineWidth: 2,
                            outlineColor: "transparent",
                            outlineWidth: 4
                        },
                        maxConnections: 5,
                        onMaxConnections: function (info, e) {
                            alert("Maximum connections (" + info.maxConnections + ") reached");
                        }
                    });
                    // initialise all '.w' elements as connection targets.
                    instance.makeTarget(windows, {
                        dropOptions: {hoverClass: "dragHover"},
                        anchor: "Continuous",
                        allowLoopback: true
                    });
                    for (var i = 0; i < connections.length; i++) {
                        instance.connect({source: connections[i].sourceId, target: connections[i].targetId});
                    }
                    connections = [];
                });
                jsPlumb.fire("jsPlumbDemoLoaded", instance);
            });
        });
        $("#save").click(function () {
            //先删除 需要删除的连接 然后再保存
            var connectionsToSave = [];
            $.each(instance.getConnections(), function (idx, connection) {
                connectionsToSave.push({
                    sourceId: connection.sourceId,
                    sourceType: "",
                    targetId: connection.targetId,
                    targetType: ""
                });
            });
            var saveConnectionCallBack = function saveConnectionCallBack() {
                alert("保存成功");
            }
            var delConnectionCallBack = function delConnectionCallBack() {
                serviceLinkManager.saveConnections(connectionsToSave, saveConnectionCallBack);
            }
            serviceLinkManager.delConnections(connectionsToDel, delConnectionCallBack);
        });
    });
</script>
</body>
</html>
