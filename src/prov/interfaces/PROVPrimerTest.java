package prov.interfaces;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.util.OWLOntologyMerger;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

public class PROVPrimerTest
{
	/** Hash map for all the nodes (URI -> number in graph) */
	HashMap<String, PROVIndividual> individualsHM = new HashMap<String, PROVIndividual>();
	

	public String getGraph(String URI)
	{
		//String uri = "http://trust.utep.edu/web-probe/primer-turtle-examples.ttl"; //"http://www.w3.org/TR/prov-primer/primer-turtle-examples.ttl";


		//build graph from URI
		try
		{
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			
			// Load PROV
			IRI iri = IRI.create(URI);//"http://www.co-ode.org/ontologies/pizza/pizza.owl");
			OWLOntology primerExampleOntology = manager.loadOntologyFromOntologyDocument(iri);
			System.out.println("Loaded ontology: " + primerExampleOntology);
			
			//OWLOntology prov = manager.loadOntologyFromOntologyDocument(IRI.create("http://www.w3.org/ns/prov-o"));
			
			
			//Pellet Reasoner
			System.out.println("----- Reasoner -----");
			
			PelletReasoner reasoner = PelletReasonerFactory.getInstance().createReasoner( primerExampleOntology );
			reasoner.getKB().realize();
			reasoner.getKB().printClassTree();
			
			OWLClass provActivityClass = manager.getOWLDataFactory().getOWLClass( IRI.create("http://www.w3.org/ns/prov#Activity") );
			
			//get Individuals of Activity class.
			NodeSet<OWLNamedIndividual> individuals = reasoner.getInstances( provActivityClass , false);
			
			for(Node<OWLNamedIndividual> sameInd : individuals) 
			{
				OWLNamedIndividual ind = sameInd.getRepresentativeElement();
				Set<OWLLiteral> names = reasoner.getDataPropertyValues( ind, foafName );	    		    		    	
			    NodeSet<OWLClass> types = reasoner.getTypes( ind, true );		    
			    NodeSet<OWLNamedIndividual> homepages = reasoner.getObjectPropertyValues( ind, workHomepage );
				
				System.out.println( ind.toStringID() );
			}
			
			System.out.println("----- End of Reasoner Example -----");
			
			/*
			System.out.println("----- Activities -----");
			//Activities
			//Get individuals of type prov:Activity.
			OWLClass provActivityClass = factory.getOWLClass( IRI.create("http://www.w3.org/ns/prov#Activity") );
			Set<OWLIndividual> activityIndividuals = provActivityClass.getIndividuals(merged);//primerExampleOntology);
			
			//Get HashMap of activities
			getActivities(activityIndividuals, primerExampleOntology);
			
			
			System.out.println("----- Agents -----");
			//Agents
			//Get individuals of type prov:Agent.
			OWLClass provAgentClass = factory.getOWLClass( IRI.create("http://www.w3.org/ns/prov#Agent") );
			Set<OWLIndividual> agentIndividuals = provAgentClass.getIndividuals(primerExampleOntology);
			
			//Get HashMap of activities
			getAgents(agentIndividuals, primerExampleOntology);
			
			
			
			System.out.println("----- Enitities -----");
			//Entities
			//Get individuals of type prov:Entity.
			OWLClass provEntityClass = factory.getOWLClass( IRI.create("http://www.w3.org/ns/prov#Entity") );
			Set<OWLIndividual> entityIndividuals = provEntityClass.getIndividuals(primerExampleOntology);
			
			//Get HashMap of activities
			getEntities(entityIndividuals, primerExampleOntology);
			
			*/
			
		} 
		catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}

		//convert Java object of Graph to JSON
		return "Done"; //convertToJSON();
	}
	
	/** Get HashMap of activities (Key=URI, value = PROVIndividual) */
	public void getActivitiesTest(Set<OWLIndividual> activityIndividuals, OWLOntology ontology)
	{
		
		Iterator<OWLIndividual> activityIterator = activityIndividuals.iterator();
		
		int activityCntr = 1;
		while(activityIterator.hasNext())
		{
			OWLIndividual ind = activityIterator.next();
			
			PROVIndividual provInd = new PROVIndividual(ind.toStringID(), "Activity");
			
			//*add connections
			java.util.Set<OWLObjectPropertyAssertionAxiom> OPaxioms = ontology.getObjectPropertyAssertionAxioms(ind);
			
			System.out.println("Number of OPs: "+OPaxioms.size());
			
			
			individualsHM.put(ind.toStringID(), provInd);
			
			System.out.println(activityCntr+" : "+ provInd.id +", "+ provInd.type +", "+ provInd.uri );
			activityCntr++;
		}
	}
	
	
	
	/** Get HashMap of activities (Key=URI, value = PROVIndividual) */
	public void getActivities(Set<OWLIndividual> activityIndividuals, OWLOntology ontology)
	{
		
		Iterator<OWLIndividual> activityIterator = activityIndividuals.iterator();
		
		int activityCntr = 1;
		while(activityIterator.hasNext())
		{
			OWLIndividual ind = activityIterator.next();
			
			PROVIndividual provInd = new PROVIndividual(ind.toStringID(), "Activity");
			
			//*add connections
			Map<OWLObjectPropertyExpression,Set<OWLIndividual>> OPvaluesMap = ind.getObjectPropertyValues(ontology);
			Map<OWLDataPropertyExpression,Set<OWLLiteral>> DPvaluesMap = ind.getDataPropertyValues(ontology);
			
			System.out.println("Object Property Map size: "+OPvaluesMap.size());
			System.out.println("Data Property Map size: "+DPvaluesMap.size());
			
			provInd.addOPConnections(OPvaluesMap);
			provInd.addDPConnections(DPvaluesMap);
			
			individualsHM.put(ind.toStringID(), provInd);
			
			System.out.println(activityCntr+" : "+ provInd.id +", "+ provInd.type +", "+ provInd.uri );
			activityCntr++;
		}
	}
	
	
	/** Get HashMap of activities (Key=URI, value = PROVIndividual) */
	public void getAgents(Set<OWLIndividual> agentIndividuals, OWLOntology ontology)
	{
		
		Iterator<OWLIndividual> agentIterator = agentIndividuals.iterator();
		
		int agentCntr = 1;
		while(agentIterator.hasNext())
		{
			OWLIndividual ind = agentIterator.next();
			
			PROVIndividual provInd = new PROVIndividual(ind.toStringID(), "Agent");
			
			//*add connections
			Map<OWLObjectPropertyExpression,Set<OWLIndividual>> OPvaluesMap = ind.getObjectPropertyValues(ontology);
			Map<OWLDataPropertyExpression,Set<OWLLiteral>> DPvaluesMap = ind.getDataPropertyValues(ontology);
			
			System.out.println("Object Property Map size: "+OPvaluesMap.size());
			System.out.println("Data Property Map size: "+DPvaluesMap.size());
			
			provInd.addOPConnections(OPvaluesMap);
			provInd.addDPConnections(DPvaluesMap);
			
			individualsHM.put(ind.toStringID(), provInd);
			
			System.out.println(agentCntr+" : "+ provInd.id +", "+ provInd.type +", "+ provInd.uri );
			agentCntr++;
		}
	}
	
	
	/** Get HashMap of activities (Key=URI, value = PROVIndividual) */
	public void getEntities(Set<OWLIndividual> entityIndividuals, OWLOntology ontology)
	{
		
		Iterator<OWLIndividual> entityIterator = entityIndividuals.iterator();
		
		int entityCntr = 1;
		while(entityIterator.hasNext())
		{
			OWLIndividual ind = entityIterator.next();
			
			PROVIndividual provInd = new PROVIndividual(ind.toStringID(), "Entity");
			
			//*add connections
			Map<OWLObjectPropertyExpression,Set<OWLIndividual>> OPvaluesMap = ind.getObjectPropertyValues(ontology);
			Map<OWLDataPropertyExpression,Set<OWLLiteral>> DPvaluesMap = ind.getDataPropertyValues(ontology);
			
			System.out.println("Object Property Map size: "+OPvaluesMap.size());
			System.out.println("Data Property Map size: "+DPvaluesMap.size());
			
			provInd.addOPConnections(OPvaluesMap);
			provInd.addDPConnections(DPvaluesMap);
			
			individualsHM.put(ind.toStringID(), provInd);
			
			System.out.println(entityCntr+": "+ provInd.id +"(ID), "+ provInd.type +", "+ provInd.uri );
			entityCntr++;
		}
	}
	
	
	public static void main(String args[])
	{
		//WPJustificationTree tree = new WPJustificationTree(\"http://rio.cs.utep.edu/ciserver/ciprojects/GravityMapProvenance/gravityContourMap.ps_038568341971146025.owl#answer\");
		PROVPrimerTest test = new PROVPrimerTest();
		System.out.println( test.getGraph("https://raw.github.com/hdporras/Web-Probe/master/primer-turtle-examples.ttl") );
		
	}
	
}
