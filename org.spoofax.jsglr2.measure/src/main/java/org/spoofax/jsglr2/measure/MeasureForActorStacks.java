package org.spoofax.jsglr2.measure;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.IParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.collections.ForActorStacksArrayDeque;

public class MeasureForActorStacks<ParseForest extends IParseForest, StackNode extends IStackNode, ParseState extends IParseState<ParseForest, StackNode>>
    extends ForActorStacksArrayDeque<ParseForest, StackNode, ParseState> {

    long forActorAdds = 0, forActorDelayedAdds = 0, forActorMaxSize = 0, forActorDelayedMaxSize = 0, containsChecks = 0,
        nonEmptyChecks = 0;

    public MeasureForActorStacks(ParserObserving<ParseForest, StackNode, ParseState> observing) {
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
