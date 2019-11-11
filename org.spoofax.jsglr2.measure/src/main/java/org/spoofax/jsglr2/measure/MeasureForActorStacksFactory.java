package org.spoofax.jsglr2.measure;

import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;
import org.spoofax.jsglr2.stack.collections.IForActorStacksFactory;

public class MeasureForActorStacksFactory implements IForActorStacksFactory {

    MeasureForActorStacks<?, ?, ?, ?, ?> measureForActorStacks = null;

    @Override public
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<?, StackNode>>
//@formatter:on
    IForActorStacks<StackNode>
        get(ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing) {
        if(measureForActorStacks == null) {
            MeasureForActorStacks<ParseForest, Derivation, ParseNode, StackNode, ParseState> measureForActorStacks =
                new MeasureForActorStacks<>(observing);

            this.measureForActorStacks = measureForActorStacks;

            return measureForActorStacks;
        } else
            return (MeasureForActorStacks<ParseForest, Derivation, ParseNode, StackNode, ParseState>) measureForActorStacks;
    }

}
