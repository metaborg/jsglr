package org.spoofax.jsglr2.parseforest.basic;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.productions.ProductionType;
import org.spoofax.jsglr2.parseforest.ParseForestManagerFactory;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;

public class BasicParseForestManager
//@formatter:off
   <StackNode  extends IStackNode,
    ParseState extends AbstractParseState<BasicParseForest, StackNode>>
//@formatter:on
    extends AbstractBasicParseForestManager<BasicParseForest, BasicDerivation, BasicParseNode, StackNode, ParseState> {

    public BasicParseForestManager(
        ParserObserving<BasicParseForest, BasicDerivation, BasicParseNode, StackNode, ParseState> observing) {
        super(observing);
    }

    public static
//@formatter:off
   <StackNode_  extends IStackNode,
    ParseState_ extends AbstractParseState<BasicParseForest, StackNode_>>
//@formatter:on
    ParseForestManagerFactory<BasicParseForest, BasicDerivation, BasicParseNode, StackNode_, ParseState_> factory() {
        return BasicParseForestManager::new;
    }

    @Override protected BasicParseNode constructParseNode(IProduction production) {
        return new BasicParseNode(production);
    }

    @Override protected BasicDerivation constructDerivation(IProduction production, ProductionType productionType,
        BasicParseForest[] parseForests) {
        return new BasicDerivation(production, productionType, parseForests);
    }

    @Override protected BasicParseForest constructCharacterNode(int character) {
        return new BasicCharacterNode(character);
    }

    @Override public BasicParseForest[] parseForestsArray(int length) {
        return new BasicParseForest[length];
    }

}
