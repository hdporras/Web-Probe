package prov.interfaces;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

public class PROVPrimerTest
{

	public String getGraph(String URI)
	{
		String uri = "http://trust.utep.edu/web-probe/primer-turtle-examples.ttl"; //"http://www.w3.org/TR/prov-primer/primer-turtle-examples.ttl";


		//build graph from URI
		try
		{
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			
			// Let's load an ontology from the web
			IRI iri = IRI.create(uri);//"http://www.co-ode.org/ontologies/pizza/pizza.owl");
			OWLOntology primerExampleOntology = manager.loadOntologyFromOntologyDocument(iri);
			System.out.println("Loaded ontology: " + primerExampleOntology);
			
			OWLDataFactory factory = manager.getOWLDataFactory();
			
			//get individuals of type prov:Activity.
			OWLClass provActivity = factory.getOWLClass( IRI.create("http://www.w3.org/ns/prov#Activity") );
			Set<OWLIndividual> activityIndividuals = provActivity.getIndividuals(primerExampleOntology);
			
			Iterator<OWLIndividual> activityIterator = activityIndividuals.iterator();
			
			//Hash map for all the nodes (URI -> number in graph)
			HashMap<String, PROVIndividual> nodesHM = new HashMap<String, PROVIndividual>();
			
			int activityCntr = 1;
			while(activityIterator.hasNext())
			{
				OWLIndividual ind = activityIterator.next();
				
				PROVIndividual provInd = new PROVIndividual(ind.toStringID(), "Activity");
				
				//*add connections
				
				nodesHM.put(ind.toStringID(), provInd);
				
				System.out.println(activityCntr+" : "+ provInd.id +", "+ provInd.type +", "+ provInd.uri );
				activityCntr++;
			}
			
			
		} 
		catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}

		//convert Java object of Graph to JSON
		return "Done"; //convertToJSON();
	}

	
	/** Converts the object into a JSON String representation of the object. */
	public String convertToJSON()
	{
		String json = "\"nodes\":[" +
						   " {\"name\":\"Myriel\",\"group\":1}," +
				           " {\"name\":\"Napoleon\",\"group\":1}," +
				           " {\"name\":\"Mlle.Baptistine\",\"group\":1}," +
				           " {\"name\":\"Mme.Magloire\",\"group\":1}," +
				           " {\"name\":\"CountessdeLo\",\"group\":1}," +
				           " {\"name\":\"Geborand\",\"group\":1}," +
				           " {\"name\":\"Champtercier\",\"group\":1}," +
				           " {\"name\":\"Cravatte\",\"group\":1}," +
				           " {\"name\":\"Count\",\"group\":1}," +
				           " {\"name\":\"Joly\",\"group\":8}," +
				           " {\"name\":\"Grantaire\",\"group\":8}," +
				           " {\"name\":\"MotherPlutarch\",\"group\":9}," +
				           " {\"name\":\"Gueulemer\",\"group\":4}," +
				           " {\"name\":\"Babet\",\"group\":4}," +
				           " {\"name\":\"Claquesous\",\"group\":4}," +
				           " {\"name\":\"Montparnasse\",\"group\":4}," +
				           " {\"name\":\"Toussaint\",\"group\":5}," +
				           " {\"name\":\"Child1\",\"group\":10}," +
				           " {\"name\":\"Child2\",\"group\":10}," +
				           " {\"name\":\"Brujon\",\"group\":4}," +
				           " {\"name\":\"Mme.Hucheloup\",\"group\":8} ]," +
				         " \"links\":[ "+
				           " {\"source\":1,\"target\":0,\"value\":1}," +
				           " {\"source\":2,\"target\":0,\"value\":8}," +
				           " {\"source\":3,\"target\":0,\"value\":10}," +
				           " {\"source\":3,\"target\":2,\"value\":6}," +
				           " {\"source\":4,\"target\":0,\"value\":1}," +
				           " {\"source\":5,\"target\":0,\"value\":1}," +
				           " {\"source\":6,\"target\":0,\"value\":1}," +
				           " {\"source\":7,\"target\":0,\"value\":1}," +
				           " {\"source\":8,\"target\":0,\"value\":2}," +
				           " {\"source\":9,\"target\":0,\"value\":1}," +
				           " {\"source\":11,\"target\":10,\"value\":1}," +
				           " {\"source\":11,\"target\":3,\"value\":3}," +
				           " {\"source\":11,\"target\":2,\"value\":3}," +
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
