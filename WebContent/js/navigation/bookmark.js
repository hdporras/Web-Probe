/* To Add/Remove Bookmarks edit the index.html file. There is a div of id="bookmarks" in this file.
 * Follow the examples already used here to add bookmarks.
 */

/** Mouse Click Listeners for anchors of type "bookmark"(Query) or "answerBookmark" */
$(document).ready(function()
{
	
	//Query bookmarks
	$("a.bookmark").click(function(event)
	{
		event.preventDefault();
		startLoadingScreen();
		
		var uri = $(this).attr("href");
		$("#uriName").val(uri);
		
		resetTabs();
		
		getQuery();	
		//alert(uri);
		
		updateLookupButton();
   });
	
	//Answer Bookmarks
	$("a.answerBookmark").click(function(event)
	{
		event.preventDefault();
		startLoadingScreen();
		
		var uri = $(this).attr("href");
		$("#uriName").val(uri);

		resetTabs();
		
		//getViskoVis();
		/*setCurrentLocalURI(uri);
		initLocalView();
		setupLocalView();*/

		setCurrentGlobalURI(uri);
		initGlobalView();
		getTree(currentGlobalURI);

		
		//lookupURI();
		
		
		updateLookupButton();
   });
	
 });