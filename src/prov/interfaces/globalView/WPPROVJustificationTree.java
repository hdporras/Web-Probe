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

package prov.interfaces.globalView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.inference_web.iw.pml.pmlj.IWNodeSetOccur;
import org.inference_web.iw.pml.util.IWPMLObjectManager;


public class WPPROVJustificationTree
{
	/** Root Tree Node */
	public WPPROVGlobalJustificationTreeNode root;
	
	/** Tree Width */
	public int treeWidth = 1;
	
	/** Tree Height */
	public int treeHeight = 1;
	
	
	public WPPROVJustificationTree(String URI)
	{
		
		WPPROVGlobalJustificationNode justRootNode = createJustificationPROVNode(URI);
		
		root = buildTree(justRootNode, 1);
		
		calculateMaxDimensions();
	}
	
	/** Returns the created WPPROVGlobalJustificationNode from the given URI.*/
	public WPPROVGlobalJustificationNode createJustificationPROVNode(String URI)
	{		
		WPPROVGlobalJustificationNode justNode = new WPPROVGlobalJustificationNode(URI);
		return justNode;
	}
	
	
	//**Add logic content
	/** Builds the PROV Tree and Returns the root of the Tree. */
	public WPPROVGlobalJustificationTreeNode buildTree(WPPROVGlobalJustificationNode justRoot, int level)
	{
		WPPROVGlobalJustificationTreeNode treeNode = new WPPROVGlobalJustificationTreeNode(justRoot, level);
		
		return treeNode;
	}
	
	
	
	private HashMap<Integer,Integer> widthMap = new HashMap<Integer,Integer>();
	
	/** Traverses the tree to compute the max width and height of the tree. */
	public void calculateMaxDimensions()
	{
		int maxWidth = 1;
		int maxHeight = 1;
		
		Iterator< Map.Entry<Integer,Integer> > it = widthMap.entrySet().iterator();
	    while (it.hasNext())
	    {
	        Map.Entry<Integer,Integer> pairs = (Map.Entry<Integer,Integer>)it.next();
	        //System.out.println(pairs.getKey() + " = " + pairs.getValue());
	        it.remove(); // avoids a ConcurrentModificationException
	        
	        int value = pairs.getValue().intValue();
	        int level = pairs.getKey().intValue();
	        
	        if( value > maxWidth )
	        {
	        	maxWidth = value;
	        }
	        
	        if( level > maxHeight )
	        {
	        	maxHeight = level;
	        }
	    }
	    
	    treeWidth = maxWidth;
	    treeHeight = maxHeight;
	}
	
	/** updates the width of the tree at different levels, for later use, in the widthMap HashMap. */
	public void addToWidth(int nodeWidth, int level)
	{
		if(widthMap.containsKey(level))// if already an entry for this level
		{
            int count = widthMap.get(level);// get latest count
            widthMap.put(level, count + nodeWidth);// add the node width to the count 
        }
		else//else if not defined already
        {
        	widthMap.put(level, nodeWidth);// start the count with the node width.
        }
	}
	
	
	
	/** Converts the object into a JSON String representation of the object. */
	public String convertToJSON()
	{
		String json = "\"treeWidth\" : " + treeWidth + ", " +
				"\"treeHeight\" : " + treeHeight + ", " +
				"\"root\" : " +root.convertToJSON();
		
		return "{ "+ json +" }";
	}
	
	
	public static void main(String args[])
	{
		WPPROVJustificationTree tree = new WPPROVJustificationTree("http://rio.cs.utep.edu/ciserver/ciprojects/GravityMapProvenance/gravityContourMap.ps_038568341971146025.owl#answer");
		System.out.println( tree.convertToJSON() );
		
	}
}
