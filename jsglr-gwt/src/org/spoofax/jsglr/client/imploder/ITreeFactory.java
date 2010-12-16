package org.spoofax.jsglr.client.imploder;

import java.util.List;



/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public interface ITreeFactory<TNode> {

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
	 * Creates a new node similar to an existing node,
	 * used for incremental tree construction.
	 */
	public TNode recreateNode(TNode oldNode, IToken leftToken, IToken rightToken, List<TNode> children);

	/**
	 * Create an injection node.
	 */
	public TNode createInjection(String sort, List<TNode> children);
	
	public boolean isStringTerminal(TNode node);
	
	/**
	 * Gets the children of a node.
	 */
	public Iterable<TNode> getChildren(TNode node);
	
	/**
	 * Gets the left token of a node, or null if not supported/applicable.
	 */
	public IToken getLeftToken(TNode node);
	
	/**
	 * Gets the right token of a node, or null if not supported/applicable.
	 */
	public IToken getRightToken(TNode node);
}
