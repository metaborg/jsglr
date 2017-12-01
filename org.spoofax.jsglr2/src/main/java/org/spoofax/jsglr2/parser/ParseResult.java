package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.JSGLR2Result;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.stack.AbstractStackNode;

public abstract class ParseResult<ParseForest extends AbstractParseForest, StackNode extends AbstractStackNode<ParseForest>, AbstractSyntaxTree>
    extends JSGLR2Result<ParseForest, StackNode, AbstractSyntaxTree> {

    protected ParseResult(Parse<ParseForest, StackNode> parse, boolean isSuccess) {
        super(parse, isSuccess);
    }

}
