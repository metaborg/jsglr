package org.spoofax.jsglr2.measure;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.collections.IActiveStacks;
import org.spoofax.jsglr2.stack.collections.IActiveStacksFactory;

public class MeasureActiveStacksFactory implements IActiveStacksFactory {

    MeasureActiveStacks<?, ?> measureActiveStacks = null;

    @Override
    public <ParseForest extends AbstractParseForest, StackNode extends AbstractStackNode<ParseForest>>
        IActiveStacks<StackNode> get(ParserObserving<ParseForest, StackNode> observing) {
        MeasureActiveStacks<ParseForest, StackNode> measureActiveStacks =
            new MeasureActiveStacks<ParseForest, StackNode>(observing);

        this.measureActiveStacks = measureActiveStacks;

        return measureActiveStacks;
    }

}
