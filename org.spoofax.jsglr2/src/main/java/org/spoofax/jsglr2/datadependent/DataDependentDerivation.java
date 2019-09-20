package org.spoofax.jsglr2.datadependent;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.productions.ProductionType;
import org.metaborg.sdf2table.parsetable.ParseTableProduction;
import org.spoofax.jsglr2.parseforest.basic.BasicDerivation;

public class DataDependentDerivation
//@formatter:off
   <ParseForest extends IDataDependentParseForest>
//@formatter:on
    extends BasicDerivation<ParseForest> implements IDataDependentDerivation<ParseForest> {

    private long contextBitmap = 0L;

    public DataDependentDerivation(IProduction production, ProductionType productionType, ParseForest[] parseForests) {
        super(production, productionType, parseForests);

        if(parseForests.length > 0) {
            if(parseForests.length == 1) {
                if(parseForests[0] instanceof IDataDependentParseNode) {
                    final IDataDependentParseNode<ParseForest, IDataDependentDerivation<ParseForest>> parseNode =
                        (IDataDependentParseNode<ParseForest, IDataDependentDerivation<ParseForest>>) parseForests[0];
                    final ParseTableProduction onlyProduction = (ParseTableProduction) parseNode.production();

                    // introduction of contextual token
                    contextBitmap |= onlyProduction.contextL();
                    contextBitmap |= onlyProduction.contextR();

                    // aggregation of recursive contextual tokens
                    for(IDataDependentDerivation<ParseForest> derivation : parseNode.getDerivations()) {
                        contextBitmap |= (derivation.getContextBitmap());
                    }
                }
            } else {
                if(parseForests[0] instanceof IDataDependentParseNode) {
                    final IDataDependentParseNode<ParseForest, IDataDependentDerivation<ParseForest>> parseForest =
                        (IDataDependentParseNode<ParseForest, IDataDependentDerivation<ParseForest>>) parseForests[0];
                    final ParseTableProduction leftmostProduction = (ParseTableProduction) parseForest.production();

                    // introduction of contextual token
                    contextBitmap |= leftmostProduction.contextL();

                    // aggregation of recursive contextual tokens
                    for(IDataDependentDerivation<ParseForest> derivation : parseForest.getDerivations()) {
                        contextBitmap |= (derivation.getContextBitmap());
                    }
                }

                if(parseForests[parseForests.length - 1] instanceof IDataDependentParseNode) {
                    final IDataDependentParseNode<ParseForest, IDataDependentDerivation<ParseForest>> parseForest =
                        (IDataDependentParseNode<ParseForest, IDataDependentDerivation<ParseForest>>) parseForests[parseForests.length
                            - 1];
                    final ParseTableProduction rightmostProduction = (ParseTableProduction) parseForest.production();

                    // introduction of contextual token
                    contextBitmap |= rightmostProduction.contextR();

                    // aggregation of recursive contextual tokens
                    for(IDataDependentDerivation<ParseForest> derivation : parseForest.getDerivations()) {
                        contextBitmap |= (derivation.getContextBitmap());
                    }
                }

            }
        }
    }

    public final long getContextBitmap() {
        return contextBitmap;
    }

}
