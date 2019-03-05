package org.spoofax.jsglr2.incremental.parseforest;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.IState;
import org.metaborg.parsetable.ProductionType;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.util.TreePrettyPrinter;

public class IncrementalDerivation implements IDerivation<IncrementalParseForest> {
    private final int width;
    private final IProduction production;
    private final ProductionType productionType;
    public final IncrementalParseForest[] parseForests;
    public IncrementalParseNode parent;
    public final IState state;
    private boolean changed;

    public IncrementalDerivation(IProduction production, ProductionType productionType,
        IncrementalParseForest[] parseForests, IState state) {

        this.production = production;
        this.productionType = productionType;
        this.parseForests = parseForests;
        this.state = state;
        int width = 0;
        for(int i = 0; i < parseForests.length; i++) {
            IncrementalParseForest parseForest = parseForests[i];
            if(parseForest == null)
                continue; // Skippable parse nodes are null
            parseForest.parent = this;
            parseForest.childIndex = i;
            width += parseForest.width();
        }
        this.width = width;
    }

    @Override public int width() {
        return width;
    }

    @Override public IProduction production() {
        return changed ? null : production;
    }

    @Override public ProductionType productionType() {
        return changed ? null : productionType;
    }

    @Override public IncrementalParseForest[] parseForests() {
        return parseForests;
    }

    public void markChanged() {
        this.changed = true;
    }

    protected void prettyPrint(TreePrettyPrinter printer) {
        if(production == null)
            printer.println("p null : {");
        else
            printer.println("p" + production.id() + " : " + production.descriptor() + "{");
        printer.indent(2);

        for(IncrementalParseForest parseForest : parseForests) {
            if(parseForest != null) {
                parseForest.prettyPrint(printer);
            } else
                printer.println("null");
        }

        printer.indent(-2);
        printer.println("}");
    }


}
