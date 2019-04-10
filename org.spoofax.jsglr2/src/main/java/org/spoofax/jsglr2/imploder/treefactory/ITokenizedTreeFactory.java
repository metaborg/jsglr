package org.spoofax.jsglr2.imploder.treefactory;

import java.util.List;

import org.spoofax.jsglr.client.imploder.IToken;

public interface ITokenizedTreeFactory<T> {

    T createStringTerminal(String sort, String value, IToken token);

    T createNonTerminal(String sort, String constructor, List<T> childASTs, IToken leftToken, IToken rightToken);

    T createList(String sort, List<T> children, IToken leftToken, IToken rightToken);

    T createOptional(String sort, List<T> children, IToken leftToken, IToken rightToken);

    T createTuple(String sort, List<T> children, IToken leftToken, IToken rightToken);

    T createAmb(String sort, List<T> alternatives, IToken leftToken, IToken rightToken);

}
