package org.spoofax.jsglr2.imploder;

import java.util.List;

import org.spoofax.jsglr2.parseforest.basic.RuleNode;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForest;
import org.spoofax.jsglr2.parseforest.basic.SymbolNode;
import org.spoofax.jsglr2.parsetable.IProduction;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.tokenizer.BasicParseForestTokenizer;

public class BasicParseForestStrategoImploder<StackNode extends AbstractStackNode<BasicParseForest>>
    extends StrategoTermImploder<StackNode, BasicParseForest, SymbolNode, RuleNode> {

    public BasicParseForestStrategoImploder() {
        super(new BasicParseForestTokenizer());
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
