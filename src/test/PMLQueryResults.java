package test;

import pml.PMLQuery;
import pml.loading.Loader;

public class PMLQueryResults//PMLQueryDWRInterface-- Acts as an interface between PMLQuery and DWR for Web-Probe.
{
	
	public String getQueryContent(String uri)
	{
		PMLQuery query = loadQuery(uri);
		return getQueryContent(query);
	}
	
	private String getQueryContent(PMLQuery query)
	{
		return query.getContent();
	}
	
	/** Loads Query from URI, and returns the loaded PMLQuery Object. */
	private PMLQuery loadQuery(String uri)
	{
		PMLQuery query;
		Loader loader = new Loader(false);

		if (loader.isQuery(uri))
		{				
			query = loader.loadQuery(uri);
			return query;
		}
		else
		{
			System.out.println("Error reading " + uri);
		}

		return null;
	}
}
