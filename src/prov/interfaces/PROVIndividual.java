package prov.interfaces;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;

public class PROVIndividual
{
	private static int lastID = 0;
	
	/** Number ID (for D3 JSON graph)*/
	int id;

	/** Individual URI */
	String uri;
	
	/** Individual Type */
	OWLClass[] types;
	
	/** Individual's Object and Data Property Connections.*/
	ArrayList<String> OPconnections;
	ArrayList<Integer> OPconnectionObjectIDs;
	
	ArrayList<String> DPconnections;
	ArrayList<String> DPconnectionValues;
	
	
	public PROVIndividual(String uri)
	{
		id = lastID++;
		this.uri = uri;
		
		OPconnections = new ArrayList<String>();
		OPconnectionObjectIDs = new ArrayList<Integer>();
		
		DPconnections = new ArrayList<String>();
		DPconnectionValues = new ArrayList<String>();
	}
	
	/*public PROVIndividual(String uri, String type)
	{
		id = lastID++;
		this.uri = uri;
		
		this.type = new String[1];
		this.type[0] = type;
	}*/
	
	public void setTypes(OWLClass[] types)
	{
		this.types = types;
	}
	
	
	/** Gather information about all Activities. Indivisduals are placed in a HashMap (individualsHM: Key=URI, value = PROVIndividual) */
	public void addActivity(OWLNamedIndividual ind)
	{

		//Get list of Object Property values associated with ind.
		ArrayList< NodeSet<OWLNamedIndividual> > OPNodeSets = getAllActivityOPs(ind);
		
		//Get list of Data Property values associated with ind.
		ArrayList< OWLLiteral > DPNodeSets = getAllActivityDPs(ind);
		
		//get Types
		NodeSet<OWLClass> types = PROVPrimerTest.reasoner.getTypes( ind, true );		    
		OWLClass[] typesArray = types.getFlattened().toArray(new OWLClass[0]);
		setTypes(typesArray);
		
	}
	
	public ArrayList< OWLLiteral > getAllActivityDPs(OWLNamedIndividual ind)
	{
		ArrayList< OWLLiteral > DPNodeSets = new ArrayList< OWLLiteral >();
		
		//endedAtTime
		OWLDataProperty endedAtTime = PROVPrimerTest.manager.getOWLDataFactory().getOWLDataProperty(IRI.create(PROVPrimerTest.provPrefix+"endedAtTime"));
		Set<OWLLiteral> endedAtTimeLiterals = PROVPrimerTest.reasoner.getDataPropertyValues( ind, endedAtTime );
		DPNodeSets.addAll(endedAtTimeLiterals);
		//*endedAtTimeLiterals.size();
		
	    
		//endedAtTime
		OWLDataProperty startedAtTime = PROVPrimerTest.manager.getOWLDataFactory().getOWLDataProperty(IRI.create(PROVPrimerTest.provPrefix+"startedAtTime"));
		Set<OWLLiteral> startedAtTimeLiterals = PROVPrimerTest.reasoner.getDataPropertyValues( ind, startedAtTime );
		DPNodeSets.addAll(startedAtTimeLiterals);
		
		
		return DPNodeSets;
	}
	
	public ArrayList< NodeSet<OWLNamedIndividual> > getAllActivityOPs(OWLNamedIndividual ind)
	{
		//Where all Object Property values are contained (for this individual)
		ArrayList< NodeSet<OWLNamedIndividual> > OPNodeSets = new ArrayList< NodeSet<OWLNamedIndividual> >();
		
		//*Better way of getting all object/data properties associated with the individual.
		
		//generated
		OWLObjectProperty generated = PROVPrimerTest.manager.getOWLDataFactory().getOWLObjectProperty(IRI.create(PROVPrimerTest.provPrefix+"generated"));
		NodeSet<OWLNamedIndividual> generatedObjects = PROVPrimerTest.reasoner.getObjectPropertyValues( ind, generated );
		OPNodeSets.add(generatedObjects);
		
		//used
		OWLObjectProperty used = PROVPrimerTest.manager.getOWLDataFactory().getOWLObjectProperty(IRI.create(PROVPrimerTest.provPrefix+"used"));
		NodeSet<OWLNamedIndividual> usedObjects = PROVPrimerTest.reasoner.getObjectPropertyValues( ind, used );
		OPNodeSets.add(usedObjects);
		
		//wasAssociatedWith
		OWLObjectProperty associatedWith = PROVPrimerTest.manager.getOWLDataFactory().getOWLObjectProperty(IRI.create(PROVPrimerTest.provPrefix+"wasAssociatedWith"));
		NodeSet<OWLNamedIndividual> associatedWithObjects = PROVPrimerTest.reasoner.getObjectPropertyValues( ind, associatedWith );
		OPNodeSets.add(associatedWithObjects);
		
		//qualifiedUsage 
		OWLObjectProperty qualifiedUsage = PROVPrimerTest.manager.getOWLDataFactory().getOWLObjectProperty(IRI.create(PROVPrimerTest.provPrefix+"qualifiedUsage"));
		NodeSet<OWLNamedIndividual> qualifiedUsageObjects = PROVPrimerTest.reasoner.getObjectPropertyValues( ind, qualifiedUsage );
		OPNodeSets.add(qualifiedUsageObjects);
		
		//qualifiedAssociation
		OWLObjectProperty qualifiedAssociation = PROVPrimerTest.manager.getOWLDataFactory().getOWLObjectProperty(IRI.create(PROVPrimerTest.provPrefix+"qualifiedAssociation"));
		NodeSet<OWLNamedIndividual> qualifiedAssociationObjects = PROVPrimerTest.reasoner.getObjectPropertyValues( ind, qualifiedAssociation );
		OPNodeSets.add(qualifiedAssociationObjects);
		
		//wasInformedBy
		OWLObjectProperty wasInformedBy = PROVPrimerTest.manager.getOWLDataFactory().getOWLObjectProperty(IRI.create(PROVPrimerTest.provPrefix+"wasInformedBy"));
		NodeSet<OWLNamedIndividual> wasInformedByObjects = PROVPrimerTest.reasoner.getObjectPropertyValues( ind, wasInformedBy );
		OPNodeSets.add(wasInformedByObjects);
		
		//wasInfluencedBy
		OWLObjectProperty wasInfluencedBy = PROVPrimerTest.manager.getOWLDataFactory().getOWLObjectProperty(IRI.create(PROVPrimerTest.provPrefix+"wasInfluencedBy"));
		NodeSet<OWLNamedIndividual> wasInfluencedByObjects = PROVPrimerTest.reasoner.getObjectPropertyValues( ind, wasInfluencedBy );
		OPNodeSets.add(wasInfluencedByObjects);
		
		return OPNodeSets;
	}
	
	//*not checking for duplicates yet
	public void addOPConnections()
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