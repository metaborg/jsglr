package org.spoofax.jsglr2.stack.collections;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.AbstractStackNode;

public class ForActorStacksFactory implements IForActorStacksFactory {

    @Override
    public <ParseForest extends AbstractParseForest, StackNode extends AbstractStackNode<ParseForest>>
        IForActorStacks<StackNode> get(ParserObserving<ParseForest, StackNode> observing) {
        return new ForActorStacks<>(observing);
    }

}
