/**
 * @author Hugo Porras
 * 
 * Acknowledgements:
 * This work used resources from Cyber-ShARE Center of Excellence, 
 * which is supported by National Science Foundation grant number 
 * HRD-0734825. Unless otherwise stated, work by Cyber-ShARE is 
 * licensed under a Creative Commons Attribution 3.0 Unported 
 * License. Permissions beyond the scope of this license may be 
 * available at 
 * http://cybershare.utep.edu/content/cyber-share-acknowledgment.
 */

package test;

import java.net.URL;

import pml.PMLNode;
import pml.PMLQuery;
import pml.impl.serializable.PMLConclusion;
import pml.loading.Loader;
import pml.util.pml.PMLSourceRetrieval;

public class PMLTests
{
	PMLNode _node;
	PMLQuery _query;
	String _URI;

	public String[] getStringArray(String _uri)
	{
		_URI = _uri;

		Loader loader = new Loader(false);

		if (loader.isQuery(_URI))
		{				

			_query = loader.loadQuery(_URI);
			return _query.getAnswerURIs();
		}
		else
		{
			System.out.println("Error reading " + _URI);
		}
		return null;
	}


	/** Returns a JSON array of objects containing query answer information. */
	public String getQueryAnswers(String uri)
	{
		_URI = uri;

		Loader loader = new Loader(false);

		if (loader.isQuery(_URI))
		{				

			_query = loader.loadQuery(_URI);
			String[] queryAnswers = _query.getAnswerURIs();
			
			//String[] answers = new String[query.length];
			String jsAnswers="["; 
			
			
			for(int i=0; i<queryAnswers.length; i++)
			{
				String result = "";
				
				if(loader.isNode(queryAnswers[i]))
				{
					PMLNode node = loader.loadNode(queryAnswers[i], i+"");
					
					//PMLConclusion conc = node.getConclusion();
					
					//String sources = buildSources(node);
					//result = sources+"\n -> "+ getMetadata(node);
					String conclusion = getConclusion(node);
					result = "{\"conclusion\":\""+conclusion+"\", \"metadata\":\""+ getMetadata(node)+"\", \"uri\":\""+ queryAnswers[i]+"\"}";
					
					/*
					String name = conc.getQualifiedName();
					String conclusion = conc.getStringConclusion();
					String concURI = conc.getURI();
					
					if(conclusion == null)
					{
						String concContentURL = conc.getHasURL();
						result = name+": "+concContentURL+"  ("+concURI+")";
					}
					else
					{
						result = name+": "+conclusion+"  ("+concURI+")";
					}
					*/
				}
				else
					result = "Result Not a Node ";
				
				if(queryAnswers.length-1 == i)
					jsAnswers += result;
				else
					jsAnswers += result+",";
			}
			
			return jsAnswers+"]";
		}
		else
		{
			System.out.println("Error reading " + _URI);
		}

		return null;
	}

	
	private String getMetadata(PMLNode answerNode)
	{
		String dateText = this.getDateText(answerNode);
		String urlText = this.getURLText(answerNode);
		String fromEngineText = this.getFromEngineText(answerNode);
		
		String metadata = "";
		if(dateText != null)
			metadata += "Date: " + dateText + "<br>";
		if(urlText != null)
			metadata += "Hosted at: " + urlText + "<br>";
		if(fromEngineText != null)
			metadata += "Created by: " + fromEngineText;
		
		return metadata;
	}
	
	private String getDateText(PMLNode answerNode)
	{
		String dateText = null;
		if(answerNode.getDate() != null)
			dateText = answerNode.getDate();
		return dateText;
	}
	
	private String getURLText(PMLNode answerNode)
	{
		String urlText = null;
		
		try
		{
			if(answerNode.getConclusion().getHasURL().equalsIgnoreCase("NO URL OF CONCLUSION ENCODED IN PML"))
				urlText = new URL(answerNode.getURI()).getHost() + " [embedded]";
			else
				urlText = new URL(answerNode.getConclusion().getHasURL()).getHost();
			
			return urlText;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	private String getFromEngineText(PMLNode answerNode)
	{
		String engineText = null;
		if(answerNode.getCurrentlySelectedInferenceStep().getIEName() != null)
			engineText =  answerNode.getCurrentlySelectedInferenceStep().getIEName();
		return engineText;
	}
	
	private String buildSources(PMLNode answerNode)
	{
		PMLNode justification;
		Loader loader = new Loader(false);
		justification = loader.loadJustification(answerNode.getURI(), answerNode.getID());
		
		String sources = PMLSourceRetrieval.getFirstSources(justification);
		return sources;
	}
	
	private String getConclusion(PMLNode answerNode)
	{
		PMLConclusion conc = answerNode.getConclusion();
		
		String conclusion = conc.getStringConclusion();
		
		if(conclusion == null || conclusion.equalsIgnoreCase("NO CONCLUSION EMBEDDED IN PML"))
		{
			String concContentURL = conc.getHasURL();
			return concContentURL;
			
		}

		return conclusion;
	}
	
	
	
	
	public String getTestAnswers(String uri)
	{
		_URI = uri;
		Loader loader = new Loader(false);

		if (loader.isQuery(_URI))
		{				

			_query = loader.loadQuery(_URI);
			String[] query = _query.getAnswerURIs();
			
			//String[] answers = new String[query.length];
			String answers="["; 
			
			
			for(int i=0; i<query.length; i++)
			{
				String result = "";

				result = "{\"conclusion\":\""+i+"\", \"metadata\":\""+i+"\"}";

				
				if(query.length-1 == i)
					answers += result;
				else
					answers += result+",";
			}
			
			return answers+"]";
		}
		else
		{
			System.out.println("Error reading " + _URI);
		}

		return null;
	}
	

	public static void main(String[] args)
	{
		PMLTests test = new PMLTests();
		String[] array = test.getStringArray("http://inference-web.org/proofs/tonys/tonys.owl#tonys");

		System.out.println("URIS: ");
		for(int i=0; i<array.length; i++)
			System.out.println("- "+array[i]);
/*
		String[] answers = test.getQueryAnswers("http://inference-web.org/proofs/tonys/tonys.owl#tonys");

		System.out.println();
		System.out.println("Answer Contents: ");
		for(int i=0; i<answers.length; i++)
			System.out.println("- "+answers[i]);*/
	}
}
