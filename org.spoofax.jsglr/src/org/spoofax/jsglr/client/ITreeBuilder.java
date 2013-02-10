package org.spoofax.jsglr.client;

import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.jsglr.client.imploder.ITokenizer;

/**
 * Top-level interface for participants of the tree building process during parsing. 
 */
public interface ITreeBuilder {

	/**
	 * Informs the tree builder about the properties of the parse table that will
	 * be used during parsing. This method is called once before the parsing starts.
	 */
	void initializeTable(ParseTable table, int productionCount, int labelStart, int labelCount);
	
	/** 
	 * Informs the tree builder about all of the parse tree productions that will 
	 * be used during parsing. This method call is called repeatedly until the 
	 * productions for all known labels have been accounted for.
	 * 
	 * @param labelNumber
	 * @param parseTreeProduction
	 */
	void initializeLabel(int labelNumber, IStrategoAppl parseTreeProduction);
	
	/**
	 * Informs the tree builder about the document that will be be parsed. This 
	 * method is called once before parsing starts.
	 */
	void initializeInput(String input, String filename);
	
	Object buildTree(AbstractParseNode node);
	Object buildTreeTop(Object subtree, int ambiguityCount);
	
	/**
	 * Called before parsing starts so that the tree builder can reset internal
	 * state and be ready for another parsing pass.
	 */
	void reset();

	void reset(int startOffset);

	/**
	 * The tree builder should return an initialized tokenizer, if applicable,
	 * or null.
	 * 
	 * @return initialized tokenizer, or null.
	 */
	ITokenizer getTokenizer();
}
