package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.stack.AbstractStackNode;

public class ParseSuccess<StackNode extends AbstractStackNode<ParseForest>, ParseForest extends AbstractParseForest, AbstractSyntaxTree>
    extends ParseResult<StackNode, ParseForest, AbstractSyntaxTree> {

    public final ParseForest parseResult;

    public ParseSuccess(Parse<StackNode, ParseForest> parse, ParseForest parseResult) {
        super(parse, true);

        this.parseResult = parseResult;
    }

}
