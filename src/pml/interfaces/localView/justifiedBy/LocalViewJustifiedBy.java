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
