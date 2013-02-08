/*
 * Copyright (c) 2012, Karl Trygve Kalleberg <karltk near strategoxt dot org>
 * 
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.interpreter.library.jsglr;

import org.spoofax.interpreter.core.IContext;
import org.spoofax.interpreter.core.Tools;
import org.spoofax.interpreter.stratego.Strategy;
import org.spoofax.interpreter.terms.IStrategoTerm;

public class STRSGLR_close_parse_table extends JSGLRPrimitive {

	public STRSGLR_close_parse_table() {
		super("STRSGLR_close_parse_table", 0, 1);
	}

	@Override
	public boolean call(IContext env, Strategy[] svars, IStrategoTerm[] tvars) {
		 JSGLRLibrary or = getLibrary(env);
		 if(!Tools.isTermInt(tvars[0]))
				 return false;
		 or.removeParseTable(Tools.asJavaInt(tvars[0]));
		 return true;
	}
}
