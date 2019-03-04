package org.spoofax.jsglr2.parser.failure;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.result.ParseFailureType;
import org.spoofax.jsglr2.stack.AbstractStackNode;

public class DefaultParseFailureHandler
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends AbstractStackNode<ParseForest>>
//@formatter:on
    implements IParseFailureHandler<ParseForest, StackNode> {

    public void onFailure(AbstractParse<ParseForest, StackNode> parse) {

    }

    public ParseFailureType failureType(AbstractParse<ParseForest, StackNode> parse) {
        return ParseFailureType.Unknown;
    }

}
