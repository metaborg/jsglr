package org.spoofax.jsglr2.stack.collections;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.AbstractStackNode;

public interface IActiveStacksFactory {

    <ParseForest extends IParseForest, StackNode extends AbstractStackNode<ParseForest>> IActiveStacks<StackNode>
        get(ParserObserving<ParseForest, StackNode> observing);

}
