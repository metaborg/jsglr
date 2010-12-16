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
	private ATermAppl[] labels;
	
	public Asfix2TreeBuilder() {
	    applAFun = factory.makeAFun("appl", 2, false);
	    ambAFun = factory.makeAFun("amb", 1, false);
	    parseTreeAFun = factory.makeAFun("parsetree", 2, false);
	}

	public void initialize(int productionCount, int labelCount) {
		labels = new ATermAppl[labelCount];
	}
	
	@Override
	public void addLabel(int labelNumber, ATermAppl parseTreeProduction) {
		labels[labelNumber] = parseTreeProduction;
	}

	@Override
	public Object buildNode(int labelNumber, Object[] subtrees) {
		ATermList ls = factory.makeList();
		for(int i = subtrees.length - 1; i >= 0; i--)
			ls = ls.prepend((ATerm)subtrees[i]);
		return factory.makeAppl(applAFun, labels[labelNumber], ls);
	}

	@Override
	public Object buildAmb(Object[] alternatives) {
		return factory.makeAppl(ambAFun, (ATerm[])alternatives);
	}

	@Override
	public Object buildProduction(int productionNumber) {
		return factory.makeInt(productionNumber);
	}

	@Override
	public Object buildToplevel(Object node, int ambCount) {
		return factory.makeAppl(parseTreeAFun, (ATerm)node, factory.makeInt(ambCount));	}

}
