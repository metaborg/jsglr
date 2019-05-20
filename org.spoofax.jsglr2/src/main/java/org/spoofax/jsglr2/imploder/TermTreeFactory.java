package org.spoofax.jsglr2.imploder;

import static org.spoofax.jsglr.client.imploder.ImploderAttachment.putImploderAttachment;

import java.util.Collections;
import java.util.List;

import org.spoofax.interpreter.terms.IStrategoConstructor;
import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr.client.imploder.ImploderAttachment;

public class TermTreeFactory implements ITreeFactory<IStrategoTerm> {

    private final ITermFactory termFactory;

    public TermTreeFactory(ITermFactory termFactory) {
        this.termFactory = termFactory;
    }

    @Override public IStrategoTerm createStringTerminal(String sort, String value, IToken token) {
        IStrategoTerm stringTerminalTerm = termFactory.makeString(value);

        configure(stringTerminalTerm, sort, token, token);

        return stringTerminalTerm;
    }

    @Override public IStrategoTerm createNonTerminal(String sort, String constructor, List<IStrategoTerm> childASTs,
            IToken leftToken, IToken rightToken) {
        IStrategoConstructor constructorTerm =
                termFactory.makeConstructor(constructor != null ? constructor : sort, childASTs.size());
        IStrategoTerm nonTerminalTerm = termFactory.makeAppl(constructorTerm, toArray(childASTs));

        configure(nonTerminalTerm, sort, leftToken, rightToken);

        return nonTerminalTerm;
    }

    @Override public IStrategoTerm createList(String sort, List<IStrategoTerm> children, IToken leftToken,
            IToken rightToken) {
        IStrategoTerm listTerm = termFactory.makeList(toArray(children));

        configure(listTerm, sort, leftToken, rightToken);

        return listTerm;
    }

    @Override public IStrategoTerm createOptional(String sort, List<IStrategoTerm> children, IToken leftToken,
            IToken rightToken) {
        String constructor = children == null || children.isEmpty() ? "None" : "Some";

        return createNonTerminal(sort, constructor, children, leftToken, rightToken);
    }

    @Override public IStrategoTerm createTuple(String sort, List<IStrategoTerm> children, IToken leftToken,
            IToken rightToken) {
        IStrategoTerm tupleTerm = termFactory.makeTuple(toArray(children));

        configure(tupleTerm, sort, leftToken, rightToken);

        return tupleTerm;
    }

    @Override public IStrategoTerm createAmb(String sort, List<IStrategoTerm> alternatives, IToken leftToken,
            IToken rightToken) {
        IStrategoTerm alternativesListTerm = createList(null, alternatives, leftToken, rightToken);

        return createNonTerminal(null, "amb", Collections.singletonList(alternativesListTerm), leftToken, rightToken);
    }

    private static IStrategoTerm[] toArray(List<IStrategoTerm> children) {
        return children.toArray(new IStrategoTerm[children.size()]);
    }

    protected void configure(IStrategoTerm term, String sort, IToken leftToken, IToken rightToken) {
        // rightToken can be null, e.g. for an empty string lexical
        putImploderAttachment(term, false, sort, leftToken, rightToken != null ? rightToken : leftToken, false, false,
                false, false);
        if(term.getTermType() == IStrategoTerm.LIST) {
            IStrategoList sublist = (IStrategoList) term;
            IToken lastRightToken;
            while(!sublist.isEmpty()) {
                lastRightToken = ImploderAttachment.getRightToken(sublist.head());
                sublist = sublist.tail();
                leftToken = sublist.isEmpty() ? lastRightToken : ImploderAttachment.getLeftToken(sublist.head());
                // assuming rightToken is never null for lists
                putImploderAttachment(sublist, false, sort, leftToken, rightToken, false, false, false, false);
            }
        }
    }

}
