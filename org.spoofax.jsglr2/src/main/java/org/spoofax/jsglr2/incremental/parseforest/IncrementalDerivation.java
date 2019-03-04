package org.spoofax.jsglr2.incremental.parseforest;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.IState;
import org.metaborg.parsetable.ProductionType;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.util.TreePrettyPrinter;

public class IncrementalDerivation implements IDerivation<IncrementalParseForest> {
    private final int width;
    public final IProduction production;
    public final ProductionType productionType;
    public final IncrementalParseForest[] parseForests;
    public IncrementalParseNode parent;
    public final IState state;

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
        return production;
    }

    @Override public ProductionType productionType() {
        return productionType;
    }

    @Override public IncrementalParseForest[] parseForests() {
        return parseForests;
    }

    protected void prettyPrint(TreePrettyPrinter printer) {
        printer.println("p" + production.id() + " : " + production.descriptor() + "{");
        printer.indent(2);

        for(IncrementalParseForest parseForest : parseForests) {
            if(parseForest != null) {
                parseForest.prettyPrint(printer);
                printer.println("");
            } else
                printer.println("null");
        }

        printer.indent(-2);
        printer.print("}");
    }


}
