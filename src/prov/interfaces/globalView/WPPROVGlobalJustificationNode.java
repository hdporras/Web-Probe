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

public class WPPROVGlobalJustificationNode 
{
	String uri = "";
	
	public WPPROVGlobalJustificationNode(String URI)
	{
		uri = URI;
	}
	
	public String convertToJSON()
	{
		String json = "\"uri\" : " + uri + ", " +
				"\"property\" : "+"property"+","; 
		
		return json;
	}
}
