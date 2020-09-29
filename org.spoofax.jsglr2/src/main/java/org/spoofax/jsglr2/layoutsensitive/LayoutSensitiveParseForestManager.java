package org.spoofax.jsglr2.layoutsensitive;

import static org.spoofax.jsglr2.parseforest.IParseForest.sumWidth;

import java.util.List;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.productions.ProductionType;
import org.spoofax.jsglr2.inputstack.LayoutSensitiveInputStack;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parseforest.ParseForestManagerFactory;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;

public class LayoutSensitiveParseForestManager
//@formatter:off
   <StackNode  extends IStackNode,
    ParseState extends AbstractParseState<LayoutSensitiveInputStack, StackNode>>
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
    ParseState_ extends AbstractParseState<LayoutSensitiveInputStack, StackNode_>>
//@formatter:on
    ParseForestManagerFactory<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>, ILayoutSensitiveParseNode<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>>, StackNode_, ParseState_>
        factory() {
        return LayoutSensitiveParseForestManager::new;
    }

    @Override public
        ILayoutSensitiveParseNode<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>>
        createParseNode(ParseState parseState, IStackNode stack, IProduction production,
            ILayoutSensitiveDerivation<ILayoutSensitiveParseForest> firstDerivation) {
        Shape shape = shape(firstDerivation.parseForests(), parseState.inputStack.previousPosition);

        ILayoutSensitiveParseNode<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>> parseNode =
            new LayoutSensitiveParseNode<>(sumWidth(firstDerivation.parseForests()), production, shape.start, shape.end,
                shape.left, shape.right);

        observing.notify(observer -> observer.createParseNode(parseNode, production));

        addDerivation(parseState, parseNode, firstDerivation);

        return parseNode;
    }

    @Override public ILayoutSensitiveDerivation<ILayoutSensitiveParseForest> createDerivation(ParseState parseState,
        IStackNode stack, IProduction production, ProductionType productionType,
        ILayoutSensitiveParseForest[] parseForests) {
        ILayoutSensitiveDerivation<ILayoutSensitiveParseForest> derivation =
            new LayoutSensitiveDerivation<>(production, productionType, parseForests);

        observing.notify(observer -> observer.createDerivation(derivation, production, parseForests));

        return derivation;
    }

    public static Shape shape(ILayoutSensitiveParseForest[] parseForests, Position endPosition) {
        Position startPosition = parseForests.length == 0
            // If this derivation corresponds with an epsilon production, use current parse position as startPosition
            ? endPosition
            // Else, just use the start position of the first child node
            : parseForests[0].getStartPosition();

        /*
         * From Erdweg et al. (2012): `left` selects the leftmost non-whitespace token that is not on the same line as
         * the first token. `right` right selects the rightmost non-whitespace token that is not on the same line as the
         * last token
         */
        Position leftPosition = null;
        Position rightPosition = null;

        boolean shiftStartPosition = true;

        for(ILayoutSensitiveParseForest pf : parseForests) {
            if(pf instanceof ILayoutSensitiveParseNode) {
                ILayoutSensitiveParseNode<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>> layoutSensitiveParseNode =
                    (ILayoutSensitiveParseNode<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>>) pf;

                if(layoutSensitiveParseNode.width() > 0 && !layoutSensitiveParseNode.production().isLayout()
                    && !layoutSensitiveParseNode.production().isIgnoreLayoutConstraint()) {
                    Position currentStartPosition = layoutSensitiveParseNode.getStartPosition();
                    Position currentEndPosition = layoutSensitiveParseNode.getEndPosition();
                    Position currentLeftPosition = layoutSensitiveParseNode.getLeftPosition();
                    Position currentRightPosition = layoutSensitiveParseNode.getRightPosition();

                    if(shiftStartPosition) {
                        startPosition = currentStartPosition;
                        shiftStartPosition = false;
                    }

                    if(currentLeftPosition != null) {
                        leftPosition = leftMost(leftPosition, currentLeftPosition);
                    }

                    if(currentStartPosition.line > startPosition.line) {
                        leftPosition = leftMost(leftPosition, currentStartPosition);
                    }

                    if(currentRightPosition != null) {
                        rightPosition = rightMost(rightPosition, currentRightPosition);
                    }

                    if(currentEndPosition.line < endPosition.line) {
                        rightPosition = rightMost(rightPosition, currentEndPosition);
                    }

                    endPosition = currentEndPosition;
                }
            } else if(pf instanceof ILayoutSensitiveCharacterNode) {
                if(shiftStartPosition) {
                    startPosition = pf.getStartPosition();
                    shiftStartPosition = false;
                }

                if(pf.getStartPosition().line > startPosition.line
                    && (leftPosition == null || pf.getStartPosition().column < leftPosition.column)) {
                    leftPosition = pf.getStartPosition();
                }

                if(pf.getEndPosition().line < endPosition.line
                    && (rightPosition == null || pf.getEndPosition().column > rightPosition.column)) {
                    rightPosition = pf.getEndPosition();
                }
            } else if(pf != null) {
                throw new IllegalStateException("Invalid layout sensitive node");
            }
        }

        return new Shape(startPosition, endPosition, leftPosition, rightPosition);
    }

    private static Position rightMost(Position p1, Position p2) {
        if(p1 == null) {
            return p2;
        }

        if(p1.column < p2.column) {
            return p2;
        }

        return p1;
    }

    private static Position leftMost(Position p1, Position p2) {
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

    @Override public
        ILayoutSensitiveParseNode<ILayoutSensitiveParseForest, ILayoutSensitiveDerivation<ILayoutSensitiveParseForest>>
        createSkippedNode(ParseState parseState, IProduction production, ILayoutSensitiveParseForest[] parseForests) {
        throw new IllegalStateException();
    }

    @Override public ILayoutSensitiveParseForest createCharacterNode(ParseState parseState) {
        ILayoutSensitiveParseForest termNode =
            new LayoutSensitiveCharacterNode(parseState.inputStack.currentPosition(), parseState.inputStack.getChar());

        observing.notify(observer -> observer.createCharacterNode(termNode, parseState.inputStack.getChar()));

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
            new LayoutSensitiveParseNode<>(parseNode.width(), parseNode.production(), parseNode.getStartPosition(),
                parseNode.getEndPosition(), parseNode.getLeftPosition(), parseNode.getRightPosition());

        for(ILayoutSensitiveDerivation<ILayoutSensitiveParseForest> derivation : derivations)
            topParseNode.addDerivation(derivation);

        return topParseNode;
    }

}
