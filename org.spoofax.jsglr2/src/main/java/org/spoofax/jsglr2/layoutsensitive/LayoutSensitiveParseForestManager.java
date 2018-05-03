package org.spoofax.jsglr2.layoutsensitive;

import java.util.ArrayList;
import java.util.List;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.ProductionType;
import org.metaborg.sdf2table.grammar.layoutconstraints.ConstraintSelector;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForest;
import org.spoofax.jsglr2.parseforest.basic.TermNode;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Position;

public class LayoutSensitiveParseForestManager
    extends ParseForestManager<BasicParseForest, LayoutSensitiveSymbolNode, LayoutSensitiveRuleNode> {

    @Override public LayoutSensitiveSymbolNode createParseNode(Parse<BasicParseForest, ?> parse, Position beginPosition,
        IProduction production, LayoutSensitiveRuleNode firstDerivation) {
        LayoutSensitiveSymbolNode symbolNode = new LayoutSensitiveSymbolNode(parse.parseNodeCount++, parse,
            beginPosition, parse.currentPosition(), production);

        // parse.notify(observer -> observer.createParseNode(symbolNode, production));

        addDerivation(parse, symbolNode, firstDerivation);

        return symbolNode;
    }

    @Override public BasicParseForest filterStartSymbol(BasicParseForest parseForest, String startSymbol) {
        LayoutSensitiveSymbolNode topNode = (LayoutSensitiveSymbolNode) parseForest;
        List<LayoutSensitiveRuleNode> result = new ArrayList<LayoutSensitiveRuleNode>();

        for(LayoutSensitiveRuleNode derivation : topNode.getDerivations()) {
            String derivationStartSymbol = derivation.production.startSymbolSort();

            if(derivationStartSymbol != null && derivationStartSymbol.equals(startSymbol))
                result.add(derivation);
        }

        if(result.isEmpty())
            return null;
        else {
            LayoutSensitiveSymbolNode filteredTopNode = new LayoutSensitiveSymbolNode(topNode.nodeNumber, topNode.parse,
                topNode.startPosition, topNode.endPosition, topNode.production);

            for(LayoutSensitiveRuleNode derivation : result)
                filteredTopNode.addDerivation(derivation);

            return filteredTopNode;
        }
    }

    @Override public LayoutSensitiveRuleNode createDerivation(Parse<BasicParseForest, ?> parse, Position beginPosition,
        IProduction production, ProductionType productionType, BasicParseForest[] parseForests) {

        // FIXME since EndPosition is wrong, right is also wrong
        Position leftPosition = null;
        Position rightPosition = null;

        for(BasicParseForest pf : parseForests) {
            if(pf instanceof LayoutSensitiveSymbolNode && (((LayoutSensitiveSymbolNode) pf).getProduction().isLayout()
                || ((LayoutSensitiveSymbolNode) pf).getProduction().isIgnoreLayoutConstraint())) {
                continue;
            }
            if(pf instanceof LayoutSensitiveSymbolNode
                && !((LayoutSensitiveSymbolNode) pf).getProduction().isLayout()) {
                Position currentStartPosition = verifyPositionAmbiguities(ConstraintSelector.FIRST, pf);
                Position currentLeftPosition = verifyPositionAmbiguities(ConstraintSelector.LEFT, pf);
                Position currentRightPosition = verifyPositionAmbiguities(ConstraintSelector.RIGHT, pf);
                Position currentEndPosition = verifyPositionAmbiguities(ConstraintSelector.LAST, pf);

                if(currentLeftPosition != null) {
                    if(leftPosition == null) {
                        leftPosition = currentLeftPosition;
                    } else if(leftPosition.column > currentLeftPosition.column
                        && beginPosition.line < currentLeftPosition.line) {
                        leftPosition = currentLeftPosition;
                    }
                }

                if(currentStartPosition != null) {
                    if(leftPosition == null && currentStartPosition.line > beginPosition.line
                        && !currentStartPosition.equals(currentEndPosition)) {
                        leftPosition = currentStartPosition;
                    } else if(leftPosition != null && currentStartPosition.line > beginPosition.line
                        && currentStartPosition.column < leftPosition.column
                        && !currentStartPosition.equals(currentEndPosition)) {
                        leftPosition = currentStartPosition;
                    }
                }

                if(rightPosition == null && currentRightPosition != null) {
                    rightPosition = currentRightPosition;
                }
                if(currentRightPosition != null && (rightPosition.column < currentRightPosition.column
                    && parse.currentPosition().line > currentRightPosition.line)) {
                    rightPosition = currentRightPosition;
                }
                if(currentEndPosition != null && (currentEndPosition.line < parse.currentPosition().line
                    && currentEndPosition.column > parse.currentPosition().column)) {
                    rightPosition = currentEndPosition;
                }


            } else if(pf instanceof TermNode) {
                if(pf.startPosition.line > beginPosition.line && pf.startPosition.column < beginPosition.column) {
                    leftPosition =
                        new Position(pf.startPosition.offset, pf.startPosition.line, pf.startPosition.column);
                }
                if(pf.endPosition.line < parse.currentPosition().line
                    && pf.endPosition.column > parse.currentPosition().column) {
                    rightPosition = new Position(pf.endPosition.offset, pf.endPosition.line, pf.endPosition.column);
                }
            } else if(pf != null) {
                System.err.println("Not a valid tree node.");
            }
        }

        LayoutSensitiveRuleNode ruleNode = new LayoutSensitiveRuleNode(parse.parseNodeCount++, parse, beginPosition,
            leftPosition, rightPosition, parse.currentPosition(), production, productionType, parseForests);

        // parse.notify(observer -> observer.createDerivation(ruleNode.nodeNumber, production, parseForests));

        return ruleNode;
    }

    @Override public void addDerivation(Parse<BasicParseForest, ?> parse, LayoutSensitiveSymbolNode symbolNode,
        LayoutSensitiveRuleNode ruleNode) {
        // parse.notify(observer -> observer.addDerivation(symbolNode));

        boolean initNonAmbiguous = symbolNode.isAmbiguous();

        // TODO compare with other derivations and filter nodes according to longest-match nodes
        int size = -1;
        symbolNode.addDerivation(ruleNode);


        int currentLongestDerivation = 0;
        boolean disambiguatedLongestMatch = false;

        for(int i = 1; i < symbolNode.getDerivations().size(); i++) {
            List<Position[]> list = symbolNode.getDerivations().get(i).longestMatchPos;

            // FIXME: list of longest-match nodes should be the same?
            if(size == -1) {
                size = list.size();
            } else if(size != list.size()) {
                System.out.println("Number of longest match nodes differ");
            }

            for(int j = 0; j < list.size(); j++) {
                Boolean secondNodeExpandsLonger = null;
                if(symbolNode.getDerivations().get(currentLongestDerivation).longestMatchPos.size() > j) {
                    secondNodeExpandsLonger = expandsLonger(
                        symbolNode.getDerivations().get(currentLongestDerivation).longestMatchPos.get(j), list.get(j));
                }
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
            LayoutSensitiveRuleNode disambiguated = symbolNode.getDerivations().get(currentLongestDerivation);
            symbolNode.getDerivations().clear();
            symbolNode.addDerivation(disambiguated);
        }

        if(initNonAmbiguous && symbolNode.isAmbiguous())
            parse.ambiguousParseNodes++;
    }

    private Boolean expandsLonger(Position[] pos1, Position[] pos2) {
        assert (pos1[0].equals(pos2[0]));

        if(pos2[1].line > pos1[1].line || (pos2[1].line == pos1[1].line && pos2[1].column > pos1[1].column)) {
            return true;
        } else if(pos1[1].line > pos2[1].line || (pos1[1].line == pos2[1].line && pos1[1].column > pos2[1].column)) {
            return false;
        }

        return null;
    }

    @Override public TermNode createCharacterNode(Parse<BasicParseForest, ?> parse) {
        TermNode termNode = new TermNode(parse.parseNodeCount++, parse, parse.currentPosition(), parse.currentChar);

        // parse.notify(observer -> observer.createCharacterNode(termNode, termNode.character));

        return termNode;
    }

    @Override public BasicParseForest[] parseForestsArray(int length) {
        return new BasicParseForest[length];
    }

    private Position verifyPositionAmbiguities(ConstraintSelector selector, BasicParseForest pf) {
        // For trees in an ambiguity such that left and right are different, take the one with smallest and largest
        // column, respectively
        Position currentPosition = null;
        for(LayoutSensitiveRuleNode rn : ((LayoutSensitiveSymbolNode) pf).getDerivations()) {
            switch(selector) {
                case FIRST:
                    if(currentPosition != null && !rn.startPosition.equals(currentPosition)) {
                        System.err.println("StartPosition is different for trees that are part of an ambiguity.");
                    }
                    if(rn.startPosition != null) {
                        currentPosition = new Position(rn.startPosition);
                    }
                    break;
                case LAST:
                    if(currentPosition != null && !rn.endPosition.equals(currentPosition)) {
                        System.err.println("EndPosition is different for trees that are part of an ambiguity.");
                    }
                    if(rn.endPosition != null) {
                        currentPosition = new Position(rn.endPosition);
                    }
                    break;
                case RIGHT:
                    if(rn.rightPosition != null) {
                        if(currentPosition != null && !rn.rightPosition.equals(currentPosition)) {
                            if(rn.rightPosition.column > currentPosition.column) {
                                currentPosition = new Position(rn.rightPosition);
                            }
                        } else {
                            currentPosition = new Position(rn.rightPosition);
                        }
                    }
                    break;
                case LEFT:
                    if(rn.leftPosition != null) {
                        if(currentPosition != null && !rn.leftPosition.equals(currentPosition)) {
                            if(rn.leftPosition.column < currentPosition.column) {
                                currentPosition = new Position(rn.leftPosition);
                            }
                        } else {
                            currentPosition = new Position(rn.leftPosition);
                        }

                    }
                    break;
            }
        }

        return currentPosition;
    }

}
