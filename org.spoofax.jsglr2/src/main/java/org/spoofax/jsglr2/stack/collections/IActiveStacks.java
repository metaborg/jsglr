package org.spoofax.jsglr2.stack.collections;

import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.stack.IStackNode;

public interface IActiveStacks<StackNode extends IStackNode> extends Iterable<StackNode> {

    void add(StackNode stack);

    boolean isSingle();

    StackNode getSingle();

    boolean isEmpty();

    boolean isMultiple();

    StackNode findWithState(IState state);

    Iterable<StackNode> forLimitedReductions(IForActorStacks<StackNode> forActorStacks);

    void addAllTo(IForActorStacks<StackNode> forActorStacks);

    void clear();

}
