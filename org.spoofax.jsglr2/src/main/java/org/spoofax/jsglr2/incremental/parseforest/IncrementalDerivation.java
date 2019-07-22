package org.spoofax.jsglr2.incremental.parseforest;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.states.IState;
import org.metaborg.parsetable.productions.ProductionType;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.util.TreePrettyPrinter;

public class IncrementalDerivation implements IDerivation<IncrementalParseForest> {
    private final int width;
    private final IProduction production;
    private final ProductionType productionType;
    public final IncrementalParseForest[] parseForests;
    public final IState state;

    public IncrementalDerivation(IProduction production, ProductionType productionType,
        IncrementalParseForest[] parseForests, IState state) {

        this.production = production;
        this.productionType = productionType;
        this.parseForests = parseForests;
        this.state = state;
        int width = 0;
        for(IncrementalParseForest parseForest : parseForests) {
            if(parseForest == null)
                continue; // Skippable parse nodes are null
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
        if(production == null)
            printer.println("d null : {");
        else
            printer.println("d" + production.id() + " : " + production.toString() + " { (s" + state.id() + ")");
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
