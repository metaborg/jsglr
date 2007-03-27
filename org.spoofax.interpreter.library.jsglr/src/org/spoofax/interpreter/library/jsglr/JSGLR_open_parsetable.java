package org.spoofax.interpreter.library.jsglr;

import org.spoofax.interpreter.IConstruct;
import org.spoofax.interpreter.IContext;
import org.spoofax.interpreter.InterpreterException;
import org.spoofax.interpreter.adapter.aterm.WrappedATerm;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.InvalidParseTableException;
import org.spoofax.jsglr.ParseTable;

public class JSGLR_open_parsetable extends JSGLRPrimitive {

	protected JSGLR_open_parsetable() {
		super("JSGLR_open_parsetable", 0, 1);
	}

	@Override
	public boolean call(IContext env, IConstruct[] svars, IStrategoTerm[] tvars)
			throws InterpreterException {
		if(!(tvars[0] instanceof WrappedATerm))
			return false;
		
		JSGLRLibrary lib = getLibrary(env);
		try {
			ParseTable pt = lib.getParseTableManager().loadFromTerm(((WrappedATerm)tvars[0]).getATerm());
			env.setCurrent(env.getFactory().makeInt(lib.addParseTable(pt)));
		} catch(InvalidParseTableException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
