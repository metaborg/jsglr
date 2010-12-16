package org.spoofax.jsglr.client;

import org.spoofax.jsglr.client.imploder.ITokenizer;
import org.spoofax.jsglr.shared.terms.AFun;
import org.spoofax.jsglr.shared.terms.ATerm;
import org.spoofax.jsglr.shared.terms.ATermAppl;
import org.spoofax.jsglr.shared.terms.ATermFactory;
import org.spoofax.jsglr.shared.terms.ATermList;

public class Asfix2TreeBuilder extends BottomupTreeBuilder {

	private final ATermFactory factory = new ATermFactory();
	private final AFun applAFun;
	private final AFun ambAFun;
	private final AFun parseTreeAFun;
	private ATermAppl[] labels;
	private int labelStart;

	public Asfix2TreeBuilder() {
		applAFun = factory.makeAFun("appl", 2, false);
		ambAFun = factory.makeAFun("amb", 1, false);
		parseTreeAFun = factory.makeAFun("parsetree", 2, false);
	}

	public void initializeTable(ParseTable table, int productionCount, int labelStart, int labelCount) {
		labels = new ATermAppl[labelCount - labelStart];
		this.labelStart = labelStart;
	}

	public void initializeLabel(int labelNumber, ATermAppl parseTreeProduction) {
		labels[labelNumber - labelStart] = parseTreeProduction;
	}

	public void initializeInput(String filename, String input) {
		// Not used here
	}

	public ATerm buildNode(int labelNumber, Object[] subtrees) {
		ATermList ls = factory.makeList();
		for(int i = subtrees.length - 1; i >= 0; i--) {
			ls = ls.prepend((ATerm)subtrees[i]);
		}
		return factory.makeAppl(applAFun, labels[labelNumber - labelStart], ls);
	}

	public ATerm buildAmb(Object[] alternatives) {
		return factory.makeAppl(ambAFun, (ATerm[])alternatives);
	}

	public ATerm buildProduction(int productionNumber) {
		return factory.makeInt(productionNumber);
	}

	public ATerm buildTreeTop(Object node, int ambCount) {
		return factory.makeAppl(parseTreeAFun, (ATerm)node, factory.makeInt(ambCount));
	}

	public ITokenizer getTokenizer() {
		return null;
	}
}
