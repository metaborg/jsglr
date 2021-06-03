package org.spoofax.jsglr2.inputstack.incremental;

import java.util.Stack;

class CloneableStack<E> extends Stack<E> implements IStack<E> {

    @Override public CloneableStack<E> clone() {
        CloneableStack<E> clone = new CloneableStack<>();
        for(E e : this) {
            clone.push(e);
        }
        return clone;
    }

}
