package org.spoofax.jsglr2.composite;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.productions.ProductionType;
import org.spoofax.jsglr2.layoutsensitive.LayoutSensitiveParseForestManager;
import org.spoofax.jsglr2.layoutsensitive.Shape;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parseforest.ParseForestManagerFactory;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;

import java.util.List;

public class CompositeParseForestManager
//@formatter:off
   <StackNode  extends IStackNode,
    ParseState extends AbstractParseState<StackNode> & ICompositeParseState>
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
    ParseState_ extends AbstractParseState<StackNode_> & ICompositeParseState>
//@formatter:on
    ParseForestManagerFactory<ICompositeParseForest, ICompositeDerivation<ICompositeParseForest>, ICompositeParseNode<ICompositeParseForest, ICompositeDerivation<ICompositeParseForest>>, StackNode_, ParseState_>
        factory() {
        return CompositeParseForestManager::new;
    }

    @Override public
        ICompositeParseNode<ICompositeParseForest, ICompositeDerivation<ICompositeParseForest>>
        createParseNode(ParseState parseState, IStackNode stack, IProduction production,
            ICompositeDerivation<ICompositeParseForest> firstDerivation) {
        ICompositeParseNode<ICompositeParseForest, ICompositeDerivation<ICompositeParseForest>> parseNode =
            new CompositeParseNode<>(firstDerivation.getStartPosition(), parseState.currentPosition(),
                production);

        observing.notify(observer -> observer.createParseNode(parseNode, production));

        addDerivation(parseState, parseNode, firstDerivation);

        return parseNode;
    }

    @Override public ICompositeDerivation<ICompositeParseForest> createDerivation(ParseState parseState,
        IStackNode stack, IProduction production, ProductionType productionType,
        ICompositeParseForest[] parseForests) {
        Shape shape = LayoutSensitiveParseForestManager.shape(parseForests, parseState.currentPosition());

        ICompositeDerivation<ICompositeParseForest> derivation =
            new CompositeDerivation<>(shape.start, shape.left, shape.right, shape.end, production, productionType,
                parseForests);

        observing.notify(observer -> observer.createDerivation(derivation, production, parseForests));

        return derivation;
    }

    @Override public void addDerivation(ParseState parseState,
        ICompositeParseNode<ICompositeParseForest, ICompositeDerivation<ICompositeParseForest>> parseNode,
        ICompositeDerivation<ICompositeParseForest> derivation) {
        observing.notify(observer -> observer.addDerivation(parseNode, derivation));

        parseNode.addDerivation(derivation);
    }

    @Override public ICompositeParseForest createCharacterNode(ParseState parseState) {
        ICompositeParseForest termNode =
            new CompositeCharacterNode(parseState.currentPosition(), parseState.currentChar);

        observing.notify(observer -> observer.createCharacterNode(termNode, parseState.currentChar));

        return termNode;
    }

    @Override public ICompositeParseForest[] parseForestsArray(int length) {
        return new ICompositeParseForest[length];
    }

    @Override protected
        ICompositeParseNode<ICompositeParseForest, ICompositeDerivation<ICompositeParseForest>>
        filteredTopParseNode(
            ICompositeParseNode<ICompositeParseForest, ICompositeDerivation<ICompositeParseForest>> parseNode,
            List<ICompositeDerivation<ICompositeParseForest>> derivations) {
        ICompositeParseNode<ICompositeParseForest, ICompositeDerivation<ICompositeParseForest>> topParseNode =
            new CompositeParseNode<>(parseNode.getStartPosition(), parseNode.getEndPosition(),
                parseNode.production());

        for(ICompositeDerivation<ICompositeParseForest> derivation : derivations)
            topParseNode.addDerivation(derivation);

        return topParseNode;
    }

}
