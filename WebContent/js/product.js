var currentProductURI;

function productSelectActivate()
{
	$(".answerConclusion").click( function()
			{
				var index = $(this).attr("value");//$(this).val();//this.attr("value");
				//alert("Index Val: "+index);
				
				currentProductURI = queryResult[index].uri;
				//alert("URI: "+uri+"\n Conclusion: "+queryResult[index].conclusion);
				//$("#uriName").val(uri);
				//getViskoVis();
				
				setupProductView(index);
			});
	/*
	$(".answerConclusion").hover(
			  function () {
				  $(this).css("cursor", "pointer");
			  },
			  function () {
			    //$(this).removeClass("hover");
			  }
		);
	*/
}

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
}