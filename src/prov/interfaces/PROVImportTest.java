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

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

public class PROVImportTest
{
    /** This example shows how to use IRI mappers to redirect imports and
     * loading.
     * 
     * @throws OWLOntologyCreationException */
    public void shouldUseIRIMappers() throws OWLOntologyCreationException 
    {
    	
    	/*
    	IRI provPrimer = IRI.create("https://raw.github.com/hdporras/Web-Probe/master/primer-turtle-examples.ttl");
        IRI PROV_ONTOLOGY_IRI = IRI.create("http://www.w3.org/ns/prov-o");
        
        // Create a manager to work with
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        // Load the MGED ontology. There is a copy of the MGED ontology located
        // at the address pointed to by its ontology IRI (this is good practice
        // and is recommended in the OWL 2 spec).
        OWLOntology ontology = manager.loadOntology(provPrimer);
        // Print out the ontology IRI and its imported ontology IRIs
        printOntologyAndImports(manager, ontology);
        
        reasoner = PelletReasonerFactory.getInstance().createReasoner( ontology );
		reasoner.getKB().realize();
		reasoner.getKB().printClassTree();
    	 * 
    	 */
    	
    	
        IRI MGED_ONTOLOGY_IRI = IRI
                .create("http://mged.sourceforge.net/ontologies/MGEDOntology.owl");
        IRI PROTEGE_ONTOLOGY_IRI = IRI
                .create("http://protege.stanford.edu/plugins/owl/protege");
        IRI TONES_REPOSIITORY_IRI = IRI
                .create("http://owl.cs.manchester.ac.uk/repository/download");
        // Create a manager to work with
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        // Load the MGED ontology. There is a copy of the MGED ontology located
        // at the address pointed to by its ontology IRI (this is good practice
        // and is recommended in the OWL 2 spec).
        OWLOntology ontology = manager.loadOntology(MGED_ONTOLOGY_IRI);
        // Print out the ontology IRI and its imported ontology IRIs
        printOntologyAndImports(manager, ontology);
        
        
        
        // We'll load the MGED ontology again, but this time, we'll get the
        // Protege ontology (that it imports) from the TONES repository. To tell
        // the ontology manager to do this we need to add an IRI mapper. We need
        // an implementation of OWLOntologyIRIMapper. Given and IRI and
        // OWLOntologyIRIMapper simply returns some other IRI. There are quite a
        // few implementations of IRI mapper in the OWL API, here we will just
        // use a really basic implementation that maps a specific IRI to another
        // specific IRI. Create a mapper that maps the Protege ontology IRI to
        // the document IRI that points to a copy in the TONES ontology
        // repository.
        IRI protegeOntologyDocumentIRI = getTONESRepositoryDocumentIRI(
                PROTEGE_ONTOLOGY_IRI, TONES_REPOSIITORY_IRI);
        OWLOntologyIRIMapper iriMapper = new SimpleIRIMapper(PROTEGE_ONTOLOGY_IRI,
                protegeOntologyDocumentIRI);
        System.out.println();
        System.out.println();
        // Create a new manager that we will use to load the MGED ontology
        OWLOntologyManager manager2 = OWLManager.createOWLOntologyManager();
        // Register our mapper with the manager
        manager2.addIRIMapper(iriMapper);
        // Now load our MGED ontology
        OWLOntology ontology2 = manager2.loadOntology(MGED_ONTOLOGY_IRI);
        // Print out the details
        printOntologyAndImports(manager2, ontology2);
        
        
        // Notice that the document IRI of the protege ontology is different to
        // the document IRI of the ontology when it was loaded the first time.
        // This is due to the mapper redirecting the ontology loader. For
        // example, AutoIRIMapper: An AutoIRIMapper finds ontologies in a local
        // folder and maps their IRIs to their locations in this folder We
        // specify a directory/folder where the ontologies are located. In this
        // case we've just specified the tmp directory.
        File file = new File("/tmp");
        // We can also specify a flag to indicate whether the directory should
        // be searched recursively.
        OWLOntologyIRIMapper autoIRIMapper = new AutoIRIMapper(file, false);
        // We can now use this mapper in the usual way, i.e.
        manager2.addIRIMapper(autoIRIMapper);
        // Of course, applications (such as Protege) usually implement their own
        // mappers to deal with specific application requirements.
    }
    
    
    
    private static void printOntologyAndImports(OWLOntologyManager manager,
            OWLOntology ontology) {
        System.out.println("Loaded ontology:");
        // Print ontology IRI and where it was loaded from (they will be the
        // same)
        printOntology(manager, ontology);
        // List the imported ontologies
        for (OWLOntology importedOntology : ontology.getImports()) {
            System.out.println("Imports:");
            printOntology(manager, importedOntology);
        }
    }

    /** Prints the IRI of an ontology and its document IRI.
     * 
     * @param manager
     *            The manager that manages the ontology
     * @param ontology
     *            The ontology */
    private static void printOntology(OWLOntologyManager manager, OWLOntology ontology) {
        IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI();
        IRI documentIRI = manager.getOntologyDocumentIRI(ontology);
        System.out.println(ontologyIRI == null ? "anonymous" : ontologyIRI
                .toQuotedString());
        System.out.println("    from " + documentIRI.toQuotedString());
    }

    /** Convenience method that obtains the document IRI of an ontology contained
     * in the TONES ontology repository given the ontology IRI. The TONES
     * repository contains various ontologies of interest to reasoner developers
     * and tools developers. Ontologies in the repository may be accessed in a
     * RESTful way (see http://owl.cs.manchester.ac.uk/repository/) for more
     * details). We basically get an ontology by specifying the repository IRI
     * with an ontology query parameter that has the ontology IRI that we're
     * after as its value.
     * 
     * @param ontologyIRI
     *            The IRI of the ontology.
     * @return The document IRI of the ontology in the TONES repository. */
    private static IRI getTONESRepositoryDocumentIRI(IRI ontologyIRI, IRI Tones) {
        StringBuilder sb = new StringBuilder();
        sb.append(Tones);
        sb.append("?ontology=");
        sb.append(ontologyIRI);
        return IRI.create(sb.toString());
    }
    
    
    public static void main(String[] args)
    {
    	PROVImportTest test = new PROVImportTest();
    	
    	try 
    	{
			test.shouldUseIRIMappers();
		} 
    	catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
    }
}
