

/** Mouse Click Listeners for anchors of type "bookmark"(Query) or "answerBookmark" */
$(document).ready(function(){
	
	
	//Query bookmarks
	$("a.bookmark").click(function(event){
		
		var uri = $(this).attr("href");
		$("#uriName").val(uri);
		
		resetTabs();
		
		getQuery();	
		//alert(uri);
		event.preventDefault();
   });
	
	//Answer Bookmarks
	$("a.answerBookmark").click(function(event){
		
		var uri = $(this).attr("href");
		$("#uriName").val(uri);

		resetTabs();
		
		getViskoVis();
		//getQuery();	
		//alert(uri);
		event.preventDefault();
   });
	
 });