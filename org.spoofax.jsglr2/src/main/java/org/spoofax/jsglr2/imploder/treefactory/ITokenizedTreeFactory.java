package org.spoofax.jsglr2.imploder.treefactory;

import java.util.List;

import org.metaborg.parsetable.symbols.ISymbol;
import org.spoofax.jsglr.client.imploder.IToken;

public interface ITokenizedTreeFactory<T> {

    T createStringTerminal(ISymbol symbol, String value, IToken token);

    T createNonTerminal(ISymbol symbol, String constructor, List<T> childASTs, IToken leftToken, IToken rightToken);

    T createList(List<T> children, IToken leftToken, IToken rightToken);

    T createOptional(ISymbol symbol, List<T> children, IToken leftToken, IToken rightToken);

    T createTuple(List<T> children, IToken leftToken, IToken rightToken);

    T createAmb(List<T> alternatives, IToken leftToken, IToken rightToken);

    T createInjection(ISymbol symbol, T injected);

}
