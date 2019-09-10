package org.spoofax.jsglr2.cli.output;

import java.io.PrintStream;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.JSGLR2Success;
import org.spoofax.jsglr2.parser.result.ParseSuccess;

public class DefaultOutputProcessor implements IOutputProcessor {

    @Override public void outputParseResult(ParseSuccess<?> parseResult, PrintStream output) {
        output.println(parseResult.parseResult);
    }

    @Override public void outputResult(JSGLR2Success<IStrategoTerm> result, PrintStream output) {
        output.println(result.ast);
    }

}
