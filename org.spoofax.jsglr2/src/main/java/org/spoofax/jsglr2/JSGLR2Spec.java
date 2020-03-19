package org.spoofax.jsglr2;

import java.util.function.Consumer;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.util.log.ILogger;
import org.metaborg.util.log.LoggerUtils;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.parser.observing.LogParserObserver;

public class JSGLR2Spec {

    private static final ILogger logger = LoggerUtils.logger(JSGLR2.class);
    private static final Consumer<String> loggerConsumer = logger::info;

    public final JSGLR2Variant.Preset variant;
    public final JSGLR2Logging logging;

    public JSGLR2Spec(JSGLR2Variant.Preset variant, JSGLR2Logging logging) {
        this.variant = variant;
        this.logging = logging;
    }

    public JSGLR2<IStrategoTerm> getJSGLR2(IParseTable parseTable) {
        JSGLR2<IStrategoTerm> jsglr2 = variant.getJSGLR2(parseTable);

        if(logging != JSGLR2Logging.None)
            jsglr2.attachObserver(new LogParserObserver(loggerConsumer, logging));

        return jsglr2;
    }

}
