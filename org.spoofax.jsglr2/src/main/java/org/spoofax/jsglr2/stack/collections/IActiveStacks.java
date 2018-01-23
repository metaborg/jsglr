package org.spoofax.jsglr2.stack.collections;

import org.metaborg.parsetable.IState;
import org.spoofax.jsglr2.stack.AbstractStackNode;

public interface IActiveStacks<StackNode extends AbstractStackNode<?>> extends Iterable<StackNode> {

    public void add(StackNode stack);

    public boolean isSingle();

    public StackNode getSingle();

    public boolean isEmpty();

    public StackNode findWithState(IState state);

    public Iterable<StackNode> forLimitedReductions(IForActorStacks<StackNode> forActorStacks);

    public void addAllTo(IForActorStacks<StackNode> forActorStacks);

    public void clear();

}
