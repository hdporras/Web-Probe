package pml.interfaces.localView;

import java.util.List;

import org.inference_web.iw.pml.pmlj.IWNodeSetOccur;
import org.inference_web.iw.pml.util.IWPMLObjectManager;
import org.inference_web.pml.v2.pmlj.IWInferenceStep;
import org.inference_web.pml.v2.pmlj.IWQuery;
import org.inference_web.pml.v2.pmlj.IWQuestion;

import pml.interfaces.localView.conclusion.LocalViewConclusion;
import pml.interfaces.localView.finalConclusion.LocalViewFinalConclusion;
import pml.interfaces.localView.justifiedBy.LocalViewJustifiedBy;
import pml.interfaces.localView.usedToInfer.LocalViewUsedToInfer;

public class NodeSetDetails
{
	private IWNodeSetOccur ns = null;
	private IWNodeSetOccur rootNS = null;
	private IWQuery query = null;
	private List<IWQuestion> questions = null;
	private IWInferenceStep infStep = null;
	private List isAntecedentOf = null;
	private boolean isRoot = false;
	
	
	LocalViewConclusion conclusion;
	LocalViewJustifiedBy just;
	LocalViewUsedToInfer infer;
	LocalViewFinalConclusion finalConclusion;

	
	public void buildLocalView(String URI)
	{
		setupNodeSet(URI);
		
		conclusion = new LocalViewConclusion(URI);
		just = new LocalViewJustifiedBy(ns);
		infer = new LocalViewUsedToInfer(ns, isAntecedentOf, isRoot);
		finalConclusion = new LocalViewFinalConclusion(ns, rootNS, query, questions, isRoot);

	}


	public String convertToJSON()
	{

		String jsonConclusion = "\"conclusion\" : " + conclusion.convertToJSON();
		String jsonJust = "\"justifiedBy\" : "+ just.convertToJSON();
		String jsonInfer = "\"usedToInfer\" : "+ infer.convertToJSON();
		String jsonFinalConclusion = "\"finalConclusion\" : "+ finalConclusion.convertToJSON();

		String jsonDetails = "{"+jsonConclusion +"" +
				","+ jsonJust + "" +
				","+ jsonInfer + "" +
				","+ jsonFinalConclusion + "" +
				"}";

		System.out.println("Final JSON: "+ jsonDetails);
		
		return jsonDetails;
	}




	public void setupNodeSet(String uriStr)
	{
		if (uriStr != null)
		{   
			// initial loading to check root ns, query and question
			ns = IWPMLObjectManager.loadNodeSetOccurrence(uriStr, 2);
			
			if (ns != null)
			{
				infStep = ns.getSelectedInferenceStepOccurrence();
				if (infStep != null)
				{    			
					if (infStep.getFromAnswer()!= null && infStep.getFromAnswer().getIdentifier() != null
							&& infStep.getFromAnswer().getIdentifier().getURIString()!=null)
					{
						//System.out.println("got from answer, not root -- reload "+infStep.getFromAnswer().getIdentifier().getURIString());
						// full loading
						rootNS = IWPMLObjectManager.getProofOccurrence(infStep.getFromAnswer().getIdentifier().getURIString());
						ns = IWPMLObjectManager.getProofOccurrence(uriStr);

						if (ns != null)
						{
							infStep = ns.getSelectedInferenceStepOccurrence();
							isAntecedentOf = ns.getIsAntecedentOf();
							if (isAntecedentOf.size()<1) isAntecedentOf = null;
						}
						if (rootNS != null)
						{
							query = rootNS.getSelectedInferenceStepOccurrence().getFromQuery();

							if (query != null)
							{
								questions = query.getIsQueryFor();
							}
						}
					} 
					else if (infStep.getFromQuery()!= null && infStep.getFromQuery().getIdentifier() != null
							&& infStep.getFromQuery().getIdentifier().getURIString()!=null)
					{
						// full loading
						ns = IWPMLObjectManager.getProofOccurrence(uriStr); 
						isRoot = true;
						rootNS = ns;

						if (rootNS != null)
						{
							query = rootNS.getSelectedInferenceStepOccurrence().getFromQuery();

							if (query != null)
							{
								questions = query.getIsQueryFor();
							}
						}
					}
					else 
					{
						// full loading
						ns = IWPMLObjectManager.getProofOccurrence(uriStr); 
					}
				}
			}
		}
		
	}
	
	public IWNodeSetOccur getIWNS()
	{
		return ns;
	}
	
}
