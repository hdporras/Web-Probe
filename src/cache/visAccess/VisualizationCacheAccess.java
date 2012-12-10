package cache.visAccess;

import java.util.ArrayList;
import cache.visAccess.CacheXMLAccess;

public class VisualizationCacheAccess 
{
	//protected static String cacheLocation = "/usr/local/tomcat/tomcat5.0.28/webapps/service-output/cached";

	/** Caches the list of PML nodesets it receives. Returns a parallel array describing whether each nodeset was successfully cached.*/
/*	public static boolean[] cacheNodesetList(String[] nodesetURIs)
	{
		boolean[] transformed = new boolean[nodesetURIs.length];
		
		for(int i=0; i < transformed.length; i++)
		{
			transformed[i] = false;
		}
		
		try
		{
			Cache cache = new Cache();

			for(int i=0; i < nodesetURIs.length; i++)
			{
				//total++;
				String nextURI = nodesetURIs[i];

				System.out.println("Next Nodeset:  "+nextURI);

				//cache uri nodeset.
				cache.transformNode(nextURI);
				transformed[i] = true;

				//cntr++;
			}
			//System.out.println(cntr+"/"+total);

		}catch(Exception e){
			e.printStackTrace();
		}
		
		return transformed;
	}*/

	/** given a list of nodeset URIs, return a subset of that list that has cached conclusions. Know in advance which 
	 * nodesets to call the "getCachedThumbnail" operation on. This will prevent from having to make an expensive 
	 * service call for every nodeset encountered. */
	public static String[] getThumbnailCachedNodesetList(String[] nodesetURIs)
	{
		ArrayList<String> cachedURIs = new ArrayList<String>();
		
		for(int i=0; i< nodesetURIs.length; i++)
		{
			String loc = CacheXMLAccess.getThumbLoc(nodesetURIs[i]);
			if(loc!=null)
				cachedURIs.add(loc);
		}
		
		String[] URIs = new String[cachedURIs.size()];
		for(int i=0; i<URIs.length; i++)
			URIs[i] = (String)cachedURIs.get(i);

		return URIs;
	}

	/** Given a list of nodeset URIs, return a subset of that list that does not have cached conclusions.*/
/*	public static String[] getNonCachedNodesetList(String[] nodesetURIs)
	{
		ArrayList<String> noncachedURIs = new ArrayList<String>();
		try
		{
			for(int i=0; i < nodesetURIs.length; i++)
			{
				noncachedURIs.add(nodesetURIs[i]);
			}

			BufferedReader reader = new BufferedReader(new FileReader(cacheLocation+"/cache2log.txt"));
			String line = reader.readLine();
			while(line!=null)
			{

				for(int i=0; i < noncachedURIs.size(); i++)
				{
					System.out.println(line+" = "+nodesetURIs[i]);
					if(line.equalsIgnoreCase(noncachedURIs.get(i)))//if already cached.
					{
						System.out.println("Are equal! (already cached)");
						System.out.println("Removing from list. (does not need to be cached)");
						noncachedURIs.remove(i);
					}
				}
				line = reader.readLine();
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		//make copy of list in an array, to return.
		String[] URIs = new String[noncachedURIs.size()];
		for(int i=0; i<URIs.length; i++)
			URIs[i] = (String)noncachedURIs.get(i);

		return URIs;
	}*/


	/** return a url to a cached thumbnail conclusion of the nodeset pointed to by "nodesetURI" 
	 * @return URL of thumbnail, the next index contains the viewer type. Null if nodesetURI not found. */
	public static String getCachedThumbnail(String nodesetURI)
	{
		try
		{
			String loc = CacheXMLAccess.getThumbLoc(nodesetURI);
			return loc;
			
		}catch(Exception e)
		{e.printStackTrace();}
		
		return null;
	}

	/** return a set of url's to cached conclusion visualizations of the nodeset pointed to by "nodesetURI" 
	 * @return URLs of visualizations. Null if nodesetURI not found. */
	public static String[] getCachedVisualizations(String nodesetURI)//Maybe change to getVisualizations? return String[]?
	{
		try
		{
			return CacheXMLAccess.getVisLoc(nodesetURI);
			
		}catch(Exception e)
		{e.printStackTrace();}
		
		return null;
	}
	

	/** return a set viewers of cached conclusion visualizations of the nodeset pointed to by "nodesetURI" 
	 * @return viewers. Null if nodesetURI not found. */
	public static String[] getViewer(String nodesetURI)//or get Viewers? return String[]?
	{
		try
		{
			return CacheXMLAccess.getViewerLoc(nodesetURI);
		
		}catch(Exception e)
		{e.printStackTrace();}
		
		return null;
	}
}
