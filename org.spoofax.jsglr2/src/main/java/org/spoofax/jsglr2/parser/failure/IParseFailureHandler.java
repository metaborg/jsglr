package org.spoofax.jsglr2.parser.failure;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.result.ParseFailureType;
import org.spoofax.jsglr2.stack.AbstractStackNode;

public interface IParseFailureHandler
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends AbstractStackNode<ParseForest>>
//@formatter:on
{

    void onFailure(AbstractParse<ParseForest, StackNode> parse);

    ParseFailureType failureType(AbstractParse<ParseForest, StackNode> parse);

}
