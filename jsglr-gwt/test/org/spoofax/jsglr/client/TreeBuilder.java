package org.spoofax.jsglr.client;

import org.spoofax.jsglr.shared.terms.ATermAppl;

public interface TreeBuilder {

	public void addLabel(int labelNumber, ATermAppl parseTreeProduction);

	public Object buildNode(int labelNumber, Object[] subtrees);
	public Object buildProduction(int productionNumber);
	public Object buildToplevel(Object subtree, int ambiguityCount);
	public Object buildAmb(Object[] alternatives);

	public void initialize(int productionCount, int labelCount);
	
}
