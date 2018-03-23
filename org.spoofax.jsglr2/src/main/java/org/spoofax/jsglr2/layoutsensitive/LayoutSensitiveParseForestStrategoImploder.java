package org.spoofax.jsglr2.layoutsensitive;

import java.util.List;

import org.metaborg.parsetable.IProduction;
import org.spoofax.jsglr2.imploder.StrategoTermImploder;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForest;

import com.google.common.collect.Lists;

public class LayoutSensitiveParseForestStrategoImploder
    extends StrategoTermImploder<BasicParseForest, LayoutSensitiveSymbolNode, LayoutSensitiveRuleNode> {

    public LayoutSensitiveParseForestStrategoImploder() {
        super(new LayoutSensitiveParseForestTokenizer());
    }

    @Override protected IProduction parseNodeProduction(LayoutSensitiveSymbolNode symbolNode) {
        return symbolNode.production;
    }

    @Override protected LayoutSensitiveRuleNode parseNodeOnlyDerivation(LayoutSensitiveSymbolNode symbolNode) {
        return symbolNode.getOnlyDerivation();
    }

    @Override protected List<LayoutSensitiveRuleNode>
        parseNodePreferredAvoidedDerivations(LayoutSensitiveSymbolNode symbolNode) {
        return symbolNode.getPreferredAvoidedDerivations();
    }

    @Override protected List<LayoutSensitiveRuleNode>
        longestMatchedDerivations(List<LayoutSensitiveRuleNode> derivations) {
        // TODO remove derivations according to longest match criteria
        // get the derivations where longest match node expands the most



        List<LayoutSensitiveRuleNode> longestMatchDerivations = Lists.newArrayList();
        // collect longestMatch nodes

        List<List<LayoutSensitiveRuleNode>> longestMatchNodes = Lists.newArrayList();

        for(LayoutSensitiveRuleNode rn : derivations) {
            List<LayoutSensitiveRuleNode> currentLongestMatchNodes = collectLongestMatchNodes(rn);
            longestMatchNodes.add(currentLongestMatchNodes);
        }

        int size = -1;

        int currentLongestDerivation = 0;
        derivations.toString();
        boolean disambiguatedLongestMatch = false;

        for(int i = 1; i < longestMatchNodes.size(); i++) {
            List<LayoutSensitiveRuleNode> list = longestMatchNodes.get(i);

            // FIXME: list of longest-match nodes should be the same?
            if(size == -1) {
                size = list.size();
            } else if(size != list.size()) {
                System.out.println("Number of longest match nodes differ");
            }

            for(int j = 0; j < list.size(); j++) {
                Boolean secondNodeExpandsLonger =
                    expandsLonger(longestMatchNodes.get(currentLongestDerivation).get(j), list.get(j));
                if(secondNodeExpandsLonger == null) {
                    continue;
                } else if(secondNodeExpandsLonger) {
                    currentLongestDerivation = i;
                    disambiguatedLongestMatch = true;
                } else {
                    disambiguatedLongestMatch = true;
                }
            }            
        }

        if(disambiguatedLongestMatch) {
            return Lists.newArrayList(derivations.get(currentLongestDerivation));
        }

        return derivations;
    }

    // whether node2 expands longer than node1
    private Boolean expandsLonger(LayoutSensitiveRuleNode node1, LayoutSensitiveRuleNode node2) {
        assert (node1.startPosition.equals(node2.startPosition));

        if(node2.endPosition.line > node1.endPosition.line || (node2.endPosition.line == node1.endPosition.line
            && node2.endPosition.column > node1.endPosition.column)) {
            return true;
        } else if(node1.endPosition.line > node2.endPosition.line || (node1.endPosition.line == node2.endPosition.line
            && node1.endPosition.column > node2.endPosition.column)) {
            return false;
        }

        return null;
    }

    private List<LayoutSensitiveRuleNode> collectLongestMatchNodes(LayoutSensitiveRuleNode rn) {
        List<LayoutSensitiveRuleNode> result = Lists.newArrayList();

        if(rn.production.isLongestMatch()) {
            result.add(rn);
        }

        for(BasicParseForest pf : rn.parseForests) {
            if(pf instanceof LayoutSensitiveSymbolNode) {
                result.addAll(collectLongestMatchNodes(((LayoutSensitiveSymbolNode) pf).getOnlyDerivation()));
            } else if(pf instanceof LayoutSensitiveRuleNode) {
                result.addAll(collectLongestMatchNodes((LayoutSensitiveRuleNode) pf));
            }
        }


        return result;
    }



}
