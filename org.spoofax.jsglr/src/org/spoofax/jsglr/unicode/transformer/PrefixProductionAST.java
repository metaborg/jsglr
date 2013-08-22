package org.spoofax.jsglr.unicode.transformer;

import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.jsglr.unicode.terms.UnicodeUtils;

/**
 * Implementation of {@link ProductionAST} for prefix productions.
 * 
 * @author moritzlichter
 * 
 */
public class PrefixProductionAST extends ProductionAST {

	private static final String PREFIX_FUN_CONSTRUCTOR = "prefix-fun";

	private IStrategoTerm functionName;

	public PrefixProductionAST() {
	}

	@Override
	public void unpack(IStrategoTerm productionTerm) {
		UnicodeUtils.forceConstructors(productionTerm, PREFIX_FUN_CONSTRUCTOR);
		this.functionName = productionTerm.getSubterm(0);
		this.symbols = UnicodeUtils.strategoListToLinkedList((IStrategoList) productionTerm.getSubterm(1));
		this.resultSymbol = productionTerm.getSubterm(2);
		this.attributes = productionTerm.getSubterm(3);
	}

	@Override
	public IStrategoTerm pack(final ITermFactory factory) {
		return factory.makeAppl(factory.makeConstructor(PREFIX_FUN_CONSTRUCTOR, 4), this.functionName,
				factory.makeList(this.symbols), this.resultSymbol, this.attributes);

	}

	@Override
	public boolean matches(IStrategoTerm maybeProduction) {
		return UnicodeUtils.isConstructors(maybeProduction, PREFIX_FUN_CONSTRUCTOR);
	}

}
