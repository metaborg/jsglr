package org.spoofax.interpreter.library.jsglr;

import org.spoofax.interpreter.adapter.aterm.WrappedATermFactory;
import org.spoofax.interpreter.library.AbstractStrategoOperatorRegistry;

public class JSGLRLibrary extends AbstractStrategoOperatorRegistry {
    
	public static final String REGISTRY_NAME = "JSGLR";

	public JSGLRLibrary(WrappedATermFactory factory) {
        add(new JSGLR_parse_stratego(factory));
    }

}
