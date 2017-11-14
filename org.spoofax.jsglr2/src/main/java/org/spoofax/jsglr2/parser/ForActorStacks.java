package org.spoofax.jsglr2.parser;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

import org.spoofax.jsglr2.stack.AbstractStackNode;

public final class ForActorStacks<StackNode extends AbstractStackNode<?>> implements IForActorStacks<StackNode> {

    final Queue<StackNode> forActor, forActorDelayed;

    public ForActorStacks() {
        Comparator<StackNode> stackNodePriorityComparator = new Comparator<StackNode>() {
            @Override public int compare(StackNode stackNode1, StackNode stackNode2) {
                return 0; // TODO: implement priority (see P9707 Section 8.4)
            }
        };

        this.forActor = new ArrayDeque<StackNode>();
        this.forActorDelayed = new PriorityQueue<StackNode>(stackNodePriorityComparator);
    }

    @Override public void add(StackNode stack) {
        if(stack.state.isRejectable())
            forActorDelayed.add(stack);
        else
            forActor.add(stack);
    }

    @Override public void clear() {
        forActor.clear();
        forActorDelayed.clear();
    }

    @Override public boolean contains(StackNode stack) {
        return forActor.contains(stack) || forActorDelayed.contains(stack);
    }

    @Override public boolean hasNext() {
        return !forActor.isEmpty() || !forActorDelayed.isEmpty();
    }

    @Override public StackNode getNext() {
        // First return all actors in forActor
        if(!forActor.isEmpty())
            return forActor.remove();

        // Then return actors from forActorDelayed
        return forActorDelayed.remove();
    }

}
