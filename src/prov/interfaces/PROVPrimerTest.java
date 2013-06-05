package prov.interfaces;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

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
			
			OWLDataFactory factory = manager.getOWLDataFactory();
			
			//Activities
			//Get individuals of type prov:Activity.
			OWLClass provActivityClass = factory.getOWLClass( IRI.create("http://www.w3.org/ns/prov#Activity") );
			Set<OWLIndividual> activityIndividuals = provActivityClass.getIndividuals(primerExampleOntology);
			
			//Get HashMap of activities
			getActivitiesTest(activityIndividuals, primerExampleOntology);
			
			
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
	
	public static void main(String args[])
	{
		//WPJustificationTree tree = new WPJustificationTree(\"http://rio.cs.utep.edu/ciserver/ciprojects/GravityMapProvenance/gravityContourMap.ps_038568341971146025.owl#answer\");
		PROVPrimerTest test = new PROVPrimerTest();
		System.out.println( test.getGraph("http://trust.utep.edu/web-probe/primer-turtle-examples.ttl") );
		
	}
	
}
