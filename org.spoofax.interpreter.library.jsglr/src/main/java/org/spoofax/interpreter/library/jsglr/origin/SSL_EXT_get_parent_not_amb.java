package org.spoofax.interpreter.library.jsglr.origin;

import org.spoofax.interpreter.core.IContext;
import org.spoofax.interpreter.library.AbstractPrimitive;
import org.spoofax.interpreter.stratego.Strategy;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.ImploderAttachment;
import org.spoofax.terms.attachments.ParentAttachment;

/**
 * @author Eduardo Souza
 */
public class SSL_EXT_get_parent_not_amb extends AbstractPrimitive {
    
    public SSL_EXT_get_parent_not_amb() {
        super("SSL_EXT_get_parent_not_amb", 0, 1);
    }

    @Override
    public boolean call(IContext env, Strategy[] svars, IStrategoTerm[] tvars) {
    	IStrategoTerm parent = ParentAttachment.getParent(tvars[0]);
    	if (parent == null) return false;
		IStrategoTerm result = getNonAmbiguityParent(parent);
    	env.setCurrent(result);
    	return true;
    }

    private IStrategoTerm getNonAmbiguityParent(IStrategoTerm parent) {
        if (ImploderAttachment.getSort(parent) == null){
            IStrategoTerm grandParent = ParentAttachment.getParent(parent);
            return getNonAmbiguityParent(grandParent);
        }
        return parent;
    }

}
