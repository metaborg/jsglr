package org.spoofax.jsglr2.stack.collections;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.metaborg.parsetable.IState;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.AbstractStackNode;

public class ActiveStacksLinkedHashMap<ParseForest extends AbstractParseForest, StackNode extends AbstractStackNode<ParseForest>>
    implements IActiveStacks<StackNode> {

    private ParserObserving<ParseForest, StackNode> observing;
    protected Map<Integer, Linked<StackNode>> activeStacks;
    private Linked<StackNode> last;

    public ActiveStacksLinkedHashMap(ParserObserving<ParseForest, StackNode> observing) {
        this.observing = observing;
        this.activeStacks = new HashMap<Integer, Linked<StackNode>>();
        this.last = null;
    }

    private static class Linked<T> {
        T stack;
        Linked<T> prev;

        Linked(T stack, Linked<T> prev) {
            this.stack = stack;
            this.prev = prev;
        }
    }

    @Override
    public void add(StackNode stack) {
        observing.notify(observer -> observer.addActiveStack(stack));

        Linked<StackNode> linkedStackNode = new Linked<>(stack, last);

        activeStacks.put(stack.state.id(), linkedStackNode);

        last = linkedStackNode;
    }

    @Override
    public boolean isSingle() {
        return last != null && last.prev == null;
    }

    @Override
    public StackNode getSingle() {
        return last.stack;
    }

    @Override
    public boolean isEmpty() {
        return last == null;
    }

    @Override
    public StackNode findWithState(IState state) {
        observing.notify(observer -> observer.findActiveStackWithState(state));

        Linked<StackNode> linkedStackNode = activeStacks.get(state.id());

        return linkedStackNode != null ? linkedStackNode.stack : null;
    }

    @Override
    public Iterable<StackNode> forLimitedReductions(IForActorStacks<StackNode> forActorStacks) {
        return () -> new Iterator<StackNode>() {

            ActiveStacksLinkedHashMap.Linked<StackNode> current = last;

            @Override
            public boolean hasNext() {
                while(current != null
                    && !(!current.stack.allLinksRejected() && !forActorStacks.contains(current.stack)))
                    current = current.prev;

                return current != null;
            }

            @Override
            public StackNode next() {
                StackNode currentStack = current.stack;

                current = current.prev;

                return currentStack;
            }

        };
    }

    @Override
    public void addAllTo(IForActorStacks<StackNode> other) {
        for(Linked<StackNode> linkedState = last; linkedState != null; linkedState = linkedState.prev)
            other.add(linkedState.stack);
    }

    @Override
    public void clear() {
        activeStacks.clear();
        last = null;
    }

    @Override
    public Iterator<StackNode> iterator() {
        return new Iterator<StackNode>() {

            ActiveStacksLinkedHashMap.Linked<StackNode> current = last;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public StackNode next() {
                StackNode currentStack = current.stack;

                current = current.prev;

                return currentStack;
            }

        };
    }

}
