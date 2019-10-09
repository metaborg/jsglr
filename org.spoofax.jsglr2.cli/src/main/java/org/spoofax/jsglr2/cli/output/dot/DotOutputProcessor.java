package org.spoofax.jsglr2.cli.output.dot;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.JSGLR2Success;
import org.spoofax.jsglr2.cli.JSGLR2CLI;
import org.spoofax.jsglr2.cli.WrappedException;
import org.spoofax.jsglr2.cli.output.IOutputProcessor;
import org.spoofax.jsglr2.parser.observing.ParserObserver;
import org.spoofax.jsglr2.parser.result.ParseSuccess;

import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Option;

public class DotOutputProcessor implements IOutputProcessor {

    private DotOutputOptions dotOutputOptions;

    @ArgGroup(exclusive = false, heading = "Dot output options%n") public void
        setDotOutputOptions(DotOutputOptions dotOutputOptions) {
        this.dotOutputOptions = dotOutputOptions;
        JSGLR2CLI.outputProcessor = this;
    }

    static class DotOutputOptions {

        @Option(names = "--dot",
            description = "Visualization in DOT: ${COMPLETION-CANDIDATES} (default: ${DEFAULT-VALUE})") DotVisualization dot =
                DotVisualization.ParseForest;

        @Option(names = "--dot-format",
            description = "DOT format: ${COMPLETION-CANDIDATES} (default: ${DEFAULT-VALUE})") DotVisualizationFormat dotFormat =
                DotVisualizationFormat.text;

    }

    protected enum DotVisualization {
        Stack, ParseForest
    }

    protected enum DotVisualizationFormat {
        text,
        // Not used directly, but converted to string in outputParseResult
        @SuppressWarnings("unused")
        pdf,
        // Not used directly, but converted to string in outputParseResult
        @SuppressWarnings("unused")
        png
    }

    private DotVisualisationParserObserver<?, ?, ?, ?, ?> observer;

    @Override public void checkAllowed(JSGLR2CLI cli) throws WrappedException {
        if(cli.input.length != 1)
            throw new WrappedException("Dot visualisation is only supported for exactly one input.");
    }

    @Override public List<ParserObserver<?, ?, ?, ?, ?>> observers() {
        switch(dotOutputOptions.dot) {
            default:
            case ParseForest:
                observer = new ParseForestDotVisualisationParserObserver<>();
                break;
            case Stack:
                observer = new StackDotVisualisationParserObserver<>();
                break;
        }
        return Collections.singletonList(observer);
    }

    @Override public void outputParseResult(ParseSuccess<?> parseResult, PrintStream output) throws WrappedException {
        if(dotOutputOptions.dotFormat == DotVisualizationFormat.text)
            output.println(observer.output());
        else
            outputDot(output, dotOutputOptions.dotFormat.name());
    }

    @Override public void outputResult(JSGLR2Success<IStrategoTerm> result, PrintStream output)
        throws WrappedException {
        outputParseResult(null, output);
    }

    private void outputDot(PrintStream output, String format) throws WrappedException {
        try {
            Process pr = Runtime.getRuntime().exec("dot -T" + format);

            try(InputStream dotOutputStream = pr.getInputStream(); OutputStream input = pr.getOutputStream()) {
                input.write(observer.output().getBytes(StandardCharsets.UTF_8));
                input.close();

                IOUtils.copy(dotOutputStream, output);

                if(!pr.waitFor(5, TimeUnit.SECONDS)) {
                    int exitCode = pr.exitValue();

                    if(exitCode == 0)
                        throw new WrappedException("DOT timed out");
                    else
                        throw new WrappedException("DOT exited with " + exitCode);
                }
            }
        } catch(IOException | InterruptedException e) {
            throw new WrappedException("Writing output failed", e);
        }
    }

}
