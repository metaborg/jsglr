package org.spoofax.jsglr2.stack.collections;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.AbstractStackNode;

public class ActiveStacksFactory implements IActiveStacksFactory {

    @Override
    public <ParseForest extends AbstractParseForest, StackNode extends AbstractStackNode<ParseForest>>
        IActiveStacks<StackNode> get(ParserObserving<ParseForest, StackNode> observing) {
        return new ActiveStacks<>(observing);
    }

}
