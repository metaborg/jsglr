package org.spoofax.jsglr2.cli.output;

import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.JSGLR2Failure;
import org.spoofax.jsglr2.JSGLR2Success;
import org.spoofax.jsglr2.cli.JSGLR2CLI;
import org.spoofax.jsglr2.cli.WrappedException;
import org.spoofax.jsglr2.parser.observing.ParserObserver;
import org.spoofax.jsglr2.parser.result.ParseFailure;
import org.spoofax.jsglr2.parser.result.ParseSuccess;

public interface IOutputProcessor {

    default void checkAllowed(JSGLR2CLI cli) throws WrappedException {
    }

    default List<ParserObserver<?, ?, ?, ?, ?>> observers() {
        return Collections.emptyList();
    }

    void outputParseResult(ParseSuccess<?> parseResult, PrintStream output) throws WrappedException;

    default void outputParseFailure(ParseFailure<?> parseResult, PrintStream output) throws WrappedException {
        output.println(parseResult.failureType.message);
    }

    void outputResult(JSGLR2Success<IStrategoTerm> result, PrintStream output) throws WrappedException;

    default void outputFailure(JSGLR2Failure<IStrategoTerm> result, PrintStream output) throws WrappedException {
        output.println(result.parseFailure.failureType.message);
    }

}
