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
					
					alert(jsonResult);
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
	var forcenodes = force.nodes(graph.nodes);
	var forcelinks = forcenodes.links(graph.links)
						.linkDistance(120)
						.charge(-400);
		forcelinks.start();
/*
	var link = svg
		.selectAll(".link")
		.data(graph.links)
		.enter().append("line")
		.attr("class", "provLink");
		//.style("stroke-width", function(d) { return Math.sqrt(d.value); });
	*/
	// build the arrow.
	svg.append("svg:defs").selectAll("marker")
	    .data(["end"])
	  .enter().append("svg:marker")
	    .attr("id", String)
	    .attr("viewBox", "0 -5 10 10")
	    .attr("refX", 15)
	    .attr("refY", -1.5)
	    .attr("markerWidth", 6)
	    .attr("markerHeight", 6)
	    .attr("orient", "auto")
	  .append("svg:path")
	    .attr("d", "M0,-5L10,0L0,5");

	// add the links and the arrows
	var path = svg.append("svg:g").selectAll("path")
	    .data(force.links())
	  .enter().append("svg:path")
	    .attr("class", "link")
	    .attr("marker-end", "url(#end)");
	
	

	var node = svg.selectAll(".node")
		.data(graph.nodes)
	.enter().append("g")
		.attr("class", "node")
		.on("mouseover", mouseover)
		.on("mouseout", mouseout)
		.call(force.drag);
	
	node.append("circle")
		.attr("class", "provNode")
		.attr("r", 15)
		.style("fill", function(d) 
				{
					if(d.group == "Activity")
						return "#6baed6";
					else if(d.group == "Entity")
						return "#FBF782";
					else if(d.group == "Agent")
						return "#fdd0a2";
					else if(d.group == "DataProperty")
						return "#dadaeb";
					else
						return "#bdbdbd";//anon
				});
	
	node.append("text")
	    .attr("x", 10)
	    .attr("dy", ".4em")
	    //.attr("dx", 1)
	    .text(function(d) { return d.name; });
	
	node.append("title")
		.text(function(d) { return d.name; });

	force.on("tick", function()
	{
		 path.attr("d", function(d) {
		        var dx = d.target.x - d.source.x,
		            dy = d.target.y - d.source.y,
		            dr = Math.sqrt(dx * dx + dy * dy);
		        return "M" + 
		            d.source.x + "," + 
		            d.source.y + "A" + 
		            dr + "," + dr + " 0 0,1 " + 
		            d.target.x + "," + 
		            d.target.y;
		    });

		    node.attr("transform", function(d) { 
		            					return "translate(" + d.x + "," + d.y + ")"; 
		            				});
		
		/*
		link.attr("x1", function(d) { return d.source.x; })
			.attr("y1", function(d) { return d.source.y; })
			.attr("x2", function(d) { return d.target.x; })
			.attr("y2", function(d) { return d.target.y; });

		node.attr("cx", function(d) { return d.x; })
			.attr("cy", function(d) { return d.y; });
		*/
	});

	
	function mouseover() 
	{
		  d3.select(this).select("circle").transition()
		      .duration(750)
		      .attr("r", 30);
	}

	function mouseout() 
	{
		 d3.select(this).select("circle").transition()
		      .duration(750)
		      .attr("r", 15);
	}
}



$(document).ready(function()
{
	//Don't show popup in PROV Graph
	hidePopup = true;
	
	$("#popup").hide();
	$("#unhidePopup").show("slide", { direction: "right" }, 1000);
	
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
