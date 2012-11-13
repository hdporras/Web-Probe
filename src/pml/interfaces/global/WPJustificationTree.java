package pml.interfaces.global;

import org.inference_web.iw.pml.pmlj.IWNodeSetOccur;
import org.inference_web.iw.pml.util.IWPMLObjectManager;

import pml.interfaces.WPJustificationPMLNode;
import pml.interfaces.global.WPGlobalJustificationTreeNode;
import pml.interfaces.localView.NodeSetDetails;

public class WPJustificationTree
{
	/** Root Tree Node */
	public WPGlobalJustificationTreeNode root;
	
	//Use pml.interfaces.localView.justifiedBy classes to get children information
	
	/* Other approach is to load Justification and figure out how to go through the nodes...
	 * Loader factory = new Loader(false);
	 * PMLNode node = factory.loadJustification(URI, null); 
	 */
	
	public WPJustificationTree(String URI)
	{
		/*Loader factory = new Loader(false);
		PMLNode node = factory.loadJustification(URI, null);*/
		
		WPJustificationPMLNode justRootNode = createJustificationPMLNode(URI);
		root = buildTree(justRootNode);
	}
	
	/** Returns the created WPJustificationPMLNode from the given URI. It does this by building an IWNodeSetOccur from the URI and */
	public WPJustificationPMLNode createJustificationPMLNode(String URI)
	{
		//NodeSetDetails nsDetails = new NodeSetDetails(); 
		//nsDetails.setupNodeSet(URI);
		//IWNodeSetOccur ns = nsDetails.getIWNS();
		
		IWNodeSetOccur ns = IWPMLObjectManager.loadNodeSetOccurrence(URI, 2);
		WPJustificationPMLNode justNode = new WPJustificationPMLNode(ns);
		return justNode;
	}
	
	/** Builds the PML Tree and Returns the root of the Tree. */
	public WPGlobalJustificationTreeNode buildTree(WPJustificationPMLNode justRoot)
	{		
		if(justRoot != null)
		{
			WPGlobalJustificationTreeNode parentNode = new WPGlobalJustificationTreeNode(justRoot);
			
			String[] antecedentURIs = justRoot.inferenceSteps.get(0).antecedentURIs;
			
			if(antecedentURIs != null)
			{
				for(int i=0; i < antecedentURIs.length; i++)
				{
					WPJustificationPMLNode antecedentPMLNode = createJustificationPMLNode( antecedentURIs[i] );
					//WPGlobalJustificationTreeNode antecedentTreeNode = new WPGlobalJustificationTreeNode(antecedentPMLNode);
					WPGlobalJustificationTreeNode antecedentTreeNode = buildTree(antecedentPMLNode);
					parentNode.children.add(antecedentTreeNode);
				}
			}
			
			return parentNode;
		}
		else
		{
			return null;
		}
		
	}
	
	/** Converts the object into a JSON String representation of the object. */
	public String convertToJSON()
	{
		return root.convertToJSON();
	}
	
	
	public static void main(String args[])
	{
		WPJustificationTree tree = new WPJustificationTree("http://rio.cs.utep.edu/ciserver/ciprojects/GravityMapProvenance/gravityContourMap.ps_038568341971146025.owl#answer");
		System.out.println( tree.convertToJSON() );
		
	}
}
