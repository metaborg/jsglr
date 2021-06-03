package org.spoofax.jsglr2.parseforest.basic;

import static org.spoofax.jsglr2.parseforest.IParseForest.sumWidth;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.productions.ProductionType;
import org.spoofax.jsglr2.parseforest.Disambiguator;
import org.spoofax.jsglr2.parseforest.ParseForestManagerFactory;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;

public class BasicParseForestManager
//@formatter:off
   <StackNode  extends IStackNode,
    ParseState extends AbstractParseState<?, StackNode>>
//@formatter:on
    extends
    AbstractBasicParseForestManager<IBasicParseForest, IBasicDerivation<IBasicParseForest>, IBasicParseNode<IBasicParseForest, IBasicDerivation<IBasicParseForest>>, StackNode, ParseState> {

    public BasicParseForestManager(
        ParserObserving<IBasicParseForest, IBasicDerivation<IBasicParseForest>, IBasicParseNode<IBasicParseForest, IBasicDerivation<IBasicParseForest>>, StackNode, ParseState> observing,
        Disambiguator<IBasicParseForest, IBasicDerivation<IBasicParseForest>, IBasicParseNode<IBasicParseForest, IBasicDerivation<IBasicParseForest>>, StackNode, ParseState> disambiguator) {
        super(observing, disambiguator);
    }

    public static
//@formatter:off
   <StackNode_  extends IStackNode,
    ParseState_ extends AbstractParseState<?, StackNode_>>
//@formatter:on
    ParseForestManagerFactory<IBasicParseForest, IBasicDerivation<IBasicParseForest>, IBasicParseNode<IBasicParseForest, IBasicDerivation<IBasicParseForest>>, StackNode_, ParseState_>
        factory() {
        return BasicParseForestManager::new;
    }

    @Override protected IBasicParseNode<IBasicParseForest, IBasicDerivation<IBasicParseForest>>
        constructParseNode(int width, IProduction production) {
        return new BasicParseNode<>(width, production);
    }

    @Override protected IBasicDerivation<IBasicParseForest> constructDerivation(IProduction production,
        ProductionType productionType, IBasicParseForest[] parseForests) {
        return new BasicDerivation<>(production, productionType, parseForests);
    }

    @Override public IBasicParseNode<IBasicParseForest, IBasicDerivation<IBasicParseForest>>
        createSkippedNode(ParseState parseState, IStackNode stack, IProduction production, IBasicParseForest[] parseForests) {
        return new BasicParseNode<>(sumWidth(parseForests), production);
    }

    @Override protected IBasicParseForest constructCharacterNode(ParseState parseState) {
        return new BasicCharacterNode(parseState.inputStack.getChar());
    }

    @Override public IBasicParseForest[] parseForestsArray(int length) {
        return new IBasicParseForest[length];
    }

}
