package org.spoofax.jsglr2.imploder;

import java.util.List;

import org.spoofax.jsglr.client.imploder.IToken;

public interface ITreeFactory<T> {

    public T createStringTerminal(String sort, String value, IToken leftToken, IToken rightToken);

    public T createNonTerminal(String sort, String constructor, List<T> childASTs, IToken leftToken, IToken rightToken);

    public T createList(String sort, List<T> children, IToken leftToken, IToken rightToken);

    public T createOptional(String sort, List<T> children, IToken leftToken, IToken rightToken);

    public T createTuple(String sort, List<T> children, IToken leftToken, IToken rightToken);

    public T createAmb(String sort, List<T> alternatives, IToken leftToken, IToken rightToken);

}
