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
