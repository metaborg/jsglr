package org.spoofax.jsglr2.datadependent;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.ProductionType;
import org.metaborg.sdf2table.parsetable.ParseTableProduction;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.Position;

public class DataDependentRuleNode extends BasicParseForest implements IDerivation<BasicParseForest> {

    public final IProduction production;
    public final ProductionType productionType;
    public final BasicParseForest[] parseForests;

    private long contextBitmap = 0L;

    public DataDependentRuleNode(Position startPosition, Position endPosition,
        IProduction production, ProductionType productionType, BasicParseForest[] parseForests) {
        super(startPosition, endPosition);
        this.production = production;
        this.productionType = productionType;
        this.parseForests = parseForests;

        if (parseForests.length > 0) {
            if (parseForests.length == 1) {
                if (parseForests[0] instanceof DataDependentSymbolNode) {
                    final DataDependentSymbolNode parseForest = (DataDependentSymbolNode) parseForests[0];
                    final ParseTableProduction onlyProduction = (ParseTableProduction) parseForest.production;

                    // introduction of contextual token
                    contextBitmap |= onlyProduction.contextL();
                    contextBitmap |= onlyProduction.contextR();

                    // aggregation of recursive contextual tokens
                    for (DataDependentRuleNode ruleNode : parseForest.getDerivations()) {
                        contextBitmap |= (ruleNode.getContextBitmap());
                    }
                }
            } else {
                if (parseForests[0] instanceof DataDependentSymbolNode) {
                    final DataDependentSymbolNode parseForest = (DataDependentSymbolNode) parseForests[0];
                    final ParseTableProduction leftmostProduction = (ParseTableProduction) parseForest.production;

                    // introduction of contextual token
                    contextBitmap |= leftmostProduction.contextL();

                    // aggregation of recursive contextual tokens
                    for (DataDependentRuleNode ruleNode : parseForest.getDerivations()) {
                        contextBitmap |= (ruleNode.getContextBitmap());
                    }
                }

                if (parseForests[parseForests.length - 1] instanceof DataDependentSymbolNode) {
                    final DataDependentSymbolNode parseForest = (DataDependentSymbolNode) parseForests[parseForests.length - 1];
                    final ParseTableProduction rightmostProduction = (ParseTableProduction) parseForest.production;

                    // introduction of contextual token
                    contextBitmap |= rightmostProduction.contextR();

                    // aggregation of recursive contextual tokens
                    for (DataDependentRuleNode ruleNode : parseForest.getDerivations()) {
                        contextBitmap |= (ruleNode.getContextBitmap());
                    }
                }

            }
        }
    }

    public String descriptor() {
        return production.descriptor();
    }

    public IProduction production() {
        return production;
    }

    public ProductionType productionType() {
        return productionType;
    }

    public BasicParseForest[] parseForests() {
        return parseForests;
    }

    public final long getContextBitmap() {
        return contextBitmap;
    }

}
