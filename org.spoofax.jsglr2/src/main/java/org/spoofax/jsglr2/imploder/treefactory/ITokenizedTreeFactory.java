package org.spoofax.jsglr2.imploder.treefactory;

import java.util.List;

import org.metaborg.parsetable.symbols.IMetaVarSymbol;
import org.metaborg.parsetable.symbols.ISymbol;
import org.spoofax.jsglr.client.imploder.IToken;

public interface ITokenizedTreeFactory<T> {

    T createCharacterTerminal(int character, IToken token);

    T createStringTerminal(ISymbol symbol, String value, IToken token);

    T createMetaVar(IMetaVarSymbol symbol, String value, IToken token);

    T createNonTerminal(ISymbol symbol, String constructor, List<T> childASTs, IToken leftToken, IToken rightToken);

    T createList(List<T> children, IToken leftToken, IToken rightToken);

    T createOptional(ISymbol symbol, List<T> children, IToken leftToken, IToken rightToken);

    T createTuple(List<T> children, IToken leftToken, IToken rightToken);

    T createAmb(List<T> alternatives, IToken leftToken, IToken rightToken);

    T createInjection(ISymbol symbol, T injected, boolean isBracket);

}
