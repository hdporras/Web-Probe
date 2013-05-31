package prov.interfaces;

public class PROVIndividual
{
	private static int lastID = 0;
	
	//Number ID (for graph)
	int id;

	//Individual URI
	String uri;
	
	//Individual Type
	String type;
	
	//Individual's annotation connections.
	String[] connections;
	String[] connectionIDs;
	
	
	public PROVIndividual(String uri)
	{
		id = lastID++;
		this.uri = uri;
	}
	
	public PROVIndividual(String uri, String type)
	{
		id = lastID++;
		this.uri = uri;
		this.type = type;
	}
}