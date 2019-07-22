package org.spoofax.jsglr2.imploder.treefactory;

import org.metaborg.parsetable.symbols.IMetaVarSymbol;
import org.metaborg.parsetable.symbols.ISymbol;

public interface ITreeFactory<T> {

    T createStringTerminal(ISymbol symbol, String value);

    T createMetaVar(IMetaVarSymbol symbol, String value);

    T createNonTerminal(ISymbol symbol, String constructor, Iterable<T> childASTs);

    T createList(Iterable<T> children);

    T createOptional(ISymbol symbol, Iterable<T> children);

    T createTuple(Iterable<T> children);

    T createAmb(Iterable<T> alternatives);

}
