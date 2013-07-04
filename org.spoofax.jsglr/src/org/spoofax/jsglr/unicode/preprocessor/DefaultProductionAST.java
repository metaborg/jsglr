package org.spoofax.jsglr.unicode.preprocessor;

import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.jsglr.unicode.UnicodeUtils;

public class DefaultProductionAST extends ProductionAST {

	public static final String PROD_CONSTRUCTOR = "prod";
	
	public DefaultProductionAST() {}

	@Override
	public void unpack(IStrategoTerm productionTerm) {
		UnicodeUtils.forceConstructors(productionTerm, PROD_CONSTRUCTOR);
		this.symbols = UnicodeUtils.strategoListToLinkedList((IStrategoList) productionTerm.getSubterm(0));
		this.resultSymbol = productionTerm.getSubterm(1);
		this.attributes = productionTerm.getSubterm(2);
	}

	@Override
	public IStrategoTerm pack(final ITermFactory factory) {
		return factory.makeAppl(factory.makeConstructor(PROD_CONSTRUCTOR, 3), factory.makeList(this.symbols), this.resultSymbol,
				this.attributes);
	}

	@Override
	public boolean matches(IStrategoTerm maybeProduction) {
		return UnicodeUtils.isConstructors(maybeProduction, PROD_CONSTRUCTOR);
	}

}
