

function productSelectActivate()
{
	$(".answerConclusion").click( function()
			{
				var index = $(this).attr("value");//$(this).val();//this.attr("value");
				//alert("Index Val: "+index);
				
				var uri = queryResult[index].uri;
				//alert("URI: "+uri+"\n Conclusion: "+queryResult[index].conclusion);
				//$("#uriName").val(uri);
				//getViskoVis();
				
				setupProductView(index)
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
	$("#tabs").tabs("select",1);
	var uri = queryResult[index].uri;
	//var uri = $("#uriName").val();// get Value from URI Text Bar.
	
	clearProductTabs();
	
	$("#tabsBottom").tabs("add", "#raw", "Text",0);
	$("#raw").html("<p> "+ queryResult[index].conclusion +" </p>");
	
	createViskoViewers(uri);
}

function clearProductTabs()
{
	$("#tabsBottom").tabs("remove", 0);
	$("#tabsBottom").tabs("remove", 0);
	$("#tabsBottom").tabs("remove", 0);
	$("#tabsBottom").tabs("remove", 0);
	$("#tabsBottom").tabs("remove", 0);
	$("#tabsBottom").tabs("remove", 0);
}