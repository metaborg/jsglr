package org.spoofax.jsglr2.imploder;

public interface ITreeFactory<T> {

    T createStringTerminal(String sort, String value);

    T createNonTerminal(String sort, String constructor, Iterable<T> childASTs);

    T createList(String sort, Iterable<T> children);

    T createOptional(String sort, Iterable<T> children);

    T createTuple(String sort, Iterable<T> children);

    T createAmb(String sort, Iterable<T> alternatives);

}
