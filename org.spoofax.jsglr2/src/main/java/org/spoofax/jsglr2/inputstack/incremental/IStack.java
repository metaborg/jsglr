package org.spoofax.jsglr2.inputstack.incremental;

interface IStack<E> {
    E push(E e);

    E pop();

    E peek();

    boolean isEmpty();

    IStack<E> clone();
}
