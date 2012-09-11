

/** Mouse Click Listeners for anchors of type "bookmark"(Query) or "answerBookmark" */
$(document).ready(function(){
	
	
	//Query bookmarks
	$("a.bookmark").click(function(event){
		event.preventDefault();
		
		var uri = $(this).attr("href");
		$("#uriName").val(uri);
		
		resetTabs();
		
		getQuery();	
		//alert(uri);
		
		updateLookupButton();
   });
	
	//Answer Bookmarks
	$("a.answerBookmark").click(function(event){
		event.preventDefault();
		
		var uri = $(this).attr("href");
		$("#uriName").val(uri);

		resetTabs();
		
		//getViskoVis();
		setCurrentLocalURI(uri);
		initLocalView();
		setupLocalView();
		//lookupURI();
		
		
		updateLookupButton();
   });
	
 });