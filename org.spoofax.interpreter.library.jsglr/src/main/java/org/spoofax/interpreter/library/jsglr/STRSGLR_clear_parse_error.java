package org.spoofax.interpreter.library.jsglr;

import org.spoofax.interpreter.core.IContext;
import org.spoofax.interpreter.library.IOperatorRegistry;
import org.spoofax.interpreter.stratego.Strategy;
import org.spoofax.interpreter.terms.IStrategoTerm;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class STRSGLR_clear_parse_error extends JSGLRPrimitive {

	public STRSGLR_clear_parse_error() {
		super("STRSGLR_clear_parse_error", 0, 0);
	}

	@Override
	public boolean call(IContext env, Strategy[] svars, IStrategoTerm[] tvars) {
		IOperatorRegistry or = getLibrary(env);
		STRSGLR_parse_string_pt parser = (STRSGLR_parse_string_pt) or.get(STRSGLR_parse_string_pt.NAME);
		parser.clearLastException();
		return true;
	}
}
