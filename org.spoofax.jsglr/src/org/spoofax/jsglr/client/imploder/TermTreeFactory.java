package org.spoofax.jsglr.client.imploder;

import static org.spoofax.interpreter.terms.IStrategoTerm.APPL;
import static org.spoofax.interpreter.terms.IStrategoTerm.INT;
import static org.spoofax.interpreter.terms.IStrategoTerm.LIST;
import static org.spoofax.interpreter.terms.IStrategoTerm.MUTABLE;
import static org.spoofax.interpreter.terms.IStrategoTerm.REAL;
import static org.spoofax.interpreter.terms.IStrategoTerm.STRING;
import static org.spoofax.interpreter.terms.IStrategoTerm.TUPLE;
import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getElementSort;
import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getSort;
import static org.spoofax.jsglr.client.imploder.ImploderAttachment.putImploderAttachment;
import static org.spoofax.terms.StrategoListIterator.iterable;
import static org.spoofax.terms.Term.isTermAppl;
import static org.spoofax.terms.Term.isTermString;
import static org.spoofax.terms.attachments.ParentAttachment.putParent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoConstructor;
import org.spoofax.interpreter.terms.IStrategoInt;
import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoReal;
import org.spoofax.interpreter.terms.IStrategoString;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.IStrategoTuple;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.terms.StrategoListIterator;
import org.spoofax.terms.StrategoSubList;
import org.spoofax.terms.TermFactory;
import org.spoofax.terms.attachments.ParentAttachment;
import org.spoofax.terms.attachments.TermAttachmentType;
import org.spoofax.terms.util.NotImplementedException;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class TermTreeFactory implements ITreeFactory<IStrategoTerm> {

    private final ITermFactory originalFactory;

    private ITermFactory factory;

    private boolean enableTokens;

    public TermTreeFactory() {
        this(new TermFactory());
    }

    /**
     * Creates a new TermTreeFactory. Must be done using an ITermFactory that supports creating mutable terms; this
     * constructor calls setDefaultStorageType(MUTABLE).
     */
    public TermTreeFactory(ITermFactory factory) {
        originalFactory = factory;
        setEnableTokens(false); // (is set by TreeBuilder.initializeParseTable)
    }

    public void setEnableTokens(boolean enableTokens) {
        this.enableTokens = enableTokens;
        if(enableTokens) {
            factory = originalFactory.getFactoryWithStorageType(MUTABLE);
            if(!TermFactory.checkStorageType(factory, MUTABLE))
                throw new IllegalStateException(
                    "Term factory does not support MUTABLE terms, required for creating terms with (token) attachments");
        } else {
            factory = originalFactory;
        }
    }

    /**
     * @deprecated May return a copy of the term factory that creates mutable terms.
     */
    public ITermFactory getTermFactory() {
        return factory;
    }

    public ITermFactory getOriginalTermFactory() {
        return originalFactory;
    }

    public IStrategoConstructor createConstructor(String name, int childCount) {
        return factory.makeConstructor(name, childCount);
    }

    public IStrategoTerm createNonTerminal(String sort, String constructor, IToken leftToken, IToken rightToken,
        List<IStrategoTerm> children, boolean isCompletion, boolean isNestedCompletion,
        boolean isSinglePlaceholderCompletion) {

        // TODO: Optimize - cache IStrategoConstructors in fields? hard to do up front, messy to do now in the LabelInfo
        // objects
        IStrategoConstructor cons = factory.makeConstructor(constructor, children.size());
        IStrategoTerm result = factory.makeAppl(cons, children.toArray(new IStrategoTerm[children.size()]));



        configure(result, sort, leftToken, rightToken, false, false, isCompletion, isNestedCompletion,
            isSinglePlaceholderCompletion);
        return result;
    }

    public IStrategoTerm createIntTerminal(String sort, IToken token, int value) {
        IStrategoTerm result = factory.makeInt(value);
        configure(result, sort, token, token, false, false, false, false, false);
        return result;
    }

    public IStrategoTerm createRealTerminal(String sort, IToken token, double value) {
        IStrategoTerm result = factory.makeReal(value);
        configure(result, sort, token, token, false, false, false, false, false);
        return result;
    }

    public IStrategoTerm createStringTerminal(String sort, IToken leftToken, IToken rightToken, String value, boolean caseInsensitive) {
        if(caseInsensitive){
            value = value.toLowerCase();
        }
        IStrategoTerm result = factory.makeString(value);
        configure(result, sort, leftToken, rightToken, false, false, false, false, false);
        return result;
    }

    public IStrategoTerm createTuple(String elementSort, IToken leftToken, IToken rightToken,
        List<IStrategoTerm> children) {

        IStrategoTerm result = factory.makeTuple(toArray(children));
        configure(result, elementSort, leftToken, rightToken, true, false, false, false, false);
        return result;
    }

    public IStrategoTerm createAmb(List<IStrategoTerm> alternatives, IToken leftToken, IToken rightToken,
        boolean isCompletion, boolean isNestedCompletion, boolean isSinglePlaceholderCompletion) {
        List<IStrategoTerm> alternativesInList = new ArrayList<IStrategoTerm>();
        alternativesInList.add(createList(null, leftToken, rightToken, alternatives));

        return createNonTerminal(null, "amb", leftToken, rightToken, alternativesInList, isCompletion,
            isNestedCompletion, isSinglePlaceholderCompletion);
    }

    public IStrategoTerm createList(String elementSort, IToken leftToken, IToken rightToken,
        List<IStrategoTerm> children) {

        IStrategoTerm result = factory.makeList(toArray(children));
        configure(result, elementSort, leftToken, rightToken, true, false, false, false, false);
        return result;
    }

    public IStrategoTerm createSublist(IStrategoList list, IStrategoTerm firstChild, IStrategoTerm lastChild) {
        ArrayList<IStrategoTerm> children = new ArrayList<IStrategoTerm>();
        boolean isStartChildFound = false;
        int indexStart = -1;
        int indexEnd = -1;

        int i = 0;
        for(IStrategoTerm child : StrategoListIterator.iterable(list)) {
            if(child == firstChild) {
                indexStart = i;
                isStartChildFound = true;
            }
            if(isStartChildFound) {
                children.add(child);
                if(child == lastChild) {
                    indexEnd = i;
                    break;
                }
            }
            i++;
        }
        assert (0 <= indexStart && indexStart <= indexEnd);
        IStrategoList wrapped = factory.makeList(children);
        StrategoSubList result = new StrategoSubList(list, wrapped, indexStart, indexEnd);

        /*
         * XXX: support updateParents again?? if (cloneFirst) result = result.cloneIgnoreTokens();
         * list.overrideReferences(getLeftToken(list), getRightToken(list), children, result); setParent(result, list);
         */
        putParent(result, ParentAttachment.get(list));

        if(ImploderAttachment.get(firstChild) != null && ImploderAttachment.get(lastChild) != null)
            putImploderAttachment(result, true, getElementSort(list), getLeftToken(firstChild),
                getRightToken(lastChild), false, false, false, false);
        return result;
    }



    public IStrategoTerm recreateNode(IStrategoTerm oldNode, IToken leftToken, IToken rightToken,
        List<IStrategoTerm> children) {
        ImploderAttachment it = oldNode.getAttachment(ImploderAttachment.TYPE);


        switch(oldNode.getTermType()) {
            case INT:
                return createIntTerminal(getSort(oldNode), leftToken, ((IStrategoInt) oldNode).intValue());
            case APPL:
                return createNonTerminal(getSort(oldNode), ((IStrategoAppl) oldNode).getName(), leftToken, rightToken,
                    children, it.isCompletion(), it.isNestedCompletion(), it.isSinglePlaceholderCompletion());
            case LIST:
                return createList(getElementSort(oldNode), leftToken, rightToken, children);
            case STRING:
                return createStringTerminal(getSort(oldNode), leftToken, rightToken,
                    ((IStrategoString) oldNode).stringValue(), !(((IStrategoString) oldNode).stringValue().equals(it.toString())));
            case TUPLE:
                return createTuple(getElementSort(oldNode), leftToken, rightToken, children);
            case REAL:
                return createRealTerminal(getElementSort(oldNode), leftToken, ((IStrategoReal) oldNode).realValue());
            default:
                throw new NotImplementedException(
                    "Recreating term of type " + oldNode.getTermType() + " (" + oldNode + ") not supported");
        }
    }

    public String tryGetStringValue(IStrategoTerm node) {
        return isTermString(node) ? ((IStrategoString) node).stringValue() : null;
    }

    public IStrategoTerm createInjection(String sort, IToken leftToken, IToken rightToken, List<IStrategoTerm> children,
        boolean isCompletion, boolean isNestedCompletion, boolean isSinglePlaceholderCompletion, boolean isBracket) {
        if(children.size() == 1) {
            // tagging bracket nodes as completed and adding the brackets as part of the origin
            if(isBracket) {
                IStrategoTerm result = children.get(0);
                if(result instanceof IStrategoAppl) {
                    if(((IStrategoAppl) result).getConstructor().getName().equals("amb")) {
                        IStrategoTerm ambList = result.getSubterm(0);
                        configureAmbNodes(ambList, isBracket, isCompletion, isNestedCompletion,
                            isSinglePlaceholderCompletion);
                        return result;
                    }
                }
                IToken left = leftToken;
                IToken right = rightToken;
                if(ImploderAttachment.get(result).isCompletion()) {
                    configure(result, result.getAttachment(ImploderAttachment.TYPE).getSort(), left, right, false,
                        isBracket, true, false, isSinglePlaceholderCompletion);
                } else {
                    configure(result, result.getAttachment(ImploderAttachment.TYPE).getSort(), left, right, false,
                        isBracket, isCompletion, isNestedCompletion, isSinglePlaceholderCompletion);
                }
                return result;
            } else {
                return children.get(0);
            }
        } else {
            IStrategoTuple result = factory.makeTuple(toArray(children));
            IToken left = getLeftToken(children.get(0));
            IToken right = getRightToken(children.get(children.size() - 1));
            configure(result, null, left, right, true, false, isCompletion, isNestedCompletion,
                isSinglePlaceholderCompletion);
            return result;
        }
    }

    private void configureAmbNodes(IStrategoTerm result, boolean isBracket, boolean isCompletion,
        boolean isNestedCompletion, boolean isSinglePlaceholderCompletion) {
        for(int i = 0; i < result.getSubtermCount(); i++) {
            IStrategoTerm child = result.getSubterm(i);
            if(child instanceof IStrategoAppl) {
                if(((IStrategoAppl) child).getConstructor().getName().equals("amb")) {
                    IStrategoTerm ambList = child.getSubterm(0);
                    configureAmbNodes(ambList, isBracket, isCompletion, isNestedCompletion,
                        isSinglePlaceholderCompletion);
                } else {
                    IToken left = getLeftToken(child);
                    IToken right = getRightToken(child);
                    if(ImploderAttachment.get(child).isCompletion()) {
                        configure(child, child.getAttachment(ImploderAttachment.TYPE).getSort(), left, right, false,
                            isBracket, true, false, isSinglePlaceholderCompletion);
                    } else {
                        configure(child, child.getAttachment(ImploderAttachment.TYPE).getSort(), left, right, false,
                            isBracket, isCompletion, isNestedCompletion, isSinglePlaceholderCompletion);
                    }
                }
            }
        }
    }

    public Iterable<IStrategoTerm> getChildren(IStrategoTerm node) {
        if(node instanceof Iterable<?>) {
            @SuppressWarnings("unchecked") Iterable<IStrategoTerm> result = (Iterable<IStrategoTerm>) node;
            return result;
        } else {
            if(node.getSubtermCount() == 0)
                return Collections.emptyList();
            ArrayList<IStrategoTerm> children = new ArrayList<IStrategoTerm>(node.getSubtermCount());
            for(int i = 0, max = node.getSubtermCount(); i < max; i++) {
                children.add(node.getSubterm(i));
            }
            return children;
        }
    }

    private static IStrategoTerm[] toArray(List<IStrategoTerm> children) {
        return children.toArray(new IStrategoTerm[children.size()]);
    }

    public final IToken getLeftToken(IStrategoTerm term) {
        return ImploderAttachment.getLeftToken(term);
    }

    public final IToken getRightToken(IStrategoTerm term) {
        return ImploderAttachment.getRightToken(term);
    }

    public Iterable<IStrategoTerm> tryGetAmbChildren(IStrategoTerm node) {
        // TODO: optimize - use getConstructor() instead and make an ambConstructor field
        if(isTermAppl(node) && "amb".equals(((IStrategoAppl) node).getName())) {
            return iterable((IStrategoList) node.getSubterm(0));
        } else {
            return null;
        }
    }

    public IStrategoTerm createTop(IStrategoTerm tree, String filename, int ambiguityCount) {
        return tree;
    }

    protected void configure(IStrategoTerm term, String sort, IToken leftToken, IToken rightToken,
        boolean isListOrTuple, boolean isBracket, boolean isCompletion, boolean isNestedCompletion,
        boolean isSinglePlaceholderCompletion) {
        assert isListOrTuple == (term.getTermType() == TUPLE || term.getTermType() == LIST);
        if(enableTokens)
            putImploderAttachment(term, isListOrTuple, sort, leftToken, rightToken, isBracket, isCompletion,
                isNestedCompletion, isSinglePlaceholderCompletion);
    }
}
