package org.spoofax.jsglr.client.imploder;

/**
 * An interface for tree nodes with tokens.
 * 
 * @see ITreeFactory
 *      A factory interface that typically constructs nodes of this type.
 * 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public interface IAstNode {
	IToken getLeftToken();
	
	IToken getRightToken();
	
	int getChildCount();
	
	IAstNode getChildAt(int i);
	
	String getSort();

	/**
	 * The element sort for lists and tuples.
	 * 
	 * @throws UnsupportedOperationException
	 *             If the node is not a list or tuple.
	 */
	String getElementSort();
	
	boolean isList();
}