package org.spoofax.jsglr2.layoutsensitive;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.productions.ProductionType;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParseState;

import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;

import java.util.ArrayList;
import java.util.List;

public class LayoutSensitiveParseForestManager
//@formatter:off
   <StackNode  extends IStackNode,
    ParseState extends AbstractParseState<LayoutSensitiveParseForest, StackNode>>
//@formatter:on
    extends
    ParseForestManager<LayoutSensitiveParseForest, LayoutSensitiveParseNode, LayoutSensitiveDerivation, StackNode, ParseState> {

    @Override public LayoutSensitiveParseNode createParseNode(
        ParserObserving<LayoutSensitiveParseForest, StackNode, ParseState> observing, ParseState parseState,
        IStackNode stack, IProduction production, LayoutSensitiveDerivation firstDerivation) {
        LayoutSensitiveParseNode parseNode =
            new LayoutSensitiveParseNode(firstDerivation.getStartPosition(), parseState.currentPosition(), production);

        observing.notify(observer -> observer.createParseNode(parseNode, production));

        addDerivation(observing, parseState, parseNode, firstDerivation);

        return parseNode;
    }

    @Override public LayoutSensitiveParseForest filterStartSymbol(LayoutSensitiveParseForest parseForest,
        String startSymbol, ParseState parseState) {
        LayoutSensitiveParseNode topNode = (LayoutSensitiveParseNode) parseForest;
        List<LayoutSensitiveDerivation> result = new ArrayList<>();

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

    @Override public LayoutSensitiveDerivation createDerivation(
        ParserObserving<LayoutSensitiveParseForest, StackNode, ParseState> observing, ParseState parseState,
        IStackNode stack, IProduction production, ProductionType productionType,
        LayoutSensitiveParseForest[] parseForests) {

        Position beginPosition = parseForests.length == 0
            // If this derivation corresponds with an epsilon production, use current parse position as startPosition
            ? parseState.currentPosition()
            // Else, just use the start position of the first child node
            : parseForests[0].getStartPosition();

        // FIXME since EndPosition is wrong, right is also wrong
        Position leftPosition = null;
        Position rightPosition = null;

        for(LayoutSensitiveParseForest pf : parseForests) {
            if(pf instanceof LayoutSensitiveParseNode) {
                LayoutSensitiveParseNode layoutSensitiveParseNode = (LayoutSensitiveParseNode) pf;

                if(!layoutSensitiveParseNode.production().isLayout()
                    && !layoutSensitiveParseNode.production().isIgnoreLayoutConstraint()) {
                    LayoutSensitiveDerivation firstDerivation = layoutSensitiveParseNode.getFirstDerivation();

                    Position currentStartPosition = firstDerivation.getStartPosition();
                    Position currentLeftPosition = firstDerivation.leftPosition;
                    Position currentRightPosition = firstDerivation.rightPosition;
                    Position currentEndPosition = firstDerivation.getEndPosition();

                    if(currentLeftPosition != null) {
                        leftPosition = leftMost(leftPosition, currentLeftPosition);
                    }

                    if(currentStartPosition.line > beginPosition.line
                        && !currentStartPosition.equals(currentEndPosition)) {
                        leftPosition = leftMost(leftPosition, currentStartPosition);
                    }

                    if(currentRightPosition != null) {
                        rightPosition = rightMost(rightPosition, currentRightPosition);
                    }

                    if(currentEndPosition.line < parseState.currentPosition().line
                        && !currentStartPosition.equals(currentEndPosition)) {
                        rightPosition = rightMost(rightPosition, currentEndPosition);
                    }
                }
            } else if(pf instanceof LayoutSensitiveCharacterNode) {
                if(pf.getStartPosition().line > beginPosition.line
                    && pf.getStartPosition().column < beginPosition.column) {
                    leftPosition = new Position(pf.getStartPosition().offset, pf.getStartPosition().line,
                        pf.getStartPosition().column);
                }
                if(pf.getEndPosition().line < parseState.currentPosition().line
                    && pf.getEndPosition().column > parseState.currentPosition().column) {
                    rightPosition =
                        new Position(pf.getEndPosition().offset, pf.getEndPosition().line, pf.getEndPosition().column);
                }
            } else if(pf != null) {
                throw new IllegalStateException("Invalid layout sensitive node");
            }
        }

        LayoutSensitiveDerivation derivation = new LayoutSensitiveDerivation(beginPosition, leftPosition, rightPosition,
            parseState.currentPosition(), production, productionType, parseForests);

        observing.notify(observer -> observer.createDerivation(derivation, production, parseForests));

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

    @Override public void addDerivation(ParserObserving<LayoutSensitiveParseForest, StackNode, ParseState> observing,
        ParseState parseState, LayoutSensitiveParseNode parseNode, LayoutSensitiveDerivation derivation) {
        observing.notify(observer -> observer.addDerivation(parseNode, derivation));

        parseNode.addDerivation(derivation);
    }

    @Override public LayoutSensitiveCharacterNode createCharacterNode(
        ParserObserving<LayoutSensitiveParseForest, StackNode, ParseState> observing, ParseState parseState) {
        LayoutSensitiveCharacterNode termNode =
            new LayoutSensitiveCharacterNode(parseState.currentPosition(), parseState.currentChar);

        observing.notify(observer -> observer.createCharacterNode(termNode, termNode.character));

        return termNode;
    }

    @Override public LayoutSensitiveParseForest[] parseForestsArray(int length) {
        return new LayoutSensitiveParseForest[length];
    }

}
