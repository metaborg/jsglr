package org.spoofax.interpreter.library.jsglr;

import org.spoofax.interpreter.core.IContext;
import org.spoofax.interpreter.library.AbstractPrimitive;

public abstract class JSGLRPrimitive extends AbstractPrimitive {

	protected JSGLRPrimitive(String name, int svars, int tvars) {
		super(name, svars, tvars);
	}

    protected static JSGLRLibrary getLibrary(IContext env) {
        return (JSGLRLibrary) env.getOperatorRegistry(JSGLRLibrary.REGISTRY_NAME);
    }

}
