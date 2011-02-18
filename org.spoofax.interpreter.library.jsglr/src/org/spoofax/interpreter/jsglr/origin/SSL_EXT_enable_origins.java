package org.spoofax.interpreter.jsglr.origin;

import org.spoofax.interpreter.core.IContext;
import org.spoofax.interpreter.library.AbstractPrimitive;
import org.spoofax.interpreter.stratego.Strategy;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.jsglr.client.imploder.ImploderOriginTermFactory;
import org.spoofax.terms.attachments.OriginTermFactory;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class SSL_EXT_enable_origins extends AbstractPrimitive {
    
    public SSL_EXT_enable_origins() {
        super("SSL_EXT_enable_origins", 0, 0);
    }

    @Override
    public boolean call(IContext env, Strategy[] svars, IStrategoTerm[] tvars) {
    	ITermFactory factory = env.getFactory();
    	if (!(factory instanceof OriginTermFactory))
    		env.setFactory(new ImploderOriginTermFactory(factory));
    	return true;
    }

}
