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

package pml.interfaces.localView.usedToInfer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.inference_web.iw.app.util.rendering.SentenceRenderer;
import org.inference_web.iw.pml.pmlj.IWInferenceStepOccur;
import org.inference_web.iw.pml.pmlj.IWNodeSetOccur;
import org.inference_web.pml.v2.pmlj.IWNodeSet;

import pml.interfaces.localView.justifiedBy.WebProbeInferenceStep;

public class LocalViewUsedToInfer
{

	ArrayList<WebProbeNSInferenceInfo> inferredNodeSets;



	/** Converts the object into a JSON String representation of the object. */
	public String convertToJSON()
	{
		String json = "";

		if(inferredNodeSets.size() > 0)
		{
			//Inference Steps
			String jsonArray = "[ ";

			int i=0;
			for(i=0; i < inferredNodeSets.size() -1 ; i++)
			{
				jsonArray += inferredNodeSets.get(i).convertToJSON() +", ";
			}
			//insert last element and close the array.
			jsonArray += inferredNodeSets.get(i).convertToJSON() +" ]";


			json += " \"inferredNodeSets\" : "+ jsonArray;

		}
		else
			json += " \"inferredNodeSets\" : null ";

		return "{ "+ json +" }";
	}



	public LocalViewUsedToInfer(IWNodeSetOccur ns, List isAntecedentOf, boolean isRoot)
	{
		inferredNodeSets = new ArrayList<WebProbeNSInferenceInfo>();

		setUsedToInfer(ns, isAntecedentOf, isRoot);
	}


	private void setUsedToInfer(IWNodeSetOccur ns, List isAntecedentOf, boolean isRoot) 
	{
		if (ns != null)
		{
			if (!isRoot)
			{
				if (isAntecedentOf != null && isAntecedentOf.size()>0)
				{
					// prepare consequences
					Map consequence = null;
					IWNodeSetOccur consequenceNS;
					IWInferenceStepOccur consequenceInfStep;

					//iterate through InferredNodeSets
					int numOfInferredNSs = isAntecedentOf.size();
					for (int i=0; i<numOfInferredNSs; i++ ) 
					{
						consequence = (Map)isAntecedentOf.get(i);
						consequenceNS = (IWNodeSetOccur)consequence.keySet().iterator().next();
						consequenceInfStep = (IWInferenceStepOccur)consequence.get(consequenceNS);
						IWNodeSetOccur inferredNodeSet = consequenceNS;

						WebProbeNSInferenceInfo webProbeInfNS = new WebProbeNSInferenceInfo(inferredNodeSet, consequenceInfStep, ns);
						inferredNodeSets.add(webProbeInfNS);
					}

				} 
			} // end not root
		}

	}//end setUsedToInfer()


}
