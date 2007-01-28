package org.spoofax.interpreter.library.jsglr;

import java.io.IOException;

import org.spoofax.interpreter.library.AbstractStrategoOperatorRegistry;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.jsglr.InvalidParseTableException;

public class JSGLRLibrary extends AbstractStrategoOperatorRegistry {
    public JSGLRLibrary(ITermFactory factory) throws IOException, InvalidParseTableException {
        add(new JSGLR_parse_stratego(factory));
    }
}
