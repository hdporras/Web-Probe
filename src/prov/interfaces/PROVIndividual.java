/**
 * @author Hugo Porras
 * 
 * Acknowledgements:
 * This work used resources from Cyber-ShARE Center of Excellence, 
 * which is supported by National Science Foundation grant number 
 * HRD-0734825. Unless otherwise stated, work by Cyber-ShARE is 
 * licensed under a Creative Commons Attribution 3.0 Unported 
 * License. Permissions beyond the scope of this license may be 
 * available at 
 * http://cybershare.utep.edu/content/cyber-share-acknowledgment.
 */

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
	private static int anonIndsCntr = 0;
	
	/** Number ID (for D3 JSON graph)*/
	int id;

	/** Individual URI */
	String uri;
	
	/** Individual Type */
	OWLClass[] types;
	
	/** Graph Group */
	String group;
	
	//Individual's Object and Data Property Connections.
	/** Individual's Object Property Connections.*/
	ArrayList<String> OPconnections;
	ArrayList<PROVIndividual> OPconnectionObjectInds;
	
	/** Individual's Data Property Connections.*/
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
	
	public void setGroup(String group)
	{
		this.group = group;
	}
	
	public static int getLastID()
	{
		return lastID;
	}
	
//Activities
	/** Gather information about all Activities. Indivisduals are placed in a HashMap (individualsHM: Key=URI, value = PROVIndividual) */
	public void addActivity(OWLNamedIndividual ind)
	{
		//Add list of Object Property values associated with ind.
		addAllActivityOPs(ind);
		
		//Add list of Data Property values associated with ind.
		addAllActivityDPs(ind);
		
		//get Types
		NodeSet<OWLClass> types = PROVGraph.reasoner.getTypes( ind, true );		    
		OWLClass[] typesArray = types.getFlattened().toArray(new OWLClass[0]);
		setTypes(typesArray);
		
	}
	
	//Activity Properties
	/** Get the Data properties associated with this prov:Activity */
	public void addAllActivityDPs(OWLNamedIndividual ind)
	{
		//endedAtTime
		String dataPropertyURI = PROVGraph.provPrefix+"endedAtTime";
		addDataPropertiesByPredicateType(dataPropertyURI, ind);
			    
		//endedAtTime
		dataPropertyURI = PROVGraph.provPrefix+"startedAtTime";
		addDataPropertiesByPredicateType(dataPropertyURI, ind);
		
	}
	
	/** Get the Object properties associated with this prov:Activity */
	public void addAllActivityOPs(OWLNamedIndividual ind)
	{
		//*Better way of getting all object/data properties associated with the individual.

		//generated
		String predicateURI = PROVGraph.provPrefix+"generated";
		addObjectPropertiesByPredicateType(predicateURI, ind);

		//qualifiedAssociation
		predicateURI = PROVGraph.provPrefix+"qualifiedAssociation";
		addObjectPropertiesByPredicateType(predicateURI, ind);

		//wasAssociatedWith
		predicateURI = PROVGraph.provPrefix+"wasAssociatedWith";
		addObjectPropertiesByPredicateType(predicateURI, ind);

		//qualifiedEnd
		predicateURI = PROVGraph.provPrefix+"qualifiedEnd";
		addObjectPropertiesByPredicateType(predicateURI, ind);

		//wasEndedBy
		predicateURI = PROVGraph.provPrefix+"wasEndedBy";
		addObjectPropertiesByPredicateType(predicateURI, ind);

		//qualifiedUsage
		predicateURI = PROVGraph.provPrefix+"qualifiedUsage";
		addObjectPropertiesByPredicateType(predicateURI, ind);

		//used
		predicateURI = PROVGraph.provPrefix+"used";
		addObjectPropertiesByPredicateType(predicateURI, ind);

		//qualifiedStart
		predicateURI = PROVGraph.provPrefix+"qualifiedStart";
		addObjectPropertiesByPredicateType(predicateURI, ind);

		//wasInformedBy
		predicateURI = PROVGraph.provPrefix+"wasInformedBy";
		addObjectPropertiesByPredicateType(predicateURI, ind);

		//wasStartedBy
		predicateURI = PROVGraph.provPrefix+"wasStartedBy";
		addObjectPropertiesByPredicateType(predicateURI, ind);

		//qualifiedCommunication
		predicateURI = PROVGraph.provPrefix+"qualifiedCommunication";
		addObjectPropertiesByPredicateType(predicateURI, ind);

		//wasInfluencedBy
		predicateURI = PROVGraph.provPrefix+"wasInfluencedBy";
		addObjectPropertiesByPredicateType(predicateURI, ind);

		//qualifiedInfluence
		predicateURI = PROVGraph.provPrefix+"qualifiedInfluence";
		addObjectPropertiesByPredicateType(predicateURI, ind);

		//atLocation
		predicateURI = PROVGraph.provPrefix+"atLocation";
		addObjectPropertiesByPredicateType(predicateURI, ind);
	}

	
//Entities
	/** Gather information about all Entities. Indivisduals are placed in a HashMap (individualsHM: Key=URI, value = PROVIndividual) */
	public void addEntity(OWLNamedIndividual ind)
	{
		//Add list of Object Property values associated with ind.
		addAllEntityOPs(ind);

		//Add list of Data Property values associated with ind.
		addAllEntityDPs(ind);

		//get Types
		NodeSet<OWLClass> types = PROVGraph.reasoner.getTypes( ind, true );		    
		OWLClass[] typesArray = types.getFlattened().toArray(new OWLClass[0]);
		setTypes(typesArray);

	}

	//Entity Properties
	/** Get the Data properties associated with this prov:Entity */
	public void addAllEntityDPs(OWLNamedIndividual ind)
	{
		//wasInvalidatedBy
		String dataPropertyURI = PROVGraph.provPrefix+"wasInvalidatedBy";
		addDataPropertiesByPredicateType(dataPropertyURI, ind);

		//generatedAtTime
		dataPropertyURI = PROVGraph.provPrefix+"generatedAtTime";
		addDataPropertiesByPredicateType(dataPropertyURI, ind);

		//value
		dataPropertyURI = PROVGraph.provPrefix+"value";
		addDataPropertiesByPredicateType(dataPropertyURI, ind);
	}

	/** Get the Object properties associated with this prov:Entity */
	public void addAllEntityOPs(OWLNamedIndividual ind)
	{
		//*Better way of getting all object/data properties associated with the individual.

		//wasAttributedTo
		String predicateURI = PROVGraph.provPrefix+"wasAttributedTo";
		addObjectPropertiesByPredicateType(predicateURI, ind);

		//qualifiedGeneration
		predicateURI = PROVGraph.provPrefix+"qualifiedGeneration";
		addObjectPropertiesByPredicateType(predicateURI, ind);

		//wasGeneratedBy
		predicateURI = PROVGraph.provPrefix+"wasGeneratedBy";
		addObjectPropertiesByPredicateType(predicateURI, ind);

		//wasDerivedFrom
		predicateURI = PROVGraph.provPrefix+"wasDerivedFrom";
		addObjectPropertiesByPredicateType(predicateURI, ind);

		//specializationOf
		predicateURI = PROVGraph.provPrefix+"specializationOf";
		addObjectPropertiesByPredicateType(predicateURI, ind);

		//qualifiedDerivation
		predicateURI = PROVGraph.provPrefix+"qualifiedDerivation";
		addObjectPropertiesByPredicateType(predicateURI, ind);

		//qualifiedInvalidation
		predicateURI = PROVGraph.provPrefix+"qualifiedInvalidation";
		addObjectPropertiesByPredicateType(predicateURI, ind);

		//qualifiedQuotation
		predicateURI = PROVGraph.provPrefix+"qualifiedQuotation";
		addObjectPropertiesByPredicateType(predicateURI, ind);

		//hadPrimarySource
		predicateURI = PROVGraph.provPrefix+"hadPrimarySource";
		addObjectPropertiesByPredicateType(predicateURI, ind);

		//qualifiedPrimarySource
		predicateURI = PROVGraph.provPrefix+"qualifiedPrimarySource";
		addObjectPropertiesByPredicateType(predicateURI, ind);

		//alternateOf
		predicateURI = PROVGraph.provPrefix+"alternateOf";
		addObjectPropertiesByPredicateType(predicateURI, ind);

		//wasInvalidatedBy
		predicateURI = PROVGraph.provPrefix+"wasInvalidatedBy";
		addObjectPropertiesByPredicateType(predicateURI, ind);

		//qualifiedAttribution
		predicateURI = PROVGraph.provPrefix+"qualifiedAttribution";
		addObjectPropertiesByPredicateType(predicateURI, ind);

		//wasQuotedFrom
		predicateURI = PROVGraph.provPrefix+"wasQuotedFrom";
		addObjectPropertiesByPredicateType(predicateURI, ind);

		//qualifiedRevision
		predicateURI = PROVGraph.provPrefix+"qualifiedRevision";
		addObjectPropertiesByPredicateType(predicateURI, ind);

		//wasRevisionOf
		predicateURI = PROVGraph.provPrefix+"wasRevisionOf";
		addObjectPropertiesByPredicateType(predicateURI, ind);
	}

	
//Agents
	/** Gather information about all Agents. Indivisduals are placed in a HashMap (individualsHM: Key=URI, value = PROVIndividual) */
	public void addAgent(OWLNamedIndividual ind)
	{
		//Add list of Object Property values associated with ind.
		addAllAgentOPs(ind);

		//Add list of Data Property values associated with ind.
		//addAllAgentDPs(ind);

		//get Types
		NodeSet<OWLClass> types = PROVGraph.reasoner.getTypes( ind, true );		    
		OWLClass[] typesArray = types.getFlattened().toArray(new OWLClass[0]);
		setTypes(typesArray);

	}

	//Agent Properties
	/** Get the Data properties associated with this prov:Agent */
	/*
	public void addAllAgentDPs(OWLNamedIndividual ind)
	{
		
		//text
		String dataPropertyURI = PROVPrimerTest.provPrefix+"text";
		addDataPropertiesByPredicateType(dataPropertyURI, ind);

		//text
		dataPropertyURI = PROVPrimerTest.provPrefix+"text";
		addDataPropertiesByPredicateType(dataPropertyURI, ind);
	}
	*/

	/** Get the Object properties associated with this prov:Agent */
	public void addAllAgentOPs(OWLNamedIndividual ind)
	{
		//*Better way of getting all object/data properties associated with the individual.

		//actedOnBehalfOf
		String predicateURI = PROVGraph.provPrefix+"actedOnBehalfOf";
		addObjectPropertiesByPredicateType(predicateURI, ind);

		//qualifiedDelegation
		predicateURI = PROVGraph.provPrefix+"qualifiedDelegation";
		addObjectPropertiesByPredicateType(predicateURI, ind);

		//wasInfluencedBy
		predicateURI = PROVGraph.provPrefix+"wasInfluencedBy";
		addObjectPropertiesByPredicateType(predicateURI, ind);

		//qualifiedInfluence
		predicateURI = PROVGraph.provPrefix+"qualifiedInfluence";
		addObjectPropertiesByPredicateType(predicateURI, ind);

		//atLocation
		predicateURI = PROVGraph.provPrefix+"atLocation";
		addObjectPropertiesByPredicateType(predicateURI, ind);
	}


	
	/**Adds Object Properties to the current Individual, and adds corresponding predicate to the list (parallel arraylists) */
	public void addObjectPropertiesByPredicateType(String predicateURI, OWLNamedIndividual ind)
	{		
		OWLObjectProperty objectProp = PROVGraph.manager.getOWLDataFactory().getOWLObjectProperty(IRI.create(predicateURI));
		NodeSet<OWLNamedIndividual> owlObjectIndividuals = PROVGraph.reasoner.getObjectPropertyValues( ind, objectProp );
		System.out.println(predicateURI+" Nodes: "+owlObjectIndividuals.getNodes().size());
		
		for(Node<OWLNamedIndividual> owlSameInd : owlObjectIndividuals)
		{
			OWLNamedIndividual currInd = owlSameInd.getRepresentativeElement();

			if(currInd != null)
			{
				String owlIndURI = currInd.toStringID();
				System.out.println("owlIndURI: "+owlIndURI);

				//if the target individual has been previously created, point to that PROVIndividual object
				if(PROVGraph.individualsHM.containsKey( owlIndURI ))
				{
					PROVIndividual targetProvInd = PROVGraph.individualsHM.get( owlSameInd.getRepresentativeElement().toStringID() );

					OPconnectionObjectInds.add(targetProvInd);
					OPconnections.add(predicateURI);
				}
				else //otherwise create a new object with the URI, and add it to the pool of collected PROVIndividuals
				{
					PROVIndividual targetProvInd = new PROVIndividual( owlSameInd.getRepresentativeElement().toStringID() );
					PROVGraph.individualsHM.put(targetProvInd.uri, targetProvInd);

					OPconnectionObjectInds.add(targetProvInd);
					OPconnections.add(predicateURI);
				}
			}
			else
			{
				PROVIndividual anonProvInd = new PROVIndividual( "Anonymous Individual " + (++anonIndsCntr));
				//**anonProvInd.uri not unique
				PROVGraph.individualsHM.put(anonProvInd.uri, anonProvInd);

				OPconnectionObjectInds.add(anonProvInd);
				OPconnections.add(predicateURI);
			}
		}
	}
	
	/** Adds Data Properties to the list. */
	public void addDataPropertiesByPredicateType(String predicateURI, OWLNamedIndividual ind)
	{
		OWLDataProperty dataProp = PROVGraph.manager.getOWLDataFactory().getOWLDataProperty(IRI.create(predicateURI));
		Set<OWLLiteral> literals = PROVGraph.reasoner.getDataPropertyValues( ind, dataProp );
		
		for(OWLLiteral literal : literals)
		{
			DPconnectionValues.add( literal.getLiteral() );
			DPconnections.add(predicateURI);
		}
	}
	
	/** Print out the Individual's OPs */
	public void printObjectProperties()
	{	
		System.out.println();
		System.out.println("---"+ id +": "+ types.toString() +", "+ uri +"---");
		System.out.println("#OPs: "+OPconnections.size());
		int indCntr = 0;
		for(String predicateURI : OPconnections)
		{			
			System.out.println(predicateURI+" -> "+ OPconnectionObjectInds.get(indCntr).uri +" (id:"+ OPconnectionObjectInds.get(indCntr).id +")");
			
			indCntr++;
		}
	}
	
	
	/** Create and return JSON representation of this Individual */
	public String toJSON()
	{
		String json = "";
		
		json = 	" \"name\" : \""+ uri +"\", " +
				" \"indID\" : \""+ id +"\", " +
				//" \"types\" : \""+ types.toJSON() +"\", " +
				" \"group\" : \""+ group +"\" ";
		
		return "{ "+ json +" }";
	}
	
}