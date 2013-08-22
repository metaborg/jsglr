package org.spoofax.jsglr.unicode.transformer;

import static org.spoofax.jsglr.unicode.terms.UnicodeUtils.factory;
import static org.spoofax.jsglr.unicode.terms.UnicodeUtils.isContextFreeGrammar;
import static org.spoofax.jsglr.unicode.terms.UnicodeUtils.isContextFreePriorities;
import static org.spoofax.jsglr.unicode.terms.UnicodeUtils.makePriorities;
import static org.spoofax.jsglr.unicode.terms.UnicodeUtils.makeSyntaxGrammar;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.terms.TermTransformer;

/**
 * The {@link ProductionTransformer} desugars all context-free productions in a
 * given AST and converts context-free syntax and context-free priorities
 * sections to syntax or priorities sections.
 * 
 * @author moritzlichter
 * 
 */
public class ProductionTransformer extends TermTransformer {

	public ProductionTransformer() {
		super(factory, false);
	}

	@Override
	public IStrategoTerm preTransform(IStrategoTerm term) {
		// Desugar productions
		if (ProductionAST.isProduction(term)) {
			ProductionAST prod = ProductionAST.selectProduction(term);
			prod.unpack(term);
			// if (prod.containsUnicode()) {
			prod.insertLayoutAndWrapSorts();
			return prod.pack(factory);
			// }
		}
		// Convert sections
		if (isContextFreeGrammar(term)) {
			return makeSyntaxGrammar(term.getSubterm(0));
		}
		if (isContextFreePriorities(term)) {
			return makePriorities(term.getSubterm(0));
		}
		return term;
	}

}
