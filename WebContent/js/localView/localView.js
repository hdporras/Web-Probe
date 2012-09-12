var currentLocalURI;

function setCurrentLocalURI(uri)
{
	currentLocalURI = uri;
}

function initLocalView()
{
	showMainTabs();
	$("#tabs").tabs('enable', 2);
	$("#tabs").tabs("select",2);
	resetTabs();
	
}

function setupLocalView()
{
	startLoadingScreen();
	
	
	LocalViewExtractor.getLocalViewDetails(currentLocalURI, function(jsonResult)
		{
		
			//This javascript replaces all 3 types of line breaks with an html break
			jsonResult = jsonResult.replace(/(\r\n|\n|\r)/gm," <br/> ");
			//Replace all double white spaces with single spaces
			//result = result.replace(/\s+/g," ");
		
			//result = result.replace(/\\/g,"\\\\");
			//result = JSON.stringify(result+"");
			
			//alert(jsonResult+"");
			
			//var localViewDetails = JSON.parse( result + ""); //eval("(" + result + ")");
			var localViewDetails = jsonParse(jsonResult);
			
			var conclusion = localViewDetails.conclusion;
			var justified = localViewDetails.justifiedBy;
			var inferred = localViewDetails.usedToInfer; //htmlParts[2];
			var finalConc = localViewDetails.finalConclusion;
			var isFinalConc = finalConc.isRoot;
			
			/*if(finalConc is set)
			{
				finalConc = htmlParts[3];
				isFinalConc = false;
			}
			else
				isFinalConc = true;*/
			
			
			
		//Conclusion Section
			
			//check for image in cache, otherwise print conclusion text
			if(conclusion.thumbURL != null)
				$("#LocalViewConclusion").html("<div class=\"conclusionSection\"> <img class=\"localViewImage\" src=\""+conclusion.thumbURL+"\" /> </div>");
			else if(conclusion.conclusionText != null)
				$("#LocalViewConclusion").html("<div class=\"conclusionSection\"> "+conclusion.conclusionText+" </div>");
			else
				$("#LocalViewConclusion").html("<div class=\"conclusionSection\"> "+currentLocalURI+" </div>");
			
			//Add View Product Detail Button
			$("#LocalViewConclusion").append("	<div id=\"ConclusionBottom\" >" +
					"<button type=\"button\" onclick=\"currentProductURI=currentLocalURI; getViskoVis(currentProductURI);\" class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only\">" +
					"<span class=\"ui-button-text\">View Product Details</span> </button> </div>");

			
		//Justified By Section
			
			var justBySection ="";
			
			for(var i=0; i<justified.inferenceSteps.length; i++)
			{
				var inferenceStep = justified.inferenceSteps[i];
				
				var justTitle = "<div class=\"justTitle\">Inferred by inference engine <a class=\"engineRule\" href=\""+inferenceStep.infEngURI+"\" target=\"new\" >"+inferenceStep.infEngine+" </a> with declarative rule  <a class=\"engineRule\" href=\" "+inferenceStep.declRuleURI+" \" target=\"new\" >"+inferenceStep.declRule+" </a>  from the parents: </div>";
				var antecedentsHtml = "<div class=\"justifiedByAntecedents\"> <ol>";
				
				if(inferenceStep.antecedents != null)
				{
					for(var i=0; i<inferenceStep.antecedents.length; i++)
					{
						var antecedent = inferenceStep.antecedents[i];
						if(antecedent.antecedentCachedThumbURL != null)//Cached Thumbnail
							antecedentsHtml += "<li><div class=\"justAntecedent\">  <a class=\"localViewTextLink\" href=\" "+antecedent.antecedentURI+" \" onclick=\"currentLocalURI='"+antecedent.antecedentURI+"'; setupLocalView(); return false;\" > <img class=\"localViewImage\" src=\""+antecedent.antecedentCachedThumbURL+"\" /> </a> </div></li>";
						else if(antecedent.antecedentRawString != "hasURL?")//Embedded Raw String
							antecedentsHtml += "<li><div class=\"justAntecedent\">  <a class=\"localViewTextLink\" href=\" "+antecedent.antecedentURI+" \" onclick=\"currentLocalURI='"+antecedent.antecedentURI+"'; setupLocalView(); return false;\" >"+antecedent.antecedentRawString+" </a> </div></li>";
						else//External URL
							antecedentsHtml += "<li><div class=\"justAntecedent\">  <a class=\"localViewTextLink\" href=\" "+antecedent.antecedentURI+" \" onclick=\"currentLocalURI='"+antecedent.antecedentURI+"'; setupLocalView(); return false;\" >"+antecedent.antecedentConclusionURL+" </a> </div></li>";
					}
					antecedentsHtml += " </ol></div> ";
					
					//var justAntecedents = ;
				}
				else
					antecedentsHtml = "<div class=\"justifiedByAntecedents\"> <b>No Antecedents</b> </div>";
				
				justBySection += "<div class=\"inferenceStep\"> "+justTitle + antecedentsHtml+"</div>";
			}
			
			
			$("#LocalViewJustifiedBy").html(""+justBySection);
			
			
			
			
		//Used To Infer Section
			
			
			var usedToInferSection ="";
			
			if(inferred.inferredNodeSets != null)
			{
				for(var i=0; i<inferred.inferredNodeSets.length; i++)
				{
					var inferredNodeSet = inferred.inferredNodeSets[i];
					
					var inferredNodeSetTitle = "";
					if(inferredNodeSet.inferredNSCachedThumbURL != null && inferredNodeSet.inferredNSCachedThumbURL != "null")//Cached Thumbnail
						inferredNodeSetTitle += "<div class=\"inferredTitle\"> <a class=\"localViewTextLink\" href=\""+inferredNodeSet.inferredNSURI+"\"  onclick=\"currentLocalURI='"+inferredNodeSet.inferredNSURI+"'; setupLocalView(); return false;\" > <img class=\"localViewImage\" src=\""+inferredNodeSet.inferredNSCachedThumbURL+"\" /> </a> </div> <br\> <b> with the help of:</b>";
					else if(inferredNodeSet.inferredNSRawString != "hasURL?")//Embedded Raw String
						inferredNodeSetTitle += "<div class=\"inferredTitle\"> <a class=\"localViewTextLink\" href=\""+inferredNodeSet.inferredNSURI+"\"  onclick=\"currentLocalURI='"+inferredNodeSet.inferredNSURI+"'; setupLocalView(); return false;\" > "+inferredNodeSet.inferredNSRawString+" </a> </div> <br\> <b> with the help of:</b>";
					else//External URL
						inferredNodeSetTitle += "<div class=\"inferredTitle\"> <a class=\"localViewTextLink\" href=\""+inferredNodeSet.inferredNSURI+"\"  onclick=\"currentLocalURI='"+inferredNodeSet.inferredNSURI+"'; setupLocalView(); return false;\" > "+inferredNodeSet.inferredNSConclusionURL+" </a> </div> <br\> <b> with the help of:</b>";
					
					var siblingsHtml = "<div class=\"inferenceSiblings\"> <ol>";
					
					if(inferredNodeSet.siblings != null)
					{
						for(var i=0; i<inferredNodeSet.siblings.length; i++)
						{
							var sibling = inferredNodeSet.siblings[i];
							
							if(sibling.siblingURI != currentLocalURI)
							{
								if(sibling.siblingCachedThumbURL != null)//Cached Thumbnail
									siblingsHtml += "<li><div class=\"inferredBySibling\">  <a class=\"localViewTextLink\" href=\" "+sibling.siblingURI+" \" onclick=\"currentLocalURI='"+sibling.siblingURI+"'; setupLocalView(); return false;\" > <img class=\"localViewImage\" src=\""+sibling.siblingCachedThumbURL+"\" /> </a> </div></li>";
								else if(sibling.siblingRawString != "hasURL?")//Embedded Raw String
									siblingsHtml += "<li><div class=\"inferredBySibling\">  <a class=\"localViewTextLink\" href=\" "+sibling.siblingURI+" \" onclick=\"currentLocalURI='"+sibling.siblingURI+"'; setupLocalView(); return false;\" >"+sibling.siblingRawString+" </a> </div></li>";
								else//External URL
									siblingsHtml += "<li><div class=\"inferredBySibling\">  <a class=\"localViewTextLink\" href=\" "+sibling.siblingURI+" \" onclick=\"currentLocalURI='"+sibling.siblingURI+"'; setupLocalView(); return false;\" >"+sibling.siblingConclusionURL+" </a> </div></li>";
							}
						}
						siblingsHtml += " </ol></div> ";
					}
					else
						siblingsHtml = "<div class=\"inferenceSiblings\"> <b>No siblings used to help infer Node</b> </div>";
					
					usedToInferSection += "<div class=\"inferredNodeSet\"> "+inferredNodeSetTitle + siblingsHtml+"</div>";
				}
			}
			else
				usedToInferSection = "<div class=\"usedToInferSection\"> No Inferred Nodesets </div>";
			
			$("#LocalViewInferred").html(""+usedToInferSection);
			
			
			
			
		//Final Conclusion Section
			
			var finalConclusionSection ="";
			
			var rootNSHtml = "";
			//if (!isRoot) 
			if(!isFinalConc)
			{
				rootNSHtml = "<div class=\"rootNSTitle\"> Final Conclusion: </div>"
				
				if(finalConc.rootCachedThumbURL != null && finalConc.rootCachedThumbURL != "null")//Cached Thumbnail
					rootNSHtml += "<div class=\"rootNS\">  <a class=\"localViewTextLink\" href=\""+finalConc.rootURI+"\"  onclick=\"currentLocalURI='"+finalConc.rootURI+"'; setupLocalView(); return false;\" > <img class=\"localViewImage\" src=\""+finalConc.rootCachedThumbURL+"\" /> </a> </div>";
				else if(finalConc.rootRawString != "hasURL?")//Embedded Raw String
					rootNSHtml += "<div class=\"rootNS\">  <a class=\"localViewTextLink\" href=\""+finalConc.rootURI+"\"  onclick=\"currentLocalURI='"+finalConc.rootURI+"'; setupLocalView(); return false;\" > "+finalConc.rootRawString+" </a> </div>";
				else//External URL
					rootNSHtml += "<div class=\"rootNS\">  <a class=\"localViewTextLink\" href=\""+finalConc.rootURI+"\"  onclick=\"currentLocalURI='"+finalConc.rootURI+"'; setupLocalView(); return false;\" > "+finalConc.rootConclusionURL+" </a> </div>";
			}
			
			
			var queryStringHtml = "<div class=\"finalQueryTitle\"> That Answers the Query: </div>";
			
			if(finalConc.queryString != null)
			{
				queryStringHtml += "<div class=\"finalQueryString\"> <pre> "+finalConc.queryString+" </pre> </div>";
			}
			else
				queryStringHtml += "<div class=\"finalQueryString\"> No Query String found in PML </div>";
			
			
			var questionsHtml = "<div class=\"finalQuestions\"> <div class=\"finalQuestionLeadIn\">That is a formal representation of: </div> <ol>";
			
			if(finalConc.questionStrings != null)
			{
				for(var i=0; i<finalConc.questionStrings.length; i++)
				{
					var question = finalConc.questionStrings[i];
					
					questionsHtml += "<li> <div class=\"finalQuestionText\"> <pre> "+question+" </pre> </div> </li>";
					
				}
				questionsHtml += " </ol></div> ";
			}
			else
				questionsHtml = "<div class=\"finalQuestions\"> <b>No question string found in PML</b> </div>";
			
			finalConclusionSection += "<div class=\"finalConclusion\"> "+rootNSHtml + queryStringHtml + questionsHtml +"</div>";
			
			
			$("#LocalViewFinalConclusion").html(""+finalConclusionSection);
			
			
			
			
		//Show Results	
			$("#tabs").tabs('enable', 2);
			$("#tabs").tabs("select",2);
			
			
			//reset
			if($( "#accordion" ).accordion( "option", "active") != "0" )
			{
				$( "#accordion" ).accordion( "option", "active", 0 );
			}
			else if( $( "#accordion" ).accordion( "option", "active")+"" == "false")
			{
				$( "#accordion" ).accordion( "option", "active", 0 );
			}
			
			
			endLoadingScreen();
			
		});
}
