package org.spoofax.jsglr.client.imploder;


/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public interface IAstNode {
	IToken getLeftToken();
	
	IToken getRightToken();
	
	int getChildCount();
	
	IAstNode getChildAt(int i);
	
	String getSort();
}
