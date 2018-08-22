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

    @Override public BasicParseForest filterStartSymbol(BasicParseForest parseForest, String startSymbol, Parse<BasicParseForest, ?> parse) {
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
            if(pf instanceof LayoutSensitiveSymbolNode) {
                Position currentStartPosition = ((LayoutSensitiveSymbolNode) pf).getOnlyDerivation().startPosition;
                Position currentLeftPosition = ((LayoutSensitiveSymbolNode) pf).getOnlyDerivation().leftPosition;
                Position currentRightPosition = ((LayoutSensitiveSymbolNode) pf).getOnlyDerivation().rightPosition;
                Position currentEndPosition = ((LayoutSensitiveSymbolNode) pf).getOnlyDerivation().endPosition;

                if(currentLeftPosition != null) {
                    leftPosition = leftMost(leftPosition, currentLeftPosition);
                }
                
                if(currentStartPosition.line > beginPosition.line && !currentStartPosition.equals(currentEndPosition)) {
                    leftPosition = leftMost(leftPosition, currentStartPosition);
                }
                
                if(currentRightPosition != null) {
                    rightPosition = rightMost(rightPosition, currentRightPosition);
                }
                
                if(currentEndPosition.line < parse.currentPosition().line && !currentStartPosition.equals(currentEndPosition)) {
                    rightPosition = rightMost(rightPosition, currentEndPosition);
                }
                
                
//                if(currentLeftPosition != null) {
//                    if(leftPosition == null) {
//                        leftPosition = currentLeftPosition;
//                    } else if(leftPosition.column > currentLeftPosition.column
//                        && beginPosition.line < currentLeftPosition.line) {
//                        leftPosition = currentLeftPosition;
//                    }
//                }
//
//                if(currentStartPosition != null) {
//                    if(leftPosition == null && currentStartPosition.line > beginPosition.line
//                        ) {
//                        leftPosition = currentStartPosition;
//                    } else if(leftPosition != null && currentStartPosition.line > beginPosition.line
//                        && currentStartPosition.column < leftPosition.column
//                        && !currentStartPosition.equals(currentEndPosition)) {
//                        leftPosition = currentStartPosition;
//                    }
//                }
//
//                if(rightPosition == null && currentRightPosition != null) {
//                    rightPosition = currentRightPosition;
//                }
//                if(currentRightPosition != null && (rightPosition.column < currentRightPosition.column
//                    && parse.currentPosition().line > currentRightPosition.line)) {
//                    rightPosition = currentRightPosition;
//                }
//                if(currentEndPosition != null && (currentEndPosition.line < parse.currentPosition().line
//                    && currentEndPosition.column > parse.currentPosition().column)) {
//                    rightPosition = currentEndPosition;
//                }


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

    private Position rightMost(Position p1, Position p2) {
        if(p1 == null) {
            return p2;
        }
        
        if(p1.column < p2.column) {
            return p2;
        } 
        
        return p1;
    }

    private Position leftMost(Position p1, Position p2) {
        if(p1 == null) {
            return p2;
        }
        
        if(p1.column > p2.column) {
            return p2;
        } 
        
        return p1;
    }

    @Override public void addDerivation(Parse<BasicParseForest, ?> parse, LayoutSensitiveSymbolNode symbolNode,
        LayoutSensitiveRuleNode ruleNode) {
        // parse.notify(observer -> observer.addDerivation(symbolNode));

        boolean initNonAmbiguous = symbolNode.isAmbiguous();

        symbolNode.addDerivation(ruleNode);

        if(initNonAmbiguous && symbolNode.isAmbiguous())
            parse.ambiguousParseNodes++;
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
//                    if(currentPosition != null && !rn.startPosition.equals(currentPosition)) {
//                        System.err.println("StartPosition is different for trees that are part of an ambiguity.");
//                    }
                    if(rn.startPosition != null) {
                        currentPosition = new Position(rn.startPosition);
                    }
                    break;
                case LAST:
//                    if(currentPosition != null && !rn.endPosition.equals(currentPosition)) {
//                        System.err.println("EndPosition is different for trees that are part of an ambiguity.");
//                    }
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
