var URIparam = null;


/** URL GET parameters */
function URLGETparams()//call after tabs are setup.
{
	URIparam = getUrlVars()["uri"];
	
	//alert(URIparam);
	
	if(URIparam != null)
	{
		$("#uriName").val(URIparam);
		lookupURI();
	}
}


function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        vars[key] = value;
    });
    
    //alert(vars);
    
    return vars;
}




/** Lookup URI Action */
function lookupURI()
{
	var uri = $("#uriName").val();
	
	if( uri.indexOf("#answer") != -1 ) 
	{ 
		getViskoVis();
	} 
	else 
	{ 
		getQuery();
	}
		
}



/** Functionality for hiding/unhiding top navigation bar */ 
function hideNav()
{
	//$("#header").hide();
	$("#header").slideUp();
	$("#hideTop").slideDown();
	$("#maincontent").css("top", "16px");
}

function unhideNav()
{
	$("#hideTop").slideUp();
	$("#header").slideDown();
	$("#maincontent").css("top", "81px");
}




/** Disables View tabs. This is best used for a new context. (must re-enable tab that will be starting point for new context) */
function resetTabs()
{
	/*$("#tabs div").each(function(){
		$(this).html("");
	});*/
	
	
	$("#tabs").tabs("option", "disabled", [0,1,2,3]);
}

function restToTabIndex(index)
{
	resetLocalView();
	$("#tabs").tabs('enable', index);
	$("#tabs").tabs("select", index);
	resetTabs();
}

function resetLocalView()
{
	$("#tabs").tabs('enable', 2);
	$("#tabs").tabs("select",2);
	$('#accordion #conclusionHeader').click();
}

/*
function showBookmarks(){
	$("#bookmarks").slideDown();
}*/