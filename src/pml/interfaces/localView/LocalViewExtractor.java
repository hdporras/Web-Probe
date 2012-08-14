package pml.interfaces.localView;


public class LocalViewExtractor
{
	
	/** Loads PML from URI and generates HTML for Local Justification View */
	public static String getLocalViewDetails(String URI)
	{
		NodeSetDetails details = new NodeSetDetails();
		details.buildLocalView(URI);
		
		return details.convertToJSON();
	}
	
	
	public static void main(String[] args)
	{
		boolean isroot = false;
		String test = ""+isroot;
		
		System.out.println(test);
		
		//String URI = "http://rio.cs.utep.edu/ciserver/ciprojects/ScientificPublication/ipaw-v4.tex_0016939260199497874.owl#answer";
		String URI = "http://inference-web.org/proofs/tonys/tonys_4/ns1.owl#ns1"; //Direct Assertion
		//String URI = "http://inference-web.org/proofs/tonys/tonys_2/ns8.owl#ns8";
		//String URI = "http://inference-web.org/proofs/tonys/tonys_5/ns18.owl#ns18";
		//String URI = "http://inference-web.org/proofs/tptp/Problems/PUZ/PUZ001+1/query.owl#query";
		//String URI = "http://rio.cs.utep.edu/ciserver/ciprojects/GravityMapProvenance/gravityContourMap.ps_038568341971146025.owl#answer";
		System.out.println( getLocalViewDetails(URI) );
		
		//htmlExtractor.getHTMLParts();
	}
}
