package org.spoofax.jsglr2.inputstack.incremental;

import java.util.EmptyStackException;

class LinkedStack<E> implements IStack<E> {
    StackTuple<E> head;

    @Override public E push(E e) {
        head = new StackTuple<E>(e, head);
        return e;
    }

    @Override public E pop() {
        E res = head.node;
        head = head.next;
        return res;
    }

    @Override public E peek() {
        if(head == null)
            throw new EmptyStackException();
        return head.node;
    }

    @Override public boolean isEmpty() {
        return head == null;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod") @Override public LinkedStack<E> clone() {
        LinkedStack<E> clone = new LinkedStack<>();
        clone.head = this.head;
        return clone;
    }

    static class StackTuple<E> {
        final E node;
        final StackTuple<E> next;

        StackTuple(E node, StackTuple<E> next) {
            this.node = node;
            this.next = next;
        }
    }
}
