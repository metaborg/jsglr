package org.spoofax.jsglr.client;

import org.spoofax.jsglr.shared.terms.ATermAppl;

public interface TreeBuilder {

	public Object mapProduction(int labelNumber, ATermAppl parseTreeProduction);

	public Object buildNode(int label, Object node, Object[] subtrees);
	public Object buildProduction(int productionNumber);
	public Object buildToplevel(Object node, int ambiguityCount);
	public Object buildAmb(Object[] alternatives);
	
}
