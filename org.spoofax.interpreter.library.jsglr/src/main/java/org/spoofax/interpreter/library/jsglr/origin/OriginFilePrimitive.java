package org.spoofax.interpreter.library.jsglr.origin;

import static mb.jsglr.shared.ImploderAttachment.getLeftToken;

import org.spoofax.interpreter.core.IContext;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;

public class OriginFilePrimitive extends AbstractOriginPrimitive {

	public OriginFilePrimitive() {
		super("SSL_EXT_origin_file");
	}

	@Override
	protected IStrategoTerm call(IContext env, IStrategoTerm origin) {
		ITermFactory factory = env.getFactory();
		return factory.makeString(getLeftToken(origin).getFilename());
	}

}
