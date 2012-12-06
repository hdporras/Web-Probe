var currentGlobalURI;
var currentNode;

var hidePopup=false;

function setCurrentGlobalURI(uri)
{
	currentGlobalURI = uri;
}

function initGlobalView()
{
	showMainTabs();
	$("#tabs").tabs('enable', 3);
	$("#tabs").tabs("select", 3);
	resetTabs();	
}

function getTree(URI)
{
	
	d3.select("svg").remove();
	startLoadingScreen();
	
	JustificationTreeBuilder.getJustificationTree(URI,
			{
				callback: function(jsonResult)
				{
					//alert("JSON TREE: \n"+jsonResult);
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

function drawTree(jsonTree)
{
	//var m = [ , 100, 40, 100 ]; 
	var w = 8000, // - m[1] - m[3], 
	h = 8000, // - m[0] - m[2], 
	i = 0, 
	root;
	
	nodeWidth = 250;
	nodeHeight = 125;

	
	var tree = d3.layout.tree().size([ h, w ]);

	var diagonal = d3.svg.diagonal().projection(function(d) {
			return [ d.y, d.x ];
		});


	var zoom = d3.behavior.zoom()
					//.translate([transx,transy])
					.on("zoom", redraw);
	
	//setting up canvas
	var vis = d3.select("#container")
		.append("svg:svg")
			.attr("width", "100%" )//+ m[1] + m[3])
			.attr("height", "100%" )//+ m[0] + m[2])
			.attr('fill', '#B8B5DF')//purplish gray
			.attr('fill-opacity', 0.5)
		//Zoom and Pan
		.append("svg:g")
			.style("cursor", "move")
			.call( zoom )
			.on("dblclick.zoom", null)//;
			.attr('fill', '#fff')//'#4BAA4C')//greenish
			.attr('fill-opacity', 1.0)
		.append("svg:g");
			//.attr('fill', '#B5524C')//redish
			//.attr('fill-opacity', 0.5);
	

	vis.append('svg:rect')
    	.attr('width', w*2)
    	.attr('height', h*2)
    	//.attr('fill', '#8DDABC')//bluish
    	//.attr('fill-opacity', 0.5)
		.attr("transform", "translate(" + (-w/2) + "," + (-h/2) + ")");
    
	

	//Zoom toolbar action listener
	$("#zoomIn").click( function()
	{
		var zoomScale = zoom.scale() * 2;
		zoom.scale(zoomScale);

		vis.transition().duration(1000).attr("transform",
			      "translate(" + zoom.translate() + ")"
			      + " scale(" + zoomScale + ")");
		centerOnNode(currentNode);
	});
	$("#zoomOut").click( function()
	{
		if(zoom.scale() > .03)
		{
			var zoomScale = zoom.scale() * .5;
			zoom.scale(zoomScale);

			vis.transition().duration(1000).attr("transform",
				      "translate(" + zoom.translate() + ")"
				      + " scale(" + zoomScale + ")");
			centerOnNode(currentNode);
		}
	});
	

	
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

	
	
	
	
	//**testing text html in svg
	var test = vis.append("svg:g")
		.attr("width", 225)
		.attr("height", 75)
		.attr("x", 0)
		.attr("y", 0)
		.attr("transform", "translate(1000, 1000)");
	
	test.append('svg:rect')
    	.attr("width", 225)
    	.attr("height", 75)
    	.style("fill", "gray");
	
	test.append("foreignObject")
		.attr("width", 225)
		.attr("height", 75)
		.attr("x", 0)
		.attr("y", 0)
		.style("background-color", "blue")
	
	.append("body")
		.attr("width", 225)
		.attr("height", 75)
		.attr("x", 0)
		.attr("y", 0)
		
		.append("div")
		//.style("background-color", "green")
		.attr("width", "200")
		.attr("height", "70")
		.append("p")
		.text("This is a paragraph test. This is a paragraph test. This is a paragraph test.");
	
	//End Test (Remove when done)
	
	
	
	
	update(root);
	nodeClick(root);//Center on root node to start.
	
	

	function update(source)
	{
		var duration = d3.event && d3.event.altKey ? 5000 : 500;

		// Compute the new tree layout.
		var nodes = tree.nodes(root);//.reverse();

		// Normalize for fixed-depth.
		nodes.forEach(function(d) {
				d.y = d.depth * 450;
			});
		
		
		/** Setting up the Nodes */
		
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
			/*.on("dblclick", function(d) {
				toggle(d);
				update(d);
				centerOnNode(d);
			})*/
			.on("dblclick", function(d) {
				setCurrentLocalURI(d.PMLnode.conclusion.concURI);
				showLocalView();
				setupLocalView();
				
			})
			.on("click", nodeClick);
		

		//Node Rectangle
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
			.attr("y", -80);

		//Rule and Engine Rectangle
		nodeEnter.append("svg:rect")
			.attr("width", 200)
			.attr("height", 30)
			.attr("x", -100)
			.attr("y", -75)
			.style("fill", "white")
			.style("stroke", "black")
			.style("cursor", "pointer");
		
		//Add Engine text
		nodeEnter.append("svg:text")
			.attr("y", -65)
			.attr("text-anchor", "middle")
			.text(function(d) {	
				if(d.PMLnode.inferenceSteps != null && d.PMLnode.inferenceSteps[0].infEngine != "null")
					return ""+d.PMLnode.inferenceSteps[0].infEngine;//Engine
				else
					return "No Engine";
			})
			.style("cursor", "pointer");
		//Add Rule text
		nodeEnter.append("svg:text")
			.attr("y", -50)
			.attr("text-anchor", "middle")
			.text(function(d) {	
				if(d.PMLnode.inferenceSteps != null && d.PMLnode.inferenceSteps[0].declRule != "null")
					return ""+d.PMLnode.inferenceSteps[0].declRule;//Rule
				else
					return "No Rule";
			})
			.style("cursor", "pointer");
				
		
		//Add image to node, if available
		nodeEnter.append("svg:image")
			.attr("xlink:href", function(d) 
			{
				if(d.PMLnode.conclusion.thumbURL != null)
					return d.PMLnode.conclusion.thumbURL;
				if(d.PMLnode.conclusion.conclusionText == null)
					return "../../images/No_sign.svg.png";
				else
					return null;//"../../images/No_sign.svg.png";
			})
			.attr("width", 225)
			.attr("height", 75)
			.attr("x", -113)
			.attr("y", -38)
			.style("cursor", "pointer");
			//.attr("cx", 40)
			//.attr("cy", 40);
		
		
		//Add Conclusion Text to node if no Image Available.
		nodeEnter.append("svg:text")
			.attr("x", -113)
			.attr("y", -30)
			.attr("dy", ".5em")
			.attr("text-anchor", "start")
			.text(function(d) {
				
				if(d.PMLnode.conclusion.thumbURL == null && d.PMLnode.conclusion.conclusionText != null)
				{
					return d.PMLnode.conclusion.conclusionText;
				}
				else
					return null;
			})
			.style("stroke", "black")
			.style("fill", "white")
			.style("fill-opacity", 1)
			.style("cursor", "pointer");
		
		
		
		/** Transitions */
				
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
	
	
	function nodeClick(d)
	{
		centerOnNode(d);
		loadPopupViewer(d);
	}
	
	function loadPopupViewer(d)
	{
		startLoadingPopupViewerScreen();
		clearPopupViewerTabs();
		setupViewers("popupViewerTabs", d.PMLnode.conclusion.concURI, d.PMLnode.conclusion.conclusionText);
	}
		
	function centerOnNode(d)
	{
		var centroid = [d.x, d.y], // The selected nodes location
		      scale = zoom.scale(); //current zoom scale

		  var containerWidth = $("#container").width();
		  var containerHeight = $("#container").height();
		  
		  var translation;
		  
		  //loadPopupViewer(d);
		  
		  // if Popup is visible
		  if(!hidePopup)
		  {  			
			  translation = [
			     (containerWidth * 0.5 * 0.65) + ( -centroid[1] /*- (nodeWidth/2)*/ ) * scale,   // X
			     (containerHeight/2) + ( -centroid[0] /*- (nodeHeight/2)*/ ) * scale // Y
			  ];		  
		  }
		  else
		  {			  
			  translation = [
			     (containerWidth/2) + ( -centroid[1] /*- (nodeWidth/2)*/ ) * scale,   // X
			     (containerHeight/2) + ( -centroid[0] /*- (nodeHeight/2)*/ ) * scale // Y
			  ];
		  }

		  //zoom and translate
		  zoom.translate(translation);

		  vis.transition().duration(1000).attr("transform",
				  "translate(" + zoom.translate() + ")"
				  + " scale(" + scale + ")");

		  
		  currentNode = d; //make this the currently slected node.
		  
		  
		  
		  
		  /*
		  //yellow box
		  this.append("svg:rect")
			.attr("width", nodeWidth)
			.attr("height", nodeHeight)
			.attr("cx", 0)//d.x)
			.attr("cy", 0)//d.y)
			//.style("fill", none)
			.style("stroke", "#FFFF00")
			.style("stroke-opacity", .9)
			.style("stroke-width", "5px");
		  */
		  
		  
		  /*states.selectAll("path").transition()
		      .duration(1000)
		      .attr("d", path);*/
		  
		  
		}
}



$(document).ready(function()
{
	$("#unhidePopup").hide();
	
	//Hide popup action.
	$("#hidePopup").click(function(){
		hidePopup = true;
		
		$("#popup").hide("slide", { direction: "right" }, 1000);
		$("#unhidePopup").show("slide", { direction: "right" }, 1000);
	});
	
	$("#unhidePopup").click(function(){
		hidePopup = false;
		
		$("#popup").show("slide", { direction: "right" }, 1000);
		$("#unhidePopup").hide();
	});
	
	
	//Give Node a Yellow border when slected
	$(".node").click(function(event)
	{
		//event.preventDefault();
		//startLoadingScreen();
		
		$(this).attr("stroke", "yellow");
		$(this).attr("stroke-width", "4.0px");
	});
});
