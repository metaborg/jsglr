package org.spoofax.jsglr2.stack.collections;

import java.util.ArrayDeque;
import java.util.Queue;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;

public class ForActorStacksArrayDeque<ParseForest extends IParseForest, StackNode extends IStackNode>
    extends ForActorStacks<ParseForest, StackNode> {

    protected final Queue<StackNode> forActor;

    public ForActorStacksArrayDeque(ParserObserving<ParseForest, StackNode> observing) {
        super(observing);

        this.forActor = new ArrayDeque<>();
    }

    @Override protected void forActorAdd(StackNode stack) {
        forActor.add(stack);
    }

    @Override protected boolean forActorContains(StackNode stack) {
        return forActor.contains(stack);
    }

    @Override protected boolean forActorNonEmpty() {
        return !forActor.isEmpty();
    }

    @Override protected StackNode forActorRemove() {
        return forActor.remove();
    }

}
