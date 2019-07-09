package org.spoofax.jsglr2.cli;

import java.util.function.Consumer;
import java.util.regex.Matcher;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.observing.ParserObserver;
import org.spoofax.jsglr2.parser.result.ParseFailure;
import org.spoofax.jsglr2.parser.result.ParseSuccess;
import org.spoofax.jsglr2.stack.IStackNode;

abstract class DotVisualisationParserObserver
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends IStackNode>
//@formatter:on
    extends ParserObserver<ParseForest, StackNode> {

    final Consumer<String> outputConsumer;

    DotVisualisationParserObserver(Consumer<String> outputConsumer) {
        this.outputConsumer = outputConsumer;
    }

    StringBuilder sb;

    @Override public void parseStart(AbstractParse<ParseForest, StackNode> parse) {
        super.parseStart(parse);
        sb = new StringBuilder();
    }

    String stackNodeId(StackNode stack) {
        return stackNodeId(id(stack));
    }

    String stackNodeId(int id) {
        return "stack_" + id;
    }

    void append(String string) {
        sb.append(string + "\n");
    }

    @Override public void success(ParseSuccess<ParseForest> success) {
        output();
    }

    @Override public void failure(ParseFailure<ParseForest> failure) {
        output();
    }

    abstract void output();

    String escape(String string) {
        return string.replaceAll("\"", Matcher.quoteReplacement("\\\""));
    }

}
