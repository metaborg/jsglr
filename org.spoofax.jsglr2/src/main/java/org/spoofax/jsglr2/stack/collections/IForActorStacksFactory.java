package org.spoofax.jsglr2.stack.collections;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;

public interface IForActorStacksFactory {

    <ParseForest extends IParseForest, StackNode extends IStackNode, ParseState extends AbstractParseState<ParseForest, StackNode>>
        IForActorStacks<StackNode> get(ParserObserving<ParseForest, StackNode, ParseState> observing);

}
