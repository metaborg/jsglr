package org.spoofax.jsglr2.layoutsensitive;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.productions.ProductionType;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parseforest.ParseForestManagerFactory;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;

import java.util.List;

public class LayoutSensitiveParseForestManager
//@formatter:off
   <StackNode  extends IStackNode,
    ParseState extends AbstractParseState<ILayoutSensitiveParseForest, StackNode> & ILayoutSensitiveParseState>
//@formatter:on
    extends
    ParseForestManager<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>, ILayoutSensitiveParseNode<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>>, StackNode, ParseState> {

    private LayoutSensitiveParseForestManager(
        ParserObserving<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>, ILayoutSensitiveParseNode<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>>, StackNode, ParseState> observing) {
        super(observing);
    }

    public static
//@formatter:off
   <StackNode_  extends IStackNode,
    ParseState_ extends AbstractParseState<ILayoutSensitiveParseForest, StackNode_> & ILayoutSensitiveParseState>
//@formatter:on
    ParseForestManagerFactory<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>, ILayoutSensitiveParseNode<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>>, StackNode_, ParseState_>
        factory() {
        return LayoutSensitiveParseForestManager::new;
    }

    @Override public
        ILayoutSensitiveParseNode<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>>
        createParseNode(ParseState parseState, IStackNode stack, IProduction production,
            ILayoutSensitiveDerivation<ILayoutSensitiveParseForest> firstDerivation) {
        ILayoutSensitiveParseNode<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>> parseNode =
            new LayoutSensitiveParseNode<>(firstDerivation.getStartPosition(), parseState.currentPosition(),
                production);

        observing.notify(observer -> observer.createParseNode(parseNode, production));

        addDerivation(parseState, parseNode, firstDerivation);

        return parseNode;
    }

    @Override public ILayoutSensitiveDerivation<ILayoutSensitiveParseForest> createDerivation(ParseState parseState,
        IStackNode stack, IProduction production, ProductionType productionType,
        ILayoutSensitiveParseForest[] parseForests) {

        Position beginPosition = parseForests.length == 0
            // If this derivation corresponds with an epsilon production, use current parse position as startPosition
            ? parseState.currentPosition()
            // Else, just use the start position of the first child node
            : parseForests[0].getStartPosition();

        // FIXME since EndPosition is wrong, right is also wrong
        Position leftPosition = null;
        Position rightPosition = null;

        for(ILayoutSensitiveParseForest pf : parseForests) {
            if(pf instanceof ILayoutSensitiveParseNode) {
                ILayoutSensitiveParseNode<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>> layoutSensitiveParseNode =
                    (ILayoutSensitiveParseNode<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>>) pf;

                if(!layoutSensitiveParseNode.production().isLayout()
                    && !layoutSensitiveParseNode.production().isIgnoreLayoutConstraint()) {
                    ILayoutSensitiveDerivation<ILayoutSensitiveParseForest> firstDerivation =
                        layoutSensitiveParseNode.getFirstDerivation();

                    Position currentStartPosition = firstDerivation.getStartPosition();
                    Position currentLeftPosition = firstDerivation.getLeftPosition();
                    Position currentRightPosition = firstDerivation.getRightPosition();
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

        ILayoutSensitiveDerivation<ILayoutSensitiveParseForest> derivation =
            new LayoutSensitiveDerivation<>(beginPosition, leftPosition, rightPosition, parseState.currentPosition(),
                production, productionType, parseForests);

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

    @Override public void addDerivation(ParseState parseState,
        ILayoutSensitiveParseNode<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>> parseNode,
        ILayoutSensitiveDerivation<ILayoutSensitiveParseForest> derivation) {
        observing.notify(observer -> observer.addDerivation(parseNode, derivation));

        parseNode.addDerivation(derivation);
    }

    @Override public ILayoutSensitiveParseForest createCharacterNode(ParseState parseState) {
        ILayoutSensitiveParseForest termNode =
            new LayoutSensitiveCharacterNode(parseState.currentPosition(), parseState.currentChar);

        observing.notify(observer -> observer.createCharacterNode(termNode, parseState.currentChar));

        return termNode;
    }

    @Override public ILayoutSensitiveParseForest[] parseForestsArray(int length) {
        return new ILayoutSensitiveParseForest[length];
    }

    @Override protected
        ILayoutSensitiveParseNode<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>>
        filteredTopParseNode(
            ILayoutSensitiveParseNode<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>> parseNode,
            List<ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>> derivations) {
        ILayoutSensitiveParseNode<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>> topParseNode =
            new LayoutSensitiveParseNode<>(parseNode.getStartPosition(), parseNode.getEndPosition(),
                parseNode.production());

        for(ILayoutSensitiveDerivation<ILayoutSensitiveParseForest> derivation : derivations)
            topParseNode.addDerivation(derivation);

        return topParseNode;
    }

}
