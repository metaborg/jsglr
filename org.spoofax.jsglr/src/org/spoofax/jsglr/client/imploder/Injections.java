package org.spoofax.jsglr.client.imploder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.metaborg.util.collection.ImList;
import org.metaborg.util.functions.PartialFunction2;
import org.metaborg.util.iterators.ReverseListIterator;
import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoConstructor;
import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.IStrategoTuple;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.terms.attachments.OriginAttachment;

import mb.jsglr.shared.ImploderAttachment;

public class Injections {

    private static final String INJ_ANNO = "inj";

    public static IStrategoTerm explicate(IStrategoTerm term, PartialFunction2<String, String, String> injName,
            ITermFactory factory) {
        final List<String> injections = ImList.Immutable.copyOf(getInjections(term));

        IStrategoTerm result;
        String sort;

        switch(term.getType()) {
            case APPL: {
                final IStrategoAppl appl = (IStrategoAppl) term;
                final IStrategoTerm[] subterms =
                        Arrays.stream(appl.getAllSubterms()).map(subterm -> explicate(subterm, injName, factory))
                                .collect(Collectors.toList()).toArray(new IStrategoTerm[appl.getSubtermCount()]);
                result = factory.makeAppl(appl.getConstructor(), subterms, appl.getAnnotations());
                sort = getImploderAttachment(term).getSort();
                break;
            }
            case TUPLE: {
                final IStrategoTuple tuple = (IStrategoTuple) term;
                final IStrategoTerm[] subterms =
                        Arrays.stream(tuple.getAllSubterms()).map(subterm -> explicate(subterm, injName, factory))
                                .collect(Collectors.toList()).toArray(new IStrategoTerm[tuple.getSubtermCount()]);
                result = factory.makeTuple(subterms, tuple.getAnnotations());
                final List<String> componentSorts = Arrays.stream(term.getAllSubterms())
                        .map(t -> getImploderAttachment(t).getSort()).collect(Collectors.toList());
                sort = "Tuple_" + String.join("_", componentSorts) + "_";
                break;
            }
            case LIST: {
                final IStrategoList list = (IStrategoList) term;
                final IStrategoTerm[] subterms =
                        Arrays.stream(list.getAllSubterms()).map(subterm -> explicate(subterm, injName, factory))
                                .collect(Collectors.toList()).toArray(new IStrategoTerm[list.getSubtermCount()]);
                result = factory.makeList(subterms, list.getAnnotations());
                final String elementSort = getImploderAttachment(term).getElementSort();
                sort = "List_" + elementSort + "_";
                break;
            }
            default:
                result = term;
                sort = getImploderAttachment(term).getSort();
                break;
        }
        result = factory.copyAttachments(term, result);
        getImploderAttachment(result).clearInjections();

        for(String injection : ReverseListIterator.reverse(injections)) {
            final Optional<String> name = injName.apply(sort, injection);
            if(!name.isPresent()) {
                continue;
            }
            final IStrategoConstructor cons = factory.makeConstructor(name.get(), 1);
            final IStrategoTerm anno =
                    factory.makeAppl(INJ_ANNO, factory.makeString(sort), factory.makeString(injection));
            ImploderAttachment ia = getImploderAttachment(result);
            result = factory.makeAppl(cons, new IStrategoTerm[] { result }, factory.makeList(anno));
            sort = injection;
            if(ia != null) {
                ImploderAttachment.putImploderAttachment(result, false, sort, ia.getLeftToken(), ia.getRightToken(),
                        false, false, false, false);
            }
        }

        return result;
    }

    private static List<String> getInjections(IStrategoTerm term) {
        final ImploderAttachment ia = getImploderAttachment(term);
        return ia == null ? Collections.emptyList() : ia.getInjections();
    }

    private static ImploderAttachment getImploderAttachment(IStrategoTerm term) {
        final IStrategoTerm origin = OriginAttachment.tryGetOrigin(term); // Simply returns `term` when there is no origin.
        return ImploderAttachment.get(origin);
    }

}
