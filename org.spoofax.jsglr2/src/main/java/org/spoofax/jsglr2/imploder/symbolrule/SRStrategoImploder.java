package org.spoofax.jsglr2.imploder.symbolrule;

import java.util.List;

import org.spoofax.jsglr2.imploder.StrategoTermImploder;
import org.spoofax.jsglr2.parseforest.symbolrule.RuleNode;
import org.spoofax.jsglr2.parseforest.symbolrule.SRParseForest;
import org.spoofax.jsglr2.parseforest.symbolrule.SymbolNode;
import org.spoofax.jsglr2.parsetable.IProduction;
import org.spoofax.jsglr2.tokenizer.symbolrule.SRTokenizer;

public class SRStrategoImploder extends StrategoTermImploder<SRParseForest, SymbolNode, RuleNode> {
	
    public SRStrategoImploder() {
        super(new SRTokenizer());
    }

    protected IProduction parseNodeProduction(SymbolNode symbolNode) {
        return symbolNode.production;
    }

    protected RuleNode parseNodeOnlyDerivation(SymbolNode symbolNode) {
        return symbolNode.getOnlyDerivation();
    }

    protected List<RuleNode> parseNodePreferredAvoidedDerivations(SymbolNode symbolNode) {
        return symbolNode.getPreferredAvoidedDerivations();
    }

}
