package org.spoofax.jsglr.client;

import org.spoofax.jsglr.client.imploder.ITokenizer;
import org.spoofax.jsglr.shared.terms.ATermAppl;

public interface ITreeBuilder {

	void initialize(ParseTable table, int productionCount, int labelStart, int labelCount);
	void initializeLabel(int labelNumber, ATermAppl parseTreeProduction);
	
	Object buildTree(AbstractParseNode node);
	Object buildTreeTop(Object subtree, int ambiguityCount);
	
	/**
	 * Gets the tokenizer, if applicable, or null.
	 */
	ITokenizer getTokenizer();
}
