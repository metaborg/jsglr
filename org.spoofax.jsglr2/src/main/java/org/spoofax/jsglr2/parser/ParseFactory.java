package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;

public interface ParseFactory
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends IStackNode,
    Parse       extends AbstractParse<ParseForest, StackNode>>
//@formatter:on
{

    Parse get(String inputString, String filename, ParserObserving<ParseForest, StackNode> observing);

}
