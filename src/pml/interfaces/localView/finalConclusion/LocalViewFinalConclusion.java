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

package pml.interfaces.localView.finalConclusion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.inference_web.iw.app.util.rendering.SentenceRenderer;
import org.inference_web.iw.pml.pmlj.IWInferenceStepOccur;
import org.inference_web.iw.pml.pmlj.IWNodeSetOccur;
import org.inference_web.pml.v2.pmlj.IWNodeSet;
import org.inference_web.pml.v2.pmlj.IWQuery;
import org.inference_web.pml.v2.pmlj.IWQuestion;

import pml.PMLNode;
import pml.interfaces.localView.usedToInfer.WebProbeNSInferenceInfo;
import pml.loading.Loader;
import util.JSONUtils;

public class LocalViewFinalConclusion
{
	String rootURI;
	String rootRawString;
	String rootConclusionURL;
	String rootCachedThumbURL;
	
	String queryString;
	String[] questionStrings;
	
	boolean isRoot;
	
	
	
	/** Converts the object into a JSON String representation of the object. */
	public String convertToJSON()
	{
		String json = "";
		
		//Final Conclusion NodeSet info
		if(!isRoot)
		{
			json += " \"rootURI\" : \""+ rootURI +"\", " +
				" \"rootRawString\" : \""+ rootRawString +"\", " +
				" \"rootConclusionURL\" : \""+ rootConclusionURL +"\", " +
				" \"rootCachedThumbURL\" : \""+ rootCachedThumbURL +"\", " +
				" \"isRoot\" : "+ isRoot +", ";
		}
		else
			json += " \"isRoot\" : "+ isRoot +", ";
		
		//Query String
		if(queryString != null)
			json +=	" \"queryString\" : \""+ queryString +"\", ";
		else
			json +=	" \"queryString\" : null , ";

		//Siblings info
		if(questionStrings != null)
		{

			String jsonArray = " [ ";

			int i;
			for(i=0; i < questionStrings.length -1 ; i++)
			{
				String currQuestionString = JSONUtils.toValidJSONString(questionStrings[i]);
				jsonArray += " \""+ currQuestionString +"\" , ";
			}

			String currQuestionString = JSONUtils.toValidJSONString(questionStrings[i]);

			//insert last element and close the array.
			jsonArray += " \""+ currQuestionString +"\" ]";

			json += " \"questionStrings\" : "+ jsonArray;//+", ";

		}
		else 
			json += " \"questionStrings\" : null";//+", ";

		return "{ "+ json +" }";
	}
	
	
	
	public LocalViewFinalConclusion(IWNodeSetOccur ns, IWNodeSetOccur root, IWQuery query, List questions, boolean isRoot)
	{
		setFinalConclusion(ns, root, query, questions, isRoot);
		this.isRoot = isRoot;
	}


	private void setFinalConclusion (IWNodeSetOccur ns, IWNodeSetOccur root, IWQuery query, List questions, boolean isRoot)
	{
		List questionStrs = null;
		IWQuestion question = null;

		if (ns != null) 
		{

			if (!isRoot) 
			{
				if (root!=null) 
				{
					setRootNSConclusionStringForLocalView(root);
				}
			} // end not root

			if (query != null)
			{
				//" that answers the query: "
				SentenceRenderer renderer = new SentenceRenderer(query, "English");
				queryString = renderer.getRawString();//render();
			}

			if (questions != null && questions.size()>0)
			{
				questionStrs = new ArrayList();
				Iterator questionsItr = questions.listIterator();

				while (questionsItr.hasNext())
				{
					question = (IWQuestion)questionsItr.next();
					String tempStr = question.getContentString();

					if (tempStr != null && tempStr.trim().length()>0)
						questionStrs.add(tempStr);
				}
				
				
				if (questionStrs != null && questionStrs.size()>0)
				{
					Iterator questionStrsItr = questionStrs.listIterator();

					questionStrings = new String[questionStrs.size()];

					//Iterate through questions..
					for (int qitCntr = 0; questionStrsItr.hasNext(); qitCntr++)
					{
						//" that is a formal representation of the question: "
						questionStrings[qitCntr] = (String)questionStrsItr.next();
					}
				}
			}
		}

	}//end setFinalConclusion()	

	
	/** Sets either the InferredNS's cachedThumbURL, RawString, or external URL in order to know what to display in the Local View.
	 *  Also, sets the URI of the NodeSet. 
	 * */ 
	public void setRootNSConclusionStringForLocalView(IWNodeSet root)
	{
		rootRawString = WebProbeNSInferenceInfo.getNodeSetRawString(root);
		rootURI = WebProbeNSInferenceInfo.getNodeSetURI(root);
		
		rootConclusionURL = "Other Solution";//default value unless last must be used as last resort.
		
		String thumbURL = WebProbeNSInferenceInfo.getNodeSetCachedVisURL(rootURI);

		//Thumbnail available in cache?
		if(thumbURL != null)
		{
			rootCachedThumbURL = thumbURL;
			rootRawString = "Image Used Instead";
			rootConclusionURL = "Image Used Instead";
		}
		else 
		{
			rootCachedThumbURL = null;
			String rootRaw = WebProbeNSInferenceInfo.getNodeSetRawString(root);
			
			if(rootRaw != null)
			{
				rootRawString = rootRaw;
				rootConclusionURL = "Embedded Raw String Used Instead";
			}
			else
			{//No Conclusion Embedded, try getting URL to Conclusion
				
				rootRawString = "hasURL?";
				
				Loader loader = new Loader(false);
				PMLNode pmlNode = loader.loadNode(rootURI, null);
				rootConclusionURL = pmlNode.getConclusion().getHasURL();
				
				//GetURLContents.downloadText(conclusion.getHasURL());
			}
		}
	}
	
}
