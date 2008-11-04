package org.spoofax.interpreter.library.jsglr;

import org.spoofax.interpreter.core.IContext;
import org.spoofax.interpreter.core.InterpreterException;
import org.spoofax.interpreter.adapter.aterm.WrappedATerm;
import org.spoofax.interpreter.adapter.aterm.WrappedATermFactory;
import org.spoofax.interpreter.stratego.Strategy;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.TermConverter;
import org.spoofax.jsglr.InvalidParseTableException;
import org.spoofax.jsglr.ParseTable;

public class JSGLR_open_parsetable extends JSGLRPrimitive {
    
    private final TermConverter termConverter;

	protected JSGLR_open_parsetable(WrappedATermFactory factory) {
		super("JSGLR_open_parsetable", 0, 1);
		
		termConverter = new TermConverter(factory);
	}

	@Override
	public boolean call(IContext env, Strategy[] svars, IStrategoTerm[] tvars)
			throws InterpreterException {
		if(!(tvars[0] instanceof WrappedATerm))
			tvars[0] = termConverter.convert(tvars[0]);
		
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
