

function getTree(URI)
{
	
	JustificationTreeBuilder.getJustificationTree(URI,
			{
				callback: function(jsonResult)
				{
					alert("JSON TREE: \n"+jsonResult);
					/*
					var infSteps = jsonTree.PMLnode.inferenceSteps;
					
					if(infSteps !=null && infSteps.length > 0)
					{
						infSteps[0]...
					}
					*/
					
					//This javascript replaces all 3 types of line breaks with an html break
					jsonResult = jsonResult.replace(/(\r\n|\n|\r)/gm," <br/> ");
					
					var jsonTree = jsonParse(jsonResult);
					
					
					drawTree(jsonTree);
				},
				
				
				errorHandler: function(errorString, exception)
				{
					alert("Error getting Tree content: " + errorString );//+ "\n Exception: " + dwr.util.toDescriptiveString(exception, 2));
				    
				}
			});
	
}


var transx, transy;
var nodeWidth, nodeHeight;

function drawTree(jsonTree)
{
	//var m = [ , 100, 40, 100 ]; 
	var w = 8000, // - m[1] - m[3], 
	h = 8000, // - m[0] - m[2], 
	i = 0, 
	root;
	
	nodeWidth = 250;
	nodeHeight = 100;
	
	transx = 200;
	transy = (-h / 2) + 200;


	var tree = d3.layout.tree().size([ h, w ]);

	var diagonal = d3.svg.diagonal().projection(function(d) {
		return [ d.y, d.x ];
	});


	var zoom = d3.behavior.zoom()
					.translate([transx,transy])
					.on("zoom", redraw);
	
	var vis = d3.select("#container")
		.append("svg:svg")
			.attr("width", w )//+ m[1] + m[3])
			.attr("height", h )//+ m[0] + m[2])
			.attr('fill', '#B8B5DF')//purplish gray
			.attr('fill-opacity', 0.5)
		//Zoom and Pan
		.append("svg:g")
			.call( zoom )
			.on("dblclick.zoom", null)//;
			.attr('fill', '#4BAA4C')//greenish
			.attr('fill-opacity', 0.5)
		.append("svg:g")
			.attr('fill', '#B5524C')//redish
			.attr('fill-opacity', 0.5)
	.attr("transform", "translate(" + transx + "," + transy + ")");
			

	vis.append('svg:rect')
    	.attr('width', w*2)
    	.attr('height', h*2)
    	.attr('fill', '#8DDABC')//bluish
    	.attr('fill-opacity', 0.5)
		.attr("transform", "translate(" + (-w/2) + "," + (-h/2) + ")");
    
	//redraw first time with offset.
	//redraw();
	
	function redraw()
	{
		  trans=d3.event.translate;
		  scale=d3.event.scale;
		  
		  vis.attr("transform",
		      "translate(" + trans + ")"
		      + " scale(" + scale + ")");
	}
	
	
	root = jsonTree;
	root.x0 = h / 2;
	root.y0 = 0;

	function toggleAll(d) 
	{
		if (d.children) {
			d.children.forEach(toggleAll);
			toggle(d);
		}
	}

	// Initialize the display to show a few nodes.
	//root.children.forEach(toggleAll);
	//toggle(root.children[0]);

	update(root);

	function update(source)
	{
		var duration = d3.event && d3.event.altKey ? 5000 : 500;

		// Compute the new tree layout.
		var nodes = tree.nodes(root);//.reverse();

		// Normalize for fixed-depth.
		nodes.forEach(function(d) {
			d.y = d.depth * 300;
		});

		// Update the nodes…
		var node = vis.selectAll("g.node").data(nodes, function(d) {
			return d.id || (d.id = ++i);
		});

		// Enter any new nodes at the parent's previous position.
		var nodeEnter = node.enter()
		.append("svg:g")
		.attr("class", "node")
		.attr("transform", function(d) {
			return "translate(" + source.y0 + "," + source.x0 + ")";
		})
		/*.on("click", function(d) {
			toggle(d);
			update(d);
		})*/
		//.on("dblclick", centerOnNode);
		.on("click", centerOnNode);

		//Rect
		nodeEnter.append("svg:rect")
		.attr("width", nodeWidth)
		.attr("height", nodeHeight)
		.attr("rx", 20)
		.attr("ry", 20)
		.style("fill", function(d) 
		{
			if(d.children)
				return "#D3E8F7";
			else if(d._children)
				return "#30E2FF";//"#2EC8FD";//33ABFF
			else
				return "#FFEBB3";//leaf
		})
		.attr("x", -125)
		.attr("y", -50);

		  
		//Add image to node, if available
		nodeEnter.append("svg:image")
		//.append("svg:image")
		.attr("xlink:href", function(d) 
		{
			if(d.PMLnode.conclusion.thumbURL != null)
				return d.PMLnode.conclusion.thumbURL;
			else
				return "../../images/No_sign.svg.png";
		})
		.attr("width", 225)
		.attr("height", 75)
		.attr("x", -113)
		.attr("y", -38);
		//.attr("cx", 40)
		//.attr("cy", 40);

		//Add text to node if available
		nodeEnter.append("svg:text").attr("x", function(d) {
			return d.children || d._children ? 125 : 125;//125 : -125;
		})
		.attr("dy", ".35em")
		.attr("text-anchor", function(d) {
			return d.children || d._children ? "start" : "start";//"start" : "end";
		}).text(function(d) {
			
			if(d.PMLnode.inferenceSteps != null)
			{
				return "Engine: "+d.PMLnode.inferenceSteps[0].infEngine+" <br> Rule: "+d.PMLnode.inferenceSteps[0].declRule;
			}
			
			return "Inference Step Information not found";
		}).style("fill-opacity", 1e-3);

		// Transition nodes to their new position.
		var nodeUpdate = node.transition().duration(duration).attr(
				"transform", function(d) {
					return "translate(" + d.y + "," + d.x + ")";
				});

		nodeUpdate.select("rect")
			.attr("width", nodeWidth)
			.attr("height", nodeHeight)
			.style("fill", function(d) 
			{
				if(d.children)
					return "#D3E8F7";
				else if(d._children)
					return "#30E2FF";//"#2EC8FD";//33ABFF
				else
					return "#FFEBB3";//leaf
			});

		nodeUpdate.select("text").style("fill-opacity", 1);

		// Transition exiting nodes to the parent's new position.
		var nodeExit = node.exit().transition().duration(duration)
		.attr(
				"transform",
				function(d) {
					return "translate(" + source.y + ","
					+ source.x + ")";
				}).remove();

		nodeExit.select("rect").attr("width", 1e-6).attr("height", 1e-6);
		
		nodeExit.select("image").attr("width", 1e-6).attr("height", 1e-6);

		nodeExit.select("text").style("fill-opacity", 1e-6);

		// Update the links…
		var link = vis.selectAll("path.link").data(tree.links(nodes),
				function(d) {
			return d.target.id;
		});

		// Enter any new links at the parent's previous position.
		link.enter().insert("svg:path", "g").attr("class", "link")
		.attr("d", function(d) {
			var o = {
					x : source.x0,
					y : source.y0
			};
			return diagonal({
				source : o,
				target : o
			});
		}).transition().duration(duration).attr("d", diagonal);

		// Transition links to their new position.
		link.transition().duration(duration).attr("d", diagonal);

		// Transition exiting nodes to the parent's new position.
		link.exit().transition().duration(duration).attr("d",
				function(d) {
			var o = {
					x : source.x,
					y : source.y
			};
			return diagonal({
				source : o,
				target : o
			});
		}).remove();

		// Stash the old positions for transition.
		nodes.forEach(function(d) {
			d.x0 = d.x;
			d.y0 = d.y;
		});
	}

	// Toggle children.
	function toggle(d) {
		if (d.children) {
			d._children = d.children;
			d.children = null;
		} else {
			d.children = d._children;
			d._children = null;
		}
	}
	
	
	function centerOnNode(d) {
		  var centroid = [d.x, d.y], // The selected nodes location
		      scale = zoom.scale(); //current zoom scale

		  var containerWidth = $("#container").width();
		  var containerHeight = $("#container").height();
		  
		  var translation = [
		     (containerWidth/2) + ( -centroid[1] /*- (nodeWidth/2)*/ ) * scale,   // X
		     (containerHeight/2) + ( -centroid[0] /*- (nodeHeight/2)*/ ) * scale // Y
		  ];

		  zoom.translate(translation);
		  //zoom.scale(1);

		  vis.transition().duration(1000).attr("transform",
			      "translate(" + zoom.translate() + ")"
			      + " scale(" + scale + ")");
		  
		  /*states.selectAll("path").transition()
		      .duration(1000)
		      .attr("d", path);*/
		}
	
/*	
	function dblclickCenter(d) {
		var x = 0,
	      y = 0;

	  // If the click was on the centered state or the background, re-center.
	  // Otherwise, center the clicked-on state.
	  if (!d || centered === d) {
	    centered = null;
	  } else {
	    var centroid = path.centroid(d);
	    x = w / 2 - centroid[0];
	    y = h / 2 - centroid[1];
	    centered = d;
	  }

	  // Transition to the new transform.
	  vis.transition()
	      .duration(1000)
	      .attr("transform", "translate(" + x + "," + y + ")");
	}*/
}