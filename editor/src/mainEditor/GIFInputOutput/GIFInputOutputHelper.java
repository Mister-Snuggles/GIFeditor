package mainEditor.GIFInputOutput;

import javax.imageio.metadata.IIOMetadataNode;

/**
 * A helper class that can manipulate image metadata of GIFs. 
 * Contains the method "getNode" which helps get metadata nodes from a GIF animation.
 * Used in the GIF I/O classes only.
 * <p>
 * Copyright 2017-2018 Joey Sun.<p>
 *  This work is licensed under the Creative Commons Attribution 3.0 Unported
 *  License. To view a copy of this license, visit
 *  http://creativecommons.org/licenses/by/3.0/ or send a letter to Creative
 *  Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA
 */
public final class GIFInputOutputHelper {
	
    //================================================================================
    // Region: Metadata and Frame Drawing Helpers
    //================================================================================
	/**
	 * Returns an existing child node, or creates and returns a new child node (if 
	 * the requested node does not exist).
	 * 
	 * @param rootNode The <tt>IIOMetadataNode</tt> to search for the child node.
	 * @param nodeName The name of the child node.
	 * 
	 * @return the child node, if found or a new node created with the given name.
	 */
    public static IIOMetadataNode getNode(IIOMetadataNode rootNode, String nodeName) {
        int nNodes = rootNode.getLength();
        for (int i = 0; i < nNodes; i++) { // traverses metadata tree
            if (rootNode.item(i).getNodeName().compareToIgnoreCase(nodeName)== 0) { // node is found
            	return((IIOMetadataNode) rootNode.item(i));
            }
        }
        
        // if a node with the given name cannot be found, create one, append it to the root node, and return it.
        IIOMetadataNode node = new IIOMetadataNode(nodeName);
        rootNode.appendChild(node);
        return(node);
    }
    
} // end of GIF animation helper
