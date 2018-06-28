package org.spoofax.jsglr2.imploder;

import static org.spoofax.jsglr.client.imploder.ImploderAttachment.putImploderAttachment;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.metaborg.parsetable.IProduction;
import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoConstructor;
import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.jsglr.client.imploder.IToken;

public class TermTreeFactory implements ITreeFactory<IStrategoTerm> {

    private final ITermFactory termFactory;

    public TermTreeFactory(ITermFactory termFactory) {
        this.termFactory = termFactory;
    }

    @Override
    public IStrategoTerm createStringTerminal(String sort, String value, IToken leftToken, IToken rightToken) {
        IStrategoTerm stringTerminalTerm = termFactory.makeString(value);

        configure(stringTerminalTerm, sort, leftToken, rightToken);

        return stringTerminalTerm;
    }

    @Override
    public IStrategoTerm createNonTerminal(String sort, String constructor, List<IStrategoTerm> childASTs,
        IToken leftToken, IToken rightToken) {
        IStrategoConstructor constructorTerm =
            termFactory.makeConstructor(constructor != null ? constructor : sort, childASTs.size());
        IStrategoTerm nonTerminalTerm = termFactory.makeAppl(constructorTerm, toArray(childASTs));

        configure(nonTerminalTerm, sort, leftToken, rightToken);

        return nonTerminalTerm;
    }

    @Override
    public IStrategoTerm createList(String sort, List<IStrategoTerm> children, IToken leftToken, IToken rightToken) {
        IStrategoTerm listTerm = termFactory.makeList(toArray(children));

        configure(listTerm, sort, leftToken, rightToken);

        return listTerm;
    }

    @Override
    public IStrategoTerm createOptional(String sort, List<IStrategoTerm> children, IToken leftToken,
        IToken rightToken) {
        String constructor = children == null || children.isEmpty() ? "None" : "Some";

        return createNonTerminal(sort, constructor, children, leftToken, rightToken);
    }

    @Override
    public IStrategoTerm createTuple(String sort, List<IStrategoTerm> children, IToken leftToken, IToken rightToken) {
        IStrategoTerm tupleTerm = termFactory.makeTuple(toArray(children));

        configure(tupleTerm, sort, leftToken, rightToken);

        return tupleTerm;
    }

    @Override
    public IStrategoTerm createAmb(String sort, List<IStrategoTerm> alternatives, IToken leftToken, IToken rightToken) {
        IStrategoTerm alternativesListTerm = createList(null, alternatives, leftToken, rightToken);

        IStrategoTerm ambTerm =
            createNonTerminal(null, "amb", Arrays.asList(alternativesListTerm), leftToken, rightToken);

        return ambTerm;
    }
    
    private static boolean isAmb(IStrategoTerm term) {
        if (term instanceof IStrategoAppl) {
            IStrategoAppl appl = (IStrategoAppl) term;
            return appl.getName() == "amb" && appl.getSubtermCount() == 1 && appl.getSubterm(0) instanceof IStrategoList;
        }
        return false;
    }

    @Override
    public IStrategoTerm concatLists(IProduction production, IStrategoTerm leftChild, IStrategoTerm rightChild, IToken leftToken,
            IToken rightToken) {
        final IStrategoTerm term;
        final boolean leftChildIsLift = leftChild instanceof IStrategoList;
        final boolean rightChildIsLift = rightChild instanceof IStrategoList;
        final boolean consistent = leftChildIsLift == production.isListLeftChild()
                && rightChildIsLift == production.isListRightChild();
        
        // If children are not lists when expected we create a Conc term for the symbolic concatenation of lists
        if (!consistent) {
            // However, if one of the unexpected terms is an ambiguity term, we lift that term out of the list
            if (production.isListLeftChild() && !leftChildIsLift && isAmb(leftChild)) {
                term = liftAmb(production, leftChild, leftToken, rightToken, (IStrategoTerm sublist) -> 
                    concatLists(production, sublist, rightChild, leftToken, rightToken));
            } else if(production.isListRightChild() && !rightChildIsLift && isAmb(rightChild)) {
                term = liftAmb(production, rightChild, leftToken, rightToken, (IStrategoTerm sublist) -> 
                    concatLists(production, leftChild, sublist, leftToken, rightToken));
            } else {
                term = termFactory.makeAppl(termFactory.makeConstructor("Conc", 2),
                        new IStrategoTerm[] { leftChild, rightChild });
            }
        } else if (leftChildIsLift) {
            if (rightChildIsLift) {
                IStrategoList list = (IStrategoList) rightChild;
                ListIterator<IStrategoTerm> it = Arrays.asList(leftChild.getAllSubterms())
                        .listIterator(leftChild.getSubtermCount());
                while (it.hasPrevious()) {
                    list = termFactory.makeListCons(it.previous(), list);
                }
                term = list;
            } else {
                IStrategoList list = termFactory.makeList(rightChild);
                ListIterator<IStrategoTerm> it = Arrays.asList(leftChild.getAllSubterms())
                        .listIterator(leftChild.getSubtermCount());
                while (it.hasPrevious()) {
                    list = termFactory.makeListCons(it.previous(), list);
                }
                term = list;
            }
        } else {
            if (rightChildIsLift) {
                IStrategoList list = (IStrategoList) rightChild;
                term = termFactory.makeListCons(leftChild, list);
            } else {
                // Not sure if this particular combination actually occurs in practice 
                term = termFactory.makeList(leftChild, rightChild);
            }
        }

        configure(term, production.sort(), leftToken, rightToken);

        return term;
    }

    private IStrategoTerm liftAmb(IProduction production, IStrategoTerm ambNode, IToken leftToken, IToken rightToken,
            final Function<IStrategoTerm, ? extends IStrategoTerm> mapper) {
        final IStrategoTerm term;
        List<IStrategoTerm> alternatives = StreamSupport.stream(ambNode.getSubterm(0).spliterator(), false)
            .map(mapper)
            .flatMap(resultList -> {
                if (isAmb(resultList)) {
                    return StreamSupport.stream(resultList.getSubterm(0).spliterator(), false);
                }
                return Arrays.stream(new IStrategoTerm[] {resultList});
            })
            .collect(Collectors.toList());
        term = createAmb(production.sort(), alternatives, leftToken, rightToken);
        return term;
    }

    private static IStrategoTerm[] toArray(List<IStrategoTerm> children) {
        return children.toArray(new IStrategoTerm[children.size()]);
    }

    protected void configure(IStrategoTerm term, String sort, IToken leftToken, IToken rightToken) {
        // rightToken can be null, e.g. for an empty string lexical
        putImploderAttachment(term, false, sort, leftToken, rightToken != null ? rightToken : leftToken, false, false,
            false, false);
    }

}