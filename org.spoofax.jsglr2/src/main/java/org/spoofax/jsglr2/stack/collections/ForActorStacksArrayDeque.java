package org.spoofax.jsglr2.stack.collections;

import java.util.ArrayDeque;
import java.util.Queue;

import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;

public class ForActorStacksArrayDeque
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<?, StackNode>>
//@formatter:on
    extends ForActorStacks<ParseForest, Derivation, ParseNode, StackNode, ParseState> {

    protected final Queue<StackNode> forActor;

    public ForActorStacksArrayDeque(
        ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing) {
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

    @Override protected Iterable<StackNode> forActorIterable() {
        return forActor;
    }

}
