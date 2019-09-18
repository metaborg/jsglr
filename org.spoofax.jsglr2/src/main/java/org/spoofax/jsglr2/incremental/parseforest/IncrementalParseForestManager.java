package org.spoofax.jsglr2.incremental.parseforest;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.productions.ProductionType;
import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.incremental.IIncrementalParseState;
import org.spoofax.jsglr2.incremental.IncrementalParseState;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parseforest.ParseForestManagerFactory;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;

import java.util.ArrayList;
import java.util.List;

public class IncrementalParseForestManager
//@formatter:off
   <StackNode extends IStackNode,
    ParseState extends AbstractParseState<IncrementalParseForest, StackNode> & IIncrementalParseState>
//@formatter:on
    extends
    ParseForestManager<IncrementalParseForest, IncrementalParseNode, IncrementalDerivation, StackNode, ParseState> {

    public IncrementalParseForestManager(ParserObserving<IncrementalParseForest, StackNode, ParseState> observing) {
        super(observing);
    }

    public static
//@formatter:off
   <StackNode_   extends IStackNode,
    ParseState_  extends AbstractParseState<IncrementalParseForest, StackNode_> & IIncrementalParseState>
//@formatter:on
    ParseForestManagerFactory<IncrementalParseForest, IncrementalParseNode, IncrementalDerivation, StackNode_, ParseState_>
        factory() {
        return IncrementalParseForestManager::new;
    }

    @Override public IncrementalParseForest filterStartSymbol(IncrementalParseForest parseForest, String startSymbol,
        ParseState parseState) {

        IncrementalParseNode topNode = (IncrementalParseNode) parseForest;
        List<IncrementalDerivation> result = new ArrayList<>();

        for(IncrementalDerivation derivation : topNode.getDerivations()) {
            String derivationStartSymbol = derivation.production().startSymbolSort();

            if(derivationStartSymbol != null && derivationStartSymbol.equals(startSymbol))
                result.add(derivation);
        }

        if(result.isEmpty())
            return null;
        else {
            IncrementalParseNode filteredTopNode = new IncrementalParseNode(topNode.production(), result.get(0));

            for(int i = 1; i < result.size(); i++)
                filteredTopNode.addDerivation(result.get(i));

            return filteredTopNode;
        }
    }

    @Override public IncrementalParseNode createParseNode(ParseState parseState, IStackNode stack,
        IProduction production, IncrementalDerivation firstDerivation) {

        IncrementalParseNode parseNode = new IncrementalParseNode(production, firstDerivation);

        observing.notify(observer -> observer.createParseNode(parseNode, production));
        observing.notify(observer -> observer.addDerivation(parseNode, firstDerivation));

        return parseNode;
    }

    public IncrementalParseNode createChangedParseNode(ParseState parseState, IncrementalParseForest... children) {
        IncrementalParseNode parseNode = new IncrementalParseNode(children);

        observing.notify(observer -> observer.createDerivation(parseNode.getFirstDerivation(), null, children));
        observing.notify(observer -> observer.createParseNode(parseNode, null));
        observing.notify(observer -> observer.addDerivation(parseNode, parseNode.getFirstDerivation()));

        return parseNode;
    }

    @Override public IncrementalDerivation createDerivation(ParseState parseState, IStackNode stack,
        IProduction production, ProductionType productionType, IncrementalParseForest[] parseForests) {

        IState state = parseState.isMultipleStates() ? IncrementalParseState.NO_STATE : stack.state();
        IncrementalDerivation derivation = new IncrementalDerivation(production, productionType, parseForests, state);

        observing.notify(observer -> observer.createDerivation(derivation, production, derivation.parseForests));

        return derivation;
    }

    @Override public void addDerivation(ParseState parseState, IncrementalParseNode parseNode,
        IncrementalDerivation derivation) {

        observing.notify(observer -> observer.addDerivation(parseNode, derivation));

        parseNode.addDerivation(derivation);
    }

    @Override public IncrementalParseForest createCharacterNode(ParseState parseState) {
        return createCharacterNode(parseState, parseState.currentChar);
    }

    public IncrementalParseForest createCharacterNode(ParseState parseState, int currentChar) {
        IncrementalCharacterNode characterNode = new IncrementalCharacterNode(currentChar);

        observing.notify(observer -> observer.createCharacterNode(characterNode, characterNode.character));

        return characterNode;
    }

    @Override public IncrementalParseForest[] parseForestsArray(int length) {
        return new IncrementalParseForest[length];
    }
}
