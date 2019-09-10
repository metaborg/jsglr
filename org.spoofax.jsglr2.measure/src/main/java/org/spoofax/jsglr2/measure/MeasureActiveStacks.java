package org.spoofax.jsglr2.measure;

import java.util.Iterator;

import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.collections.ActiveStacksArrayList;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;

public class MeasureActiveStacks
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<ParseForest, StackNode>>
//@formatter:on
    extends ActiveStacksArrayList<ParseForest, StackNode, ParseState> {

    long adds = 0, maxSize = 0, iSingleChecks = 0, isEmptyChecks = 0, findsWithState = 0, forLimitedReductions = 0,
        addAllTo = 0, clears = 0, iterators = 0;

    public MeasureActiveStacks(ParserObserving<ParseForest, StackNode, ParseState> observing) {
        super(observing);
    }

    @Override public void add(StackNode stack) {
        adds++;

        super.add(stack);

        maxSize = Math.max(maxSize, activeStacks.size());
    }

    @Override public boolean isSingle() {
        iSingleChecks++;

        return super.isSingle();
    }

    @Override public boolean isEmpty() {
        isEmptyChecks++;

        return super.isEmpty();
    }

    @Override public StackNode findWithState(IState state) {
        findsWithState++;

        return super.findWithState(state);
    }

    @Override public Iterable<StackNode> forLimitedReductions(IForActorStacks<StackNode> forActorStacks) {
        forLimitedReductions++;

        return super.forLimitedReductions(forActorStacks);
    }

    @Override public void addAllTo(IForActorStacks<StackNode> other) {
        addAllTo++;

        super.addAllTo(other);
    }

    @Override public void clear() {
        clears++;

        super.clear();
    }

    @Override public Iterator<StackNode> iterator() {
        iterators++;

        return super.iterator();
    }

}
