package org.spoofax.jsglr2.stack.collections;

import java.util.*;

import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;

public class ActiveStacksLinkedHashMap
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<?, StackNode>>
//@formatter:on
    implements IActiveStacks<StackNode> {

    private ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing;

    protected Map<Integer, Linked<StackNode>> activeStacks;
    private Linked<StackNode> last;

    protected List<StackNode> activeStacks2; // ArrayList

    public ActiveStacksLinkedHashMap(
        ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing) {
        this.observing = observing;
        this.activeStacks = new HashMap<>();
        this.last = null;

        this.activeStacks2 = new ArrayList<>(); // ArrayList
    }

    private static class Linked<T> {
        T stack;
        Linked<T> prev;

        Linked(T stack, Linked<T> prev) {
            this.stack = stack;
            this.prev = prev;
        }
    }

    @Override public void add(StackNode stack) {
        observing.notify(observer -> observer.addActiveStack(stack));

        Linked<StackNode> linkedStackNode = new Linked<>(stack, last);

        activeStacks.put(stack.state().id(), linkedStackNode);

        last = linkedStackNode;

        activeStacks2.add(stack); // ArrayList
    }

    @Override public boolean isSingle() {
        return last != null && last.prev == null;

        // return activeStacks2.size() == 1; // ArrayList
    }

    @Override public StackNode getSingle() {
        return last.stack;

        // return activeStacks2.get(0); // ArrayList
    }

    @Override public boolean isEmpty() {
        return last == null;

        // return activeStacks2.isEmpty(); // ArrayList
    }

    @Override public boolean isMultiple() {
        return activeStacks.size() > 1;

        // return activeStacks2.size() > 1; // ArrayList
    }

    @Override public StackNode findWithState(IState state) {
        observing.notify(observer -> observer.findActiveStackWithState(state));

//        Linked<StackNode> linkedStackNode = activeStacks.get(state.id());
//
//        return linkedStackNode != null ? linkedStackNode.stack : null;

        for(StackNode stack : activeStacks2) // ArrayList
            if(stack.state().id() == state.id())
                return stack;
        return null;
    }

    @Override public Iterable<StackNode> forLimitedReductions(IForActorStacks<StackNode> forActorStacks) {
        Iterable<StackNode> linkedHashMapIterable = () -> new Iterator<StackNode>() {
            ActiveStacksLinkedHashMap.Linked<StackNode> current = last;

            @Override public boolean hasNext() {
                while(current != null
                    && !(!current.stack.allLinksRejected() && !forActorStacks.contains(current.stack)))
                    current = current.prev;

                return current != null;
            }

            @Override public StackNode next() {
                StackNode currentStack = current.stack;

                current = current.prev;

                return currentStack;
            }

        };

        Iterable<StackNode> arrayListIterable = () -> new Iterator<StackNode>() {

            int index = 0;

            // Save the number of active stacks to prevent the for loop from processing active stacks that are added
            // by doLimitedReductions. We can safely limit the loop by the current number of stacks since new stack are
            // added at the end.
            final int currentSize = activeStacks2.size();

            @Override public boolean hasNext() {
                // skip non-applicable actions
                while(index < currentSize && !(!activeStacks2.get(index).allLinksRejected()
                        && !forActorStacks.contains(activeStacks2.get(index)))) {
                    index++;
                }
                return index < currentSize;
            }

            @Override public StackNode next() {
                if(!hasNext()) {
                    throw new NoSuchElementException();
                }
                return activeStacks2.get(index++);
            }

        };

        System.out.println("Iterable arrayList    : " + getIterableAsList(arrayListIterable));
        System.out.println("Iterable linkedHashMap: " + getIterableAsList(linkedHashMapIterable));

        return linkedHashMapIterable;
    }

    private List<StackNode> getIterableAsList(Iterable<StackNode> arrayListIterable) {
        List<StackNode> list = new ArrayList<>();

        arrayListIterable.forEach(list::add);

        return list;
    }

    @Override public void addAllTo(IForActorStacks<StackNode> other) {
        for(Linked<StackNode> linkedState = last; linkedState != null; linkedState = linkedState.prev)
            other.add(linkedState.stack);

//        for(StackNode stack : activeStacks2)  // ArrayList
//            other.add(stack);
    }

    @Override public void clear() {
        activeStacks.clear();
        last = null;

        activeStacks2.clear(); // ArrayList
    }

    @Override public Iterator<StackNode> iterator() {
        return new Iterator<StackNode>() {

            ActiveStacksLinkedHashMap.Linked<StackNode> current = last;

            @Override public boolean hasNext() {
                return current != null;
            }

            @Override public StackNode next() {
                StackNode currentStack = current.stack;

                current = current.prev;

                return currentStack;
            }

        };

//        return activeStacks2.iterator();  // ArrayList
    }

}
