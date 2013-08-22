package org.spoofax.jsglr.unicode.transformer;

import java.util.List;

import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.unicode.terms.UnicodeUtils;
import org.spoofax.terms.TermTransformer;

public class RestrictionsTransformer extends TermTransformer {

	public static enum Task {
		REMOVE_LISTS, LIFT_ALTS, ADD_BRACKETS;
	}
	private Task task;

	public RestrictionsTransformer() {
		super(UnicodeUtils.factory, false);
	}
	
	public void setTask(Task t) {
		task = t;
	}

	@Override
	public IStrategoTerm preTransform(IStrategoTerm term) {
		if (this.task == Task.REMOVE_LISTS) {
			if (UnicodeUtils.isList(term)) {
				List<IStrategoTerm> content = UnicodeUtils.strategoListToLinkedList((IStrategoList) term.getSubterm(0));
				return UnicodeUtils.makeOrSymbol(content);
			}
		} else if (this.task == Task.LIFT_ALTS){
			if (UnicodeUtils.isConstructors(term, "seq")) {
				if (UnicodeUtils.isConstructors(term.getSubterm(0), "alt")) {
					return UnicodeUtils
							.makeOrSymbol(
									UnicodeUtils.makeRestrictionSymbolSeq(term.getSubterm(0).getSubterm(0),
											term.getSubterm(1)),
									UnicodeUtils.makeRestrictionSymbolSeq(term.getSubterm(0).getSubterm(1),
											term.getSubterm(1)));
				}
			}
		}
		return term;
	}
	
	@Override
	public IStrategoTerm postTransform(IStrategoTerm term) {
		 if (this.task == Task.ADD_BRACKETS) {
			if (UnicodeUtils.isConstructors(term, "alt")) {
				return UnicodeUtils.makeBracket(term);
			}
		}
		return super.postTransform(term);
	}
}
