package pml.interfaces.global;

import org.inference_web.iw.pml.pmlj.IWNodeSetOccur;

import pml.PMLNode;
import pml.loading.Loader;

import pml.interfaces.localView.NodeSetDetails;
import pml.interfaces.localView.justifiedBy.*;

public class PMLTree
{
	PMLGlobalNode root;
	
	//Use pml.interfaces.localView.justifiedBy classes to get children information
	
	/* Other approach is to load Justification and figure out how to go through the nodes...
	 * Loader factory = new Loader(false);
	 * PMLNode node = factory.loadJustification(URI, null); 
	 */
	
	public PMLTree(String URI)
	{
		/*Loader factory = new Loader(false);
		PMLNode node = factory.loadJustification(URI, null);*/
		
		NodeSetDetails nsDetails = new NodeSetDetails(); 
		nsDetails.setupNodeSet(URI);
		
		IWNodeSetOccur ns = nsDetails.getIWNS();
		LocalViewJustifiedBy just = new LocalViewJustifiedBy(ns);
		
	}
}
