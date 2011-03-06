package org.spoofax.interpreter.library.jsglr.origin;

import org.spoofax.interpreter.core.IContext;
import org.spoofax.interpreter.library.AbstractPrimitive;
import org.spoofax.interpreter.stratego.Strategy;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.interpreter.terms.TermConverter;
import org.spoofax.terms.attachments.ParentTermFactory;

/**a
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class SSL_EXT_clone_and_set_parents extends AbstractPrimitive {
    
    public SSL_EXT_clone_and_set_parents() {
        super("SSL_EXT_clone_and_set_parents", 0, 1);
    }

    @Override
    public boolean call(IContext env, Strategy[] svars, IStrategoTerm[] tvars) {
    	ITermFactory factory = env.getFactory();
    	if (!ParentTermFactory.isParentTermFactory(factory))
    		factory = new ParentTermFactory(factory);
    	TermConverter converter = new TermConverter(factory);
    	converter.setOriginEnabled(true);
    	env.setCurrent(converter.convert(tvars[0]));
    	return true;
    }

}
