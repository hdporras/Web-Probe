package pml.interfaces.localView.usedToInfer;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.inference_web.iw.app.util.rendering.SentenceRenderer;
import org.inference_web.iw.pml.pmlj.IWInferenceStepOccur;
import org.inference_web.iw.pml.pmlj.IWNodeSetOccur;
import org.inference_web.pml.v2.pmlj.IWNodeSet;

import pml.PMLNode;
import pml.loading.Loader;

import cache.visAccess.VisualizationCacheAccess;

import util.JSONUtils;

public class WebProbeNSInferenceInfo
{
	
	String inferredNSRawString;
	String inferredNSURI;
	String inferredNSCachedThumbURL;
	String inferredNSConclusionURL;
	
	String[] siblingRawStrings;
	String[] siblingURIs;
	String[] siblingCachedThumbURL;
	String[] siblingConclusionURL;
	
	
	
	/** Converts the object into a JSON String representation of the object. */
	public String convertToJSON()
	{
		String json = "";

		//Inferred NodeSet info
		json = 	" \"inferredNSRawString\" : \""+ inferredNSRawString +"\", " +
				" \"inferredNSURI\" : \""+ inferredNSURI +"\", " +
				" \"inferredNSCachedThumbURL\" : \""+ inferredNSCachedThumbURL +"\", " +
				" \"inferredNSConclusionURL\" : \""+ inferredNSConclusionURL +"\" ";

		//Siblings info
		if(siblingRawStrings != null)
		{

			String jsonArray = " [ ";

			int i;
			for(i=0; i < siblingRawStrings.length -1 ; i++)
			{
				String currSiblingRawString = JSONUtils.toValidJSONString(siblingRawStrings[i]);
				jsonArray += "{ \"siblingRawString\" : \""+ currSiblingRawString +"\" , \"siblingURI\" : \""+ siblingURIs[i] +"\" ";
				if(siblingCachedThumbURL[i] != null)
					jsonArray +=", \"siblingCachedThumbURL\" : \""+siblingCachedThumbURL[i]+"\" , \"siblingConclusionURL\" : \""+siblingConclusionURL[i]+"\"  }, ";
				else
					jsonArray +=", \"siblingCachedThumbURL\" : null , \"siblingConclusionURL\" : \""+siblingConclusionURL[i]+"\"  }, ";
			}

			String currSiblingRawString = JSONUtils.toValidJSONString(siblingRawStrings[i]);
			
			//insert last element and close the array.
			jsonArray += "{ \"siblingRawString\" : \""+ currSiblingRawString +"\" , \"siblingURI\" : \""+ siblingURIs[i] +"\" ";
			if(siblingCachedThumbURL[i] != null)
				jsonArray +=", \"siblingCachedThumbURL\" : \""+siblingCachedThumbURL[i]+"\" , \"siblingConclusionURL\" : \""+siblingConclusionURL[i]+"\"  }  ]";
			else
				jsonArray +=", \"siblingCachedThumbURL\" : null , \"siblingConclusionURL\" : \""+siblingConclusionURL[i]+"\"  }  ]";


			json += ", \"siblings\" : "+ jsonArray;//+", ";

		}
		else 
			json += ", \"siblings\" : null";//+", ";

		return "{ "+ json +" }";
	}
	
	
	
	/** Sets all of the information needed about what was used to Infer the NodeSet */
	public WebProbeNSInferenceInfo (IWNodeSet inferredNodeSet, IWInferenceStepOccur consequenceInfStep, IWNodeSetOccur ns) 
	{
		
		if (inferredNodeSet!=null ) 
		{
			// " UsedtoInfer: ";
			setInferredNSConclusionStringForLocalView(inferredNodeSet);
			
			//" with help of: ";
			setSiblings(consequenceInfStep, ns);

		}
	}
	
	
	/** Sets either the InferredNS's cachedThumbURL, RawString, or external URL in order to know what to display in the Local View.
	 *  Also, sets the URI of the NodeSet. 
	 * */ 
	public void setInferredNSConclusionStringForLocalView(IWNodeSet inferredNodeSet)
	{
		String uriStr = getNodeSetURI(inferredNodeSet);
		inferredNSURI = uriStr;
		inferredNSConclusionURL = "Other Solution";//default value unless last must be used as last resort.
		
		String thumbURL = getNodeSetCachedVisURL(uriStr);

		//Thumbnail available in cache?
		if(thumbURL != null)
		{
			inferredNSCachedThumbURL = thumbURL;
			inferredNSRawString = "Image Used Instead";
			inferredNSConclusionURL = "Image Used Instead";
		}
		else 
		{
			siblingCachedThumbURL = null;
			String inferredNSRaw = getNodeSetRawString(inferredNodeSet);
			
			if(inferredNSRaw != null)
			{
				inferredNSRawString = inferredNSRaw;
				inferredNSConclusionURL = "Embedded Raw String Used Instead";
			}
			else
			{//No Conclusion Embedded, try getting URL to Conclusion
				
				inferredNSRawString = "hasURL?";
				
				Loader loader = new Loader(false);
				PMLNode pmlNode = loader.loadNode(uriStr, null);
				inferredNSConclusionURL = pmlNode.getConclusion().getHasURL();
				
				//GetURLContents.downloadText(conclusion.getHasURL());
			}
		}
	}
	
	
	/** Sets the information needed for each sibling that helped to infer the NodeSet */ 
	public void setSiblings (IWInferenceStepOccur parentInfStep, IWNodeSetOccur ns)
	{
		List siblings = null;

		if (parentInfStep != null && ns != null)
		{
			siblings = parentInfStep.getAntecedentNodeSetOccurrences();
			
			if (siblings != null && siblings.size()>1)
			{
				siblingURIs = new String[siblings.size()];
				siblingRawStrings = new String[siblings.size()];
				siblingConclusionURL = new String[siblings.size()];
				siblingCachedThumbURL = new String[siblings.size()];
				
				Iterator siblingsItr = siblings.listIterator();
				
				int siblingCounter = 0;
				
				//iterate through siblings
				while (siblingsItr.hasNext())
				{
					IWNodeSetOccur siblingNS = (IWNodeSetOccur)siblingsItr.next();
					
					setSiblingConclusionStringForLocalView(siblingNS, siblingCounter);
					
					siblingCounter++;
					
				}//End iterate through siblings
				
			}  		
		}
	}
	
	/** Sets either the Sibling cachedThumbURL, RawString, or external URL in order to know what to display in the Local View*/ 
	public void setSiblingConclusionStringForLocalView(IWNodeSet siblingIWNodeSet, int siblingIndex)
	{
		String uriStr = getNodeSetURI(siblingIWNodeSet);
		siblingURIs[siblingIndex] = uriStr;
		siblingConclusionURL[siblingIndex] = "Other Solution";//default value unless last must be used as last resort.
		
		String thumbURL = getNodeSetCachedVisURL(uriStr);

		//Thumbnail available in cache?
		if(thumbURL != null)
		{
			siblingCachedThumbURL[siblingIndex] = thumbURL;
			siblingRawStrings[siblingIndex] = "Image Used Instead";
			siblingConclusionURL[siblingIndex] = "Image Used Instead";
		}
		else 
		{
			siblingCachedThumbURL[siblingIndex] = null;
			String siblingRaw = getNodeSetRawString(siblingIWNodeSet);
			
			if(siblingRaw != null)
			{
				siblingRawStrings[siblingIndex] = siblingRaw;
				siblingConclusionURL[siblingIndex] = "Embedded Raw String Used Instead";
			}
			else
			{//No Conclusion Embedded, try getting URL to Conclusion
				
				siblingRawStrings[siblingIndex] = "hasURL?";
				
				Loader loader = new Loader(false);
				PMLNode pmlNode = loader.loadNode(uriStr, null);
				siblingConclusionURL[siblingIndex] = pmlNode.getConclusion().getHasURL();
				
				//GetURLContents.downloadText(conclusion.getHasURL());
			}
		}
	}
	
	
	
	/** returns the rawstring of the nodeset. Null if IWNodeSet is null */
	public static String getNodeSetRawString(IWNodeSet ns)
	{
		if (ns != null)
		{
			SentenceRenderer renderer = new SentenceRenderer(ns, "English");
			String rawStr = renderer.getRawString();

			System.out.println("RENDERED Raw String:  "+rawStr);

			return rawStr;
		}

		return null;
	}
	
	/** Returns the URI of the IWNodeSet. Null if IWNodeSet is null */ 
	public static String getNodeSetURI(IWNodeSet ns)
	{
		if (ns != null)
			return ns.getIdentifier().getURIString();
		else
			return null;
	}
	
	/** returns the NodeSet's cached Thumbnail URI if there is one. Otherwise Null is returned. */ 
	public static String getNodeSetCachedVisURL(String uriStr)
	{
		String thumbURL = VisualizationCacheAccess.getCachedThumbnail(uriStr);

		return thumbURL;
	}
	
}
