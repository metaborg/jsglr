package org.spoofax.jsglr2.imploder;

import java.util.List;

import org.metaborg.parsetable.IProduction;
import org.spoofax.jsglr2.parseforest.basic.BasicDerivation;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForest;
import org.spoofax.jsglr2.parseforest.basic.BasicParseNode;
import org.spoofax.jsglr2.tokenizer.BasicParseForestTokenizer;

public class BasicParseForestStrategoImploder
    extends StrategoTermImploder<BasicParseForest, BasicParseNode, BasicDerivation> {

    public BasicParseForestStrategoImploder() {
        super(new BasicParseForestTokenizer());
    }

    @Override protected IProduction parseNodeProduction(BasicParseNode parseNode) {
        return parseNode.production;
    }

    @Override protected BasicDerivation parseNodeOnlyDerivation(BasicParseNode parseNode) {
        return parseNode.getOnlyDerivation();
    }

    @Override protected List<BasicDerivation> parseNodePreferredAvoidedDerivations(BasicParseNode parseNode) {
        return parseNode.getPreferredAvoidedDerivations();
    }

    @Override protected List<BasicDerivation> longestMatchedDerivations(List<BasicDerivation> derivations) {
        // TODO remove derivations according to longest match criteria
        return derivations;
    }

}
