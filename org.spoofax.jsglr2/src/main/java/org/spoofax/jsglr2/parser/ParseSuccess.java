package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.stack.AbstractStackNode;

public class ParseSuccess<ParseForest extends AbstractParseForest, StackNode extends AbstractStackNode<ParseForest>, AbstractSyntaxTree>
    extends ParseResult<ParseForest, StackNode, AbstractSyntaxTree> {

    public final ParseForest parseResult;

    public ParseSuccess(Parse<ParseForest, StackNode> parse, ParseForest parseResult) {
        super(parse, true);

        this.parseResult = parseResult;
    }

}
