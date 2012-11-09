package pml.interfaces.global;

public class JustificationTreeBuilder
{
	static WPJustificationTree tree;
	
	/** builds the Justification Tree and returns the JSON representation of it */
	public static String getJustificationTree(String URI)
	{
		tree = new WPJustificationTree(URI);
		return tree.convertToJSON();
	}
	
	public static void main(String args[])
	{
		String uri = "http://rio.cs.utep.edu/ciserver/ciprojects/GravityMapProvenance/gravityContourMap.ps_038568341971146025.owl#answer";
		String treeJSON = getJustificationTree(uri);
		System.out.println( treeJSON );
		
	}
}
