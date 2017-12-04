package org.spoofax.jsglr2.measure;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;
import org.spoofax.jsglr2.stack.collections.IForActorStacksFactory;

public class MeasureForActorStacksFactory implements IForActorStacksFactory {

    MeasureForActorStacks<?, ?> measureForActorStacks = null;

    @Override
    public <ParseForest extends AbstractParseForest, StackNode extends AbstractStackNode<ParseForest>>
        IForActorStacks<StackNode> get(ParserObserving<ParseForest, StackNode> observing) {
        MeasureForActorStacks<ParseForest, StackNode> measureForActorStacks = new MeasureForActorStacks<>(observing);

        this.measureForActorStacks = measureForActorStacks;

        return measureForActorStacks;
    }

}
