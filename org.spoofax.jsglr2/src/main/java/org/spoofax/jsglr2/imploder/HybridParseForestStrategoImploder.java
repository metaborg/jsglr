package org.spoofax.jsglr2.imploder;

import java.util.List;

import org.metaborg.parsetable.IProduction;
import org.spoofax.jsglr2.parseforest.hybrid.Derivation;
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseForest;
import org.spoofax.jsglr2.parseforest.hybrid.ParseNode;
import org.spoofax.jsglr2.tokenizer.HybridParseForestTokenizer;

public class HybridParseForestStrategoImploder extends StrategoTermImploder<HybridParseForest, ParseNode, Derivation> {

    public HybridParseForestStrategoImploder() {
        super(new HybridParseForestTokenizer());
    }

    @Override
    protected IProduction parseNodeProduction(ParseNode parseNode) {
        return parseNode.production;
    }

    @Override
    protected Derivation parseNodeOnlyDerivation(ParseNode parseNode) {
        return parseNode.getOnlyDerivation();
    }

    @Override
    protected List<Derivation> parseNodePreferredAvoidedDerivations(ParseNode parseNode) {
        return parseNode.getPreferredAvoidedDerivations();
    }

}
