(function () {

    jsPlumbToolkit.ready(function () {

        // ------------------------ toolkit setup ------------------------------------

        // This function is what the toolkit will use to get an ID from a node.
        var idFunction = function (n) {
            return n.id;
        };

        // This function is what the toolkit will use to get the associated type from a node.
        var typeFunction = function (n) {
            return n.type;
        };

        // get the various dom elements
        var mainElement = document.querySelector("#jtk-demo-flowchart"),
            canvasElement = mainElement.querySelector(".jtk-demo-canvas"),
            miniviewElement = mainElement.querySelector(".miniview"),
            nodePalette = mainElement.querySelector(".node-palette"),
            controls = mainElement.querySelector(".controls");

        // Declare an instance of the Toolkit, and supply the functions we will use to get ids and types from nodes.
        var toolkit = jsPlumbToolkit.newInstance({
            idFunction: idFunction,
            typeFunction: typeFunction,
            nodeFactory: function (type, data, callback) {
                jsPlumbToolkit.Dialogs.show({
                    id: "dlgText",
                    title: "Enter " + type + " name:",
                    onOK: function (d) {
                        data.text = d.text;
                        // if the user entered a name...
                        if (data.text) {
                            // and it was at least 2 chars
                            if (data.text.length >= 2) {
                                // set an id and continue.
                                data.id = jsPlumbToolkitUtil.uuid();
                                callback(data);
                            }
                            else
                            // else advise the user.
                                alert(type + " names must be at least 2 characters!");
                        }
                        // else...do not proceed.
                    }
                });
            },
            beforeStartConnect: function (node, edgeType) {
                return {label: "..."};
            }
        });

// ------------------------ / toolkit setup ------------------------------------

// ------------------------- dialogs -------------------------------------

        jsPlumbToolkit.Dialogs.initialize({
            selector: ".dlg"
        });

// ------------------------- / dialogs ----------------------------------

// ------------------------ rendering ------------------------------------

        var _editLabel = function (edge) {
            jsPlumbToolkit.Dialogs.show({
                id: "dlgText",
                data: {
                    text: edge.data.label || ""
                },
                onOK: function (data) {
                    toolkit.updateEdge(edge, {label: data.text});
                }
            });
        };

        // Instruct the toolkit to render to the 'canvas' element. We pass in a view of nodes, edges and ports, which
        // together define the look and feel and behaviour of this renderer.  Note that we can have 0 - N renderers
        // assigned to one instance of the Toolkit..
        var renderer = window.renderer = toolkit.render({
            container: canvasElement,
            view: {
                nodes: {
                    "table": {
                        template: "tmplTable"
                    },
                    "view": {
                        template: "tmplView"
                    }
                },
                // There are two edge types defined - 'yes' and 'no', sharing a common
                // parent.
                edges: {
                    "default": {
                        connector: ["Flowchart", {cornerRadius: 2}],
                        //connector: ["StateMachine", {cornerRadius: 2}],
                        paintStyle: {
                            lineWidth: 2,
                            strokeStyle: "#f76258",
                            outlineWidth: 3,
                            outlineColor: "transparent"
                        },	//	paint style for this edge type.
                        hoverPaintStyle: {lineWidth: 2, strokeStyle: "rgb(67,67,67)"}, // hover paint style for this edge type.
                        events: {
                            "dblclick": function (params) {
                                jsPlumbToolkit.Dialogs.show({
                                    id: "dlgConfirm",
                                    data: {
                                        msg: "Delete Edge"
                                    },
                                    onOK: function () {
                                        toolkit.removeEdge(params.edge);
                                    }
                                });
                            }
                        },
                        overlays: [
                            ["Arrow", {location: 1, width: 10, length: 10}],
                            ["Arrow", {location: 0.3, width: 10, length: 10}]
                        ]
                    },
                    "connection": {
                        parent: "default",
                        overlays: [
                            [
                                "Label", {
                                label: "${label}",
                                events: {
                                    click: function (params) {
                                        _editLabel(params.edge);
                                    }
                                }
                            }
                            ]
                        ]
                    }
                },
                ports: {
                    "default": {
                        //endpoint: "Blank",
                        endpoint: [ "Dot", { radius: 5 } ],		// the type of the endpoint
                        paintStyle: { fillStyle: "#f76258" },		// the endpoint's appearance
                        hoverPaintStyle: { fillStyle: "#434343" }, // appearance when mouse hovering on endpoint or connection
                        anchor: [ "Left", "Right" ], // anchors for the endpoint
                        edgeType: "common", // the type of edge for connections from this port type
                        maxConnections: -1, // no limit on connections
                        isSource: true, // indicates new connections can be dragged from this port type
                        isTarget: true,// indicates new connections can be dragged to this port type
                        dropOptions: {  //drop options for the port. here we attach a css class.
                            hoverClass: "drop-hover"
                        },
                        allowLoopback: false,   // do not allow loopback connections from a port to itself.
                        allowNodeLoopback:false, // do not allow connections from this port to any other port on the same node.
                        events: {
                            "dblclick": function () {
                                console.log(arguments);
                            }
                        }
                    }
                }
            },
            // Layout the nodes using an absolute layout
            layout: {
                type: "Absolute"
            },
            events: {
                canvasClick: function (e) {
                    toolkit.clearSelection();
                },
                edgeAdded: function (params) {
                    if (params.addedByMouse) {
                        _editLabel(params.edge);
                    }
                }
            },
            miniview: {
                container: miniviewElement
            },
            consumeRightClick: false,
            dragOptions: {
                filter: ".jtk-draw-handle, .node-action, .node-action i"
            }
        });

        // Load the data.
        toolkit.load({
            //url: "/assets/serviceLink2.0/data/3.json",
            url: "/serviceLink/getServiceLink/start/node/" + sourceId,
            onload: function () {
                _updateDataset();
            }
        });

        // listener for mode change on renderer.
        renderer.bind("modeChanged", function (mode) {
            jsPlumb.removeClass(controls.querySelectorAll("[mode]"), "selected-mode");
            jsPlumb.addClass(controls.querySelectorAll("[mode='" + mode + "']"), "selected-mode");
        });

        // pan mode/select mode
        jsPlumb.on(controls, "tap", "[mode]", function () {
            renderer.setMode(this.getAttribute("mode"));
        });

        // on home button click, zoom content to fit.
        jsPlumb.on(controls, "tap", "[reset]", function () {
            toolkit.clearSelection();
            renderer.zoomToFit();
        });

        // configure Drawing tools. This is an optional include.
        new jsPlumbToolkit.DrawingTools({
            renderer: renderer
        });

        jsPlumb.on(canvasElement, "tap", ".node-delete, .node-delete i", function () {
            var info = renderer.getObjectInfo(this);
            jsPlumbToolkit.Dialogs.show({
                id: "dlgConfirm",
                data: {
                    msg: "Delete '" + info.obj.data.text + "'"
                },
                onOK: function () {
                    toolkit.removeNode(info.obj);
                }
            });
        });

        // change a question or action's label
        jsPlumb.on(canvasElement, "tap", ".node-edit, .node-edit i", function () {
            // getObjectInfo is a method that takes some DOM element (this function's `this` is
            // set to the element that fired the event) and returns the toolkit data object that
            // relates to the element. it ascends through parent nodes until it finds a node that is
            // registered with the toolkit.
            var info = renderer.getObjectInfo(this);
            jsPlumbToolkit.Dialogs.show({
                id: "dlgText",
                data: info.obj.data,
                title: "Edit " + info.obj.data.type + " name",
                onOK: function (data) {
                    if (data.text && data.text.length > 2) {
                        // if name is at least 2 chars long, update the underlying data and
                        // update the UI.
                        toolkit.updateNode(info.obj, data);
                    }
                }
            });
        });

// ------------------------ / rendering ------------------------------------

        var _syntaxHighlight = function (json) {
            json = json.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
            return "<pre>" + json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function (match) {
                    var cls = 'number';
                    if (/^"/.test(match)) {
                        if (/:$/.test(match)) {
                            cls = 'key';
                        } else {
                            cls = 'string';
                        }
                    } else if (/true|false/.test(match)) {
                        cls = 'boolean';
                    } else if (/null/.test(match)) {
                        cls = 'null';
                    }
                    return '<span class="' + cls + '">' + match + '</span>';
                }) + "</pre>";
        };

        var datasetContainer = document.querySelector(".jtk-demo-dataset");
        var _updateDataset = function () {
            datasetContainer.innerHTML = _syntaxHighlight(JSON.stringify(toolkit.exportData(), null, 4));
        };
        //
        // any operation that caused a data update (and would have caused an autosave), fires a dataUpdated event.
        //
        toolkit.bind("dataUpdated", _updateDataset);

// ------------------------ drag and drop new tables/views -----------------

        //
        // Here, we are registering elements that we will want to drop onto the workspace and have
        // the toolkit recognise them as new nodes.
        //
        //  typeExtractor: this function takes an element and returns to jsPlumb the type of node represented by
        //                 that element. In this application, that information is stored in the 'jtk-node-type' attribute.
        //
        //  dataGenerator: this function takes a node type and returns some default data for that node type.
        //
        renderer.registerDroppableNodes({
            droppables: nodePalette.querySelectorAll("li"),
            dragOptions: {
                zIndex: 50000,
                cursor: "move",
                clone: true
            },
            typeExtractor: function (el) {
                return el.getAttribute("jtk-node-type");
            },
            dataGenerator: function (type) {
                return {
                    w: 120,
                    h: 80
                };
            }
        });

// ------------------------ / drag and drop new tables/views -----------------

    });

})();