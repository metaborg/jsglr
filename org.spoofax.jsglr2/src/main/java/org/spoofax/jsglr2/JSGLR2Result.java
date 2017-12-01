package org.spoofax.jsglr2;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.stack.AbstractStackNode;

public abstract class JSGLR2Result<ParseForest extends AbstractParseForest, StackNode extends AbstractStackNode<ParseForest>, AbstractSyntaxTree> {

    public final Parse<ParseForest, StackNode> parse;
    public final boolean isSuccess;

    protected JSGLR2Result(Parse<ParseForest, StackNode> parse, boolean isSuccess) {
        this.parse = parse;
        this.isSuccess = isSuccess;
    }

}
