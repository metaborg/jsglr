package org.spoofax.jsglr2.imploder;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.stack.AbstractStackNode;

public interface IImploder<ParseForest extends AbstractParseForest, StackNode extends AbstractStackNode<ParseForest>, AbstractSyntaxTree> {

    public ImplodeResult<ParseForest, StackNode, AbstractSyntaxTree> implode(Parse<ParseForest, StackNode> parse,
        ParseForest parseForest);

}
