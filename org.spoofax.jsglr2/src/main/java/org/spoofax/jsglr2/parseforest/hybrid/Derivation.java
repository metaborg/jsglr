package org.spoofax.jsglr2.parseforest.hybrid;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.ProductionType;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.util.TreePrettyPrinter;

public class Derivation implements IDerivation<HybridParseForest> {

    public final IProduction production;
    public final ProductionType productionType;
    public final HybridParseForest[] parseForests;

    public Derivation(IProduction production, ProductionType productionType, HybridParseForest[] parseForests) {
        this.production = production;
        this.productionType = productionType;
        this.parseForests = parseForests;
    }

    @Override public IProduction production() {
        return production;
    }

    @Override public ProductionType productionType() {
        return productionType;
    }

    @Override public HybridParseForest[] parseForests() {
        return parseForests;
    }

    protected void prettyPrint(TreePrettyPrinter printer) {
        printer.println("p" + production.id() + " : " + production.descriptor() + "{");
        printer.indent(2);

        for(HybridParseForest parseForest : parseForests) {
            if(parseForest != null)
                parseForest.prettyPrint(printer);
            else
                printer.println("null");
        }

        printer.indent(-2);
        printer.println("}");
    }

}
