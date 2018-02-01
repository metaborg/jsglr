package org.spoofax.jsglr2.layoutsensitive;

import java.util.List;

import org.metaborg.parsetable.IProduction;
import org.spoofax.jsglr2.imploder.StrategoTermImploder;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForest;

public class LayoutSensitiveParseForestStrategoImploder extends StrategoTermImploder<BasicParseForest, LayoutSensitiveSymbolNode, LayoutSensitiveRuleNode> {

    public LayoutSensitiveParseForestStrategoImploder() {
        super(new LayoutSensitiveParseForestTokenizer());
    }

    @Override
    protected IProduction parseNodeProduction(LayoutSensitiveSymbolNode symbolNode) {
        return symbolNode.production;
    }

    @Override
    protected LayoutSensitiveRuleNode parseNodeOnlyDerivation(LayoutSensitiveSymbolNode symbolNode) {
        return symbolNode.getOnlyDerivation();
    }

    @Override
    protected List<LayoutSensitiveRuleNode> parseNodePreferredAvoidedDerivations(LayoutSensitiveSymbolNode symbolNode) {
        return symbolNode.getPreferredAvoidedDerivations();
    }

}
