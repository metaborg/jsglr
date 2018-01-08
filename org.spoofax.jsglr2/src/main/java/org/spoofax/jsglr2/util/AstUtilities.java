package org.spoofax.jsglr2.util;

import static org.spoofax.interpreter.terms.IStrategoTerm.APPL;
import static org.spoofax.interpreter.terms.IStrategoTerm.LIST;
import static org.spoofax.interpreter.terms.IStrategoTerm.TUPLE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoConstructor;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.terms.TermFactory;

public class AstUtilities {

    private ITermFactory termFactory;

    public AstUtilities() {
        this.termFactory = new TermFactory();
    }

    public List<IStrategoTerm> expand(IStrategoTerm ast) {
        List<IStrategoTerm> result = new ArrayList<IStrategoTerm>();

        switch(ast.getTermType()) {
            case APPL:
                IStrategoAppl appl = ((IStrategoAppl) ast);
                IStrategoConstructor constructor = appl.getConstructor();

                if("amb".equals(constructor.getName())) {
                    for(IStrategoTerm subAst : appl.getSubterm(0).getAllSubterms())
                        result.addAll(expand(subAst));
                } else {
                    for(List<IStrategoTerm> subAsts : expandSubterms(appl))
                        result
                            .add(termFactory.makeAppl(constructor, subAsts.toArray(new IStrategoTerm[subAsts.size()])));
                }

                break;
            case LIST:
                for(List<IStrategoTerm> subAsts : expandSubterms(ast))
                    result.add(termFactory.makeList(subAsts.toArray(new IStrategoTerm[subAsts.size()])));

                break;
            case TUPLE:
                for(List<IStrategoTerm> subAsts : expandSubterms(ast))
                    result.add(termFactory.makeTuple(subAsts.toArray(new IStrategoTerm[subAsts.size()])));

                break;
            default:
                result.add(ast);

                break;
        }

        return result;
    }

    private List<List<IStrategoTerm>> expandSubterms(IStrategoTerm astWithSubAsts) {
        List<List<IStrategoTerm>> expandedSubAsts =
            new ArrayList<List<IStrategoTerm>>(astWithSubAsts.getSubtermCount());

        for(IStrategoTerm subAst : astWithSubAsts.getAllSubterms()) {
            expandedSubAsts.add(expand(subAst));
        }

        return expand(expandedSubAsts);
    }

    /*
     * Expands [[a], [b1, b2], [c]] into [[a, b1, c], [a, b2, c]]
     */
    public <T> List<List<T>> expand(List<List<T>> elements) {
        if(elements.isEmpty())
            return Arrays.asList(Arrays.asList());
        else {
            ArrayList<List<T>> results = new ArrayList<List<T>>();

            List<T> head = elements.get(0);
            List<List<T>> tail = elements.subList(1, elements.size());

            List<List<T>> tailExpansions = expand(tail);

            for(T headElement : head) {
                for(List<T> tailExpansion : tailExpansions) {
                    List<T> result = new ArrayList<T>();

                    result.add(headElement);
                    result.addAll(tailExpansion);

                    results.add(result);
                }
            }

            return results;
        }
    }

    public int ambCount(IStrategoTerm ast) {
        int result = 0;

        switch(ast.getTermType()) {
            case APPL:
                IStrategoAppl appl = ((IStrategoAppl) ast);
                IStrategoConstructor constructor = appl.getConstructor();

                if("amb".equals(constructor.getName())) {
                    result++;

                    for(IStrategoTerm subAst : appl.getSubterm(0).getAllSubterms())
                        result += ambCount(subAst);
                } else {
                    for(IStrategoTerm subAst : appl.getAllSubterms())
                        result += ambCount(subAst);
                }

                break;
            case LIST:
            case TUPLE:
                for(IStrategoTerm subAst : ast.getAllSubterms())
                    result += ambCount(subAst);

                break;
            default:
                break;
        }

        return result;
    }

    public int ambCountShared(IStrategoTerm ast) {
        Set<IStrategoAppl> ambs = new HashSet<IStrategoAppl>();

        ambCountShared(ast, ambs);

        return ambs.size();
    }

    private void ambCountShared(IStrategoTerm ast, Set<IStrategoAppl> ambs) {
        switch(ast.getTermType()) {
            case APPL:
                IStrategoAppl appl = ((IStrategoAppl) ast);
                IStrategoConstructor constructor = appl.getConstructor();

                if("amb".equals(constructor.getName())) {
                    ambs.add(appl);

                    for(IStrategoTerm subAst : appl.getSubterm(0).getAllSubterms())
                        ambCountShared(subAst, ambs);
                } else {
                    for(IStrategoTerm subAst : appl.getAllSubterms())
                        ambCountShared(subAst, ambs);
                }

                break;
            case LIST:
            case TUPLE:
                for(IStrategoTerm subAst : ast.getAllSubterms())
                    ambCountShared(subAst, ambs);

                break;
            default:
                break;
        }
    }

    public IStrategoTerm ambFlatten(IStrategoTerm ast) {
        switch(ast.getTermType()) {
            case APPL:
                IStrategoAppl appl = ((IStrategoAppl) ast);
                IStrategoConstructor constructor = appl.getConstructor();

                if("amb".equals(constructor.getName())) {
                    List<IStrategoTerm> flattenSubAsts = new ArrayList<IStrategoTerm>();

                    for(IStrategoTerm subAst : appl.getSubterm(0).getAllSubterms()) {
                        IStrategoTerm flattenSubAst = ambFlatten(subAst);

                        if(APPL == subAst.getTermType()) {
                            IStrategoAppl flattenSubAppl = ((IStrategoAppl) subAst);
                            IStrategoConstructor flattenSubConstructor = flattenSubAppl.getConstructor();

                            if("amb".equals(flattenSubConstructor.getName())) {
                                flattenSubAsts.addAll(Arrays.asList(flattenSubAppl.getSubterm(0).getAllSubterms()));
                            } else
                                flattenSubAsts.add(flattenSubAst);
                        } else
                            flattenSubAsts.add(flattenSubAst);
                    }

                    return termFactory.makeAppl(constructor, new IStrategoTerm[] {
                        termFactory.makeList(flattenSubAsts.toArray(new IStrategoTerm[flattenSubAsts.size()])) });
                } else {
                    return ast;
                }
            default:
                return ast;
        }
    }

}
