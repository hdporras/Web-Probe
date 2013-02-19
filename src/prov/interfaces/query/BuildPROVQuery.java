package prov.interfaces.query;

import java.io.InputStream;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

public class BuildPROVQuery
{
	public static String buildQuery(String uri)
	{
		// create an empty model
		Model model = ModelFactory.createDefaultModel();

		InputStream in = FileManager.get().open( uri );
		if (in == null)
		{
			throw new IllegalArgumentException( "File: " + uri + " not found");
		}

		// read the RDF/Turtle file
		model.read(in, "", "TURTLE");

		
		PROVQuery pq = new PROVQuery(model);
		return pq.convertToJSON();
	}
}
