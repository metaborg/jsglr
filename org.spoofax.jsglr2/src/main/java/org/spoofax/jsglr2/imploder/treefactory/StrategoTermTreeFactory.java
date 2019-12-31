package org.spoofax.jsglr2.imploder.treefactory;

import java.util.Collections;

import org.metaborg.parsetable.symbols.IMetaVarSymbol;
import org.metaborg.parsetable.symbols.ISymbol;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.terms.TermFactory;

import com.google.common.collect.Iterables;

public class StrategoTermTreeFactory implements ITreeFactory<IStrategoTerm> {

    private final ITermFactory termFactory;

    public StrategoTermTreeFactory() {
        this.termFactory = new TermFactory();
    }

    @Override public IStrategoTerm createStringTerminal(ISymbol symbol, String value) {
        return termFactory.makeString(value);
    }

    @Override public IStrategoTerm createMetaVar(IMetaVarSymbol symbol, String value) {
        return termFactory.makeAppl(symbol.metaVarCardinality().constructor, termFactory.makeString(value));
    }

    @Override public IStrategoTerm createNonTerminal(ISymbol symbol, String constructor,
        Iterable<IStrategoTerm> childASTs) {
        IStrategoTerm[] terms = toArray(childASTs);
        return termFactory.makeAppl(
            termFactory.makeConstructor(constructor != null ? constructor : ISymbol.getSort(symbol), terms.length),
            terms);
    }

    @Override public IStrategoTerm createList(Iterable<IStrategoTerm> children) {
        return termFactory.makeList(toArray(children));
    }

    @Override public IStrategoTerm createOptional(ISymbol symbol, Iterable<IStrategoTerm> children) {
        return createNonTerminal(symbol, children == null || Iterables.isEmpty(children) ? "None" : "Some", children);
    }

    @Override public IStrategoTerm createTuple(Iterable<IStrategoTerm> children) {
        return termFactory.makeTuple(toArray(children));
    }

    @Override public IStrategoTerm createAmb(Iterable<IStrategoTerm> alternatives) {
        return createNonTerminal(null, "amb", Collections.singletonList(createList(alternatives)));
    }

    private static IStrategoTerm[] toArray(Iterable<IStrategoTerm> children) {
        return Iterables.toArray(children, IStrategoTerm.class);
    }

}
