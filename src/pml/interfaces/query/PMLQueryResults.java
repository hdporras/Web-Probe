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

package pml.interfaces.query;

/*import pml.PMLQuery;
import pml.loading.Loader;*/
import java.util.List;

import org.inference_web.pml.v2.pmlj.IWQuery;
import org.inference_web.pml.v2.pmlj.IWQuestion;
import org.inference_web.pml.v2.util.PMLObjectManager;

import pml.loading.Loader;
import util.JSONUtils;

public class PMLQueryResults//PMLQueryDWRInterface-- Acts as an interface between PML IWQuery and DWR for Web-Probe.
{
	
/*	public String getQueryContent(String uri)
	{
		PMLQuery query = loadQuery(uri);
		return getQueryContent(query);
	}
	
	private String getQueryContent(PMLQuery query)
	{
		return query.getContent();
	}
*/
	
	private String[] queryQuestions;
	private String rawString = "";
	private String prettyString = "";
	
	
	
	public String getQueryContent(String uri)
	{
		IWQuery query = loadQuery(uri);
		getQueryContent(query);
		
		return convertToJSON();
	}
	
	private void getQueryContent(IWQuery query)
	{
		List<IWQuestion> questions = query.getIsQueryFor();
		
		if(questions != null)
		{
			queryQuestions = new String[questions.size()];
			
			for(int i=0; i < questions.size(); i++)
			{
				IWQuestion question = questions.get(i);
				//String rawString = question.getContentRawString();
				String prettyString = question.getContentString();//if there is no prettyString this will be same as rawString
				
				queryQuestions[i] = prettyString;
			}
		}
		
		rawString = query.getHasContent().getHasRawString();
		prettyString = query.getHasContent().getHasPrettyString();
	}
	
	/** Converts the object into a JSON String representation of the object. */
	private String convertToJSON()
	{
		String json = "";

		//Raw and pretty string
		json = 	" \"rawString\" : \""+ rawString +"\", " +
				" \"prettyString\" : \""+ prettyString +"\" ";

		//Questions
		if(queryQuestions != null)
		{

			String jsonArray = " [ ";

			int i;
			for(i=0; i < queryQuestions.length -1 ; i++)
			{
				String currQueryQuestion = JSONUtils.toValidJSONString(queryQuestions[i]);
				jsonArray += " \""+ currQueryQuestion +"\" ,";
			}

			String currQueryQuestion = JSONUtils.toValidJSONString(queryQuestions[i]);
			//insert last element and close the array.
			jsonArray += " \""+ currQueryQuestion +"\" ]";


			json += ", \"queryQuestions\" : "+ jsonArray;//+", ";

		}
		else 
			json += ", \"queryQuestions\" : null";//+", ";


		return "{ "+ json +" }";
	}
	
	
	/** Loads Query from URI, and returns the loaded IWQuery Object. */
	private IWQuery loadQuery(String uri)
	{
		IWQuery query;
		Loader loader = new Loader(false);

		if (loader.isQuery(uri))
		{				
			query = PMLObjectManager.getQuery(uri);
			return query;
		}
		else
		{
			System.out.println("Error reading Query from: " + uri);
		}

		return null;
	}
	
	
	public static void main(String[] args)
	{
		String uri = "http://inference-web.org/proofs/tonys/tonys.owl#tonys";
		
		PMLQueryResults query = new PMLQueryResults();
		String json = query.getQueryContent(uri);
		
		System.out.println(json);
		
	}
}
