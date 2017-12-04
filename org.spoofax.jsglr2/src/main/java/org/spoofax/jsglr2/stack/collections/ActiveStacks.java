package org.spoofax.jsglr2.stack.collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.states.IState;

public class ActiveStacks<StackNode extends AbstractStackNode<ParseForest>, ParseForest extends AbstractParseForest>
    implements IActiveStacks<StackNode> {

    private Parse<ParseForest, StackNode> parse;
    private List<StackNode> activeStacks;

    public ActiveStacks(Parse<ParseForest, StackNode> parse) {
        this.parse = parse;
        this.activeStacks = new ArrayList<StackNode>();
    }

    @Override
    public void add(StackNode stack) {
        parse.notify(observer -> observer.addActiveStack(stack));

        activeStacks.add(stack);
    }

    @Override
    public boolean isSingle() {
        return activeStacks.size() == 1;
    }

    @Override
    public boolean isEmpty() {
        return activeStacks.isEmpty();
    }

    @Override
    public StackNode findWithState(IState state) {
        parse.notify(observer -> observer.findActiveStackWithState(state));

        for(StackNode stack : activeStacks)
            if(stack.state.id() == state.id())
                return stack;

        return null;
    }

    @Override
    public Iterable<StackNode> forLimitedReductions(IForActorStacks<StackNode> forActorStacks) {
        return () -> new Iterator<StackNode>() {

            int index = 0;

            // Save the number of active stacks to prevent the for loop from processing active stacks that are added
            // by doLimitedReductions. We can safely limit the loop by the current number of stacks since new stack are
            // added at the end.
            final int currentSize = activeStacks.size();

            @Override
            public boolean hasNext() {
                // skip non-applicable actions
                while(index < currentSize && !(!activeStacks.get(index).allOutLinksRejected()
                    && !forActorStacks.contains(activeStacks.get(index)))) {
                    index++;
                }
                return index < currentSize;
            }

            @Override
            public StackNode next() {
                if(!hasNext()) {
                    throw new NoSuchElementException();
                }
                return activeStacks.get(index++);
            }

        };
    }

    @Override
    public void addAllTo(IForActorStacks<StackNode> other) {
        for(StackNode stack : activeStacks)
            other.add(stack);
    }

    @Override
    public void clear() {
        activeStacks.clear();
    }

    @Override
    public Iterator<StackNode> iterator() {
        return activeStacks.iterator();
    }

}
