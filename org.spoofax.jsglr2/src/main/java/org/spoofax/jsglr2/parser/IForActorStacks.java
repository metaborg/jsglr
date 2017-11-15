package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.stack.AbstractStackNode;

/*
 * Collection of stacks that the parser operates on during a parse round. It is cleared on the start of each round and
 * filled with the collection of active stacks at the beginning of the new round (see Parser::parseCharacter). Stacks
 * are removed one by one and during a parse round new stacks can be added after reducing. Removing should return stacks
 * with a state that is rejectable last. In the original SGLR algorithm such stacks are in for-actor-delayed. The
 * rejectable stacks should be returned in a certain order (see P9707 Section 8.4).
 */
public interface IForActorStacks<StackNode extends AbstractStackNode<?>> {

    void add(StackNode stack);

    void clear();

    boolean contains(StackNode stack);

    boolean hasNext();

    StackNode getNext(); // Remove and get the next stack

}
