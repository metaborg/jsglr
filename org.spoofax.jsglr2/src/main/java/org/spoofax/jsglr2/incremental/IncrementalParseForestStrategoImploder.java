package org.spoofax.jsglr2.incremental;

import java.util.List;

import org.metaborg.parsetable.IProduction;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr2.imploder.StrategoTermImploder;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalDerivation;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseNode;
import org.spoofax.jsglr2.parser.AbstractParse;

public class IncrementalParseForestStrategoImploder
    extends StrategoTermImploder<IncrementalParseForest, IncrementalParseNode, IncrementalDerivation> {

    public IncrementalParseForestStrategoImploder() {
        super(new IncrementalParseForestTokenizer());
    }

    @Override protected IProduction parseNodeProduction(IncrementalParseNode parseNode) {
        return parseNode.production;
    }

    @Override protected IncrementalDerivation parseNodeOnlyDerivation(IncrementalParseNode parseNode) {
        return parseNode.getOnlyDerivation();
    }

    @Override protected List<IncrementalDerivation>
        parseNodePreferredAvoidedDerivations(IncrementalParseNode parseNode) {
        return parseNode.getPreferredAvoidedDerivations();
    }

    @Override protected List<IncrementalDerivation> longestMatchedDerivations(List<IncrementalDerivation> derivations) {
        // TODO remove derivations according to longest match criteria
        return derivations;
    }

    @Override protected IStrategoTerm implodeParseNode(AbstractParse<IncrementalParseForest, ?> parse,
        IncrementalParseNode parseNode, IToken leftToken, IToken rightToken) {
        IProduction production = parseNodeProduction(parseNode);
        // Only override the third if-branch of the superclass to use parseNode.getSource() instead of parse.getPart()
        if(!production.isContextFree() && !production.isLayout() && !production.isLiteral()
            && (production.isLexical() || production.isLexicalRhs()))
            return createLexicalTerm(production, parseNode.getSource(), leftToken, parseNode.token);
        return super.implodeParseNode(parse, parseNode, leftToken, rightToken);
    }
}
