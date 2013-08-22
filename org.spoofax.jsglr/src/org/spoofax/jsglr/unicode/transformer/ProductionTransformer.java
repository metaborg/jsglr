package org.spoofax.jsglr.unicode.transformer;

import static org.spoofax.jsglr.unicode.terms.UnicodeUtils.factory;
import static org.spoofax.jsglr.unicode.terms.UnicodeUtils.isContextFreeGrammar;
import static org.spoofax.jsglr.unicode.terms.UnicodeUtils.isContextFreePriorities;
import static org.spoofax.jsglr.unicode.terms.UnicodeUtils.isProduction;
import static org.spoofax.jsglr.unicode.terms.UnicodeUtils.makePriorities;
import static org.spoofax.jsglr.unicode.terms.UnicodeUtils.makeSyntaxGrammar;

import java.util.LinkedList;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.terms.TermTransformer;

public class ProductionTransformer extends TermTransformer {

	private LinkedList<IStrategoTerm> convertedSyntaxProductions;
	
	public ProductionTransformer() {
		super(factory, false);
		this.convertedSyntaxProductions = new LinkedList<IStrategoTerm>();
	}
	
	public LinkedList<IStrategoTerm> getConvertedSyntaxProductions() {
		return convertedSyntaxProductions;
	}

	@Override
	public IStrategoTerm preTransform(IStrategoTerm term) {
		if (isProduction(term)) {
			if (ProductionAST.isProduction(term)) {
				ProductionAST prod = ProductionAST.selectProduction(term);
				prod.unpack(term);
				//if (prod.containsUnicode()) {
					prod.insertLayoutAndWrapSorts();
					return prod.pack(factory);
				//}
			}
		}
		if (isContextFreeGrammar(term)) {
			return makeSyntaxGrammar(term.getSubterm(0));
		}
		if (isContextFreePriorities(term)) {
			return makePriorities(term.getSubterm(0));
		}
		return term;
	}

	

}
