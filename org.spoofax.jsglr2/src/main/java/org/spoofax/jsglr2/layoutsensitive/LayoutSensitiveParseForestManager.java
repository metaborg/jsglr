package org.spoofax.jsglr2.layoutsensitive;

import java.util.ArrayList;
import java.util.List;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.ProductionType;
import org.metaborg.sdf2table.grammar.layoutconstraints.ConstraintSelector;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.Position;

public class LayoutSensitiveParseForestManager
    extends ParseForestManager<LayoutSensitiveParseForest, LayoutSensitiveParseNode, LayoutSensitiveDerivation> {

    @Override public LayoutSensitiveParseNode createParseNode(AbstractParse<LayoutSensitiveParseForest, ?> parse,
        Position beginPosition, IProduction production, LayoutSensitiveDerivation firstDerivation) {
        LayoutSensitiveParseNode parseNode =
            new LayoutSensitiveParseNode(beginPosition, parse.currentPosition(), production);

        // parse.notify(observer -> observer.createParseNode(parseNode, production));

        addDerivation(parse, parseNode, firstDerivation);

        return parseNode;
    }

    @Override public LayoutSensitiveParseForest filterStartSymbol(LayoutSensitiveParseForest parseForest,
        String startSymbol, AbstractParse<LayoutSensitiveParseForest, ?> parse) {
        LayoutSensitiveParseNode topNode = (LayoutSensitiveParseNode) parseForest;
        List<LayoutSensitiveDerivation> result = new ArrayList<LayoutSensitiveDerivation>();

        for(LayoutSensitiveDerivation derivation : topNode.getDerivations()) {
            String derivationStartSymbol = derivation.production.startSymbolSort();

            if(derivationStartSymbol != null && derivationStartSymbol.equals(startSymbol))
                result.add(derivation);
        }

        if(result.isEmpty())
            return null;
        else {
            LayoutSensitiveParseNode filteredTopNode =
                new LayoutSensitiveParseNode(topNode.getStartPosition(), topNode.getEndPosition(), topNode.production);

            for(LayoutSensitiveDerivation derivation : result)
                filteredTopNode.addDerivation(derivation);

            return filteredTopNode;
        }
    }

    @Override public LayoutSensitiveDerivation createDerivation(AbstractParse<LayoutSensitiveParseForest, ?> parse,
        Position beginPosition, IProduction production, ProductionType productionType,
        LayoutSensitiveParseForest[] parseForests) {

        // FIXME since EndPosition is wrong, right is also wrong
        Position leftPosition = null;
        Position rightPosition = null;

        for(LayoutSensitiveParseForest pf : parseForests) {
            if(pf instanceof LayoutSensitiveParseNode && (((LayoutSensitiveParseNode) pf).production().isLayout()
                || ((LayoutSensitiveParseNode) pf).production().isIgnoreLayoutConstraint())) {
                continue;
            }
            if(pf instanceof LayoutSensitiveParseNode) {
                Position currentStartPosition = ((LayoutSensitiveParseNode) pf).getOnlyDerivation().getStartPosition();
                Position currentLeftPosition = ((LayoutSensitiveParseNode) pf).getOnlyDerivation().leftPosition;
                Position currentRightPosition = ((LayoutSensitiveParseNode) pf).getOnlyDerivation().rightPosition;
                Position currentEndPosition = ((LayoutSensitiveParseNode) pf).getOnlyDerivation().getEndPosition();

                if(currentLeftPosition != null) {
                    leftPosition = leftMost(leftPosition, currentLeftPosition);
                }

                if(currentStartPosition.line > beginPosition.line && !currentStartPosition.equals(currentEndPosition)) {
                    leftPosition = leftMost(leftPosition, currentStartPosition);
                }

                if(currentRightPosition != null) {
                    rightPosition = rightMost(rightPosition, currentRightPosition);
                }

                if(currentEndPosition.line < parse.currentPosition().line
                    && !currentStartPosition.equals(currentEndPosition)) {
                    rightPosition = rightMost(rightPosition, currentEndPosition);
                }


                // if(currentLeftPosition != null) {
                // if(leftPosition == null) {
                // leftPosition = currentLeftPosition;
                // } else if(leftPosition.column > currentLeftPosition.column
                // && beginPosition.line < currentLeftPosition.line) {
                // leftPosition = currentLeftPosition;
                // }
                // }
                //
                // if(currentStartPosition != null) {
                // if(leftPosition == null && currentStartPosition.line > beginPosition.line
                // ) {
                // leftPosition = currentStartPosition;
                // } else if(leftPosition != null && currentStartPosition.line > beginPosition.line
                // && currentStartPosition.column < leftPosition.column
                // && !currentStartPosition.equals(currentEndPosition)) {
                // leftPosition = currentStartPosition;
                // }
                // }
                //
                // if(rightPosition == null && currentRightPosition != null) {
                // rightPosition = currentRightPosition;
                // }
                // if(currentRightPosition != null && (rightPosition.column < currentRightPosition.column
                // && parse.currentPosition().line > currentRightPosition.line)) {
                // rightPosition = currentRightPosition;
                // }
                // if(currentEndPosition != null && (currentEndPosition.line < parse.currentPosition().line
                // && currentEndPosition.column > parse.currentPosition().column)) {
                // rightPosition = currentEndPosition;
                // }


            } else if(pf instanceof LayoutSensitiveCharacterNode) {
                if(pf.getStartPosition().line > beginPosition.line
                    && pf.getStartPosition().column < beginPosition.column) {
                    leftPosition = new Position(pf.getStartPosition().offset, pf.getStartPosition().line,
                        pf.getStartPosition().column);
                }
                if(pf.getEndPosition().line < parse.currentPosition().line
                    && pf.getEndPosition().column > parse.currentPosition().column) {
                    rightPosition =
                        new Position(pf.getEndPosition().offset, pf.getEndPosition().line, pf.getEndPosition().column);
                }
            } else if(pf != null) {
                System.err.println("Not a valid tree node.");
            }
        }

        LayoutSensitiveDerivation derivation = new LayoutSensitiveDerivation(parse, beginPosition, leftPosition,
            rightPosition, parse.currentPosition(), production, productionType, parseForests);

        // parse.notify(observer -> observer.createDerivation(derivation.nodeNumber, production, parseForests));

        return derivation;
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

    @Override public void addDerivation(AbstractParse<LayoutSensitiveParseForest, ?> parse,
        LayoutSensitiveParseNode parseNode, LayoutSensitiveDerivation derivation) {
        // parse.notify(observer -> observer.addDerivation(parseNode));

        parseNode.addDerivation(derivation);
    }

    @Override public LayoutSensitiveCharacterNode
        createCharacterNode(AbstractParse<LayoutSensitiveParseForest, ?> parse) {
        LayoutSensitiveCharacterNode termNode =
            new LayoutSensitiveCharacterNode(parse.currentPosition(), parse.currentChar);

        // parse.notify(observer -> observer.createCharacterNode(termNode, termNode.character));

        return termNode;
    }

    @Override public LayoutSensitiveParseForest[] parseForestsArray(int length) {
        return new LayoutSensitiveParseForest[length];
    }

    private Position verifyPositionAmbiguities(ConstraintSelector selector, LayoutSensitiveParseForest pf) {
        // For trees in an ambiguity such that left and right are different, take the one with smallest and largest
        // column, respectively
        Position currentPosition = null;
        for(LayoutSensitiveDerivation rn : ((LayoutSensitiveParseNode) pf).getDerivations()) {
            switch(selector) {
                case FIRST:
                    // if(currentPosition != null && !rn.startPosition.equals(currentPosition)) {
                    // System.err.println("StartPosition is different for trees that are part of an ambiguity.");
                    // }
                    if(rn.getStartPosition() != null) {
                        currentPosition = new Position(rn.getStartPosition());
                    }
                    break;
                case LAST:
                    // if(currentPosition != null && !rn.endPosition.equals(currentPosition)) {
                    // System.err.println("EndPosition is different for trees that are part of an ambiguity.");
                    // }
                    if(rn.getEndPosition() != null) {
                        currentPosition = new Position(rn.getEndPosition());
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
