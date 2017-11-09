package org.spoofax.jsglr2.imploder;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.stack.AbstractStackNode;

public interface IImploder<StackNode extends AbstractStackNode<ParseForest>, ParseForest extends AbstractParseForest, AbstractSyntaxTree> {

    public ImplodeResult<StackNode, ParseForest, AbstractSyntaxTree> implode(Parse<StackNode, ParseForest> parse,
        ParseForest parseForest);

}
