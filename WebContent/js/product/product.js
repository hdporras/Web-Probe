/**
 * @author Hugo Porras
 * 
 * Acknowledgements:
 * This work used resources from Cyber-ShARE Center of Excellence, 
 * which is supported by National Science Foundation grant number 
 * HRD-0734825. Unless otherwise stated, work by Cyber-ShARE is 
 * licensed under a Creative Commons Attribution 3.0 Unported 
 * License. Permissions beyond the scope of this license may be 
 * available at 
 * http://cybershare.utep.edu/content/cyber-share-acknowledgment.
 */

var currentProductURI;

/*
function setupProductView(index)
{
	resetTabs();
	$("#tabs").tabs('enable', 1);
	$("#tabs").tabs("select",1);
	//resetTabs();
	
	clearProductTabs();
	
	$("#tabsBottom").tabs("add", "#raw", "Text",0);
	$("#raw").html("<p> "+ queryResult[index].conclusion +" </p>");
	
	createViskoViewers(currentProductURI);
}*/

function clearProductTabs()
{
	var tab_count = $('#tabsBottom').tabs('length');
	
	var i;
	for (i=0; i < tab_count; i++)
	{
	    $('#tabsBottom').tabs( "remove" , 0 );
	}
}


function getViskoVis()
{
	var uri = $("#uriName").val();// get Value from URI Text Bar.
	getViskoVis(uri);
}

/*
function getViskoVis(uri)
{
	showMainTabs();
	
	$("#tabs").tabs('enable', 1);
	$("#tabs").tabs("select",1);
	//resetTabs();
	
	
	currentProductURI = uri;
	
	clearProductTabs();

	createViskoViewers(uri);
}*/