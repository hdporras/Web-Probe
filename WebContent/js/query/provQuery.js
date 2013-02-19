
/** Holds current Query results. */
var provQueryResult;

/** Lookup URI Action */
function lookupPML3URI()
{
	//starts the loading screen;
	startLoadingScreen(); 
	
	var uri = $("#uriName").val();
	
	showMainTabs();
	
	//resetTabs(); Only bookmarks ressetting at the moment. Must reset when using the Lookup and LookAnswer buttons.
	//resetLocalView();
	$("#tabs").tabs('enable', 0);
	$("#tabs").tabs("select",0);
	
	resetTabs();
	
	//var uri = dwr.util.getValue("uriName");
	
	$("#answerResults").html("<h3 class=\"ui-widget-header ui-corner-all\">Answers:</h3>Calling Server with: "+uri);

	getPROVQuery(uri);
}



function getPROVQuery(uri)
{
	BuildPROVQuery.buildQuery(uri, 
	{
		callback: function(jsonResult)
		{
			jsonResult = jsonResult.replace(/(\r\n|\n|\r)/gm," <br/> ");
			
			var query = jsonParse(jsonResult);
			
			var rawString = dwr.util.toDescriptiveString(query.rawString, 1);
			//var questions = query.queryQuestions;
			
			/*if(questions != null && questions.length > 0)
			{	
				$("#question").html("<h2 class=\"ui-widget-header ui-corner-all\">Question:</h2> " +
									"<div class=\"questionText\"><pre>"+ questions[0] +"</pre> </div>");
				
				$("#question").append("<h2 class=\"ui-widget-header ui-corner-all\">Query:</h2> " +
						"<div class=\"questionText\"><pre>"+ rawString +"</pre> </div>");
			}
			else
				$("#question").html("<h2 class=\"ui-widget-header ui-corner-all\">Query:</h2> " +
									"<div class=\"questionText\"><pre>"+ rawString +"</pre> </div>");
			*/
			if(rawString != "null")
				$("#question").append("<h2 class=\"ui-widget-header ui-corner-all\">Query:</h2> " +
										"<div class=\"questionText\"><pre>"+ rawString +"</pre> </div>");
			
			
			//----ANSWERS
			
			var answers = "<h2 class=\"ui-widget-header ui-corner-all\">Answers:</h2>";
			var queryResult = query.answers;
			
			for(var i=0; i<queryResult.length; i++)
			{
				/*var cachedThumbURI = getViskoThumbnail(queryResult[i].uri);
				if(cachedThumbURI != null)
				{
					answers = answers+" <div class='answerBox'>" +
							"<div class='answerConclusion' value='"+ i +"'><img src="+cachedThumbURI+" width=\"230px\" /></div>" +
							"<div class='answerAttributes'>"+queryResult[i].metadata+"</div>" +
							"</div>";
				}
				else
				{*/
				if(queryResult[i].indexOf(".png") !== -1)
				{
					answers = answers+" <div class='answerBox'>" +
							"<div class='answerConclusion' value='"+ i +"'><img src="+queryResult[i]+" width=\"230px\" /></div>" +
							//"<div class='answerAttributes'>"+queryResult[i].metadata+"</div>" +
							"</div>";
				}
				else
				{
					answers = answers+" <div class='answerBox'>" +
							"<div class='answerConclusion' value='"+ i +"'>"+queryResult[i]/*.conclusion*/+"</div>" +
							//"<div class='answerAttributes'>"+queryResult[i].metadata+"</div>" +
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
			
			endLoadingScreen();
			//createQAtips();
		},
		
		errorHandler: function(errorString, exception)
		{
			alert("Error getting Query Question: " + errorString + "\n Exception: " + exception);
		    endLoadingScreen();
		}
	});
}