package org.spoofax.interpreter.library.jsglr;

import java.util.WeakHashMap;

import org.spoofax.interpreter.adapter.aterm.WrappedATerm;
import org.spoofax.interpreter.adapter.aterm.WrappedATermFactory;
import org.spoofax.interpreter.core.IContext;
import org.spoofax.interpreter.core.InterpreterException;
import org.spoofax.interpreter.stratego.Strategy;
import org.spoofax.interpreter.terms.IStrategoInt;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.TermConverter;
import org.spoofax.jsglr.InvalidParseTableException;
import org.spoofax.jsglr.ParseTable;

public class JSGLR_open_parsetable extends JSGLRPrimitive {
    
    // TODO: Do (non-static) caching at the IContext level
    
    private final WeakHashMap<IStrategoTerm, IStrategoInt> parseTableCache =
        new WeakHashMap<IStrategoTerm, IStrategoInt>();
    
    private final TermConverter termConverter;

	protected JSGLR_open_parsetable(WrappedATermFactory factory) {
		super("JSGLR_open_parsetable", 0, 1);
		
		termConverter = new TermConverter(factory);
	}

	@Override
	public boolean call(IContext env, Strategy[] svars, IStrategoTerm[] tvars)
			throws InterpreterException {
	    
	    IStrategoInt cached = parseTableCache.get(tvars[0]);
	    if (cached != null) {
	        env.setCurrent(cached);
	        return true;
	    }
	    
	    WrappedATerm tableTerm = tvars[0] instanceof WrappedATerm
	            ? (WrappedATerm) tvars[0]
	            : (WrappedATerm) termConverter.convert(tvars[0]);
		
		JSGLRLibrary lib = getLibrary(env);
		try {
			ParseTable pt = lib.getParseTableManager().loadFromTerm(tableTerm.getATerm());
			IStrategoInt result = env.getFactory().makeInt(lib.addParseTable(pt));
            
			env.setCurrent(result);			
			parseTableCache.put(tvars[0], result);
		} catch (InvalidParseTableException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
