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
			getActivities(activityIndividuals, primerExampleOntology);
			
			
		} 
		catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}

		//convert Java object of Graph to JSON
		return "Done"; //convertToJSON();
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
			
			provInd.addOPConnections(OPvaluesMap);
			//provInd.addDPConnections(DPvaluesMap);
			
			individualsHM.put(ind.toStringID(), provInd);
			
			System.out.println(activityCntr+" : "+ provInd.id +", "+ provInd.type +", "+ provInd.uri );
			activityCntr++;
		}
	}

	
	/** Converts the object into a JSON String representation of the object. */
	public String convertToJSON()
	{
		//*
		String json = "\"nodes\":[" +
						   " {\"name\":\"Myriel\",\"group\":1}," +
				           " {\"name\":\"Napoleon\",\"group\":1}," +
				           " {\"name\":\"Mlle.Baptistine\",\"group\":1}," +
				           " {\"name\":\"Mme.Magloire\",\"group\":1}," +
				           " {\"name\":\"Toussaint\",\"group\":5}," +
				           " {\"name\":\"Child1\",\"group\":10}," +
				           " {\"name\":\"Child2\",\"group\":10}," +
				           " {\"name\":\"Brujon\",\"group\":4}," +
				           " {\"name\":\"Mme.Hucheloup\",\"group\":8} ]," +
				         " \"links\":[ "+
				           " {\"source\":1,\"target\":0,\"value\":1}," +
				           " {\"source\":2,\"target\":0,\"value\":8}," +
				           " {\"source\":3,\"target\":0,\"value\":10}," +
				           " {\"source\":11,\"target\":0,\"value\":5}," +
				           " {\"source\":76,\"target\":58,\"value\":1} ]";
		
		return " { "+ json +" } ";
	}
	
	
	public static void main(String args[])
	{
		//WPJustificationTree tree = new WPJustificationTree(\"http://rio.cs.utep.edu/ciserver/ciprojects/GravityMapProvenance/gravityContourMap.ps_038568341971146025.owl#answer\");
		PROVPrimerTest test = new PROVPrimerTest();
		System.out.println( test.getGraph("http://trust.utep.edu/web-probe/primer-turtle-examples.ttl") );
		
	}
	
}
