package org.spoofax.interpreter.library.jsglr.origin;

import org.spoofax.interpreter.core.IContext;
import org.spoofax.interpreter.library.AbstractPrimitive;
import org.spoofax.interpreter.stratego.Strategy;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.terms.attachments.ParentAttachment;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class SSL_EXT_get_parent extends AbstractPrimitive {
    
    public SSL_EXT_get_parent() {
        super("SSL_EXT_get_parent", 0, 0);
    }

    @Override
    public boolean call(IContext env, Strategy[] svars, IStrategoTerm[] tvars) {
    	IStrategoTerm parent = ParentAttachment.getParent(env.current());
    	if (parent == null) return false;
		env.setCurrent(parent);
    	return true;
    }

}
