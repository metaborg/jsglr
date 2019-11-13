package org.spoofax.jsglr2.measure.parsing;

import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.collections.ForActorStacksArrayDeque;

public class MeasureForActorStacks
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<?, StackNode>>
//@formatter:on
    extends ForActorStacksArrayDeque<ParseForest, Derivation, ParseNode, StackNode, ParseState> {

    long forActorAdds = 0, forActorDelayedAdds = 0, forActorMaxSize = 0, forActorDelayedMaxSize = 0, containsChecks = 0,
        nonEmptyChecks = 0;

    MeasureForActorStacks(ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing) {
        super(observing);
    }

    @Override public void add(StackNode stack) {
        if(stack.state().isRejectable())
            forActorDelayedAdds++;
        else
            forActorAdds++;

        super.add(stack);

        forActorMaxSize = Math.max(forActorMaxSize, forActor.size());
        forActorDelayedMaxSize = Math.max(forActorDelayedMaxSize, forActorDelayed.size());
    }

    @Override public boolean contains(StackNode stack) {
        containsChecks++;

        return super.contains(stack);
    }

    @Override public boolean nonEmpty() {
        nonEmptyChecks++;

        return super.nonEmpty();
    }

    @Override public StackNode remove() {
        return super.remove();
    }

}
