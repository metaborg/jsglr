package org.spoofax.jsglr2.imploder.treefactory;

import static org.spoofax.interpreter.terms.IStrategoTerm.MUTABLE;

import java.util.Collections;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.terms.TermFactory;

import com.google.common.collect.Iterables;

public class StrategoTermTreeFactory implements ITreeFactory<IStrategoTerm> {

    private final ITermFactory termFactory;

    public StrategoTermTreeFactory() {
        this.termFactory = new TermFactory().getFactoryWithStorageType(MUTABLE);
    }

    @Override public IStrategoTerm createStringTerminal(String sort, String value) {
        return termFactory.makeString(value);
    }

    @Override public IStrategoTerm createNonTerminal(String sort, String constructor,
        Iterable<IStrategoTerm> childASTs) {
        IStrategoTerm[] terms = toArray(childASTs);
        return termFactory.makeAppl(termFactory.makeConstructor(constructor != null ? constructor : sort, terms.length),
            terms);
    }

    @Override public IStrategoTerm createList(String sort, Iterable<IStrategoTerm> children) {
        return termFactory.makeList(toArray(children));
    }

    @Override public IStrategoTerm createOptional(String sort, Iterable<IStrategoTerm> children) {
        return createNonTerminal(sort, children == null || Iterables.isEmpty(children) ? "None" : "Some", children);
    }

    @Override public IStrategoTerm createTuple(String sort, Iterable<IStrategoTerm> children) {
        return termFactory.makeTuple(toArray(children));
    }

    @Override public IStrategoTerm createAmb(String sort, Iterable<IStrategoTerm> alternatives) {
        return createNonTerminal(null, "amb", Collections.singletonList(createList(null, alternatives)));
    }

    private static IStrategoTerm[] toArray(Iterable<IStrategoTerm> children) {
        return Iterables.toArray(children, IStrategoTerm.class);
    }

}
