package pml.interfaces.localView.justifiedBy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.inference_web.iw.app.util.rendering.SentenceRenderer;
import org.inference_web.iw.pml.pmlj.IWInferenceStepOccur;
import org.inference_web.iw.pml.util.AxiomHelper;
import org.inference_web.pml.v2.PMLObject;
import org.inference_web.pml.v2.pmlj.IWInferenceStep;
import org.inference_web.pml.v2.pmlj.IWNodeSet;
import org.inference_web.pml.v2.pmlp.IWIdentifiedThing;
import org.inference_web.pml.v2.pmlp.IWInferenceRule;
import org.inference_web.pml.v2.pmlp.IWSource;
import org.inference_web.pml.v2.pmlp.IWSourceUsage;
import org.inference_web.pml.v2.util.PMLObjectManager;
import org.inference_web.pml.v2.vocabulary.PMLJ;
import org.inference_web.shared.ToolURI;

import pml.PMLNode;
import pml.loading.Loader;

import cache.visAccess.VisualizationCacheAccess;

import util.JSONUtils;

public class WebProbeInferenceStep
{
	String inferenceEngine;
	String infEngURI;
	String declarativeRule;
	String decRuleURI;
	String[] antecedentRawStrings;
	public String[] antecedentURIs;
	String[] antecedentCachedThumbURL;
	String[] antecedentConclusionURL;
	String metadata;
	String[] assertions;
	String[] assertionSources;

	
	/** Converts the object into a JSON String representation of the object. */
	public String convertToJSON()
	{
		String json = "";

		//Engine and Rule info
		json = 	" \"infEngine\" : \""+ inferenceEngine +"\", " +
				" \"infEngURI\" : \""+ infEngURI +"\", " +
				" \"declRule\" : \""+ declarativeRule +"\", " +
				" \"declRuleURI\" : \""+ decRuleURI +"\" ";

		//Antecedent info
		if(antecedentRawStrings != null)
		{

			String jsonArray = " [ ";

			int i;
			for(i=0; i < antecedentRawStrings.length -1 ; i++)
			{
				String currAntecedentRawString = JSONUtils.toValidJSONString(antecedentRawStrings[i]);
				jsonArray += "{ \"antecedentRawString\" : \""+ currAntecedentRawString +"\" , \"antecedentURI\" : \""+ antecedentURIs[i] +"\" ";
				if(antecedentCachedThumbURL[i] != null)
					jsonArray +=", \"antecedentCachedThumbURL\" : \""+antecedentCachedThumbURL[i]+"\" , \"antecedentConclusionURL\" : \""+antecedentConclusionURL[i]+"\"  }, ";
				else
					jsonArray +=", \"antecedentCachedThumbURL\" : null , \"antecedentConclusionURL\" : \""+antecedentConclusionURL[i]+"\"  }, ";
			}

			String currAntecedentRawString = JSONUtils.toValidJSONString(antecedentRawStrings[i]);
			//insert last element and close the array.
			jsonArray += "{ \"antecedentRawString\" : \""+ currAntecedentRawString +"\" , \"antecedentURI\" : \""+ antecedentURIs[i] +"\" ";
			if(antecedentCachedThumbURL[i] != null)
				jsonArray +=", \"antecedentCachedThumbURL\" : \""+antecedentCachedThumbURL[i]+"\" , \"antecedentConclusionURL\" : \""+antecedentConclusionURL[i]+"\"  }  ]";
			else
				jsonArray +=", \"antecedentCachedThumbURL\" : null , \"antecedentConclusionURL\" : \""+antecedentConclusionURL[i]+"\"  }  ]";


			json += ", \"antecedents\" : "+ jsonArray;//+", ";

		}
		else 
			json += ", \"antecedents\" : null";//+", ";

		//Metadata
		//json += ", \"metadata\" : "+ ;

		//Assertions
		//json += ", \"assertions\" : "+ ;

		return "{ "+ json +" }";
	}


	/** Sets all Inference Step Information (Engine, Rule, Antecedents, metadata, and assertions). **[still in progress: metadata and assertions]*/ 
	public void setInferenceStepInformation(IWInferenceStepOccur infStep)
	{
		IWInferenceRule rule = null;
		IWIdentifiedThing engine = null;

		if (infStep != null) 
		{
			rule = infStep.getHasInferenceRule();
			engine = (IWIdentifiedThing)PMLObjectManager.getPMLObject(infStep.getPropertyObjectByLocalName(PMLJ.hasInferenceEngine_lname));

			//using rule....
			if (rule != null)
			{
				String[] ruleRes = getProvenanceInfo(rule);
				declarativeRule = ruleRes[0];
				decRuleURI = ruleRes[1];
			}

			//inferred by inference engine....
			if (engine != null)
			{
				String[] engineRes = getProvenanceInfo(engine);
				inferenceEngine = engineRes[0];
				infEngURI = engineRes[1];
			}

			//sets Antecedent URI and Raw string information
			setAntecedentInformation(infStep);


			//Metadata

			//Assertions
		}
	}

	/** Sets Antecedent URI and Raw string information */
	public void setAntecedentInformation(IWInferenceStepOccur infStep)
	{
		List antecedents = null;
		IWNodeSet antecedentIWNodeSet = null;


		antecedents = infStep.getAntecedentNodeSetOccurrences();

		if (antecedents != null )//&& numAntes>0) 
		{
			int numAntes = antecedents.size();
			if(numAntes>0)
			{
				System.out.println("Total: "+numAntes);
				antecedentRawStrings = new String[numAntes];
				antecedentURIs = new String[numAntes];
				antecedentCachedThumbURL = new String[numAntes];
				antecedentConclusionURL = new String[numAntes];

				for (int ai = 0; ai< numAntes; ai++)
				{
					antecedentIWNodeSet = (IWNodeSet)antecedents.get(ai);

					if (antecedentIWNodeSet != null)
					{
						setMethodOfVisualizingConclusion(antecedentIWNodeSet, ai);
					}
					else
					{
						antecedentCachedThumbURL[ai] = null;
						antecedentRawStrings[ai] = "No Antecedent Information";
						antecedentURIs[ai] = "No Antecedent Information";
					}
				}
			}
		}
	}

	
	public void setMethodOfVisualizingConclusion(IWNodeSet antecedentIWNodeSet, int antecedentIndex)
	{
		String uriStr = antecedentIWNodeSet.getIdentifier().getURIString();
		antecedentURIs[antecedentIndex] = uriStr;
		antecedentConclusionURL[antecedentIndex] = "Other Solution";//default value unless last must be used as last resort.
		
		String thumbURL = getNodeSetCachedVisURL(uriStr);

		if(thumbURL != null)//Thumbnail available in cache?
		{
			antecedentCachedThumbURL[antecedentIndex] = thumbURL;
			antecedentRawStrings[antecedentIndex] = "Image Used Instead";
			antecedentConclusionURL[antecedentIndex] = "Image Used Instead";
		}
		else 
		{
			antecedentCachedThumbURL[antecedentIndex] = null;
			String antecedentRaw = getNodeSetInfo(antecedentIWNodeSet);
			
			if(antecedentRaw != null)
			{
				antecedentRawStrings[antecedentIndex] = antecedentRaw;
				antecedentConclusionURL[antecedentIndex] = "Embedded Raw String Used Instead";
			}
			else
			{//No Conclusion Embedded, try getting URL to Conclusion
				
				antecedentRawStrings[antecedentIndex] = "hasURL?";
				
				Loader loader = new Loader(false);
				PMLNode pmlNode = loader.loadNode(uriStr, null);
				antecedentConclusionURL[antecedentIndex] = pmlNode.getConclusion().getHasURL();
				
				//GetURLContents.downloadText(conclusion.getHasURL());
			}
		}
	}
	

	/** returns the provenance rawstring (0) and the uri (1) in a String array. */
	public String[] getProvenanceInfo(PMLObject po)
	{
		String[] result = new String[2];

		if (po != null) 
		{
			IWIdentifiedThing pePo = (IWIdentifiedThing)po;

			if (pePo.getIdentifier() != null && pePo.getIdentifier().getURIString() != null)
			{
				String uriStr = pePo.getIdentifier().getURIString();


				result[0] = pePo.getHasName();
				result[1] = uriStr;
			} 
			else 
			{
				//				***			what should happen in this case?
				//result = getProvenanceDetailsHTMLInList(pePo);
				System.out.println("IN ELSE!! (SwingView class) NEED to getProvenanceDetailsHTMLInLIst(pePo).");

				result[0] = "No Provenance";
				result[1] = "No link";
			}
		}

		return result;
	}

	public String getNodeSetCachedVisURL(String uriStr)
	{
		String thumbURL = VisualizationCacheAccess.getCachedThumbnail(uriStr);

		return thumbURL;
	}


	/** returns the rawstring of the nodeset (0) and the uri (1) in a String array. Null otherwise */
	public String getNodeSetInfo(IWNodeSet ns)
	{

		if (ns != null)
		{
			SentenceRenderer renderer = new SentenceRenderer(ns, "English");
			//String nsType = PMLJ.NodeSet.getURI();
			//IWLanguage language = renderer.getLanguage(); // only languages have renderers return non-null
			String rawStr = renderer.getRawString();

			System.out.println("RENDERED Raw String:  "+rawStr);

			return rawStr;
		}

		return null;
	}


	public String getISMetadata(IWInferenceStepOccur infStep)
	{
		String theRest = null;
		//**** will always display "No Additional metadata". Must change  \/ to fix.
		//theRest = getObjectPropertyDetailsByTemplateHTMLInList(infStep, template);***
		if (theRest == null || theRest.trim().length()<1) 
		{
			theRest = "No additional metadata";
		}

		return " Metadata: " + theRest + " endMetadata ))) ";
	}





	public String getAssertions(IWNodeSet ns)
	{
		final String assumptionRuleURI = "http://inference-web.org/registry/DPR/Assumption.owl#Assumption";

		String content = null;

		AxiomHelper ah = new AxiomHelper();
		List axioms = new ArrayList();
		List assertionAxioms = new ArrayList();
		List assumptionAxioms = new ArrayList();
		List discharged = new ArrayList();
		List dischargedAssertions = new ArrayList();
		List dischargedAssumptions = new ArrayList();
		IWNodeSet axiom = null;
		IWNodeSet dAxiom = null;

		String assertionLabel = "";
		String assumptionLabel = "";
		String dischargedAssertionLabel = "";
		String dischargedAssumptionLabel = "";
		List leafs = ah.getAxiomsWithDischarged(ns);
		if (leafs !=null && leafs.size()>0){
			if (leafs.get(0)!=null) axioms = (List)leafs.get(0);
			if (leafs.get(1)!=null) discharged = (List)leafs.get(1);
			if (axioms != null && axioms.size()>0){
				Iterator aIt = axioms.listIterator();
				while (aIt.hasNext()) {
					axiom = (IWNodeSet)aIt.next();
					if (axiomHasRule(axiom,assumptionRuleURI)) {
						assumptionAxioms.add(axiom);
					} else {
						assertionAxioms.add(axiom);
					}
				}
			}
			if (discharged != null && discharged.size()>0){
				Iterator dIt = discharged.listIterator();
				while (dIt.hasNext()) {
					dAxiom = (IWNodeSet)dIt.next();
					if (axiomHasRule(dAxiom,assumptionRuleURI)) {
						dischargedAssumptions.add(dAxiom);
					} else {
						dischargedAssertions.add(dAxiom);
					}
				}
			}  					


			if ((axioms != null && axioms.size()>0) || (discharged != null && discharged.size()>0))
			{
				if (assertionAxioms.size()>0)
				{
					if (assertionAxioms.size()>1) {
						assertionLabel = " ancestorAssertions(2): ";
					} else {
						assertionLabel = " ancestorAssertion(1): ";
					}

					Iterator ait = assertionAxioms.listIterator();

					while (ait.hasNext())
					{
						IWNodeSet tempAxiom = (IWNodeSet)ait.next();

						assertionLabel += getNodeSetInfo(tempAxiom) + " NodeSetLink --) ";

						assertionLabel += getSourceUsage(tempAxiom) + " SourceInformation --) ";
					}

					assertionLabel += " endAncestorAssertion >>) ";
				}
				if (assumptionAxioms.size()>0) {
					if (assumptionAxioms.size()>1) {
						assumptionLabel = " ancestorAssumptions(2): ";
					} else {
						assumptionLabel = " ancestorAssumption(1): ";
					}

					Iterator ait = assumptionAxioms.listIterator();

					while (ait.hasNext())
					{
						IWNodeSet tempAxiom = (IWNodeSet)ait.next();

						assumptionLabel += getNodeSetInfo(tempAxiom) + " NodeSetLink --) ";

						assumptionLabel += getSourceUsage(tempAxiom) + " SourceInformation --) ";
					}
					assumptionLabel += " endAncestorAssumption >>) ";
				}
				if (dischargedAssertions.size()>0) {
					if (dischargedAssertions.size()>1) {
						dischargedAssertionLabel = " dischargedAncestorAssertions(2): ";
					} else {
						dischargedAssertionLabel = " dischargedAncestorAssertion(1): ";
					}

					Iterator ait = dischargedAssertions.listIterator();

					while (ait.hasNext())
					{
						IWNodeSet tempAxiom = (IWNodeSet)ait.next();

						dischargedAssertionLabel += getNodeSetInfo(tempAxiom) + " NodeSetLink --) ";

						dischargedAssertionLabel += getSourceUsage(tempAxiom) + " SourceInformation --) ";
					}

					dischargedAssertionLabel += " endDischargedAncestorAssertion >>) ";
				}
				if (dischargedAssumptions.size()>0) {
					if (dischargedAssumptions.size()>1) {
						dischargedAssumptionLabel = " dischargedAncestorAssumptions(2): ";
					} else {
						dischargedAssumptionLabel = " dischargedAncestorAssumption(1): ";
					}

					Iterator ait = dischargedAssumptions.listIterator();

					while (ait.hasNext())
					{
						IWNodeSet tempAxiom = (IWNodeSet)ait.next();

						dischargedAssumptionLabel += getNodeSetInfo(tempAxiom) + " NodeSetLink --) ";

						dischargedAssumptionLabel += getSourceUsage(tempAxiom) + " SourceInformation --) ";
					}

					dischargedAssumptionLabel += " endDischargedAncestorAssumption >>) ";
				}
			} 
		} else {
			//content=getSourceUsage(ns);
		}

		return " Assertions: " + assertionLabel + assumptionLabel + dischargedAssertionLabel + dischargedAssumptionLabel + " endAssertions ))) ";
	}


	private String getSourceUsage(IWNodeSet ns)
	{
		String _suLink = null;
		IWInferenceStep infStep = ns.getSelectedInferenceStep();

		String result = " Source:-- ";

		if (infStep != null) {
			String srcUsgStr = null;
			IWSourceUsage su = infStep.getHasSourceUsage();
			IWSource source = null;
			String srcUri = null;

			if (su != null) {
				srcUsgStr = su.getSourceInfoStringFormat();
				source = su.getHasSource();
				if (source != null) {
					srcUri = source.getIdentifier().getURIString();
				}
			}
			if (srcUsgStr != null) {
				if (srcUri != null) {
					_suLink = srcUri + " sourceProvenance >>) ";
				} else {//!!! ^^^^^^vvvvvv
					//"<A CLASS=\"unadornment\" HREF=\"javascript: void openURLSource(\'RenderSourceUsageInfo?srcUsgStr="
					_suLink = "RenderSourceUsageInfo?srcUsgStr="+ srcUsgStr + " sourceProvenance >>) ";
				}
				if (su.isSourceDocumentFragment()) {// only apply to document fragment type source
					String suStr = su.getSourceUsageStringForDocumentFragmentSource();
					if (suStr != null) {
						if (_suLink == null) {
							_suLink = "";
						}
						_suLink += "RenderSourceUsage?srcUsgStr="
								+ suStr + " sourceDocument >>) ";//source Usage (Document)
					}
				} else if (su.isV1SourceUsage()) {
					String suStr = su.getV1SourceUsageString();
					if (suStr != null) {
						_suLink += "RenderV1SourceUsage?srcUsgStr="
								+ suStr + " sourceUsage >>) ";//source
					}
				}
			}
		}
		//System.out.println("RenderNodeSet: _suLink="+_suLink);
		if(_suLink != null)
			result += _suLink + " endofSource >>) ";
		else
			result += " endofSource >>) ";
		return result;
	}

	private boolean axiomHasRule (IWNodeSet ns, String ruleURI) {
		boolean result = false;
		IWInferenceStep infStep;
		IWInferenceRule rule;
		if (ns != null && ruleURI != null){
			infStep = ns.getSelectedInferenceStep();
			if (infStep != null) {
				rule = infStep.getHasInferenceRule();
				if (rule != null && rule.getIdentifier()!=null && rule.getIdentifier().getURIString() != null) {
					String uri = rule.getIdentifier().getURIString();
					result =  (ToolURI.equalURI(ruleURI, uri));
				}
			}
		}

		return result;
	}

}
