
/* Bookmark Menu */
$(document).ready( function()
{
	$('#BookmarkButton').mouseover( function(event)
	{
		showBookmarks();
				
		$("#bookmarks").mouseover( function()
		{
			showBookmarks();
		});
				
	});
	
	$('#BookmarkButton').mouseout( function()
	{
		hideBookmarks();
	});
	
	$("#bookmarks").mouseover( function()
	{
		showBookmarks();
	});

	$("#bookmarks").mouseout( function()
	{
		hideBookmarks(); 
	});
});

function showBookmarks()
{
	var vis = $("#bookmarks").css("visibility");
	//alert(vis);
	
	if(vis == "hidden")
		$("#bookmarks").css("visibility", "visible");
		//$("#bookmarks").slideDown();
		
}

function hideBookmarks()
{
	$("#bookmarks").css("visibility", "hidden");
}


/* Settings Menu */

$(document).ready( function()
{
	$('#SettingsButton').mouseover( function(event)
	{
		showSettings();
					
		$("#settings").mouseover( function()
		{				
			showSettings();					
		});	
		
	});
	
	
	$("#SettingsButton").mouseout( function(){
		hideSettings(); 
	});
	
	$("#settings").mouseover( function()
	{				
		showSettings();					
	});
	
	$("#settings").mouseout( function(){
		hideSettings(); 
	});
	
});

function showSettings()
{
	var vis = $("#settings").css("visibility");

	if(vis == "hidden")
		$("#settings").css("visibility", "visible");

}

function hideSettings()
{
	$("#settings").css("visibility", "hidden");
}
