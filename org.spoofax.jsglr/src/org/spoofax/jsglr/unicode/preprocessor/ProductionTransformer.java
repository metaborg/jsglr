package org.spoofax.jsglr.unicode.preprocessor;

import static org.spoofax.jsglr.unicode.UnicodeUtils.*;

import java.util.LinkedList;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.tests.unicode.MyTermTransformer;

public class ProductionTransformer extends MyTermTransformer {

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
