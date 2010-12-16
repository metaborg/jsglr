package org.spoofax.jsglr.client;

import org.spoofax.jsglr.shared.terms.AFun;
import org.spoofax.jsglr.shared.terms.ATerm;
import org.spoofax.jsglr.shared.terms.ATermAppl;
import org.spoofax.jsglr.shared.terms.ATermFactory;
import org.spoofax.jsglr.shared.terms.ATermList;

public class Asfix2TreeBuilder implements TreeBuilder {

	private final ATermFactory factory = new ATermFactory();
	private final AFun applAFun;
	private final AFun ambAFun;
	private final AFun parseTreeAFun;
	
	public Asfix2TreeBuilder() {
	    applAFun = factory.makeAFun("appl", 2, false);
	    ambAFun = factory.makeAFun("amb", 1, false);
	    parseTreeAFun = factory.makeAFun("parsetree", 2, false);
	}

	@Override
	public Object mapProduction(int labelNumber, ATermAppl parseTreeProduction) {
		return parseTreeProduction;
	}

	@Override
	public Object buildNode(int label, Object node, Object[] subtrees) {
		ATerm parent = (ATerm)node;
		ATermList ls = factory.makeList();
		for(int i = subtrees.length - 1; i >= 0; i--)
			ls = ls.prepend((ATerm)subtrees[i]);
		return factory.makeAppl(applAFun, parent, ls);
	}

	@Override
	public Object buildAmb(Object[] alternatives) {
		return factory.makeAppl(ambAFun, (ATerm[])alternatives);
	}

	@Override
	public Object buildProduction(int label) {
		return factory.makeInt(label);
	}

	@Override
	public Object buildToplevel(Object node, int ambCount) {
		return factory.makeAppl(parseTreeAFun, (ATerm)node, factory.makeInt(ambCount));	}

}
