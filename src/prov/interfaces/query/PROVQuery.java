package prov.interfaces.query;

import util.JSONUtils;

import java.io.InputStream;
import java.util.ArrayList;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;

public class PROVQuery
{
	Resource query;
	ArrayList<Resource> answers;
	String queryString;
	
	public PROVQuery(Model model)
	{
		query = getQueryResource(model);
		answers = getQueryAnswers(model, query);
		queryString = getQueryValue(model, query);
	}

	public Resource getQueryResource(Model model)
	{
		StmtIterator stmtiter = model.listStatements();

		// print out the predicate, subject and object of each statement
		while (stmtiter.hasNext())
		{
			Statement stmt      = stmtiter.nextStatement();  // get next statement
			Resource  subject   = stmt.getSubject();     // get the subject
			//Property  predicate = stmt.getPredicate();   // get the predicate
			RDFNode   object    = stmt.getObject();      // get the object

			//If it is of type Query...
			if(object.toString().equals("http://provenanceweb.org/ns/pml#Query"))
			{
				System.out.println("\nThis is the Query: "+subject.toString());
				
				return subject;
			}
		}

		return null;
	}
	
	

	public ArrayList<Resource> getQueryAnswers(Model model, Resource query)
	{
		 ArrayList<Resource> answers = new ArrayList<Resource>();
		
		//Get answer from query
		Property hasAnswer = model.getProperty("http://provenanceweb.org/ns/pml#", "hasAnswer");

		if(query != null && query.hasProperty(hasAnswer) )
		{
			StmtIterator answerStmts = query.listProperties(hasAnswer);

			while (answerStmts.hasNext()) {
				Statement stmt      = answerStmts.nextStatement();  // get next statement
				Resource  subject   = stmt.getSubject();     // get the subject
				Property  predicate = stmt.getPredicate();   // get the predicate
				RDFNode   object    = stmt.getObject();      // get the object

				System.out.println("Answer: "+object.toString());
				answers.add( model.getResource( object.toString() ) );
			}
		}
		else
		{
			System.out.println("No Answers Found ");
			return null;
		}
		
		//this.answers = answers;
		return answers;
	}
	
	
	
	public String getQueryValue(Model model, Resource query)
	{
		Property queryValueProp = model.getProperty("http://www.w3.org/ns/prov#", "value");

		if(query != null && query.hasProperty(queryValueProp) )
		{
			Statement valueStmt = query.getProperty(queryValueProp);
			System.out.println("Query Value:  \n" + valueStmt.getObject().toString() );
			
			//queryString = valueStmt.getObject().toString();
			return valueStmt.getObject().toString();
		}
		else
			System.out.println("No Query Value Found ");
		
		return null;
	}

	
	/** Converts the object into a JSON String representation of the object. */
	public String convertToJSON()
	{
		String json = "";

		//Raw and pretty string
		json = 	" \"rawString\" : \""+ queryString + "\" ";

		if(answers != null)
		{

			String jsonArray = " [ ";

			int i;
			for(i=0; i < answers.size() -1 ; i++)
			{
				String currAnswer = JSONUtils.toValidJSONString(answers.get(i).toString());
				jsonArray += " \""+ currAnswer +"\" ,";
			}

			String currAnswer = JSONUtils.toValidJSONString(answers.get(i).toString());
			//insert last element and close the array.
			jsonArray += " \""+ currAnswer +"\" ]";


			json += ", \"answers\" : "+ jsonArray;//+", ";

		}
		else 
			json += ", \"answers\" : null";//+", ";
		
		//Questions
		json += ", \"queryQuestions\" : null";//+", ";


		return "{ "+ json +" }";
	}
	

	public static void main(String[] args)
	{
		String inputFileName = "http://iw.cs.utep.edu:8080/visko-web/output/prov-00668797210289025.ttl";//"http://inference-web.org/proofs/tonys/tonys.owl#tonys";//"http://minas.cs.utep.edu/web-probe/visko-provenance/prov-0057579567096369466.ttl";


		// create an empty model
		Model model = ModelFactory.createDefaultModel();

		InputStream in = FileManager.get().open( inputFileName );
		if (in == null)
		{
			throw new IllegalArgumentException( "File: " + inputFileName + " not found");
		}

		// read the RDF/Turtle file
		model.read(in, "", "TURTLE");

		
		PROVQuery pq = new PROVQuery(model);		
		ArrayList<Resource> answers = pq.answers;
		String queryValue = pq.queryString;
		
		
		//Print final results
		System.out.println("Answers: ");
		for(int i=0; i < answers.size(); i++)
		{
			System.out.println((i+1)+". " + answers.get(i).toString());
		}
		
		System.out.println("");
		System.out.println("Query Value: "+ queryValue);
		
		System.out.println("JSON:");
		System.out.println(pq.convertToJSON());
		
	}
}
