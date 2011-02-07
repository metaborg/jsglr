package org.spoofax.jsglr.client.incremental;

import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getElementSort;
import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getSort;
import static org.spoofax.terms.Term.asJavaString;
import static org.spoofax.terms.Term.isTermAppl;
import static org.spoofax.terms.Term.termAt;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.spoofax.interpreter.terms.ISimpleTerm;
import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoConstructor;
import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.jsglr.client.Label;
import org.spoofax.jsglr.client.ParseTable;

/**
 * A collection of incremental sorts for a particular language
 * and parse table. Uses injections from and to
 * the input set of sorts.
 * 
 * Should be reused, and not reconstructed with every parse.
 * 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class IncrementalSortSet {
	
	private final IStrategoConstructor sortFun;
	
	private final IStrategoConstructor cfFun;
	
	private final IStrategoConstructor lexFun;
	
	private final Set<String> incrementalSorts;
	
	private final Set<String> incrementalContainerSorts;

	/**
	 * @param expand
	 *            Whether to expand the set of sorts with injections to those
	 *            sorts (e.g., add MethodDec if ClassBodyDec was specified.)
	 */
	public IncrementalSortSet(ParseTable table, boolean expand, String... sorts) {
		sortFun = table.getFactory().makeConstructor("sort", 1);
		cfFun = table.getFactory().makeConstructor("cf", 1);
		lexFun = table.getFactory().makeConstructor("lex", 1);
		incrementalSorts = expand ? getInjectionsTo(table, Arrays.asList(sorts), false) : asSet(sorts);
		incrementalContainerSorts = getInjectionsTo(table, incrementalSorts, true);
	}
	
	public boolean isEmpty() {
		return incrementalSorts.size() == 0;
	}
	
	public boolean isIncrementalNode(ISimpleTerm term) {
		return incrementalSorts.contains(getSort(term));
	}
	
	public boolean isIncrementalContainerNode(ISimpleTerm list) {
		assert list.isList();
		return incrementalContainerSorts.contains(getElementSort(list));
	}
	
	private static<T> Set<T> asSet(T... values) {
		Set<T> results = new HashSet<T>(values.length);
		for (T value : values)
			results.add(value);
		return results;
	}
	
	private Set<String> getInjectionsTo(ParseTable table, Iterable<String> sorts, boolean reverse) {
		Set<String> results = new HashSet<String>();
		for (String sort : sorts) {
			addInjectionsTo(table, sort, reverse, results);
		}
		return results;
	}
	
	private void addInjectionsTo(ParseTable table, String sort, boolean reverse, Set<String> results) {
		ITermFactory factory = table.getFactory();
		IStrategoTerm sortTerm = factory.makeAppl(sortFun, factory.makeString(sort));
		for (Label l : table.getLabels()) {
			if (l != null) {
				IStrategoAppl production = l.getProduction();
				IStrategoTerm sort1 = reverse ? getFromSort(production) : getToSort(production);
				if (sortTerm.equals(sort1)) {
					IStrategoTerm sort2 = reverse ? getToSort(production) : getFromSort(production);
					if (sort2 != null)
						results.add(asJavaString(termAt(sort2, 0)));
				}
			}
		}
		results.add(sort);
	}

	private IStrategoTerm getToSort(IStrategoAppl production) {
		IStrategoTerm toSort = termAt(production, 1);
		toSort = stripFun(toSort, cfFun);
		toSort = stripFun(toSort, lexFun);
		if (((IStrategoAppl) toSort).getConstructor() != sortFun) return null;
		return toSort;
	}
	
	private IStrategoTerm getFromSort(IStrategoAppl production) {
		IStrategoList lhs = termAt(production, 0);
		if (lhs.getSubtermCount() != 1) return null;
		IStrategoTerm lhsFirst = lhs.head();
		lhsFirst = stripFun(lhsFirst, cfFun);
		lhsFirst = stripFun(lhsFirst, lexFun);
		if (((IStrategoAppl) lhsFirst).getConstructor() != sortFun) return null;
		return lhsFirst;
	}
	
	public IStrategoTerm stripFun(IStrategoTerm appl, IStrategoConstructor fun) {
		if (isTermAppl(appl) && ((IStrategoAppl) appl).getConstructor() == fun) {
			return termAt(appl, 0);
		} else {
			return appl;
		}
	}
}
