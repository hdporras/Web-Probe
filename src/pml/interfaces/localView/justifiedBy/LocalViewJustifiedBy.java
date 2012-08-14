package pml.interfaces.localView.justifiedBy;

import java.util.ArrayList;
import java.util.List;

import org.inference_web.iw.pml.pmlj.IWInferenceStepOccur;
import org.inference_web.iw.pml.pmlj.IWNodeSetOccur;


public class LocalViewJustifiedBy 
{
	
	ArrayList<WebProbeInferenceStep> inferenceSteps;

	
	public LocalViewJustifiedBy(IWNodeSetOccur ns)
	{
		inferenceSteps = new ArrayList<WebProbeInferenceStep>();
		//IWNodeSetOccur ns = IWPMLObjectManager.getProofOccurrence(URI);
		
		setNodeSetInferenceSteps(ns);
	}
	
	
	/** Converts the object into a JSON String representation of the object. */
	public String convertToJSON()
	{
		String json = "";

		//Inference Steps
		String jsonArray = "[ ";

		int i;
		for(i=0; i < inferenceSteps.size() -1 ; i++)
		{
			jsonArray += inferenceSteps.get(i).convertToJSON() +", ";
		}
		//insert last element and close the array.
		jsonArray += inferenceSteps.get(i).convertToJSON() +" ]";
		
		
		json += " \"inferenceSteps\" : "+ jsonArray;

		return "{ "+ json +" }";
	}
	
	
	
	public void setNodeSetInferenceSteps(IWNodeSetOccur ns)
	{
		
		List<IWInferenceStepOccur> infStepsList = null;


		if (ns != null )
		{
			infStepsList = ns.getInferenceStepOccurrences();
			
			if (infStepsList != null && infStepsList.size()>0)
			{
				//String title =  getInferenceTitle((IWInferenceStepOccur)infStepValue.get(0))+ " endISTitle ))) ";
				//***Multiple inference Steps
				int size = infStepsList.size();
					
				for (int i=0; i<size; i++)
				{
					IWInferenceStepOccur step = (IWInferenceStepOccur)infStepsList.get(i);
					inferenceSteps.add(i, getOneInferenceStep(ns, step) );
				}
			}
		}
	}
	
	
	public WebProbeInferenceStep getOneInferenceStep(IWNodeSetOccur ns, IWInferenceStepOccur step)
	{
		
		WebProbeInferenceStep is = new WebProbeInferenceStep();
		
		is.setInferenceStepInformation(step);

		//String metadata = is.getISMetadata(step);
		//String assertions = is.getAssertions(ns);

		return is;
	}
	
}
