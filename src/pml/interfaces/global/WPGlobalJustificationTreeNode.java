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

package pml.interfaces.global;

import java.util.ArrayList;

import pml.interfaces.WPJustificationPMLNode;
import util.JSONUtils;

public class WPGlobalJustificationTreeNode
{
	/** Parent of current TreeNode. (Null if root) */
	//WPGlobalJustificationTreeNode parent;
	
	/** Contains the TreeNodes PML information */
	WPJustificationPMLNode PMLnode;
	
	/** Contains children of the TreeNode */
	public ArrayList<WPGlobalJustificationTreeNode> children;
	
	/** Level of node in Tree */
	public int level;
	
	
	
	public WPGlobalJustificationTreeNode( WPJustificationPMLNode PMLcontentNode, int lvl /*, WPGlobalJustificationTreeNode parentNode*/)
	{
		//parent = parentNode;
		PMLnode = PMLcontentNode;
		level = lvl;
		children = new ArrayList<WPGlobalJustificationTreeNode>();
	}
	

	/** Converts the object into a JSON String representation of the object. */
	public String convertToJSON()
	{
		String json = "\"level\" : " + level + ", " +
				"\"PMLnode\" : " + PMLnode.convertToJSON(); 

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
