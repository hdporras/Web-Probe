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
	ArrayList<PROVIndividual> OPconnectionObjectInds;
	
	ArrayList<String> DPconnections;
	ArrayList<String> DPconnectionValues;
	
	
	public PROVIndividual(String uri)
	{
		id = lastID++;
		this.uri = uri;
		
		OPconnections = new ArrayList<String>();
		OPconnectionObjectInds = new ArrayList<PROVIndividual>();
		
		DPconnections = new ArrayList<String>();
		DPconnectionValues = new ArrayList<String>();
	}
	
	public void setTypes(OWLClass[] types)
	{
		this.types = types;
	}
	
	
	/** Gather information about all Activities. Indivisduals are placed in a HashMap (individualsHM: Key=URI, value = PROVIndividual) */
	public void addActivity(OWLNamedIndividual ind)
	{
		//Add list of Object Property values associated with ind.
		addAllActivityOPs(ind);
		
		//Add list of Data Property values associated with ind.
		addAllActivityDPs(ind);
		
		//get Types
		NodeSet<OWLClass> types = PROVPrimerTest.reasoner.getTypes( ind, true );		    
		OWLClass[] typesArray = types.getFlattened().toArray(new OWLClass[0]);
		setTypes(typesArray);
		
	}
	
	//Activity Properties
	/** Get the Data properties associated with this prov:Activity */
	public void addAllActivityDPs(OWLNamedIndividual ind)
	{
		//endedAtTime
		String dataPropertyURI = PROVPrimerTest.provPrefix+"endedAtTime";
		addDataPropertiesByPredicateType(dataPropertyURI, ind);
			    
		//endedAtTime
		dataPropertyURI = PROVPrimerTest.provPrefix+"startedAtTime";
		addDataPropertiesByPredicateType(dataPropertyURI, ind);
		
	}
	
	/** Get the Object properties associated with this prov:Activity */
	public void addAllActivityOPs(OWLNamedIndividual ind)
	{
		//*Better way of getting all object/data properties associated with the individual.
			
		//generated
		String predicateURI = PROVPrimerTest.provPrefix+"generated";
		addObjectPropertiesByPredicateType(predicateURI, ind);
		
		//used
		predicateURI = PROVPrimerTest.provPrefix+"used";
		addObjectPropertiesByPredicateType(predicateURI, ind);
		
		//wasAssociatedWith
		predicateURI = PROVPrimerTest.provPrefix+"wasAssociatedWith";
		addObjectPropertiesByPredicateType(predicateURI, ind);
		
		//qualifiedUsage
		predicateURI = PROVPrimerTest.provPrefix+"qualifiedUsage";
		addObjectPropertiesByPredicateType(predicateURI, ind);
		
		//qualifiedAssociation
		predicateURI = PROVPrimerTest.provPrefix+"qualifiedAssociation";
		addObjectPropertiesByPredicateType(predicateURI, ind);
		
		//wasInformedBy
		predicateURI = PROVPrimerTest.provPrefix+"wasInformedBy";
		addObjectPropertiesByPredicateType(predicateURI, ind);
		
		//wasInfluencedBy
		predicateURI = PROVPrimerTest.provPrefix+"wasInfluencedBy";
		addObjectPropertiesByPredicateType(predicateURI, ind);
	}
	
	/**Adds Object Properties to the current Individual, and adds corresponding predicate to the list (parallel arraylists) */
	public void addObjectPropertiesByPredicateType(String predicateURI, OWLNamedIndividual ind)
	{		
		OWLObjectProperty objectProp = PROVPrimerTest.manager.getOWLDataFactory().getOWLObjectProperty(IRI.create(predicateURI));
		NodeSet<OWLNamedIndividual> owlObjectIndividuals = PROVPrimerTest.reasoner.getObjectPropertyValues( ind, objectProp );
		System.out.println(predicateURI+" Nodes: "+owlObjectIndividuals.getNodes().size());
		
		for(Node<OWLNamedIndividual> owlSameInd : owlObjectIndividuals)
		{
			OWLNamedIndividual currInd = owlSameInd.getRepresentativeElement();

			if(currInd != null)
			{
				String owlIndURI = currInd.toStringID();
				System.out.println("owlIndURI: "+owlIndURI);

				//if the target individual has been previously created, point to that PROVIndividual object
				if(PROVPrimerTest.individualsHM.containsKey( owlIndURI ))
				{
					PROVIndividual targetProvInd = PROVPrimerTest.individualsHM.get( owlSameInd.getRepresentativeElement().toStringID() );

					OPconnectionObjectInds.add(targetProvInd);
					OPconnections.add(predicateURI);
				}
				else //otherwise create a new object with the URI, and add it to the pool of collected PROVIndividuals
				{
					PROVIndividual targetProvInd = new PROVIndividual( owlSameInd.getRepresentativeElement().toStringID() );
					PROVPrimerTest.individualsHM.put(targetProvInd.uri, targetProvInd);

					OPconnectionObjectInds.add(targetProvInd);
					OPconnections.add(predicateURI);
				}
			}
			else
			{
				PROVIndividual anonProvInd = new PROVIndividual( "Anonymous Individual" );
				//**anonProvInd.uri not unique
				PROVPrimerTest.individualsHM.put(anonProvInd.uri, anonProvInd);

				OPconnectionObjectInds.add(anonProvInd);
				OPconnections.add(predicateURI);
			}
		}
	}
	
	
	public void addDataPropertiesByPredicateType(String predicateURI, OWLNamedIndividual ind)
	{
		OWLDataProperty dataProp = PROVPrimerTest.manager.getOWLDataFactory().getOWLDataProperty(IRI.create(predicateURI));
		Set<OWLLiteral> literals = PROVPrimerTest.reasoner.getDataPropertyValues( ind, dataProp );
		
		for(OWLLiteral literal : literals)
		{
			DPconnectionValues.add( literal.getLiteral() );
			DPconnections.add(predicateURI);
		}
	}
	
	public void printObjectProperties()
	{	
		System.out.println("#OPs: "+OPconnections.size());
		int indCntr = 0;
		for(String predicateURI : OPconnections)
		{			
			System.out.println(predicateURI+" -> "+ OPconnectionObjectInds.get(indCntr).uri +" (id:"+ OPconnectionObjectInds.get(indCntr).id +")");
			
			indCntr++;
		}
	}
}