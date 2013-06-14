package prov.interfaces;

import java.io.File;
import java.util.ArrayList;
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
import org.semanticweb.owlapi.model.AddImport;

import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

public class PROVPrimerTest
{
	/** Hash map for all the nodes (URI -> number in graph) */
	static HashMap<String, PROVIndividual> individualsHM = new HashMap<String, PROVIndividual>();
	
	static OWLOntologyManager manager;
	static PelletReasoner reasoner;
	
	static String provPrefix = "http://www.w3.org/ns/prov#";
	
	public String getGraph(String URI)
	{
		//build graph from URI
		try
		{
			OWLOntologyManager tempManager = OWLManager.createOWLOntologyManager();
			
			// Load PROV
			IRI iri = IRI.create(URI);//"http://www.co-ode.org/ontologies/pizza/pizza.owl");
			OWLOntology ontology = tempManager.loadOntologyFromOntologyDocument(iri);
			System.out.println("Loaded ontology: " + ontology);
			
			//**import PROV ontology
			//OWLOntology prov = manager.loadOntologyFromOntologyDocument(IRI.create("http://www.w3.org/ns/prov-o"));
			OWLImportsDeclaration importDeclaraton = tempManager.getOWLDataFactory().getOWLImportsDeclaration(IRI.create("http://www.w3.org/ns/prov-o")); 
			tempManager.applyChange(new AddImport(ontology, importDeclaraton));
			
			manager = OWLManager.createOWLOntologyManager();
			//***
			
			
			//Pellet Reasoner
			System.out.println("----- Reasoner -----");
			
			reasoner = PelletReasonerFactory.getInstance().createReasoner( ontology );
			reasoner.getKB().realize();
			reasoner.getKB().printClassTree();
			
		
			//Activity
			OWLClass provActivityClass = manager.getOWLDataFactory().getOWLClass( IRI.create(provPrefix+"Activity") );
			
			//get Individuals of Activity class.
			NodeSet<OWLNamedIndividual> activityIndividuals = reasoner.getInstances( provActivityClass , false);
			recordActivities(activityIndividuals, ontology);
			
			
			
			System.out.println("----- End of Reasoner Example -----");
		} 
		catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}

		//convert Java object of Graph to JSON
		return "Done"; //convertToJSON();
	}
	
	/** Gather information about all Activities. Indivisduals are placed in a HashMap (individualsHM: Key=URI, value = PROVIndividual) */
	public void recordActivities(NodeSet<OWLNamedIndividual> individuals, OWLOntology ontology)
	{
		
		for(Node<OWLNamedIndividual> sameInd : individuals) 
		{
			//Rep Individual
			OWLNamedIndividual ind = sameInd.getRepresentativeElement();
			PROVIndividual provInd = new PROVIndividual(ind.toStringID());
			
			PROVPrimerTest.individualsHM.put(ind.toStringID(), provInd);
			
			//Add Activity connections and details to PROVIndividual
			provInd.addActivity(ind);
			
			
			System.out.println("---"+ provInd.id +": "+ provInd.types.toString() +", "+ provInd.uri +"---");
			provInd.printObjectProperties();
		}
		
	}
	
	
	
	public static void main(String args[])
	{
		//WPJustificationTree tree = new WPJustificationTree(\"http://rio.cs.utep.edu/ciserver/ciprojects/GravityMapProvenance/gravityContourMap.ps_038568341971146025.owl#answer\");
		PROVPrimerTest test = new PROVPrimerTest();
		System.out.println( test.getGraph("https://raw.github.com/hdporras/Web-Probe/master/primer-turtle-examples.ttl") );
		
	}
	
}
