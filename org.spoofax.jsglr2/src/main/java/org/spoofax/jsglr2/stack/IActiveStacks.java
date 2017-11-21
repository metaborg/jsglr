package org.spoofax.jsglr2.stack;

import org.spoofax.jsglr2.parser.IForActorStacks;
import org.spoofax.jsglr2.parsetable.IState;

public interface IActiveStacks<StackNode extends AbstractStackNode<?>> extends Iterable<StackNode> {

    public void add(StackNode stack);

    public boolean isSingle();

    public boolean isEmpty();

    public StackNode findWithState(IState state);

    public Iterable<StackNode> forLimitedReductions(IForActorStacks<StackNode> forActorStacks);

    public void addAllTo(IForActorStacks<StackNode> forActorStacks);

    public void clear();

}
