package org.spoofax.jsglr2.measure;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;
import org.spoofax.jsglr2.stack.collections.IForActorStacksFactory;

public class MeasureForActorStacksFactory implements IForActorStacksFactory {

    MeasureForActorStacks<?, ?> measureForActorStacks = null;

    @SuppressWarnings("unchecked") @Override public <ParseForest extends IParseForest, StackNode extends AbstractStackNode<ParseForest>>
        IForActorStacks<StackNode> get(ParserObserving<ParseForest, StackNode> observing) {
        if(measureForActorStacks == null) {
            MeasureForActorStacks<ParseForest, StackNode> measureForActorStacks =
                new MeasureForActorStacks<>(observing);

            this.measureForActorStacks = measureForActorStacks;

            return measureForActorStacks;
        } else
            return (MeasureForActorStacks<ParseForest, StackNode>) measureForActorStacks;
    }

}
