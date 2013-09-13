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

package pml.interfaces;

import pml.loading.Loader;

public class PMLServices
{
	
	
	public static boolean isQuery(String uri)
	{
		Loader loader = new Loader(false);
		
		if ( loader.isQuery(uri) )
		{
			return true;
		}
		else
			return false;
		
	}
	
	public static boolean isNode(String uri)
	{
		Loader loader = new Loader(false);
		
		if ( loader.isNode(uri) )
		{
			return true;
		}
		else
			return false;
	}
}
