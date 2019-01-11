package org.spoofax.jsglr2.parser.failure;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.result.ParseFailureType;
import org.spoofax.jsglr2.stack.AbstractStackNode;

public interface IParseFailureHandler<ParseForest extends AbstractParseForest, StackNode extends AbstractStackNode<ParseForest>> {

    void onFailure(Parse<ParseForest, StackNode> parse);
    
    ParseFailureType failureType(Parse<ParseForest, StackNode> parse);
    
}
