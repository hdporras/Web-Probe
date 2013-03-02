
/** Holds current Query results. */
var queryResult;


/** Action listener for Answer Box click action */
function answerSelectActivate()
{
	$(".answerConclusion").click( function()
	{
		updateSelectionBox(this);
		startLoadingScreen();
		
		var index = $(this).attr("value");//$(this).val();//this.attr("value");
		currentGlobalURI = queryResult[index].uri;
		currentLocalURI = currentGlobalURI;
		
		//enable product view as well
	/*	$("#tabs").tabs('enable', 1);
		clearProductTabs();
		//then setup local view
		setupLocalView();
		//setupProductView(index);*/
		
		//initGlobalView();
		$("#tabs").tabs('enable', 3);
		$("#tabs").tabs("select", 3);
		$("#tabs").tabs("option", "disabled", [1,2]);
		getTree(currentGlobalURI);
	});
}

function updateSelectionBox(selectedAnswerConc)
{
	$(".answerBox").removeClass("currentAnswerSelection");//remove selection from all answer Boxes.
	$(selectedAnswerConc).parent().addClass("currentAnswerSelection");
}


function getTestAnswers(uri)
{
	//ArrayTest.getStringArray(objectEval($("p10").value), reply1);
	PMLTests.getQueryAnswers(uri, 
	{
		callback: function(data)
		{
			queryResult = eval("(" + data + ")");
			//document.getElementById("answerResults").innerHTML = "In Callback Function ";
			//document.getElementById("answerResults").innerHTML = "Array Length: "+ queryResult.length;
		
			var answers = "<h2 class=\"ui-widget-header ui-corner-all\">Answers:</h2>"; 
		
			for(var i=0; i<queryResult.length; i++)
			{
				var cachedThumbURI = getViskoThumbnail(queryResult[i].uri);
				//Cached Image
				if(cachedThumbURI != null)
				{
					answers = answers+" <div class='answerBox'>" +
							"<div class='answerConclusion' value='"+ i +"'><img src="+cachedThumbURI+" width=\"240px\" height=\"150px\" /></div>" +
							"<div class='answerAttributes'>"+queryResult[i].metadata+"</div>" +
							"</div>";
				}
				//Image ByReference
				else if(queryResult[i].conclusion.match(/.jpg$/i) || queryResult[i].conclusion.match(/.jpeg$/i) || queryResult[i].conclusion.match(/.png$/i) || queryResult[i].conclusion.match(/.gif$/i))
				{
					answers = answers+" <div class='answerBox'>" +
							"<div class='answerConclusion' value='"+ i +"'><img src="+queryResult[i].conclusion+" width=\"240px\" height=\"150px\" /></div>" +
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
			
			endLoadingScreen();
			createQAtips();
		},
		
		errorHandler: function(errorString, exception)
		{
			alert("Error getting Query Answers: " + errorString + "\n Exception: " + dwr.util.toDescriptiveString(exception, 2));
		    endLoadingScreen();
		}
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
	PMLQueryResults.getQueryContent(uri, 
	{
		callback: function(jsonResult)
		{
			jsonResult = jsonResult.replace(/(\r\n|\n|\r)/gm," <br/> ");
			
			var query = jsonParse(jsonResult);
			
			var rawString = dwr.util.toDescriptiveString(query.rawString, 1);
			var prettyString = query.prettyString;
			var questions = query.queryQuestions;
			
			if(questions != null && questions.length > 0)
			{	
				$("#question").html("<h2 class=\"ui-widget-header ui-corner-all\">Question:</h2> " +
									"<div class=\"questionText\"><pre>"+ questions[0] +"</pre> </div>");
				
				$("#question").append("<h2 class=\"ui-widget-header ui-corner-all\">Query:</h2> " +
						"<div class=\"questionText\"><pre>"+ rawString +"</pre> </div>");
			}
			else
				$("#question").html("<h2 class=\"ui-widget-header ui-corner-all\">Query:</h2> " +
									"<div class=\"questionText\"><pre>"+ rawString +"</pre> </div>");
			
			if(prettyString != "null")
				$("#question").append("<h2 class=\"ui-widget-header ui-corner-all\">Pretty Query:</h2> " +
										"<div class=\"questionText\"><pre>"+ prettyString +"</pre> </div>");
			
			//document.getElementById("question").innerHTML = ;
		},
		
		errorHandler: function(errorString, exception)
		{
			alert("Error getting Query Question: " + errorString + "\n Exception: " + exception);
		    endLoadingScreen();
		}
	});
}