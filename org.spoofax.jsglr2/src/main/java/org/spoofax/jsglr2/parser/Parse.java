package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.JSGLR2Variants;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;

public class Parse
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends IStackNode,
    ParseState  extends IParseState<ParseForest, StackNode>>
//@formatter:on
    extends AbstractParse<ParseForest, StackNode, ParseState> {

    public static
//@formatter:off
   <ParseForest_ extends IParseForest,
    StackNode_   extends IStackNode,
    ParseState_  extends IParseState<ParseForest_, StackNode_>,
    Parse_       extends AbstractParse<ParseForest_, StackNode_, ParseState_>>
//@formatter:on
    ParseFactory<ParseForest_, StackNode_, ParseState_, Parse_> factory(JSGLR2Variants.ParserVariant variant) {
        return (inputString, filename, observing) -> (Parse_) new Parse<>(variant, inputString, filename, observing);
    }

    public Parse(JSGLR2Variants.ParserVariant variant, String inputString, String filename,
        ParserObserving<ParseForest, StackNode, ParseState> observing) {
        super(variant, inputString, filename, observing);
    }
}
