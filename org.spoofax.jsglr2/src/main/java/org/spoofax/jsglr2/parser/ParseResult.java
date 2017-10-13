package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.JSGLR2Result;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.stack.AbstractStackNode;

public abstract class ParseResult<StackNode extends AbstractStackNode<ParseForest>, ParseForest extends AbstractParseForest, AbstractSyntaxTree> extends JSGLR2Result<StackNode, ParseForest, AbstractSyntaxTree> {

    protected ParseResult(Parse<StackNode, ParseForest> parse, boolean isSuccess) {
        super(parse, isSuccess);
    }
    
}
