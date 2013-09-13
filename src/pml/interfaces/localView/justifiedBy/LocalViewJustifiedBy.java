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

package pml.interfaces.localView.justifiedBy;

import org.inference_web.iw.pml.pmlj.IWNodeSetOccur;

import pml.interfaces.WPJustificationPMLNode;


public class LocalViewJustifiedBy 
{
	/** current PML Node */
	public WPJustificationPMLNode node;

	
	public LocalViewJustifiedBy(IWNodeSetOccur ns)
	{
		node = new WPJustificationPMLNode(ns);
	}
	
	
	/** Converts the object into a JSON String representation of the object. */
	public String convertToJSON()
	{
		return node.convertToJSON();
	}
	
}
