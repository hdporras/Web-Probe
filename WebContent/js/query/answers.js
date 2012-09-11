
/** Holds current Query results. */
var queryResult;


/** Action listener for Answer Box click action */
function answerSelectActivate()
{
	$(".answerConclusion").click( function()
			{
				var index = $(this).attr("value");//$(this).val();//this.attr("value");
				
				currentLocalURI = queryResult[index].uri;
				
				setupLocalView();
				//setupProductView(index);
			});
}


function getTestAnswers(uri)
{
	//ArrayTest.getStringArray(objectEval($("p10").value), reply1);
	PMLTests.getQueryAnswers(uri, function(data)
	{
		queryResult = eval("(" + data + ")");
		//document.getElementById("answerResults").innerHTML = "In Callback Function ";
		//document.getElementById("answerResults").innerHTML = "Array Length: "+ queryResult.length;

		var answers = "<h2 class=\"ui-widget-header ui-corner-all\">Answers:</h2>"; 

		for(var i=0; i<queryResult.length; i++)
		{
			var cachedThumbURI = getViskoThumbnail(queryResult[i].uri);
			if(cachedThumbURI != null)
			{
				answers = answers+" <div class='answerBox'>" +
						"<div class='answerConclusion' value='"+ i +"'><img src="+cachedThumbURI+" width=\"230px\" /></div>" +
						"<div class='answerAttributes'>"+queryResult[i].metadata+"</div>" +
						"</div>";
			}
			else
			{
				answers = answers+" <div class='answerBox'>" +
						"<div class='answerConclusion' value='"+ i +"'>"+queryResult[i].conclusion+"</div>" +
						"<div class='answerAttributes'>"+queryResult[i].metadata+"</div>" +
						"</div>";
			}
		}
		answers= answers+" ";
		
		document.getElementById("answerResults").innerHTML = answers;
		//$(".answerBox").hide();
		//document.getElementById("testing").innerHTML = answers;
		/*
		$(".answerBox").show( "clip", {}, 3000, function(){
			
		});*/
		
		answerSelectActivate();
		//productSelectActivate();
	});
	
}

function getQuery()
{
	showMainTabs();
	
	//resetTabs(); Only bookmarks ressetting at the moment. Must reset when using the Lookup and LookAnswer buttons.
	//resetLocalView();
	$("#tabs").tabs('enable', 0);
	$("#tabs").tabs("select",0);
	
	resetTabs();
	
	$("#question").html("<h3 class=\"ui-widget-header ui-corner-all\">Question:</h3> <img src=\"images/load.gif\"> ");
	//$("#answerResults").html("Starting ...");
	//var uri = dwr.util.getValue("uriName");
	var uri = $("#uriName").val();// get Value from URI Text Bar.
	//$("#answerResults").html("<h3 class=\"ui-widget-header ui-corner-all\">Answers:</h3>Calling Server with: "+uri);
	
	getQueryQuestion(uri);
	
	$("#answerResults").html("<h3 class=\"ui-widget-header ui-corner-all\">Answers:</h3> <img src=\"images/load.gif\"> Calling Server with: "+uri);
	//getQueryAnswers(uri);
	getTestAnswers(uri);
}


function getQueryQuestion(uri)
{
	PMLQueryResults.getQueryContent(uri, function(data)
	{
		var question = dwr.util.toDescriptiveString(data, 1);
		$("#question").html("<h2 class=\"ui-widget-header ui-corner-all\">Question:</h2> <div class=\"questionText\"><pre>"+ question+"</pre>");
		//document.getElementById("question").innerHTML = ;
	});
}