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
