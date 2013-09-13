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

import java.util.ArrayList;

import util.JSONUtils;

public class WPPROVGlobalJustificationTreeNode
{
	/** Parent of current TreeNode. (Null if root) */
	//WPGlobalJustificationTreeNode parent;
	
	/** Contains the TreeNodes PROV information */
	WPPROVGlobalJustificationNode PROVnode;
	
	/** Contains children of the TreeNode */
	public ArrayList<WPPROVGlobalJustificationTreeNode> children;
	
	/** Level of node in Tree */
	public int level;
	
	
	
	public WPPROVGlobalJustificationTreeNode( WPPROVGlobalJustificationNode PROVcontentNode, int lvl)
	{
		//parent = parentNode;
		PROVnode = PROVcontentNode;
		level = lvl;
		children = new ArrayList<WPPROVGlobalJustificationTreeNode>();
	}
	

	/** Converts the object into a JSON String representation of the object. */
	public String convertToJSON()
	{
		String json = "\"level\" : " + level + ", " +
				"\"PROVnode\" : " + PROVnode.convertToJSON(); 

		if(children != null && children.size() > 0)
		{

			//Inference Steps
			String jsonArray = "[ ";

			int i;
			for(i=0; i < children.size() -1 ; i++)
			{
				jsonArray += children.get(i).convertToJSON() +", ";
			}
			//insert last element and close the array.
			jsonArray += children.get(i).convertToJSON() +" ]";


			json += ", \"children\" : "+ jsonArray;

		}
		else 
			json += ", \"children\" : null";//+", ";


		
		
		return "{ "+ json +" }";
	}
}
