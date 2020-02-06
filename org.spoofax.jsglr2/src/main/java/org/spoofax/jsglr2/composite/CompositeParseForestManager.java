package org.spoofax.jsglr2.composite;

import static org.spoofax.jsglr2.parseforest.IParseForest.sumWidth;

import java.util.List;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.productions.ProductionType;
import org.spoofax.jsglr2.inputstack.LayoutSensitiveInputStack;
import org.spoofax.jsglr2.layoutsensitive.LayoutSensitiveParseForestManager;
import org.spoofax.jsglr2.layoutsensitive.Shape;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parseforest.ParseForestManagerFactory;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;

public class CompositeParseForestManager
//@formatter:off
   <StackNode  extends IStackNode,
    ParseState extends AbstractParseState<LayoutSensitiveInputStack, StackNode>>
//@formatter:on
    extends
    ParseForestManager<ICompositeParseForest, ICompositeDerivation<ICompositeParseForest>, ICompositeParseNode<ICompositeParseForest, ICompositeDerivation<ICompositeParseForest>>, StackNode, ParseState> {

    private CompositeParseForestManager(
        ParserObserving<ICompositeParseForest, ICompositeDerivation<ICompositeParseForest>, ICompositeParseNode<ICompositeParseForest, ICompositeDerivation<ICompositeParseForest>>, StackNode, ParseState> observing) {
        super(observing);
    }

    public static
//@formatter:off
   <StackNode_  extends IStackNode,
    ParseState_ extends AbstractParseState<LayoutSensitiveInputStack, StackNode_>>
//@formatter:on
    ParseForestManagerFactory<ICompositeParseForest, ICompositeDerivation<ICompositeParseForest>, ICompositeParseNode<ICompositeParseForest, ICompositeDerivation<ICompositeParseForest>>, StackNode_, ParseState_>
        factory() {
        return CompositeParseForestManager::new;
    }

    @Override public ICompositeParseNode<ICompositeParseForest, ICompositeDerivation<ICompositeParseForest>>
        createParseNode(ParseState parseState, IStackNode stack, IProduction production,
            ICompositeDerivation<ICompositeParseForest> firstDerivation) {
        ICompositeParseNode<ICompositeParseForest, ICompositeDerivation<ICompositeParseForest>> parseNode =
            new CompositeParseNode<>(sumWidth(firstDerivation.parseForests()), firstDerivation.getStartPosition(),
                parseState.inputStack.currentPosition(), production);

        observing.notify(observer -> observer.createParseNode(parseNode, production));

        addDerivation(parseState, parseNode, firstDerivation);

        return parseNode;
    }

    @Override public ICompositeDerivation<ICompositeParseForest> createDerivation(ParseState parseState,
        IStackNode stack, IProduction production, ProductionType productionType, ICompositeParseForest[] parseForests) {
        Shape shape = LayoutSensitiveParseForestManager.shape(parseForests, parseState.inputStack.currentPosition());

        ICompositeDerivation<ICompositeParseForest> derivation = new CompositeDerivation<>(shape.start, shape.left,
            shape.right, shape.end, production, productionType, parseForests);

        observing.notify(observer -> observer.createDerivation(derivation, production, parseForests));

        return derivation;
    }

    @Override public void addDerivation(ParseState parseState,
        ICompositeParseNode<ICompositeParseForest, ICompositeDerivation<ICompositeParseForest>> parseNode,
        ICompositeDerivation<ICompositeParseForest> derivation) {
        observing.notify(observer -> observer.addDerivation(parseNode, derivation));

        parseNode.addDerivation(derivation);
    }

    @Override public ICompositeParseNode<ICompositeParseForest, ICompositeDerivation<ICompositeParseForest>>
        createSkippedNode(ParseState parseState, IProduction production, ICompositeParseForest[] parseForests) {
        Position endPosition = parseState.inputStack.currentPosition();
        return new CompositeParseNode<>(sumWidth(parseForests),
            parseForests.length == 0 ? endPosition : parseForests[0].getStartPosition(), // Same as in the shape method
            endPosition, production);
    }

    @Override public ICompositeParseForest createCharacterNode(ParseState parseState) {
        ICompositeParseForest termNode =
            new CompositeCharacterNode(parseState.inputStack.currentPosition(), parseState.inputStack.getChar());

        observing.notify(observer -> observer.createCharacterNode(termNode, parseState.inputStack.getChar()));

        return termNode;
    }

    @Override public ICompositeParseForest[] parseForestsArray(int length) {
        return new ICompositeParseForest[length];
    }

    @Override protected ICompositeParseNode<ICompositeParseForest, ICompositeDerivation<ICompositeParseForest>>
        filteredTopParseNode(
            ICompositeParseNode<ICompositeParseForest, ICompositeDerivation<ICompositeParseForest>> parseNode,
            List<ICompositeDerivation<ICompositeParseForest>> derivations) {
        ICompositeParseNode<ICompositeParseForest, ICompositeDerivation<ICompositeParseForest>> topParseNode =
            new CompositeParseNode<>(parseNode.width(), parseNode.getStartPosition(), parseNode.getEndPosition(),
                parseNode.production());

        for(ICompositeDerivation<ICompositeParseForest> derivation : derivations)
            topParseNode.addDerivation(derivation);

        return topParseNode;
    }

}
