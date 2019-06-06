package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;

public interface IObservableParser<ParseForest extends IParseForest, StackNode extends IStackNode>
    extends IParser<ParseForest> {

    ParserObserving<ParseForest, StackNode> observing();

}
