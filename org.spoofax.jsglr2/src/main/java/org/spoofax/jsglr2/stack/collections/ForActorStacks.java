package org.spoofax.jsglr2.stack.collections;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.AbstractStackNode;

public abstract class ForActorStacks<ParseForest extends AbstractParseForest, StackNode extends AbstractStackNode<ParseForest>>
    implements IForActorStacks<StackNode> {

    private final ParserObserving<ParseForest, StackNode> observing;
    protected final Queue<StackNode> forActorDelayed;

    protected ForActorStacks(ParserObserving<ParseForest, StackNode> observing) {
        this.observing = observing;

        Comparator<StackNode> stackNodePriorityComparator = new Comparator<StackNode>() {
            @Override
            public int compare(StackNode stackNode1, StackNode stackNode2) {
                return 0; // TODO: implement priority (see P9707 Section 8.4)
            }
        };

        this.forActorDelayed = new PriorityQueue<StackNode>(stackNodePriorityComparator);
    }

    protected abstract void forActorAdd(StackNode stack);

    protected abstract boolean forActorContains(StackNode stack);

    protected abstract boolean forActorNonEmpty();

    protected abstract StackNode forActorRemove();

    @Override
    public void add(StackNode stack) {
        observing.notify(observer -> observer.addForActorStack(stack));

        if(stack.state.isRejectable())
            forActorDelayed.add(stack);
        else
            forActorAdd(stack);
    }

    @Override
    public boolean contains(StackNode stack) {
        return forActorContains(stack) || forActorDelayed.contains(stack);
    }

    @Override
    public boolean nonEmpty() {
        return forActorNonEmpty() || !forActorDelayed.isEmpty();
    }

    @Override
    public StackNode remove() {
        // First return all actors in forActor
        if(forActorNonEmpty())
            return forActorRemove();

        // Then return actors from forActorDelayed
        return forActorDelayed.remove();
    }

}
