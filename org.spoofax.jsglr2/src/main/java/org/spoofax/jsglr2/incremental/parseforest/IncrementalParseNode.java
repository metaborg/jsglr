package org.spoofax.jsglr2.incremental.parseforest;

import static org.spoofax.jsglr2.incremental.IncrementalParse.NO_STATE;

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
    }

    public IncrementalParseNode(IncrementalParseForest... parseForests) {
        this(null, new IncrementalDerivation(null, null, parseForests, NO_STATE));
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

    @Override public String getYield() {
        StringBuilder sb = new StringBuilder(width());
        for(IncrementalParseForest parseForest : firstDerivation.parseForests) {
            sb.append(parseForest.getYield());
        }
        return sb.toString();
    }

    @Override public String getYield(int length) {
        StringBuilder sb = new StringBuilder(length);
        int offset = 0;
        for(IncrementalParseForest parseForest : firstDerivation.parseForests) {
            int width = parseForest.width();
            if(offset + width <= length) {
                sb.append(parseForest.getYield());
                offset += width;
            } else {
                sb.append(parseForest.getYield(length - offset));
                break;
            }
        }
        return sb.toString();
    }
}
