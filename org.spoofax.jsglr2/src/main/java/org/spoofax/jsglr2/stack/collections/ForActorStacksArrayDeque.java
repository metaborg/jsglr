package org.spoofax.jsglr2.stack.collections;

import java.util.ArrayDeque;
import java.util.Queue;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.AbstractStackNode;

public class ForActorStacksArrayDeque<ParseForest extends AbstractParseForest, StackNode extends AbstractStackNode<ParseForest>>
    extends ForActorStacks<ParseForest, StackNode> {

    protected final Queue<StackNode> forActor;

    public ForActorStacksArrayDeque(ParserObserving<ParseForest, StackNode> observing) {
        super(observing);

        this.forActor = new ArrayDeque<StackNode>();
    }

    @Override
    protected void forActorAdd(StackNode stack) {
        forActor.add(stack);
    }

    @Override
    protected boolean forActorContains(StackNode stack) {
        return forActor.contains(stack);
    }

    @Override
    protected boolean forActorNonEmpty() {
        return !forActor.isEmpty();
    }

    @Override
    protected StackNode forActorRemove() {
        return forActor.remove();
    }

}
