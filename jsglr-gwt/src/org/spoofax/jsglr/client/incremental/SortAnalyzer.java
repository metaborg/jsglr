package org.spoofax.jsglr.client.incremental;

import static org.spoofax.jsglr.shared.Tools.termAt;
import static org.spoofax.jsglr.shared.terms.ATerm.APPL;

import java.util.HashSet;
import java.util.Set;

import org.spoofax.jsglr.client.Label;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.shared.terms.AFun;
import org.spoofax.jsglr.shared.terms.ATerm;
import org.spoofax.jsglr.shared.terms.ATermAppl;
import org.spoofax.jsglr.shared.terms.ATermFactory;
import org.spoofax.jsglr.shared.terms.ATermList;
import org.spoofax.jsglr.shared.terms.ATermString;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class SortAnalyzer {

	private final ParseTable table;
	
	private final AFun sortFun;
	
	private final AFun cfFun;
	
	private final AFun lexFun;

	public SortAnalyzer(ParseTable table) {
		this.table = table;
		sortFun = table.getFactory().makeAFun("sort", 1, false);
		cfFun = table.getFactory().makeAFun("cf", 1, false);
		lexFun = table.getFactory().makeAFun("lex", 1, false);
	}
	
	public Set<String> getInjectionsTo(String... sorts) {
		Set<String> results = new HashSet<String>();
		for (String sort : sorts) {
			addInjectionsTo(sort, results);
		}
		return results;
	}
	
	private void addInjectionsTo(String sort, Set<String> results) {
		ATermFactory factory = table.getFactory();
		ATerm sortTerm = factory.makeAppl(sortFun, factory.makeString(sort));
		for (Label l : table.getLabels()) {
			if (l != null) {
				ATermAppl production = l.getProduction();
				ATermAppl toSort = termAt(production, 1);
				toSort = stripFun(toSort, cfFun);
				toSort = stripFun(toSort, lexFun);
				if (toSort.equals(sortTerm)) {
					ATermList lhs = termAt(production, 0);
					if (lhs.getChildCount() != 1) continue;
					if (lhs.getFirst().getType() != APPL) continue;
					ATermAppl lhsFirst = (ATermAppl) lhs.getFirst();
					lhsFirst = stripFun(lhsFirst, cfFun);
					lhsFirst = stripFun(lhsFirst, lexFun);
					if (lhsFirst.getAFun() != sortFun) continue;
					ATermString fromSort = termAt(lhsFirst, 0);
					results.add(fromSort.getString());
				}
			}
		}
		results.add(sort);
	}
	
	public ATermAppl stripFun(ATermAppl appl, AFun fun) {
		if (appl.getAFun() == fun) {
			return (ATermAppl) termAt(appl, 0);
		} else {
			return appl;
		}
	}
}
