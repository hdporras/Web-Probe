package prov.interfaces;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.util.FileManager;

import java.io.*;

public class ViskoPML3Test
{
	public static void main (String args[])
	{

		String inputFileName = "http://iw.cs.utep.edu:8080/visko-web/output/prov-00668797210289025.ttl";//"http://inference-web.org/proofs/tonys/tonys.owl#tonys";//"http://minas.cs.utep.edu/web-probe/visko-provenance/prov-0057579567096369466.ttl";


		// create an empty model
		Model model = ModelFactory.createDefaultModel();

		InputStream in = FileManager.get().open( inputFileName );
		if (in == null) {
			throw new IllegalArgumentException( "File: " + inputFileName + " not found");
		}

		// read the RDF/XML file
		model.read(in, "", "TURTLE");

		// write it to standard out
		model.write(System.out);

		
		System.out.println("\n----------------- Statements --------------------");
		// list the statements in the Model
		StmtIterator iter = model.listStatements();

		// print out the predicate, subject and object of each statement
		while (iter.hasNext()) {
		    Statement stmt      = iter.nextStatement();  // get next statement
		    Resource  subject   = stmt.getSubject();     // get the subject
		    Property  predicate = stmt.getPredicate();   // get the predicate
		    RDFNode   object    = stmt.getObject();      // get the object

		    System.out.print(subject.toString());
		    System.out.print(" " + predicate.toString() + " ");
		    
		    
		    if (object instanceof Resource) {
		       System.out.print(object.toString());
		    } else {
		        // object is a literal
		        System.out.print(" \"" + object.toString() + "\"");
		    }

		    System.out.println(" .");
		} 
		
		
		System.out.println("\n----------------- Resource Tests --------------------");
	
		StmtIterator stmtiter = model.listStatements();
		Resource querySubject = null;
		boolean found = false;

		// print out the predicate, subject and object of each statement
		while (stmtiter.hasNext() && !found) {
		    Statement stmt      = stmtiter.nextStatement();  // get next statement
		    Resource  subject   = stmt.getSubject();     // get the subject
		    Property  predicate = stmt.getPredicate();   // get the predicate
		    RDFNode   object    = stmt.getObject();      // get the object

		    System.out.print(subject.toString());
		    System.out.print(" " + predicate.toString() + " ");
		    
		    if(object.toString().equals("http://provenanceweb.org/ns/pml#Query"))
		    {
		    	found = true;
		    	
		    	System.out.println("\nThis is the Query: "+subject.toString());
		    	
		    	querySubject = subject;
		    	
		    	Statement s = model.getRequiredProperty(subject, predicate);
		    	System.out.println("With Object: "+s.getObject().toString());
		    }
		    
		    if (object instanceof Resource) {
		       System.out.print(object.toString());
		    } else {
		        // object is a literal
		        System.out.print(" \"" + object.toString() + "\"");
		    }

		    System.out.println(" .");
		}
		
		
		//Get answer from query
		Property hasAnswer = model.getProperty("http://provenanceweb.org/ns/pml#", "hasAnswer");
		
		if(querySubject != null && querySubject.hasProperty(hasAnswer) )
		{
			StmtIterator answerStmts = querySubject.listProperties(hasAnswer);
			
			while (answerStmts.hasNext()) {
			    Statement stmt      = answerStmts.nextStatement();  // get next statement
			    Resource  subject   = stmt.getSubject();     // get the subject
			    Property  predicate = stmt.getPredicate();   // get the predicate
			    RDFNode   object    = stmt.getObject();      // get the object

			    System.out.println("Answer: "+object.toString());
			}
		}
		else
			System.out.println("No Answer Found ");
		
		
		//Get Query Value
		Property queryValueProp = model.getProperty("http://www.w3.org/ns/prov#", "value");
		
		if(querySubject != null && querySubject.hasProperty(queryValueProp) )
		{
			Statement valueStmt = querySubject.getProperty(queryValueProp);
			System.out.println("Query Value:  \n" + valueStmt.getObject().toString() );
		}
		else
			System.out.println("No Query Value Found ");
		
		/*ResIterator resIter = model.listSubjectsWithProperty(hasAnswer);
		
		System.out.println("Resources with property "+ hasAnswer.toString() +" :");
		while (resIter.hasNext())
		{
		    Resource r = resIter.nextResource();
		    System.out.println(r.toString());
		}*/

	}
}
