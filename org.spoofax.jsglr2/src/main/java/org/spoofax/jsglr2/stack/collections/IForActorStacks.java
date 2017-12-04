package org.spoofax.jsglr2.stack.collections;

import org.spoofax.jsglr2.stack.AbstractStackNode;

/*
 * Collection of stacks that the parser operates on during a parse round. At the start of each round it is filled with
 * the collection of active stacks at the beginning of that round (see Parser::parseCharacter). Stacks are removed one
 * by one and during a parse round new stacks can be added after reducing. Removing should return stacks in a certain
 * order (see also the iterator() method).
 */
public interface IForActorStacks<StackNode extends AbstractStackNode<?>> {

    void add(StackNode stack);

    boolean contains(StackNode stack);

    public boolean nonEmpty();

    /*
     * Remove and return the next stack in the correct order. First non-rejectable stacks and last rejectable stacks.
     * Non-rejectable stacks added during a parse round still need to be returned before rejectable stacks. When only
     * rejectable stacks are left, they should be removed and returned with a certain order (see P9707 Section 8.4).
     */
    public StackNode remove();

}
