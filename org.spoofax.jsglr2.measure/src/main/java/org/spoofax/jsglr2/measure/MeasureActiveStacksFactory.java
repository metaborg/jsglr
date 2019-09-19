package org.spoofax.jsglr2.measure;

import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.collections.IActiveStacks;
import org.spoofax.jsglr2.stack.collections.IActiveStacksFactory;

public class MeasureActiveStacksFactory implements IActiveStacksFactory {

    MeasureActiveStacks<?, ?, ?, ?, ?> measureActiveStacks = null;

    @Override public
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<ParseForest, StackNode>>
//@formatter:on
    IActiveStacks<StackNode> get(ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing) {
        if(this.measureActiveStacks == null) {
            MeasureActiveStacks<ParseForest, Derivation, ParseNode, StackNode, ParseState> measureActiveStacks =
                new MeasureActiveStacks<>(observing);

            this.measureActiveStacks = measureActiveStacks;

            return measureActiveStacks;
        } else
            return (MeasureActiveStacks<ParseForest, Derivation, ParseNode, StackNode, ParseState>) this.measureActiveStacks;
    }

}
