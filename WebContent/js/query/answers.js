
/** Holds Query results. */
var queryResult;


function getQueryAnswers(uri)
{
	//ArrayTest.getStringArray(objectEval($("p10").value), reply1);
	PMLTests.getQueryAnswers(uri, function(data)
	{
		var result = eval("(" + data + ")");
		document.getElementById("answerResults").innerHTML = "In Callback Function ";
		document.getElementById("answerResults").innerHTML = "Array Length: "+ result.length;

		var answers = "<h3 class=\"ui-widget-header ui-corner-all\">Answers:</h3>"; 

		for(var i=0; i<result.length; i++)
		{
			answers = answers+" <div class='answerBox'>" +
					"<div class='answerConclusion'>"+result[i].conclusion+"</div>" +
					"<div class='answerAttributes'><pre>"+result[i].metadata+"</pre></div>" +
					"</div>";
		}
		answers= answers+" ";
		
		document.getElementById("answerResults").innerHTML = answers;
		//$(".answerBox").hide();
		//document.getElementById("testing").innerHTML = answers;
		/*
		$(".answerBox").show( "clip", {}, 3000, function(){
			
		});*/
	});
	
}

function getTestAnswers(uri)
{
	//ArrayTest.getStringArray(objectEval($("p10").value), reply1);
	PMLTests.getQueryAnswers(uri, function(data)
	{
		queryResult = eval("(" + data + ")");
		document.getElementById("answerResults").innerHTML = "In Callback Function ";
		document.getElementById("answerResults").innerHTML = "Array Length: "+ queryResult.length;

		var answers = "<h3 class=\"ui-widget-header ui-corner-all\">Answers:</h3>"; 

		for(var i=0; i<queryResult.length; i++)
		{
			var cachedThumbURI = getViskoThumbnail(queryResult[i].uri);
			if(cachedThumbURI != null)
			{
				answers = answers+" <div class='answerBox'>" +
						"<div class='answerConclusion' value='"+ i +"'><img src="+cachedThumbURI+" /></div>" +
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
		
		productSelectActivate();
	});
	
}

function getQuery()
{
	$("#tabs").tabs("select",0);
	$("#question").html("<h3 class=\"ui-widget-header ui-corner-all\">Question:</h3> ");
	$("#answerResults").html("Starting ...");
	//var uri = dwr.util.getValue("uriName");
	var uri = $("#uriName").val();// get Value from URI Text Bar.
	$("#answerResults").html("<h3 class=\"ui-widget-header ui-corner-all\">Answers:</h3>Calling Server with: "+uri);
	
	getQueryQuestion(uri);
	//getQueryAnswers(uri);
	getTestAnswers(uri);
}


function getQueryQuestion(uri)
{
	PMLQueryResults.getQueryContent(uri, function(data)
	{
		var question = dwr.util.toDescriptiveString(data, 1);
		$("#question").html("<h3 class=\"ui-widget-header ui-corner-all\">Question:</h3> <pre>"+ question+"</pre>");
		//document.getElementById("question").innerHTML = ;
	});
}