package org.spoofax.jsglr2.cli;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.JSGLR2Failure;
import org.spoofax.jsglr2.JSGLR2Implementation;
import org.spoofax.jsglr2.JSGLR2Result;
import org.spoofax.jsglr2.JSGLR2Success;
import org.spoofax.jsglr2.cli.output.DefaultOutputProcessor;
import org.spoofax.jsglr2.cli.output.IOutputProcessor;
import org.spoofax.jsglr2.cli.output.dot.DotOutputProcessor;
import org.spoofax.jsglr2.cli.parserbuilder.ParserBuilder;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.IObservableParser;
import org.spoofax.jsglr2.parser.IParser;
import org.spoofax.jsglr2.parser.observing.IParserObserver;
import org.spoofax.jsglr2.parser.observing.LogParserObserver;
import org.spoofax.jsglr2.parser.result.ParseFailure;
import org.spoofax.jsglr2.parser.result.ParseResult;
import org.spoofax.jsglr2.parser.result.ParseSuccess;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "JSGLR2 CLI", sortOptions = false, mixinStandardHelpOptions = true, abbreviateSynopsis = true)
public class JSGLR2CLI implements Runnable {

    @Parameters(arity = "1..*", description = "The input file(s)/string(s) to be parsed") public String[] input;

    @Option(names = { "-i", "--im", "--implode" }, negatable = true,
        description = "Implode parse tree to AST") public boolean implode = true;

    @Option(names = { "--logging" }, description = "Log parser operations") public boolean logging = false;

    @Option(names = { "-o", "--output" }, description = "Output file") public File outputFile;

    @Option(names = { "-v", "--verbose" }, negatable = true,
        description = "Print stack traces") public boolean verbose = false;

    @Option(names = { "-l", "--language" },
        description = "The identifier of a Spoofax language in the local Maven repository, in the format '[groupId]/[artifactId]/[version]', where the groupId is slash-separated, the artifactId is period-separated, and the version is optional. Examples: 'org/metaborg/lang.java', 'org/metaborg/lang.java/1.1.0-SNAPSHOT'") public static String language;

    // TODO As alternative to the parser builder, we could also load a Spoofax language and use its parsing services.
    // However, that cannot easily be combined with the custom output options, because 1) not all languages use JSGLR2
    // and 2) you can't seem to be able to get a JSGLR2 parser instance from a Spoofax service...
    @Mixin public ParserBuilder parserBuilder = new ParserBuilder();

    @Mixin public DotOutputProcessor dotOutputOptions = new DotOutputProcessor();

    /**
     * To create a custom output processor, just set this static variable to something else. For an example:
     * 
     * @see DotOutputProcessor#setDotOutputOptions
     */
    public static IOutputProcessor outputProcessor = new DefaultOutputProcessor();

    public static void main(String[] args) {
        int exitCode = new CommandLine(new JSGLR2CLI()).execute(args);

        System.exit(exitCode);
    }

    private PrintStream outputStream;

    public void run() {
        try {
            if(language == null && parserBuilder.parseTableOptions.parseTableFile == null) {
                System.err.println("Either option --parseTable or --language should be provided");
                new CommandLine(new JSGLR2CLI()).execute("-h");
                System.exit(2);
            }

            outputProcessor.checkAllowed(this);

            JSGLR2Implementation<IParseForest, ?, ?, IStrategoTerm, ?, ?> jsglr2 =
                (JSGLR2Implementation<IParseForest, ?, ?, IStrategoTerm, ?, ?>) parserBuilder.getJSGLR2();
            IObservableParser<?, ?, ?, ?, ?> observableParser = jsglr2.parser;

            outputStream = outputStream();

            if(logging)
                observableParser.observing().attachObserver(new LogParserObserver<>(outputStream::println));

            for(IParserObserver observer : outputProcessor.observers()) {
                observableParser.observing().attachObserver(observer);
            }

            // For each input, try to check if it is a file, and if so, read its contents
            for(int i = 0; i < input.length; i++) {
                File file = new File(input[i]);
                if(file.isFile()) {
                    try(Scanner s = new Scanner(new FileInputStream(file))) {
                        s.useDelimiter("\\A");
                        input[i] = s.hasNext() ? s.next() : "";
                    } catch(FileNotFoundException e) {
                        throw new WrappedException("File not found", e);
                    }
                }
            }

            if(implode)
                parseAndImplode(jsglr2, outputProcessor);
            else
                parse(jsglr2.parser, outputProcessor);
        } catch(WrappedException e) {
            failOnWrappedException(e);
        }
    }

    private void parse(IParser<IParseForest> parser, IOutputProcessor outputProcessor) throws WrappedException {
        String prevInput = null;
        IParseForest prevParseForest = null;
        for(String in : input) {
            ParseResult<?> result = parser.parse(in, prevInput, prevParseForest);

            if(result.isSuccess()) {
                prevInput = in;
                prevParseForest = ((ParseSuccess<?>) result).parseResult;
                outputProcessor.outputParseResult((ParseSuccess<?>) result, outputStream);
            } else
                outputProcessor.outputParseFailure((ParseFailure<?>) result, outputStream);
        }
    }

    private void parseAndImplode(JSGLR2Implementation<?, ?, ?, IStrategoTerm, ?, ?> jsglr2,
        IOutputProcessor outputProcessor) throws WrappedException {
        for(String in : input) {
            // Explicit filename to enable caching in incremental parser
            JSGLR2Result<IStrategoTerm> result = jsglr2.parseResult(in);

            if(result.isSuccess())
                outputProcessor.outputResult((JSGLR2Success<IStrategoTerm>) result, outputStream);
            else
                outputProcessor.outputFailure((JSGLR2Failure<IStrategoTerm>) result, outputStream);
        }
    }

    private PrintStream outputStream() throws WrappedException {
        try {
            if(outputFile != null)
                return new PrintStream(outputFile);
            else
                return System.out;
        } catch(FileNotFoundException e) {
            throw new WrappedException("Invalid output", e);
        }
    }

    private void failOnWrappedException(WrappedException e) {
        System.err.println(e.getMessage());

        if(verbose && e.getCause() != null)
            e.getCause().printStackTrace();
    }

}
