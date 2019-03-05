package org.spoofax.jsglr2.incremental.parseforest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.metaborg.parsetable.IProduction;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.util.TreePrettyPrinter;
import org.spoofax.jsglr2.util.iterators.SingleElementWithListIterable;

public class IncrementalParseNode extends IncrementalParseForest
    implements IParseNode<IncrementalParseForest, IncrementalDerivation> {

    private final IProduction production;
    private final IncrementalDerivation firstDerivation;
    private List<IncrementalDerivation> otherDerivations;

    public IncrementalParseNode(IProduction production, IncrementalDerivation firstDerivation) {
        super(firstDerivation.width());
        this.production = production;
        this.firstDerivation = firstDerivation;
        firstDerivation.parent = this;
    }

    public IProduction production() {
        return production;
    }

    @Override public String descriptor() {
        return production.descriptor();
    }

    @Override public void addDerivation(IncrementalDerivation derivation) {
        if(otherDerivations == null)
            otherDerivations = new ArrayList<>();

        otherDerivations.add(derivation);
        derivation.parent = this;
    }

    @Override public Iterable<IncrementalDerivation> getDerivations() {
        if(otherDerivations == null) {
            return Collections.singleton(firstDerivation);
        } else {
            return SingleElementWithListIterable.of(firstDerivation, otherDerivations);
        }
    }

    @Override public IncrementalDerivation getFirstDerivation() {
        return firstDerivation;
    }

    @Override public boolean isAmbiguous() {
        return otherDerivations != null;
    }

    @Override public IncrementalParseForest leftBreakdown() {
        IncrementalParseForest[] children = getFirstDerivation().parseForests();
        if(children.length > 0) {
            IncrementalParseForest result = children[0]; // should be from previous version
            return result.isFragile() ? result.leftBreakdown() : result;
        } else
            return popLookAhead();
    }

    @Override protected void prettyPrint(TreePrettyPrinter printer) {
        if(production == null)
            printer.println("p null {");
        else
            printer.println("p" + production.id() + " : " + production.sort() + "{");
        if(isAmbiguous()) {
            printer.indent(1);
            printer.println("amb[");
            printer.indent(1);
        } else {
            printer.indent(2);
        }

        firstDerivation.prettyPrint(printer);

        if(otherDerivations != null) {
            for(IncrementalDerivation derivation : otherDerivations) {
                derivation.prettyPrint(printer);
            }
        }

        if(isAmbiguous()) {
            printer.indent(-1);
            printer.println("]");
            printer.indent(-1);
        } else {
            printer.indent(-2);
        }
        printer.println("}");
    }

    @Override public String getSource() {
        StringBuilder sb = new StringBuilder();
        for(IncrementalParseForest parseForest : firstDerivation.parseForests) {
            sb.append(parseForest.getSource());
        }
        return sb.toString();
    }
}
