package org.spoofax.jsglr2.stack.collections;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;

import org.metaborg.util.iterators.Iterables2;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;

public abstract class ForActorStacks
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<?, StackNode>>
//@formatter:on
    implements IForActorStacks<StackNode> {

    private final ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing;
    protected final Queue<StackNode> forActorDelayed;

    protected ForActorStacks(ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing) {
        this.observing = observing;

        // TODO: implement priority (see P9707 Section 8.4)
        Comparator<StackNode> stackNodePriorityComparator = (StackNode stackNode1, StackNode stackNode2) -> 0;

        this.forActorDelayed = new PriorityQueue<>(stackNodePriorityComparator);
    }

    protected abstract void forActorAdd(StackNode stack);

    protected abstract boolean forActorContains(StackNode stack);

    protected abstract boolean forActorNonEmpty();

    protected abstract StackNode forActorRemove();

    protected abstract Iterable<StackNode> forActorIterable();

    @Override public void add(StackNode stack) {
        observing.notify(observer -> observer.addForActorStack(stack));

        if(stack.state().isRejectable())
            forActorDelayed.add(stack);
        else
            forActorAdd(stack);
    }

    @Override public boolean contains(StackNode stack) {
        return forActorContains(stack) || forActorDelayed.contains(stack);
    }

    @Override public boolean nonEmpty() {
        return forActorNonEmpty() || !forActorDelayed.isEmpty();
    }

    @Override public StackNode remove() {
        // First return all actors in forActor
        if(forActorNonEmpty())
            return forActorRemove();

        // Then return actors from forActorDelayed
        return forActorDelayed.remove();
    }

    @Override public Iterator<StackNode> iterator() {
        return Iterables2.fromConcat(forActorIterable(), forActorDelayed).iterator();
    }

}
