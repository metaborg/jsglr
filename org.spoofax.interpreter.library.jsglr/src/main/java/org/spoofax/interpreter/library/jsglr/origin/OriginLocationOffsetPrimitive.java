package org.spoofax.interpreter.library.jsglr.origin;

import org.spoofax.interpreter.core.IContext;
import org.spoofax.interpreter.library.AbstractPrimitive;
import org.spoofax.interpreter.stratego.Strategy;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.terms.attachments.OriginAttachment;

import mb.jsglr.shared.IToken;
import mb.jsglr.shared.ImploderAttachment;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class OriginLocationOffsetPrimitive extends AbstractPrimitive {

	public OriginLocationOffsetPrimitive() {
		super("SSL_EXT_origin_location_offset", 0, 1);
	}

	@Override
	public final boolean call(IContext env, Strategy[] svars, IStrategoTerm[] tvars) {
		final ITermFactory factory = env.getFactory();
		final IStrategoTerm term = env.current();

		ImploderAttachment imploder = ImploderAttachment.get(term);
		if(imploder == null) {
			final IStrategoTerm originTerm = OriginAttachment.getOrigin(term);
			if(originTerm == null)
				return false;
			imploder = ImploderAttachment.get(originTerm);
		}
		if(imploder == null)
			return false;

		final IToken left = imploder.getLeftToken();
		final IToken right = imploder.getRightToken();
		String filename = left.getFilename();
		if(filename == null)
			filename = right.getFilename();
		if(filename == null)
			filename = ".";

		env.setCurrent(factory.makeTuple(factory.makeString(filename), factory.makeInt(left.getLine()),
			factory.makeInt(left.getColumn()), factory.makeInt(left.getStartOffset()),
			factory.makeInt(right.getEndOffset())));
		return true;
	}

}
