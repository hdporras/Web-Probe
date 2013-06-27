var currentPROVGlobalURI;
var currentPROVNode;

var hidePopup=false;

function setCurrentPROVGlobalURI(uri)
{
	currentPROVGlobalURI = uri;
}

function initPROVGraphView()
{
	//
	showMainTabs();
	$("#tabs").tabs('enable', 3);
	$("#tabs").tabs("select", 3);
	resetTabs();	
}

function getPROVGraph(URI)
{
	
	d3.select("svg").remove();
	startLoadingScreen();
	
	PROVGraph.getGraph(URI,
			{
				callback: function(jsonResult)
				{
					//alert("JSON TREE: \n"+jsonResult);
					
					//This javascript replaces all 3 types of line breaks with an html break
					jsonResult = jsonResult.replace(/(\r\n|\n|\r)/gm," <br/> ");
					
					var jsonGraph = jsonParse(jsonResult);
					
					alert(jsonGraph);
					drawGraph(jsonGraph);
					endLoadingScreen();
				},
				
				
				errorHandler: function(errorString, exception)
				{
					alert("Error getting Tree content: " + errorString );//+ "\n Exception: " + dwr.util.toDescriptiveString(exception, 2));
					endLoadingScreen();
				}
			});
	
}


var nodeWidth, nodeHeight;
var vis;

function drawGraph(graph)
{

	var width = 960,
	height = 500;

	var color = d3.scale.category20();

	var force = d3.layout.force()
		.charge(-120)
		.linkDistance(50)
		.size([width, height]);

	var svg = d3.select("#container")
				.append("svg")
					.attr("width", "100%" )
					.attr("height", "100%" );


	//Set Graph
	force
		.nodes(graph.nodes)
		.links(graph.links)
		.start();

	var link = svg
		.selectAll(".link")
		.data(graph.links)
		.enter().append("line")
		.attr("class", "provLink");
		//.style("stroke-width", function(d) { return Math.sqrt(d.value); });

	var node = svg
		.selectAll(".node")
		.data(graph.nodes)
		.enter().append("circle")
		.attr("class", "provNode")
		.attr("r", 7)
		//.style("fill", function(d) { return color(d.group); })
		.call(force.drag);

	node.append("title")
		.text(function(d) { return d.name; });

	force.on("tick", function()
	{
		link.attr("x1", function(d) { return d.source.x; })
			.attr("y1", function(d) { return d.source.y; })
			.attr("x2", function(d) { return d.target.x; })
			.attr("y2", function(d) { return d.target.y; });

		node.attr("cx", function(d) { return d.x; })
			.attr("cy", function(d) { return d.y; });
	});

}



$(document).ready(function()
{
/*	
	//Give Node a Yellow border when slected
	$(".node").click(function(event)
	{
		//event.preventDefault();
		//startLoadingScreen();
		
		$(this).attr("stroke", "yellow");
		$(this).attr("stroke-width", "4.0px");
	});*/
});
