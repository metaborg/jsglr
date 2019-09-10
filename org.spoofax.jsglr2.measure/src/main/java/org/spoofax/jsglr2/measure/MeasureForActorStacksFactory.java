package org.spoofax.jsglr2.measure;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;
import org.spoofax.jsglr2.stack.collections.IForActorStacksFactory;

public class MeasureForActorStacksFactory implements IForActorStacksFactory {

    MeasureForActorStacks<?, ?, ?> measureForActorStacks = null;

    @SuppressWarnings("unchecked") @Override public <ParseForest extends IParseForest, StackNode extends IStackNode, ParseState extends AbstractParseState<ParseForest, StackNode>>
        IForActorStacks<StackNode> get(ParserObserving<ParseForest, StackNode, ParseState> observing) {
        if(measureForActorStacks == null) {
            MeasureForActorStacks<ParseForest, StackNode, ParseState> measureForActorStacks =
                new MeasureForActorStacks<>(observing);

            this.measureForActorStacks = measureForActorStacks;

            return measureForActorStacks;
        } else
            return (MeasureForActorStacks<ParseForest, StackNode, ParseState>) measureForActorStacks;
    }

}
