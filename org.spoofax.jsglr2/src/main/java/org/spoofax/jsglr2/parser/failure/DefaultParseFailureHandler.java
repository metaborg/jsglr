package org.spoofax.jsglr2.parser.failure;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.result.ParseFailureType;
import org.spoofax.jsglr2.stack.AbstractStackNode;

public class DefaultParseFailureHandler<ParseForest extends AbstractParseForest, StackNode extends AbstractStackNode<ParseForest>>
    implements IParseFailureHandler<ParseForest, StackNode> {

    public void onFailure(Parse<ParseForest, StackNode> parse) {
        
    }
    
    public ParseFailureType failureType(Parse<ParseForest, StackNode> parse) {
        return ParseFailureType.Unknown;
    }

}
