package org.spoofax.interpreter.library.jsglr.origin;

import static mb.jsglr.shared.ImploderAttachment.getLeftToken;
import static mb.jsglr.shared.ImploderAttachment.getRightToken;
import static mb.jsglr.shared.ImploderAttachment.getTokenizer;

import org.spoofax.interpreter.core.IContext;
import org.spoofax.interpreter.terms.IStrategoTerm;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class OriginTextPrimitive extends AbstractOriginPrimitive {

	public OriginTextPrimitive() {
		super("SSL_EXT_origin_text");
	}

	@Override
	protected IStrategoTerm call(IContext env, IStrategoTerm origin) {
		String result = getTokenizer(origin).toString(getLeftToken(origin), getRightToken(origin));
		//result = AutoEditStrategy.setIndentation(result, "");
		return env.getFactory().makeString(result);
	}

}
