package org.spoofax.jsglr.client;

import java.util.List;

import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoConstructor;
import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.jsglr.client.imploder.ITokenizer;
import org.spoofax.terms.TermFactory;

public class Asfix2TreeBuilder extends BottomupTreeBuilder {

	private final ITermFactory factory;
	private final IStrategoConstructor applIStrategoConstructor;
	private final IStrategoConstructor ambIStrategoConstructor;
	private final IStrategoConstructor parseTreeIStrategoConstructor;
	private ParseTable table;
	private IStrategoAppl[] labels;
	private int labelStart;

	public Asfix2TreeBuilder() {
		this(new TermFactory());
	}
	
	public Asfix2TreeBuilder(ITermFactory factory) {
		this.factory = factory;
		applIStrategoConstructor = factory.makeConstructor("appl", 2);
		ambIStrategoConstructor = factory.makeConstructor("amb", 1);
		parseTreeIStrategoConstructor = factory.makeConstructor("parsetree", 2);
	}

	public void initializeTable(ParseTable table, int productionCount, int labelStart, int labelCount) {
		this.table = table;
		labels = new IStrategoAppl[labelCount - labelStart];
		this.labelStart = labelStart;
	}

	public void initializeLabel(int labelNumber, IStrategoAppl parseTreeProduction) {
		labels[labelNumber - labelStart] = parseTreeProduction;
	}
	
	public ParseTable getTable() {
		return table;
	}

	public void initializeInput(String filename, String input) {
		// Not used here
	}

	@Override
	public IStrategoTerm buildNode(int labelNumber, List<Object> subtrees) {
		IStrategoList ls = factory.makeList();
		for(int i = subtrees.size() - 1; i >= 0; i--) {
        	ls = factory.makeListCons((IStrategoTerm) subtrees.get(i), ls);
		}
		return factory.makeAppl(applIStrategoConstructor, labels[labelNumber - labelStart], ls);
	}

	@Override
	public IStrategoTerm buildAmb(List<Object> alternatives) {
		IStrategoTerm[] alternatives2 = alternatives.toArray(new IStrategoTerm[alternatives.size()]);
		return factory.makeAppl(ambIStrategoConstructor, alternatives2);
	}

	@Override
	public IStrategoTerm buildProduction(int productionNumber) {
		return factory.makeInt(productionNumber);
	}

	@Override
	public IStrategoTerm buildTreeTop(Object node, int ambCount) {
		return factory.makeAppl(parseTreeIStrategoConstructor, (IStrategoAppl) node, factory.makeInt(ambCount));
	}

	public ITokenizer getTokenizer() {
		return null;
	}
}
