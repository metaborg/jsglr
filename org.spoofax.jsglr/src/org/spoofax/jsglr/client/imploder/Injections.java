package org.spoofax.jsglr.client.imploder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.metaborg.util.functions.Function2;
import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoConstructor;
import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.IStrategoTuple;
import org.spoofax.interpreter.terms.ITermFactory;

import com.google.common.collect.ImmutableList;

public class Injections {

    private final ITermFactory factory;
    private final Function2<String, String, String> injName;

    public Injections(ITermFactory factory, Function2<String, String, String> injName) {
        this.factory = factory;
        this.injName = injName;
    }

    public IStrategoTerm explicate(final IStrategoTerm term) {
        final List<String> injections = ImmutableList.copyOf(ImploderAttachment.get(term).getInjections()).reverse();

        IStrategoTerm result;
        String sort;

        switch(term.getTermType()) {
            case IStrategoTerm.APPL: {
                final IStrategoAppl appl = (IStrategoAppl) term;
                final IStrategoTerm[] subterms = Arrays.stream(appl.getAllSubterms()).map(this::explicate)
                        .collect(Collectors.toList()).toArray(new IStrategoTerm[appl.getSubtermCount()]);
                result = factory.makeAppl(appl.getConstructor(), subterms, appl.getAnnotations());
                sort = ImploderAttachment.get(term).getSort();
                break;
            }
            case IStrategoTerm.TUPLE: {
                final IStrategoTuple tuple = (IStrategoTuple) term;
                final IStrategoTerm[] subterms = Arrays.stream(tuple.getAllSubterms()).map(this::explicate)
                        .collect(Collectors.toList()).toArray(new IStrategoTerm[tuple.getSubtermCount()]);
                result = factory.makeTuple(subterms, tuple.getAnnotations());
                final List<String> componentSorts = Arrays.stream(term.getAllSubterms())
                        .map(t -> ImploderAttachment.get(t).getSort()).collect(Collectors.toList());
                sort = "Tuple_" + String.join("_", componentSorts) + "_";
                break;
            }
            case IStrategoTerm.LIST: {
                final IStrategoList list = (IStrategoList) term;
                final IStrategoTerm[] subterms = Arrays.stream(list.getAllSubterms()).map(this::explicate)
                        .collect(Collectors.toList()).toArray(new IStrategoTerm[list.getSubtermCount()]);
                result = factory.makeList(subterms, list.getAnnotations());
                final String elementSort = ImploderAttachment.get(term).getElementSort();
                sort = "List_" + elementSort + "_";
                break;
            }
            default:
                result = term;
                sort = ImploderAttachment.get(term).getSort();
                break;
        }
        result = factory.copyAttachments(term, result);
        ImploderAttachment.get(result).clearInjections();

        for(String injection : injections) {
            final String name = injName.apply(sort, injection);
            final IStrategoConstructor cons = factory.makeConstructor(name, 1);
            ImploderAttachment ia = ImploderAttachment.get(result);
            result = factory.makeAppl(cons, result);
            sort = injection;
            if(ia != null) {
                ImploderAttachment.putImploderAttachment(result, false, sort, ia.getLeftToken(), ia.getRightToken(),
                        false, false, false, false);
            }
        }

        return result;
    }

}