package org.spoofax.interpreter.library.jsglr.origin;

import org.spoofax.interpreter.core.IContext;
import org.spoofax.interpreter.core.Tools;
import org.spoofax.interpreter.library.AbstractPrimitive;
import org.spoofax.interpreter.stratego.Strategy;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.jsglr.client.imploder.ImploderOriginTermFactory;
import org.spoofax.terms.attachments.OriginTermFactory;

/**
 * @author Maartje de Jonge
 */
public class SSL_EXT_enable_desugared_origins extends AbstractPrimitive {
    
    public SSL_EXT_enable_desugared_origins() {
        super("SSL_EXT_enable_desugared_origins", 0, 1);
    }

    @Override
    public boolean call(IContext env, Strategy[] svars, IStrategoTerm[] tvars) {
    	ITermFactory factory = env.getFactory();
    	boolean enable = Tools.asJavaInt(tvars[0]) != 0;
    	if (factory instanceof OriginTermFactory){
       		((OriginTermFactory)env.getFactory()).setAssignDesugaredOrigins(enable);    		
    	}
    	else if (enable){
    		OriginTermFactory originFactory = new ImploderOriginTermFactory(factory);
    		originFactory.setAssignDesugaredOrigins(enable);
    		env.setFactory(originFactory);
    	}
    	return true;
    }

}
