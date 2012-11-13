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
	ArrayList<WPGlobalJustificationTreeNode> children;
	
	
	
	public WPGlobalJustificationTreeNode(/*WPGlobalJustificationTreeNode parentNode,*/ WPJustificationPMLNode PMLcontentNode)
	{
		//parent = parentNode;
		PMLnode = PMLcontentNode;
		children = new ArrayList<WPGlobalJustificationTreeNode>();
	}
	

	/** Converts the object into a JSON String representation of the object. */
	public String convertToJSON()
	{
		String json = " \"PMLnode\" : " + PMLnode.convertToJSON(); 

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
