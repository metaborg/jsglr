package org.spoofax.jsglr2.datadependent;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.productions.ProductionType;
import org.spoofax.jsglr2.parseforest.ParseForestManagerFactory;
import org.spoofax.jsglr2.parseforest.basic.AbstractBasicParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;

public class DataDependentParseForestManager
//@formatter:off
   <ParseForest extends IDataDependentParseForest,
    Derivation  extends IDataDependentDerivation<ParseForest>,
    ParseNode   extends IDataDependentParseNode<ParseForest, Derivation>,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<ParseForest, StackNode>>
//@formatter:on
    extends AbstractBasicParseForestManager<ParseForest, Derivation, ParseNode, StackNode, ParseState> {

    public DataDependentParseForestManager(
        ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing) {
        super(observing);
    }

    public static
//@formatter:off
   <ParseForest_ extends IDataDependentParseForest,
    Derivation_  extends IDataDependentDerivation<ParseForest_>,
    ParseNode_   extends IDataDependentParseNode<ParseForest_, Derivation_>,
    StackNode_   extends IStackNode,
    ParseState_  extends AbstractParseState<ParseForest_, StackNode_>>
//@formatter:on
    ParseForestManagerFactory<ParseForest_, Derivation_, ParseNode_, StackNode_, ParseState_> factory() {
        return DataDependentParseForestManager::new;
    }

    @Override protected ParseNode constructParseNode(IProduction production) {
        return (ParseNode) new DataDependentParseNode<>(production);
    }

    @Override protected Derivation constructDerivation(IProduction production, ProductionType productionType,
        ParseForest[] parseForests) {
        return (Derivation) new DataDependentDerivation<>(production, productionType, parseForests);
    }

    @Override protected ParseForest constructCharacterNode(int character) {
        return (ParseForest) new DataDependentCharacterNode(character);
    }

    @Override public ParseForest[] parseForestsArray(int length) {
        return (ParseForest[]) new IDataDependentParseForest[length];
    }

}
