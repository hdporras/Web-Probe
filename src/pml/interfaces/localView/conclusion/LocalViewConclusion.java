package pml.interfaces.localView.conclusion;

import org.inference_web.iw.pml.pmlj.IWNodeSetOccur;
import org.inference_web.iw.pml.util.IWPMLObjectManager;

import cache.visAccess.VisualizationCacheAccess;
import pml.PMLNode;
import pml.loading.Loader;
import util.JSONUtils;

public class LocalViewConclusion
{
	String concURI;
	String concText;
	String thumbURL;
	
	public LocalViewConclusion(String URI)
	{
		Loader factory = new Loader(false);
		PMLNode node = factory.loadNode(URI, null);
		
		try
		{
			concURI = URI;
			thumbURL = VisualizationCacheAccess.getCachedThumbnail(URI);
			if (thumbURL != null)//if cached thumbnail..
			{
				concText = "Thumbnail Used Instead";
			}
			else if(node.getConclusion().isByReference())//maybe just return URL and use a default image in display
			{
				concText = node.getConclusion().getHasURL();
				//concText = GetURLContents.downloadText(node.getConclusion().getHasURL());
			}
			else
				concText = node.getConclusion().getStringConclusion();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public LocalViewConclusion(String URI, IWNodeSetOccur node)
	{	
		try
		{
			String hasURL = node.getHasConclusion().getHasURL();
			concURI = URI;//concURI = node.getIdentifier().getURIString();
			thumbURL = VisualizationCacheAccess.getCachedThumbnail(URI);
			if (thumbURL != null)//if cached thumbnail..
			{
				concText = "Thumbnail Used Instead";
			}
			else if(hasURL != null)//maybe just return URL and use a default image in display
			{
				concText = hasURL;
				//concText = GetURLContents.downloadText(node.getConclusion().getHasURL());
			}
			else
			{
				concText = node.getHasConclusion().getHasPrettyString();
				if(concText == null)	
					concText = node.getConclusionRawString();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	

	public String convertToJSON()
	{
		String conclusionText = JSONUtils.toValidJSONString(concText);
		
		if(thumbURL !=null)
			return "{ \"conclusionText\" : \""+ conclusionText +"\" , \"thumbURL\" : \""+ thumbURL +"\"  , \"concURI\" : \""+ concURI +"\" }";
		else
			return "{ \"conclusionText\" : \""+ conclusionText +"\" , \"thumbURL\" : null , \"concURI\" : \""+ concURI +"\" }";
	}
	
	
	public static void main(String[] args)
	{
		System.out.println("start");
		String URI = "http://rio.cs.utep.edu/ciserver/ciprojects/ScientificPublication/holes-trace.jpg_007663978235903546.owl#answer";//"http://inference-web.org/proofs/tonys/tonys_5/ns15.owl#ns15";
		IWNodeSetOccur ns = IWPMLObjectManager.loadNodeSetOccurrence(URI, 2);
		
		LocalViewConclusion c = new LocalViewConclusion(URI, ns);
		
		System.out.println(c.convertToJSON());
	}
}
