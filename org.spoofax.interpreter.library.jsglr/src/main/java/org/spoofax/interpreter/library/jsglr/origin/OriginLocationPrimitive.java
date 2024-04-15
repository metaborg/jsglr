package org.spoofax.interpreter.library.jsglr.origin;

import static mb.jsglr.shared.ImploderAttachment.getLeftToken;
import static mb.jsglr.shared.ImploderAttachment.getRightToken;

import org.spoofax.interpreter.core.IContext;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;

import mb.jsglr.shared.IToken;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class OriginLocationPrimitive extends AbstractOriginPrimitive {

	public OriginLocationPrimitive() {
		super("SSL_EXT_origin_location");
	}

	@Override
	protected IStrategoTerm call(IContext env, IStrategoTerm origin) {
		ITermFactory factory = env.getFactory();
		IToken start = getLeftToken(origin);
		IToken end = getRightToken(origin);  
		return factory.makeTuple(
				factory.makeInt(start.getLine()),
				factory.makeInt(start.getColumn()),
				factory.makeInt(end.getEndLine()),
				factory.makeInt(end.getEndColumn()));
	}

}
