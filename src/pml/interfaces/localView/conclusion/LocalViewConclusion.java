package pml.interfaces.localView.conclusion;

import cache.visAccess.VisualizationCacheAccess;
import pml.PMLNode;
import pml.loading.Loader;
import util.GetURLContents;
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
	

	public String convertToJSON()
	{
		String conclusionText = JSONUtils.toValidJSONString(concText);
		
		if(thumbURL !=null)
			return "{ \"conclusionText\" : \""+ conclusionText +"\" , \"thumbURL\" : \""+ thumbURL +"\"  , \"concURI\" : \""+ concURI +"\" }";
		else
			return "{ \"conclusionText\" : \""+ conclusionText +"\" , \"thumbURL\" : null , \"concURI\" : \""+ concURI +"\" }";
	}
}
