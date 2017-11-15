package org.spoofax.jsglr2.parser;

import java.util.Iterator;

import org.spoofax.jsglr2.stack.AbstractStackNode;

/*
 * Collection of stacks that the parser operates on during a parse round. It is cleared on the start of each round and
 * filled with the collection of active stacks at the beginning of the new round (see Parser::parseCharacter). Stacks
 * are removed one by one and during a parse round new stacks can be added after reducing. Removing should return stacks
 * with a state that is rejectable last. In the original SGLR algorithm such stacks are in for-actor-delayed. The
 * rejectable stacks should be returned in a certain order (see P9707 Section 8.4).
 */
public interface IForActorStacks<StackNode extends AbstractStackNode<?>> extends Iterable<StackNode> {

    void add(StackNode stack);

    void clear();

    boolean contains(StackNode stack);

    /*
     * This iterator should return the stacks in the correct order. First non-rejectable stacks and last rejectable
     * stacks. Non-rejectable stacks added during a parse round still need to be returned before rejectable stacks. When
     * only rejectable stacks are left, they should be returned with a certain order (see P9707 Section 8.4).
     */
    @Override public Iterator<StackNode> iterator();

}
