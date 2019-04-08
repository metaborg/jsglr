package org.spoofax.jsglr2.imploder;

import java.util.Collections;
import java.util.List;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;

public class StrategoTermTreeFactory implements ITreeFactory<IStrategoTerm> {

    private final ITermFactory termFactory;

    public StrategoTermTreeFactory(ITermFactory termFactory) {
        this.termFactory = termFactory;
    }

    @Override public IStrategoTerm createStringTerminal(String sort, String value) {
        return termFactory.makeString(value);
    }

    @Override public IStrategoTerm createNonTerminal(String sort, String constructor, List<IStrategoTerm> childASTs) {
        return termFactory.makeAppl(
            termFactory.makeConstructor(constructor != null ? constructor : sort, childASTs.size()),
            toArray(childASTs));
    }

    @Override public IStrategoTerm createList(String sort, List<IStrategoTerm> children) {
        return termFactory.makeList(toArray(children));
    }

    @Override public IStrategoTerm createOptional(String sort, List<IStrategoTerm> children) {
        return createNonTerminal(sort, children == null || children.isEmpty() ? "None" : "Some", children);
    }

    @Override public IStrategoTerm createTuple(String sort, List<IStrategoTerm> children) {
        return termFactory.makeTuple(toArray(children));
    }

    @Override public IStrategoTerm createAmb(String sort, List<IStrategoTerm> alternatives) {
        return createNonTerminal(null, "amb", Collections.singletonList(createList(null, alternatives)));
    }

    private static IStrategoTerm[] toArray(List<IStrategoTerm> children) {
        return children.toArray(new IStrategoTerm[0]);
    }

}
