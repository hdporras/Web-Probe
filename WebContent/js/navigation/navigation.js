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
		updateLookupButton();
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
	//starts the loading screen;
	startLoadingScreen(); 
	
	var uri = $("#uriName").val();
	
	PMLServices.isQuery(uri, 
	{
		callback: function(isquery)
		{
			//check if PML Query
			if( isquery )
			{
				getQuery();
			}
			else 
			{
				PMLServices.isNode(uri, 
				{
					callback: function(isnode)
					{
						//check if PML Node
						if( isnode )
						{
							/*
							setCurrentLocalURI(uri);
							initLocalView();
							setupLocalView();*/
							
							setCurrentGlobalURI(uri);
							initGlobalView();
							getTree(currentGlobalURI);
						}
						else
						{//URI not correct.
							alert("URI does not point to PML Node/Query. \n Ensure URI is formatted correctly and try again. ");
							endLoadingScreen();
						}
					},

					errorHandler: function(errorString, exception)
					{
						alert("Error Determining if URI is Node: " + errorString + "\n Exception: " + dwr.util.toDescriptiveString(exception, 2));
						endLoadingScreen();
					}
				});
			}
		},
		
		errorHandler: function(errorString, exception)
		{
			alert("Error Determining if URI is Query: " + errorString + "\n Exception: " + dwr.util.toDescriptiveString(exception, 2));
		    endLoadingScreen();
		}
	});
}



/** Unhides the initially hidden Main Tabs (Q&A, Product, Justification..)
 *  And hides the initial splash screen. */
function showMainTabs()
{
	$("#splash").css('visibility', 'hidden');
	$("#tabs").css('visibility', 'visible');
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
	$("#maincontent").css("top", "46px");
}



/** Disables View tabs. This is best used for a new context. (must re-enable tab that will be starting point for new context) */
function resetTabs()
{
	
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


/** Settings Logic */
function toggleSettings()
{
	var vis = $("#settings").css("visibility");
	//alert(vis);
	
	if(vis == "hidden")
		$("#settings").css("visibility", "visible");
		//$("#bookmarks").slideDown();
	else
		$("#settings").css("visibility", "hidden");
}


/** Bookmark Logic */

function toggleBookmarks()
{
	var vis = $("#bookmarks").css("visibility");
	//alert(vis);
	
	if(vis == "hidden")
		$("#bookmarks").css("visibility", "visible");
		//$("#bookmarks").slideDown();
	else
		$("#bookmarks").css("visibility", "hidden");
}

$(document).ready(function()
{
	
	$("#bookmarks").click(function(){
		$("#bookmarks").css("visibility", "hidden");
	});
});



/** URI Bar Text Updates */
function updateLookupButton()
{
	var uriVal = $("#uriName").val().length;
	
	//alert("change " + uriVal);
	if( uriVal == 0)
		$("#lookupButton").button("disable");
	else	
		$("#lookupButton").button("enable");
}


function uriOnFocus()
{
	if($("#uriName").val() == "-- Enter URI Here --")
		$("#uriName").val("");
}

function uriOnBlur()
{
	if($("#uriName").val() == "")
		$("#uriName").val("-- Enter URI Here --");
}



/** Loading Screens */
function startLoadingScreen()
{
	$.blockUI({ css: { 
		message: 'Loading...', 
		border: 'none', 
		padding: '15px', 
		backgroundColor: '#000', 
		'-webkit-border-radius': '10px', 
		'-moz-border-radius': '10px', 
		opacity: .5, 
		color: '#fff' 
	} }); 
}

function endLoadingScreen()
{
	$.unblockUI();
}


/* Cancelling Load ?*/
var cancelLoad = false;
function cancelLoading()
{
	cancelLoad = true;
	endLoadingScreen();
}

/** resets variables for handling the Loading Screen. */
function resetForLoading()
{
	cancelLoad = false;
}