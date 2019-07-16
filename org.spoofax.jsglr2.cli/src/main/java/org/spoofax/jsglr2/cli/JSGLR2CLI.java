package org.spoofax.jsglr2.cli;

import java.io.*;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.ParseTableReadException;
import org.metaborg.parsetable.ParseTableReader;
import org.metaborg.parsetable.query.ActionsForCharacterRepresentation;
import org.metaborg.parsetable.query.ProductionToGotoRepresentation;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.*;
import org.spoofax.jsglr2.imploder.ImploderVariant;
import org.spoofax.jsglr2.integration.ParseTableVariant;
import org.spoofax.jsglr2.parseforest.ParseForestConstruction;
import org.spoofax.jsglr2.parseforest.ParseForestRepresentation;
import org.spoofax.jsglr2.parser.IObservableParser;
import org.spoofax.jsglr2.parser.IParser;
import org.spoofax.jsglr2.parser.result.ParseFailure;
import org.spoofax.jsglr2.parser.result.ParseResult;
import org.spoofax.jsglr2.parser.result.ParseSuccess;
import org.spoofax.jsglr2.reducing.Reducing;
import org.spoofax.jsglr2.stack.StackRepresentation;
import org.spoofax.jsglr2.stack.collections.ActiveStacksRepresentation;
import org.spoofax.jsglr2.stack.collections.ForActorStacksRepresentation;
import org.spoofax.jsglr2.tokens.TokenizerVariant;

import picocli.CommandLine;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@CommandLine.Command(name = "JSGLR2 CLI", sortOptions = false)
public class JSGLR2CLI implements Runnable {

    @Option(names = { "-pt", "--parseTable" }, required = true,
        description = "Parse table file") private File parseTableFile;

    @Parameters(arity = "1", description = "The input string to be parsed") private String input;

    @Option(names = { "-im", "--implode" }, negatable = true,
        description = "Implode parse tree to AST") private boolean implode = true;

    @ArgGroup(exclusive = false, validate = false, heading = "Parser variant%n") ParserVariantOptions parserVariant =
        new ParserVariantOptions();

    static class ParserVariantOptions {
        @Option(names = { "--activeStacks" },
            description = "Active stacks implementation: ${COMPLETION-CANDIDATES}") private ActiveStacksRepresentation activeStacksRepresentation =
                ActiveStacksRepresentation.standard();

        @Option(names = { "--forActorStacks" },
            description = "For actor stacks implementation: ${COMPLETION-CANDIDATES}") private ForActorStacksRepresentation forActorStacksRepresentation =
                ForActorStacksRepresentation.standard();

        @Option(names = { "--parseForest" },
            description = "Parse forest representation: ${COMPLETION-CANDIDATES}") private ParseForestRepresentation parseForestRepresentation =
                ParseForestRepresentation.standard();

        @Option(names = { "--parseForestConstruction" },
            description = "Parse forest construction method: ${COMPLETION-CANDIDATES}") private ParseForestConstruction parseForestConstruction =
                ParseForestConstruction.standard();

        @Option(names = { "--stack" },
            description = "Stack representation: ${COMPLETION-CANDIDATES}") private StackRepresentation stackRepresentation =
                StackRepresentation.standard();

        @Option(names = { "--reducing" },
            description = "Reducing implementation: ${COMPLETION-CANDIDATES}") private Reducing reducing =
                Reducing.standard();

        @Option(names = { "--imploder" },
            description = "Imploder variant: ${COMPLETION-CANDIDATES}") private ImploderVariant imploderVariant =
                ImploderVariant.standard();

        @Option(names = { "--tokenizer" },
            description = "Tokenizer variant: ${COMPLETION-CANDIDATES}") private TokenizerVariant tokenizerVariant =
                TokenizerVariant.standard();

        JSGLR2Variants.Variant getVariant() throws WrappedException {
            JSGLR2Variants.ParserVariant parserVariant =
                new JSGLR2Variants.ParserVariant(activeStacksRepresentation, forActorStacksRepresentation,
                    parseForestRepresentation, parseForestConstruction, stackRepresentation, reducing);

            JSGLR2Variants.Variant variant =
                new JSGLR2Variants.Variant(parserVariant, imploderVariant, tokenizerVariant);

            if(variant.isValid())
                return variant;
            else
                throw new WrappedException("Invalid parser variant");
        }
    }

    @ArgGroup(exclusive = false, validate = false,
        heading = "Parse table variant%n") ParseTableVariantOptions parseTableVariant = new ParseTableVariantOptions();

    static class ParseTableVariantOptions {
        @Option(names = { "--actionsForCharacters" },
            description = "Actions for character representation: ${COMPLETION-CANDIDATES}") private ActionsForCharacterRepresentation actionsForCharacterRepresentation =
                ActionsForCharacterRepresentation.standard();

        @Option(names = { "--productionToGoto" },
            description = "Production to goto representation: ${COMPLETION-CANDIDATES}") private ProductionToGotoRepresentation productionToGotoRepresentation =
                ProductionToGotoRepresentation.standard();

        ParseTableVariant getVariant() {
            return new ParseTableVariant(actionsForCharacterRepresentation, productionToGotoRepresentation);
        }
    }

    @Option(names = { "--logging" }, description = "Log parser operations") boolean logging = false;

    @ArgGroup(exclusive = false, validate = false, heading = "Output%n") OutputOptions outputOptions =
        new OutputOptions();

    static class OutputOptions {
        boolean isParseResult() {
            return dot == null;
        }

        @Option(names = { "-o", "--output" }, required = false, description = "Output file") private File outputFile;

        @Option(names = "--dot", required = true,
            description = "Visualization in DOT: ${COMPLETION-CANDIDATES}") DotVisualization dot;

        @Option(names = "--dot-format", required = false,
            description = "DOT format: ${COMPLETION-CANDIDATES}") DotVisualizationFormat dotFormat =
                DotVisualizationFormat.Text;
    }

    enum DotVisualization {
        Stack, ParseForest
    }

    enum DotVisualizationFormat {
        Text, PDF, PNG
    }

    @Option(names = { "-v", "--verbose" }, negatable = true, description = "Print stack traces") boolean verbose =
        false;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new JSGLR2CLI()).execute(args);

        System.exit(exitCode);
    }

    public void run() {
        try {
            JSGLR2Variants.Variant variant = parserVariant.getVariant();
            IParseTable parseTable = getParseTable();
            JSGLR2Implementation<?, ?, IStrategoTerm> jsglr2 =
                (JSGLR2Implementation<?, ?, IStrategoTerm>) JSGLR2Variants.getJSGLR2(parseTable, variant);
            IObservableParser<?, ?> observableParser = (IObservableParser<?, ?>) jsglr2.parser;

            if(logging)
                observableParser.observing().attachObserver(new LogParserObserver<>(this::output));

            if(outputOptions.dot == DotVisualization.Stack)
                observableParser.observing().attachObserver(new StackDotVisualisationParserObserver<>(this::outputDot));

            if(outputOptions.dot == DotVisualization.ParseForest)
                observableParser.observing()
                    .attachObserver(new ParseForestDotVisualisationParserObserver<>(this::outputDot));

            if(implode)
                parseAndImplode(jsglr2);
            else
                parse(jsglr2.parser);
        } catch(WrappedException e) {
            failOnWrappedException(e, verbose);
        }
    }

    private void parse(IParser<?> parser) {
        ParseResult<?> result = parser.parse(input);

        if(result.isSuccess()) {
            ParseSuccess<?> success = (ParseSuccess<?>) result;

            if(outputOptions.isParseResult())
                output(success.parseResult.toString());
        } else {
            ParseFailure<?> failure = (ParseFailure<?>) result;

            if(outputOptions.isParseResult())
                output(failure.failureType.message);
        }
    }

    private void parseAndImplode(JSGLR2Implementation<?, ?, IStrategoTerm> jsglr2) {
        JSGLR2Result<IStrategoTerm> result = jsglr2.parseResult(input);

        if(result.isSuccess()) {
            JSGLR2Success<IStrategoTerm> success = (JSGLR2Success<IStrategoTerm>) result;

            if(outputOptions.isParseResult())
                output(success.ast.toString());
        } else {
            JSGLR2Failure<IStrategoTerm> failure = (JSGLR2Failure<IStrategoTerm>) result;

            if(outputOptions.isParseResult())
                output(failure.parseFailure.failureType.message);
        }
    }

    private void output(String output) {
        try(OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream())) {
            outputStreamWriter.write(output);
        } catch(IOException e) {
            failOnWrappedException(new WrappedException("Writing output failed", e), verbose);
        }
    }

    private void outputDot(String dot) {
        try {
            switch (outputOptions.dotFormat) {
                case Text:
                    output(dot);
                    break;
                case PDF:
                    outputDot(dot, "pdf");
                    break;
                case PNG:
                    outputDot(dot, "png");
                    break;
            }
        } catch (WrappedException e) {
            failOnWrappedException(e, verbose);
        }
    }

    private void outputDot(String dot, String format) throws WrappedException {
        try(OutputStream outputStream = outputStream()) {
            Process pr = Runtime.getRuntime().exec("dot -T" + format);

            try (InputStream dotOutputStream = pr.getInputStream(); OutputStream input = pr.getOutputStream()) {
                input.write(dot.getBytes(Charset.forName("UTF-8")));
                input.close();

                IOUtils.copy(dotOutputStream, outputStream);

                if (!pr.waitFor(5, TimeUnit.SECONDS)) {
                    int exitCode = pr.exitValue();

                    if (exitCode == 0)
                        throw new WrappedException("DOT timed out");
                    else
                        throw new WrappedException("DOT exited with " + exitCode);
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new WrappedException("Writing output failed", e);
        }
    }

    private OutputStream outputStream() throws IOException {
        if(outputOptions.outputFile != null)
            return new FileOutputStream(outputOptions.outputFile);
        else
            return System.out;
    }

    private IParseTable getParseTable() throws WrappedException {
        try {
            InputStream parseTableInputStream = new FileInputStream(parseTableFile);
            ParseTableReader parseTableReader = parseTableVariant.getVariant().parseTableReader();

            return parseTableReader.read(parseTableInputStream);
        } catch(IOException e) {
            throw new WrappedException("Invalid parse table file", e);
        } catch(ParseTableReadException e) {
            throw new WrappedException("Invalid parse table", e);
        }
    }

    private static void failOnWrappedException(WrappedException e, boolean verbose) {
        System.out.println(e.message);

        if(verbose && e.exception != null)
            e.exception.printStackTrace();
    }

}
