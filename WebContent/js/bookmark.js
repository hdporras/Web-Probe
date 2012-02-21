


$(document).ready(function(){
	
	
	$("a.bookmark").click(function(event){
		
		var uri = $(this).attr("href");
		$("#uriName").val(uri);
		
		getQuery();	
		//alert(uri);
		event.preventDefault();
   });
	
	$("a.answerBookmark").click(function(event){
		
		var uri = $(this).attr("href");
		$("#uriName").val(uri);

		getViskoVis();
		//getQuery();	
		//alert(uri);
		event.preventDefault();
   });
	
 });