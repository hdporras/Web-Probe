package prov.interfaces;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;

public class PROVIndividual
{
	private static int lastID = 0;
	
	/** Number ID (for D3 JSON graph)*/
	int id;

	/** Individual URI */
	String uri;
	
	/** Individual Type */
	String[] type;
	
	/** Individual's Object and Data Property Connections.*/
	String[] OPconnections;
	String[] OPconnectionObjectIDs;
	String[] DPconnections;
	String[] DPconnectionObjectIDs;
	
	
	public PROVIndividual(String uri)
	{
		id = lastID++;
		this.uri = uri;
	}
	
	public PROVIndividual(String uri, String type)
	{
		id = lastID++;
		this.uri = uri;
		
		this.type = new String[1];
		this.type[0] = type;
	}
	
	//*not checking for duplicates yet
	public void addOPConnections(Map<OWLObjectPropertyExpression,Set<OWLIndividual>> map)
	{
		OPconnections = new String[map.size()];
		OPconnectionObjectIDs = new String[map.size()];

		Iterator<Entry<OWLObjectPropertyExpression, Set<OWLIndividual>>> connectionsIterator = map.entrySet().iterator();
		System.out.println(connectionsIterator.toString());
		
		System.out.println("Set Size: "+map.size()+", HasNext():"+connectionsIterator.hasNext());
		
		while(connectionsIterator.hasNext())
		{
			Entry<OWLObjectPropertyExpression, Set<OWLIndividual>> entry = connectionsIterator.next();
			
			String connectionPredicateName = entry.getKey().getNamedProperty().toStringID();
			
			Set<OWLIndividual> connectionObjects = entry.getValue();
			//foreach connection object
			for(OWLIndividual objectIndividual : connectionObjects)
			{
				String objectIndividualName = objectIndividual.toStringID();
				
				System.out.println(uri+"("+id+") : "+connectionPredicateName+" -> "+objectIndividualName);
			}
		}
	}
	
	public void addDPConnections(Map<OWLDataPropertyExpression, Set<OWLLiteral>> map)
	{
		DPconnections = new String[map.size()];
		DPconnectionObjectIDs = new String[map.size()];

		Iterator<Entry<OWLDataPropertyExpression, Set<OWLLiteral>>> connectionsIterator = map.entrySet().iterator();
		System.out.println(connectionsIterator.toString());
		
		System.out.println(connectionsIterator.hasNext());
		
		while(connectionsIterator.hasNext())
		{
			Entry<OWLDataPropertyExpression, Set<OWLLiteral>> entry = connectionsIterator.next();
			
			String connectionPredicateName = entry.getKey().toString();//.getNamedProperty().toStringID();
			
			Set<OWLLiteral> connectionObjects = entry.getValue();
			//foreach connection object
			for(OWLLiteral objectIndividual : connectionObjects)
			{
				String objectIndividualName = objectIndividual.getLiteral();
				
				System.out.println(uri+"("+id+") : "+connectionPredicateName+" -> "+objectIndividualName);
			}
		}
	}
}