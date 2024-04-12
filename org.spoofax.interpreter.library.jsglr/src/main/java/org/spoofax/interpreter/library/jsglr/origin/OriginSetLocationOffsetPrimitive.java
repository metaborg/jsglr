package org.spoofax.interpreter.library.jsglr.origin;

import org.spoofax.interpreter.core.IContext;
import org.spoofax.interpreter.core.Tools;
import org.spoofax.interpreter.library.AbstractPrimitive;
import org.spoofax.interpreter.stratego.Strategy;
import org.spoofax.interpreter.terms.IStrategoTerm;

import mb.jsglr.shared.ImploderAttachment;

/**
 * Tokens
 */
public class OriginSetLocationOffsetPrimitive extends AbstractPrimitive {

	public OriginSetLocationOffsetPrimitive() {
		super("SSL_EXT_set_origin_location_offset", 0, 1);
	}

	@Override
	public final boolean call(IContext env, Strategy[] svars, IStrategoTerm[] tvars) {
		final IStrategoTerm location = tvars[0];
		if(!Tools.isTermTuple(location) && location.getSubtermCount() != 4)
			return false;

		final String filename = Tools.asJavaString(location.getSubterm(0));
		final int line = Tools.asJavaInt(location.getSubterm(1));
		final int column = Tools.asJavaInt(location.getSubterm(2));
		final int startOffset = Tools.asJavaInt(location.getSubterm(3));
		final int endOffset = Tools.asJavaInt(location.getSubterm(4));

		final ImploderAttachment attachment =
			ImploderAttachment.createCompactPositionAttachment(filename, line, column, startOffset, endOffset);
		env.current().putAttachment(attachment);

		return true;
	}
}
