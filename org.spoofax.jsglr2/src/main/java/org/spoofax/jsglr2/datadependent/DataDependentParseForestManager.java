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
   <StackNode  extends IStackNode,
    ParseState extends AbstractParseState<IDataDependentParseForest, StackNode>>
    extends
    AbstractBasicParseForestManager
       <IDataDependentParseForest,
        IDataDependentDerivation<IDataDependentParseForest>,
        IDataDependentParseNode<IDataDependentParseForest, IDataDependentDerivation<IDataDependentParseForest>>,
        StackNode,
        ParseState>
//@formatter:on
{

    private DataDependentParseForestManager(
        ParserObserving<IDataDependentParseForest, IDataDependentDerivation<IDataDependentParseForest>, IDataDependentParseNode<IDataDependentParseForest, IDataDependentDerivation<IDataDependentParseForest>>, StackNode, ParseState> observing) {
        super(observing);
    }

    public static
//@formatter:off
   <StackNode_  extends IStackNode,
    ParseState_ extends AbstractParseState<IDataDependentParseForest, StackNode_>>
//@formatter:on
    ParseForestManagerFactory<IDataDependentParseForest, IDataDependentDerivation<IDataDependentParseForest>, IDataDependentParseNode<IDataDependentParseForest, IDataDependentDerivation<IDataDependentParseForest>>, StackNode_, ParseState_>
        factory() {
        return DataDependentParseForestManager::new;
    }

    @Override protected
        DataDependentParseNode<IDataDependentParseForest, IDataDependentDerivation<IDataDependentParseForest>>
        constructParseNode(IProduction production) {
        return new DataDependentParseNode<>(production);
    }

    @Override protected IDataDependentDerivation<IDataDependentParseForest> constructDerivation(IProduction production,
        ProductionType productionType, IDataDependentParseForest[] parseForests) {
        return new DataDependentDerivation<>(production, productionType, parseForests);
    }

    @Override protected IDataDependentParseForest constructCharacterNode(ParseState parseState) {
        return new DataDependentCharacterNode(parseState.currentChar);
    }

    @Override public IDataDependentParseForest[] parseForestsArray(int length) {
        return new IDataDependentParseForest[length];
    }

}
