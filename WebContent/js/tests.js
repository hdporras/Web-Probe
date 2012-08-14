function test()
{
	document.getElementById("demoReply").innerHTML = "Calling Server Via AJAX...";
}

function update() 
{
	document.getElementById("demoReply").innerHTML = "Starting ...";

	var name = dwr.util.getValue("demoName");

	document.getElementById("demoReply").innerHTML = "Calling Server ...";

	Demo.sayHello(name, function(data) {
		dwr.util.setValue("demoReply", data);
		//document.getElementById("demoReply").innerHTML = data;
	});
}


function getURIList()
{
	document.getElementById("answerResults").innerHTML = "Starting ...";

	var uri = dwr.util.getValue("uriName");

	document.getElementById("answerResults").innerHTML = "Calling Server with: "+uri;


	//ArrayTest.getStringArray(objectEval($("p10").value), reply1);
	PMLTests.getStringArray(uri, function(data)
			{
		document.getElementById("answerResults").innerHTML = "In Callback Function ";
		document.getElementById("answerResults").innerHTML = "Array Length: "+ data.length;

		var uriList = "<ul>"; 

		for(var i=0; i<data.length; i++)
		{
			uriList = uriList+" <li><a href='"+data[i]+"'>"+data[i]+"</a></li>";
		}
		uriList= uriList+" </ul>";

		//dwr.util.setValue("uriReply", uriList);
		document.getElementById("answerResults").innerHTML = uriList;
			});
}

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
		var result = eval("(" + data + ")");
		document.getElementById("answerResults").innerHTML = "In Callback Function ";
		document.getElementById("answerResults").innerHTML = "Array Length: "+ result.length;

		var answers = "<h3 class=\"ui-widget-header ui-corner-all\">Answers:</h3>"; 

		for(var i=0; i<result.length; i++)
		{
			answers = answers+" <div class='answerBox'>" +
					"<div class='answerConclusion' value=\""+ result[i].uri +"\">"+result[i].conclusion+"</div>" +
					"<div class='answerAttributes'>"+result[i].metadata+"</div>" +
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

function getQuery()
{
	//resetLocalView()
	resetTabs();
	$("#tabs").tabs("option", "enable", [0]);
	
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
		$("#question").html("<h3 class=\"ui-widget-header ui-corner-all\">Question:</h3> "+ question);
		//document.getElementById("question").innerHTML = ;
	});
}








var xmlhttp = new XMLHttpRequest();

//THE FOLOWING FUNCTION INITITATES THE REQUESTS
function ajaxTest()
{
	//document.getElementById("database").innerHTML="";
	//var name = document.getElementById("name").value;
	//var url = document.getElementById("url").value;
	//var type = document.getElementById("type").value;
	//var info = document.getElementById("info").value;

	xmlhttp.onreadystatechange=function()
	{
		if (xmlhttp.readyState==4 && xmlhttp.status==200)
		{
			document.getElementById("countrydivcontainer").innerHTML=" <div class='answerBox'><div class='answerConclusion'>"+data[i]+"</div><div class='answerAttributes'></div></div>";
		}
	}

	URL="lookup.php";//?name=AAAA&url=BBBB&type=CCCC&info=DDDD";
	//URL=URL.replace("AAAA",name);
	//URL=URL.replace("BBBB",url);
	//URL=URL.replace("CCCC",type);
	//URL=URL.replace("DDDD",info);
	//document.getElementById("database").innerHTML=URL;
	xmlhttp.open("GET",URL,true);
	xmlhttp.send();
}