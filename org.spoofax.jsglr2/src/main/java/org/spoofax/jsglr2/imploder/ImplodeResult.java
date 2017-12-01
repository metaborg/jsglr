package org.spoofax.jsglr2.imploder;

import org.spoofax.jsglr2.JSGLR2Result;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.stack.AbstractStackNode;

public class ImplodeResult<ParseForest extends AbstractParseForest, StackNode extends AbstractStackNode<ParseForest>, AbstractSyntaxTree>
    extends JSGLR2Result<ParseForest, StackNode, AbstractSyntaxTree> {

    public final AbstractSyntaxTree ast;

    public ImplodeResult(Parse<ParseForest, StackNode> parse, AbstractSyntaxTree ast) {
        super(parse, true);

        this.ast = ast;
    }

}
