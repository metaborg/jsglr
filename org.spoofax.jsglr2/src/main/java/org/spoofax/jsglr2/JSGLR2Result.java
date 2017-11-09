package org.spoofax.jsglr2;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.stack.AbstractStackNode;

public abstract class JSGLR2Result<StackNode extends AbstractStackNode<ParseForest>, ParseForest extends AbstractParseForest, AbstractSyntaxTree> {

    public final Parse<StackNode, ParseForest> parse;
    public final boolean isSuccess;

    protected JSGLR2Result(Parse<StackNode, ParseForest> parse, boolean isSuccess) {
        this.parse = parse;
        this.isSuccess = isSuccess;
    }

}
