package org.spoofax.jsglr2;

import org.metaborg.parsetable.IParseTable;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.parser.observing.LogParserObserver;

public class JSGLR2Spec {

    public final JSGLR2Variant.Preset variant;
    public final JSGLR2Logging logging;

    public JSGLR2Spec(JSGLR2Variant.Preset variant, JSGLR2Logging logging) {
        this.variant = variant;
        this.logging = logging;
    }

    public JSGLR2<IStrategoTerm> getJSGLR2(IParseTable parseTable) {
        JSGLR2<IStrategoTerm> jsglr2 = variant.getJSGLR2(parseTable);

        if(logging != JSGLR2Logging.None)
            jsglr2.attachObserver(new LogParserObserver(logging));

        return jsglr2;
    }

}
