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
