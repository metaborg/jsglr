package org.spoofax.jsglr2.imploder;

import java.util.List;

import org.spoofax.jsglr2.parseforest.hybrid.Derivation;
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseForest;
import org.spoofax.jsglr2.parseforest.hybrid.ParseNode;
import org.spoofax.jsglr2.parsetable.IProduction;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.tokenizer.HybridParseForestTokenizer;

public class HybridParseForestStrategoImploder<StackNode extends AbstractStackNode<HybridParseForest>>
    extends StrategoTermImploder<HybridParseForest, ParseNode, Derivation, StackNode> {

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
