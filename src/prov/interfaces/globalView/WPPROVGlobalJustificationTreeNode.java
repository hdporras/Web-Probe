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
