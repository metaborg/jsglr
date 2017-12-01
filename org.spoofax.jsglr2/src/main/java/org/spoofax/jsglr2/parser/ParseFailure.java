package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.stack.AbstractStackNode;

public class ParseFailure<ParseForest extends AbstractParseForest, StackNode extends AbstractStackNode<ParseForest>, AbstractSyntaxTree>
    extends ParseResult<ParseForest, StackNode, AbstractSyntaxTree> {

    public final ParseException parseException;

    public ParseFailure(Parse<ParseForest, StackNode> parse, ParseException parseException) {
        super(parse, false);

        this.parseException = parseException;
    }

}
