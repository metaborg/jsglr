package org.spoofax.interpreter.library.jsglr;

import org.spoofax.interpreter.core.IContext;
import org.spoofax.interpreter.stratego.Strategy;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.shared.SGLRException;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class STRSGLR_get_parse_error extends JSGLRPrimitive {

	public STRSGLR_get_parse_error() {
		super("STRSGLR_get_parse_error", 0, 0);
	}

	@Override
	public boolean call(IContext env, Strategy[] svars, IStrategoTerm[] tvars) {
		JSGLRLibrary lib = getLibrary(env);
		STRSGLR_parse_string_pt parser = (STRSGLR_parse_string_pt) lib.get(STRSGLR_parse_string_pt.NAME);
		SGLRException lastException = parser.getLastException();
		if (lastException == null)
			return false;
		IStrategoTerm result = lastException.toTerm(parser.getLastPath());
		if (result == null)
			return false;
		env.setCurrent(result);
		return true;
	}
}
