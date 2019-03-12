package org.spoofax.jsglr2.parser.failure;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.result.ParseFailureType;
import org.spoofax.jsglr2.stack.AbstractStackNode;

public class DefaultParseFailureHandler<ParseForest extends AbstractParseForest, StackNode extends AbstractStackNode<ParseForest>>
    implements IParseFailureHandler<ParseForest, StackNode> {

    public void onFailure(AbstractParse<ParseForest, StackNode> parse) {
        
    }
    
    public ParseFailureType failureType(AbstractParse<ParseForest, StackNode> parse) {
        return ParseFailureType.Unknown;
    }

}
