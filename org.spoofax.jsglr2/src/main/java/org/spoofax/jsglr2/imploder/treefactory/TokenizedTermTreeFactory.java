package org.spoofax.jsglr2.imploder.treefactory;

import static org.spoofax.jsglr.client.imploder.ImploderAttachment.putImploderAttachment;
import static org.spoofax.terms.AbstractTermFactory.EMPTY_TERM_ARRAY;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.metaborg.parsetable.symbols.IMetaVarSymbol;
import org.metaborg.parsetable.symbols.ISymbol;
import org.spoofax.interpreter.terms.IStrategoConstructor;
import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr.client.imploder.ImploderAttachment;
import org.spoofax.terms.TermFactory;

public class TokenizedTermTreeFactory implements ITokenizedTreeFactory<IStrategoTerm> {

    private final ITermFactory termFactory;

    public TokenizedTermTreeFactory() {
        this.termFactory = new TermFactory();
    }

    @Override public IStrategoTerm createStringTerminal(ISymbol symbol, String value, IToken token) {
        IStrategoTerm stringTerminalTerm = termFactory.makeString(value);

        configure(stringTerminalTerm, ISymbol.getSort(symbol), token, token);

        return stringTerminalTerm;
    }

    @Override public IStrategoTerm createMetaVar(IMetaVarSymbol symbol, String value, IToken token) {
        IStrategoTerm stringTerm = termFactory.makeString(value);
        IStrategoTerm metaVarTerm = termFactory.makeAppl(symbol.metaVarCardinality().constructor, stringTerm);

        configure(stringTerm, ISymbol.getSort(symbol), token, token);
        configure(metaVarTerm, null, token, token);

        return metaVarTerm;
    }

    @Override public IStrategoTerm createNonTerminal(ISymbol symbol, String constructor, List<IStrategoTerm> childASTs,
        IToken leftToken, IToken rightToken) {
        IStrategoConstructor constructorTerm =
            termFactory.makeConstructor(constructor != null ? constructor : ISymbol.getSort(symbol), childASTs.size());
        IStrategoTerm nonTerminalTerm = termFactory.makeAppl(constructorTerm, toArray(childASTs));

        configure(nonTerminalTerm, ISymbol.getSort(symbol), leftToken, rightToken);

        return nonTerminalTerm;
    }

    @Override public IStrategoTerm createList(List<IStrategoTerm> children, IToken leftToken, IToken rightToken) {
        IStrategoTerm listTerm = termFactory.makeList(toArray(children));

        configure(listTerm, null, leftToken, rightToken);

        return listTerm;
    }

    @Override public IStrategoTerm createOptional(ISymbol symbol, List<IStrategoTerm> children, IToken leftToken,
        IToken rightToken) {
        String constructor = children == null || children.isEmpty() ? "None" : "Some";

        return createNonTerminal(symbol, constructor, children, leftToken, rightToken);
    }

    @Override public IStrategoTerm createTuple(List<IStrategoTerm> children, IToken leftToken, IToken rightToken) {
        IStrategoTerm tupleTerm = termFactory.makeTuple(toArray(children));

        configure(tupleTerm, null, leftToken, rightToken);

        return tupleTerm;
    }

    @Override public IStrategoTerm createAmb(List<IStrategoTerm> alternatives, IToken leftToken, IToken rightToken) {
        IStrategoTerm alternativesListTerm = createList(alternatives, leftToken, rightToken);

        return createNonTerminal(null, "amb", Collections.singletonList(alternativesListTerm), leftToken, rightToken);
    }

    @Override public IStrategoTerm createInjection(ISymbol symbol, IStrategoTerm injected, boolean isBracket) {
        configureInjection(symbol, injected, isBracket);
        return injected;
    }

    private static IStrategoTerm[] toArray(List<IStrategoTerm> children) {
        return children.toArray(EMPTY_TERM_ARRAY);
    }

    public static void configureInjection(ISymbol symbol, IStrategoTerm injected, boolean isBracket) {
        String sort = ISymbol.getSort(symbol);

        // Prevent bogus injections from empty sorts, or lexical sorts into themselves
        String injectedSort = ImploderAttachment.get(injected).getSort();
        if(sort != null && !Objects.equals(sort, injectedSort)) {
            ImploderAttachment.get(injected).pushInjection(sort);
        }

        ImploderAttachment.get(injected).setBracket(isBracket);
    }

    public static void configure(IStrategoTerm term, String sort, IToken leftToken, IToken rightToken) {
        // rightToken can be null, e.g. for an empty string lexical
        rightToken = rightToken != null ? rightToken : leftToken;
        putImploderAttachment(term, false, sort, leftToken, rightToken, false, false, false, false);
        if(term.getTermType() == IStrategoTerm.LIST) {
            IStrategoList sublist = (IStrategoList) term;
            IToken lastRightToken;
            while(!sublist.isEmpty()) {
                lastRightToken = ImploderAttachment.getRightToken(sublist.head());
                sublist = sublist.tail();
                leftToken = sublist.isEmpty() ? lastRightToken : ImploderAttachment.getLeftToken(sublist.head());
                putImploderAttachment(sublist, false, sort, leftToken, rightToken, false, false, false, false);
            }
        }
    }

}
