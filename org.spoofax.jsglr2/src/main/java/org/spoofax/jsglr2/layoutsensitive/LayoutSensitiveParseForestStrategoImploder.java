package org.spoofax.jsglr2.layoutsensitive;

import java.util.List;

import org.metaborg.parsetable.IProduction;
import org.spoofax.jsglr2.imploder.StrategoTermImploder;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForest;

public class LayoutSensitiveParseForestStrategoImploder
    extends StrategoTermImploder<BasicParseForest, LayoutSensitiveParseNode, LayoutSensitiveDerivation> {

    public LayoutSensitiveParseForestStrategoImploder() {
        super(new LayoutSensitiveParseForestTokenizer());
    }

    @Override protected IProduction parseNodeProduction(LayoutSensitiveParseNode parseNode) {
        return parseNode.production;
    }

    @Override protected LayoutSensitiveDerivation parseNodeOnlyDerivation(LayoutSensitiveParseNode parseNode) {
        return parseNode.getOnlyDerivation();
    }

    @Override protected List<LayoutSensitiveDerivation>
        parseNodePreferredAvoidedDerivations(LayoutSensitiveParseNode parseNode) {
        return parseNode.getPreferredAvoidedDerivations();
    }

    @Override protected List<LayoutSensitiveDerivation>
        longestMatchedDerivations(List<LayoutSensitiveDerivation> derivations) {

        return derivations;
    }
}
