package org.spoofax.jsglr.client;

import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.jsglr.client.imploder.ITokenizer;

public interface ITreeBuilder {

	void initializeTable(ParseTable table, int productionCount, int labelStart, int labelCount);
	void initializeLabel(int labelNumber, IStrategoAppl parseTreeProduction);
	void initializeInput(String filename, String input);
	
	Object buildTree(AbstractParseNode node);
	Object buildTreeTop(Object subtree, int ambiguityCount);
	
	/**
	 * Gets the tokenizer, if applicable, or null.
	 */
	ITokenizer getTokenizer();
}
