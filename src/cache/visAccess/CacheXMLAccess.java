package cache.visAccess;


import java.util.LinkedList;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cacher.fromViskoServlet.Cache;

public class CacheXMLAccess 
{
	//**need to make a static reference to cache location instead of loading the whole cache object.
								//local Run?
	static Cache cache = new Cache(false);

	/** Get list of viewers for a nodesetURI.*/
	public static String[] getViewerLoc(String nodeURI)
	{
		Document xmlDoc = cache.xmlLog.getDocument();
		NodeList nodeList = xmlDoc.getDocumentElement().getChildNodes();

		for(int i=0; i < nodeList.getLength(); i++)
		{
			Node vis = nodeList.item(i);

			//if node is of type VisualizationSet
			if(vis.getNodeName().equalsIgnoreCase("VisualizationSet"))
			{
				//if it's the URI we're looking for...
				if(vis.getAttributes().item(0).getNodeValue().equals(nodeURI))
				{
					NodeList visNodeList = vis.getChildNodes();
					int numChildren = visNodeList.getLength();
					LinkedList<String> viewers = new LinkedList<String>();

					//for every node in Visualization set...
					for(int childN=0; childN < numChildren; childN++)
					{
						Node child = visNodeList.item(childN);
						//when the Node is of type Visualzation
						if(child.getNodeName().equalsIgnoreCase("Visualization"))
						{
							//get the target viewer and add to the list.
							String viewer = child.getAttributes().getNamedItem("targetViewer").getNodeValue();
							if(viewer!=null)
							{
								viewers.add(viewer);
							}
						}
					}

					String[] viewersArray = new String[1];
					viewersArray = viewers.toArray(viewersArray);
					
					if(viewersArray != null && viewersArray[0] != null)
						return viewersArray;
					else
						return null;
				}
			}
		}

		return null;

	}

	/** Get list of visualizations for a nodesetURI.*/
	public static String[] getVisLoc(String nodeURI)
	{
		Document xmlDoc = cache.xmlLog.getDocument();
		NodeList nodeList = xmlDoc.getDocumentElement().getChildNodes();

		for(int i=0; i < nodeList.getLength(); i++)
		{
			Node vis = nodeList.item(i);

			//if node is of type VisualizationSet
			if(vis.getNodeName().equalsIgnoreCase("VisualizationSet"))
			{
				//if it's the URI we're looking for...
				if(vis.getAttributes().item(0).getNodeValue().equals(nodeURI))
				{
					NodeList visNodeList = vis.getChildNodes();
					int numChildren = visNodeList.getLength();
					LinkedList<String> visualizations = new LinkedList<String>();

					//for every node in Visualization set...
					for(int childN=0; childN < numChildren; childN++)
					{
						Node child = visNodeList.item(childN);
						//when the Node is of type Visualzation
						if(child.getNodeName().equalsIgnoreCase("Visualization"))
						{
							System.out.println("Found!");
							//get the target visualization and add to the list.
							String visualization = child.getFirstChild().getNodeValue();
							System.out.println(visualization);
							
							if(visualization!=null)
							{
								visualizations.add(visualization);
							}
						}
					}

					String[] visArray = new String[1];
					visArray = visualizations.toArray(visArray);
					
					if(visArray != null && visArray[0] != null)
						return visArray;
					else
						return null;
				}
			}
		}

		return null;
	}

	/** Get the thumbnail for the specified nodesetURI.*/
	public static String getThumbLoc(String nodeURI)
	{
		Document xmlDoc = cache.xmlLog.getDocument();
		NodeList nodeList = xmlDoc.getDocumentElement().getChildNodes();

		for(int i=0; i < nodeList.getLength(); i++)
		{
			Node vis = nodeList.item(i);

			//if node is of type VisualizationSet
			if(vis.getNodeName().equalsIgnoreCase("VisualizationSet"))
			{
				//if it's the URI we're looking for...
				if(vis.getAttributes().item(0).getNodeValue().equals(nodeURI))
				{
					NodeList visNodeList = vis.getChildNodes();
					int numChildren = visNodeList.getLength();
					LinkedList<String> visualizations = new LinkedList<String>();

					//for every node in Visualization set...
					for(int childN=0; childN < numChildren; childN++)
					{
						Node child = visNodeList.item(childN);
						//when the Node is of type Visualzation
						if(child.getNodeName().equalsIgnoreCase("Thumbnail"))
						{
							System.out.println("Found!");
							//get the target thumbnail.
							String thumb = child.getFirstChild().getNodeValue();
							System.out.println(thumb);
							
							return thumb;
						}
					}
				}
			}
		}
		
		return null;
	}

	public static void main(String[] args)
	{
		String[] viewers = getViewerLoc("http://rio.cs.utep.edu/ciserver/ciprojects/ScientificPublication/ipaw-v4.tex_0016939260199497874.owl#answer");

		for(int i=0; i < viewers.length; i++)
		{
			System.out.println(i+": "+viewers[i]);
		}
		
		String[] vis = getVisLoc("http://rio.cs.utep.edu/ciserver/ciprojects/ScientificPublication/ipaw-v4.tex_0016939260199497874.owl#answer");

		for(int i=0; i < vis.length; i++)
		{
			System.out.println(i+": "+vis[i]);
		}
		
		String thumb = getThumbLoc("http://rio.cs.utep.edu/ciserver/ciprojects/ScientificPublication/ipaw-v4.tex_0016939260199497874.owl#answer");

		System.out.println("Thumbnail: "+thumb);
		
		
		String[] vis2 = getVisLoc("http://rio.cs.utep.edu/ciserver/ciprojects/AGTesting/gravityDataset.txt_07759791711943271.owl#answer");

		for(int i=0; i < vis2.length; i++)
		{
			System.out.println(i+": "+vis2[i]);
		}
	}
}
