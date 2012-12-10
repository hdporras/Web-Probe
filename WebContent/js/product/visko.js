

function createProductViewers(uri, conclusionText)
{
	startLoadingProductScreen();
	
	//enable and reset Product view
	$("#tabs").tabs('enable', 1);
	clearProductTabs();
	
	setupViewers("tabsBottom", uri, conclusionText);
}


function setupViewers(target, uri, conclusionText)
{
	var visualizations;
	
	VisCacheAccess.getCachedVisualizations(uri, 
	{
		callback: function(data)
		{
			visualizations = data;
			
			if(visualizations != null)
			{
				VisCacheAccess.getViewer(uri, 
				{
					callback: function(viewerData)
					{
						var viewers = viewerData;// eval("(" + data + ")");
						
						if(viewerData!=null)
						{
							for(var i=0; i<viewers.length; i++)
							{
								//Image
								if(viewers[i]=="http://rio.cs.utep.edu/ciserver/ciprojects/viskoOperator/imageJ-viewer.owl#imageJ-viewer" || viewers[i]=="http://rio.cs.utep.edu/ciserver/ciprojects/visko/imageJ-viewer1.owl#imageJ-viewer1")
								{
									visType = "Image";
									$("#"+target).tabs("add", "#"+target+"-"+i, "Image",1);
									$("#"+target+"-"+i).attr("class", target+"Fill");
									$("#"+target+"-"+i).html("<img src="+visualizations[i]+" />");
								}
								//PDF
								else if(viewers[i]=="http://rio.cs.utep.edu/ciserver/ciprojects/viskoOperator/pdf-viewer.owl#pdf-viewer" || viewers[i]=="http://rio.cs.utep.edu/ciserver/ciprojects/visko/pdf-viewer1.owl#pdf-viewer1")
								{
									visType = "PDF";
									$("#"+target).tabs("add", "#"+target+"-"+i, "PDF",0);
									$("#"+target+"-"+i).attr("class", target+"Fill");
									$("#"+target+"-"+i).html("<iframe src=\"http://docs.google.com/gview?url="+visualizations[i]+"&embedded=true\" style=\"width:98%; height:98%;\" frameborder=\"2\">" +
											"<object data=\""+visualizations[i]+"\" type=\"application/pdf\" width=\"100%\" height=\"100%\"><p>It appears you don't have a PDF plugin for this browser. you can <a href=\""+visualizations[i]+"\">click here to download the PDF file.</a></p>" +
											"</object></iframe>");
								}
								//Text
								else if(viewers[i]=="http://rio.cs.utep.edu/ciserver/ciprojects/viskoOperator/plain-text-viewer.owl#plain-text-viewer")
								{
									//if .tex, .txt, ...
									var text = getTextFromFile(visualizations[i]);
									
									visType = "Text";
									$("#"+target).tabs("add", "#"+target+"-"+i, "Text",3);
									$("#"+target+"-"+i).attr("class", target+"Fill");
									$("#"+target+"-"+i).html("<div class=\"fill\"> <pre>"+text+"</pre> </div>"); //<pre> </pre>
								}
							}
						}
						
						$(".tabs-bottom .ui-tabs-nav, .tabs-bottom .ui-tabs-nav > *").removeClass("ui-corner-all ui-corner-top")
						.addClass("ui-corner-bottom");
						$("#"+target).tabs("select",0);
					},
					
					errorHandler: function(errorString, exception)
					{
						alert("Error getting Visualization Viewer Types: " + errorString + "\n Exception: " + dwr.util.toDescriptiveString(exception, 2));
					    endLoadingScreen();
					}
				});
			}
			else //visualizations returned null
			{
				if(conclusionText != null)
				{
					$("#"+target).tabs("add", "#"+target+"-1", "Text",1);
					$("#"+target+"-1").attr("class", target+"Fill");
					$("#"+target+"-1").html("<div class=\"fill\"> <pre>"+conclusionText+"</pre> </div>"); //<pre> </pre>
				}
				else
				{
					$("#"+target).tabs("add", "#"+target+"-1", "No Vis",1);
					$("#"+target+"-1").attr("class", target+"Fill");
					$("#"+target+"-1").html("<h2>No Visualization(s) of Product found.</h2>");
				}
				
				$(".tabs-bottom .ui-tabs-nav, .tabs-bottom .ui-tabs-nav > *").removeClass("ui-corner-all ui-corner-top")
				.addClass("ui-corner-bottom");
				
				//alert("No Visualization(s) of Product found.");
			}
			
			endLoadingProductScreen();
			endLoadingPopupViewerScreen();
		},
		
		errorHandler: function(errorString, exception)
		{
			alert("Error Accessing Cached Visualizations: " + errorString + "\n Exception: " + dwr.util.toDescriptiveString(exception, 2));
		    endLoadingScreen();
		    endLoadingProductScreen();
		    endLoadingPopupViewerScreen();
		}
	});
}


function getTextFromFile(url)
{
	var txtFile = new XMLHttpRequest();
	txtFile.open("GET", url, false);
	
	var allText = "text";
	
	txtFile.onreadystatechange = function() 
	{
	  if (txtFile.readyState === 4) {  // Makes sure the document is ready to parse.
		  if (txtFile.status === 200) {  // Makes sure it's found the file.
			  allText = txtFile.responseText; 
	      //lines = txtFile.responseText.split("\n"); // Will separate each line into an array
	    }
	  }
	};
	
	txtFile.send(null);
	
	return allText;
}

/** Returns location of cached thumbnail from nodesetURI */
function getViskoThumbnail(uri)
{
	var thumbnailURI = null;
	
	VisCacheAccess.getCachedThumbnail(uri, 
	{
		async: false, 
		callback: function(data)
		{
			thumbnailURI = data;
		},
		
		errorHandler: function(errorString, exception)
		{
			alert("Error finding thumbnail: " + errorString + "\n Exception: " + exception);
		    endLoadingScreen();
		}
	});
	
	return thumbnailURI;
}




/* Loading for Product Visualizations */
function startLoadingProductScreen()
{
	$("#tabsBottom").block({ 
		message: '<h1>Loading Visualizations...</h1>', 
		border: 'none', 
		padding: '15px', 
		backgroundColor: '#000', 
		'-webkit-border-radius': '10px', 
		'-moz-border-radius': '10px', 
		opacity: .5, 
		color: '#fff'  
	}); 
}

function endLoadingProductScreen()
{
	$("#tabsBottom").unblock(); 
}



/* Loading for Popup Viewer Visualizations */
function startLoadingPopupViewerScreen()
{
	$("#popupViewerTabs").block({ 
		message: '<h1>Loading Visualizations...</h1>', 
		border: 'none', 
		padding: '15px', 
		backgroundColor: '#000', 
		'-webkit-border-radius': '10px', 
		'-moz-border-radius': '10px', 
		opacity: .5, 
		color: '#fff'  
	}); 
}

function endLoadingPopupViewerScreen()
{
	$("#popupViewerTabs").unblock(); 
}


function clearPopupViewerTabs()
{
	var tab_count = $('#popupViewerTabs').tabs('length');
	
	var i;
	for (i=0; i < tab_count; i++)
	{
	    $('#popupViewerTabs').tabs( "remove" , 0 );
	}
}