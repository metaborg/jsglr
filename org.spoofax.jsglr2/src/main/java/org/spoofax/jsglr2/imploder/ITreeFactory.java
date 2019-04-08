package org.spoofax.jsglr2.imploder;

import java.util.List;

public interface ITreeFactory<T> {

    T createStringTerminal(String sort, String value);

    T createNonTerminal(String sort, String constructor, List<T> childASTs);

    T createList(String sort, List<T> children);

    T createOptional(String sort, List<T> children);

    T createTuple(String sort, List<T> children);

    T createAmb(String sort, List<T> alternatives);

}
