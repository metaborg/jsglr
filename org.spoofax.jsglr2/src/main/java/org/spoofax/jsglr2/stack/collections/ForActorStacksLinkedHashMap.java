package org.spoofax.jsglr2.stack.collections;

import java.util.HashMap;
import java.util.Map;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.AbstractStackNode;

public class ForActorStacksLinkedHashMap<ParseForest extends AbstractParseForest, StackNode extends AbstractStackNode<ParseForest>>
    extends ForActorStacks<ParseForest, StackNode> {

    protected Map<Integer, Linked<StackNode>> forActor;
    private Linked<StackNode> last;

    public ForActorStacksLinkedHashMap(ParserObserving<ParseForest, StackNode> observing) {
        super(observing);

        this.forActor = new HashMap<Integer, Linked<StackNode>>();
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
    protected void forActorAdd(StackNode stack) {
        Linked<StackNode> linkedStackNode = new Linked<>(stack, last);

        forActor.put(stack.state.id(), linkedStackNode);

        last = linkedStackNode;
    }

    @Override
    protected boolean forActorContains(StackNode stack) {
        return forActor.containsKey(stack.state.id());
    }

    @Override
    protected boolean forActorNonEmpty() {
        return last != null;
    }

    @Override
    protected StackNode forActorRemove() {
        StackNode stack = last.stack;

        last = last.prev;

        forActor.remove(stack.state.id());

        return stack;
    }

}
