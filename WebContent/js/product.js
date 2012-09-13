var currentProductURI;

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
}

function clearProductTabs()
{
	$("#tabsBottom").tabs("remove", 0);
	$("#tabsBottom").tabs("remove", 0);
	$("#tabsBottom").tabs("remove", 0);
	$("#tabsBottom").tabs("remove", 0);
	$("#tabsBottom").tabs("remove", 0);
	$("#tabsBottom").tabs("remove", 0);
	$("#tabsBottom").tabs("remove", 0);
	$("#tabsBottom").tabs("remove", 0);
	$("#tabsBottom").tabs("remove", 0);
	$("#tabsBottom").tabs("remove", 0);
}


function getViskoVis()
{
	var uri = $("#uriName").val();// get Value from URI Text Bar.
	getViskoVis(uri);
}


function getViskoVis(uri)
{
	showMainTabs();
	
	$("#tabs").tabs('enable', 1);
	$("#tabs").tabs("select",1);
	//resetTabs();
	
	
	currentProductURI = uri;
	
	clearProductTabs();

	createViskoViewers(uri);
}