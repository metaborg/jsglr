package org.spoofax.jsglr2.incremental.parseforest;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.productions.ProductionType;
import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.incremental.IIncrementalParseState;
import org.spoofax.jsglr2.incremental.IncrementalParseState;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.Parse;
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

    @Override public IncrementalParseForest filterStartSymbol(IncrementalParseForest parseForest, String startSymbol,
        Parse<IncrementalParseForest, StackNode, ParseState> parse) {

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

    @Override public IncrementalParseNode createParseNode(Parse<IncrementalParseForest, StackNode, ParseState> parse,
        IStackNode stack, IProduction production, IncrementalDerivation firstDerivation) {

        IncrementalParseNode parseNode = new IncrementalParseNode(production, firstDerivation);

        parse.observing.notify(observer -> observer.createParseNode(parseNode, production));
        parse.observing.notify(observer -> observer.addDerivation(parseNode, firstDerivation));

        return parseNode;
    }

    public IncrementalParseNode createChangedParseNode(Parse<IncrementalParseForest, StackNode, ParseState> parse,
        IncrementalParseForest... children) {
        IncrementalParseNode parseNode = new IncrementalParseNode(children);

        parse.observing.notify(observer -> observer.createDerivation(parseNode.getFirstDerivation(), null, children));
        parse.observing.notify(observer -> observer.createParseNode(parseNode, null));
        parse.observing.notify(observer -> observer.addDerivation(parseNode, parseNode.getFirstDerivation()));

        return parseNode;
    }

    @Override public IncrementalDerivation createDerivation(Parse<IncrementalParseForest, StackNode, ParseState> parse,
        IStackNode stack, IProduction production, ProductionType productionType,
        IncrementalParseForest[] parseForests) {

        IState state = parse.state.isMultipleStates() ? IncrementalParseState.NO_STATE : stack.state();
        IncrementalDerivation derivation = new IncrementalDerivation(production, productionType, parseForests, state);

        parse.observing.notify(observer -> observer.createDerivation(derivation, production, derivation.parseForests));

        return derivation;
    }

    @Override public void addDerivation(Parse<IncrementalParseForest, StackNode, ParseState> parse,
        IncrementalParseNode parseNode, IncrementalDerivation derivation) {

        parse.observing.notify(observer -> observer.addDerivation(parseNode, derivation));

        parseNode.addDerivation(derivation);
    }

    @Override public IncrementalParseForest
        createCharacterNode(Parse<IncrementalParseForest, StackNode, ParseState> parse) {
        return createCharacterNode(parse, parse.state.currentChar);
    }

    public IncrementalParseForest createCharacterNode(Parse<IncrementalParseForest, StackNode, ParseState> parse,
        int currentChar) {
        IncrementalCharacterNode characterNode = new IncrementalCharacterNode(currentChar);

        parse.observing.notify(observer -> observer.createCharacterNode(characterNode, characterNode.character));

        return characterNode;
    }

    @Override public IncrementalParseForest[] parseForestsArray(int length) {
        return new IncrementalParseForest[length];
    }
}
