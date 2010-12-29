package org.spoofax.jsglr.client.incremental;

import static org.spoofax.terms.Term.isTermAppl;
import static org.spoofax.terms.Term.termAt;

import java.util.HashSet;
import java.util.Set;

import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoConstructor;
import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoString;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.jsglr.client.Label;
import org.spoofax.jsglr.client.ParseTable;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class SortAnalyzer {

	private final ParseTable table;
	
	private final IStrategoConstructor sortFun;
	
	private final IStrategoConstructor cfFun;
	
	private final IStrategoConstructor lexFun;

	public SortAnalyzer(ParseTable table) {
		this.table = table;
		sortFun = table.getFactory().makeConstructor("sort", 1);
		cfFun = table.getFactory().makeConstructor("cf", 1);
		lexFun = table.getFactory().makeConstructor("lex", 1);
	}
	
	public Set<String> getInjectionsTo(String... sorts) {
		Set<String> results = new HashSet<String>();
		for (String sort : sorts) {
			addInjectionsTo(sort, results);
		}
		return results;
	}
	
	private void addInjectionsTo(String sort, Set<String> results) {
		ITermFactory factory = table.getFactory();
		IStrategoTerm sortTerm = factory.makeAppl(sortFun, factory.makeString(sort));
		for (Label l : table.getLabels()) {
			if (l != null) {
				IStrategoAppl production = l.getProduction();
				IStrategoTerm toSort = termAt(production, 1);
				toSort = stripFun(toSort, cfFun);
				toSort = stripFun(toSort, lexFun);
				if (toSort.equals(sortTerm)) {
					IStrategoList lhs = termAt(production, 0);
					if (lhs.getSubtermCount() != 1) continue;
					if (!isTermAppl(lhs.head())) continue;
					IStrategoTerm lhsFirst = lhs.head();
					lhsFirst = stripFun(lhsFirst, cfFun);
					lhsFirst = stripFun(lhsFirst, lexFun);
					if (((IStrategoAppl) lhsFirst).getConstructor() != sortFun) continue;
					IStrategoString fromSort = termAt(lhsFirst, 0);
					results.add(fromSort.stringValue());
				}
			}
		}
		results.add(sort);
	}
	
	public IStrategoTerm stripFun(IStrategoTerm appl, IStrategoConstructor fun) {
		if (isTermAppl(appl) && ((IStrategoAppl) appl).getConstructor() == fun) {
			return termAt(appl, 0);
		} else {
			return appl;
		}
	}
}
