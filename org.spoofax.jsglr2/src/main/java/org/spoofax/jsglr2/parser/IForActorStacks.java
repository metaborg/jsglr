package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.stack.AbstractStackNode;

public interface IForActorStacks<StackNode extends AbstractStackNode<?>> {

    void add(StackNode stack);

    void clear();

    boolean contains(StackNode stack);

    boolean hasNext();

    StackNode getNext(); // Remove and get the next stack

}
