package org.spoofax.jsglr2.imploder.hybrid;

import java.util.List;

import org.spoofax.jsglr2.imploder.StrategoTermImploder;
import org.spoofax.jsglr2.parseforest.hybrid.Derivation;
import org.spoofax.jsglr2.parseforest.hybrid.HParseForest;
import org.spoofax.jsglr2.parseforest.hybrid.ParseNode;
import org.spoofax.jsglr2.parsetable.IProduction;
import org.spoofax.jsglr2.tokenizer.hybrid.HTokenizer;

public class HStrategoImploder extends StrategoTermImploder<HParseForest, ParseNode, Derivation> {
	
    public HStrategoImploder() {
        super(new HTokenizer());
    }

    protected IProduction parseNodeProduction(ParseNode parseNode) {
        return parseNode.production;
    }

    protected Derivation parseNodeOnlyDerivation(ParseNode parseNode) {
        return parseNode.getOnlyDerivation();
    }

    protected List<Derivation> parseNodePreferredAvoidedDerivations(ParseNode parseNode) {
        return parseNode.getPreferredAvoidedDerivations();
    }

}
