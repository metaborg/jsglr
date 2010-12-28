package org.spoofax.interpreter.library.jsglr;

import java.util.WeakHashMap;

import org.spoofax.interpreter.core.IContext;
import org.spoofax.interpreter.core.InterpreterException;
import org.spoofax.interpreter.stratego.Strategy;
import org.spoofax.interpreter.terms.IStrategoInt;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParseTable;

import aterm.ATerm;

public class JSGLR_open_parsetable extends JSGLRPrimitive {
    
    // TODO: Do (non-static) caching at the IContext level
    
    private final WeakHashMap<IStrategoTerm, IStrategoInt> parseTableCache =
        new WeakHashMap<IStrategoTerm, IStrategoInt>();

	public JSGLR_open_parsetable() {
		super("JSGLR_open_parsetable", 0, 1);		
	}

	@Override
	public boolean call(IContext env, Strategy[] svars, IStrategoTerm[] tvars)
			throws InterpreterException {
	    
	    IStrategoInt cached = parseTableCache.get(tvars[0]);
	    if (cached != null) {
	        env.setCurrent(cached);
	        if(getLibrary(env).getParseTable(cached.intValue()) == null)
	        	throw new IllegalStateException("Inconsistent context: wrong JSGLR library instance");
	        return true;
	    }
	    
	    ATerm tableTerm = getATermConverter(env).convert(tvars[0]);
		
		JSGLRLibrary lib = getLibrary(env);
		try {
			ParseTable pt = lib.getParseTableManager().loadFromTerm(tableTerm);
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
