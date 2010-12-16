package org.spoofax.jsglr.client.imploder;

import java.util.List;



/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public interface IImplodedTreeFactory<TNode> {

	/**
	 * Create a new non-terminal node (or a terminal with only a constructor).
	 */
	public TNode createNonTerminal(String sort, String constructor, IToken leftToken, IToken rightToken,
			List<TNode> children);
	
	/**
	 * Create a new terminal node for an int value.
	 */
	public TNode createIntTerminal(String sort, IToken token, int value);
	
	/**
	 * Create a new terminal node for an real value.
	 */
	public TNode createRealTerminal(String sort, IToken token, double value);
	
	/**
	 * Create a new terminal node for a string token.
	 */
	public TNode createStringTerminal(String sort, String value, IToken token);
	
	public TNode createTuple(String elementSort, IToken leftToken, IToken rightToken, List<TNode> children);
	
	/**
	 * Create a new node list. 
	 */
	public TNode createList(String elementSort, IToken leftToken, IToken rightToken, List<TNode> children);

	public TNode createAmb(List<TNode> alternatives);

	/**
	 * Create an injection node.
	 */
	public TNode createInjection(String sort, List<TNode> children);
	
	public boolean isStringTerminal(TNode node);
	
	public Iterable<TNode> getChildren(TNode node);
}
