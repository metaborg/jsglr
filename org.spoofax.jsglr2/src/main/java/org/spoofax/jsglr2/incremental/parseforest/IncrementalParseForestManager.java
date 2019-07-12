package org.spoofax.jsglr2.incremental.parseforest;

import java.util.ArrayList;
import java.util.List;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.states.IState;
import org.metaborg.parsetable.productions.ProductionType;
import org.spoofax.jsglr2.incremental.IncrementalParse;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.stack.IStackNode;

public class IncrementalParseForestManager
    extends ParseForestManager<IncrementalParseForest, IncrementalParseNode, IncrementalDerivation> {

    @Override public IncrementalParseForest filterStartSymbol(IncrementalParseForest parseForest, String startSymbol,
        AbstractParse<IncrementalParseForest, ?> parse) {

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

    @Override public IncrementalParseNode createParseNode(AbstractParse<IncrementalParseForest, ?> parse,
        IStackNode stack, IProduction production, IncrementalDerivation firstDerivation) {

        IncrementalParseNode parseNode = new IncrementalParseNode(production, firstDerivation);

        parse.observing.notify(observer -> observer.createParseNode(parseNode, production));
        parse.observing.notify(observer -> observer.addDerivation(parseNode, firstDerivation));

        return parseNode;
    }

    public IncrementalParseNode createChangedParseNode(AbstractParse<IncrementalParseForest, ?> parse,
        IncrementalParseForest... children) {
        IncrementalParseNode parseNode = new IncrementalParseNode(children);

        parse.observing.notify(observer -> observer.createDerivation(parseNode.getFirstDerivation(), null, children));
        parse.observing.notify(observer -> observer.createParseNode(parseNode, null));
        parse.observing.notify(observer -> observer.addDerivation(parseNode, parseNode.getFirstDerivation()));

        return parseNode;
    }

    @Override public IncrementalDerivation createDerivation(AbstractParse<IncrementalParseForest, ?> parse,
        IStackNode stack, IProduction production, ProductionType productionType,
        IncrementalParseForest[] parseForests) {

        // TODO There should be a type parameter Parse to avoid the cast, but that creates extra dependencies which make
        // the big switch in JSGLR2Variants.getParser more verbose. Now, the ParseForestManager is created in a variable
        // that can be passed to the Parser, but adding Parse as type parameter would require in-lining that variable.
        IState state = ((IncrementalParse) parse).multipleStates ? IncrementalParse.NO_STATE : stack.state();
        IncrementalDerivation derivation = new IncrementalDerivation(production, productionType, parseForests, state);

        parse.observing.notify(observer -> observer.createDerivation(derivation, production, derivation.parseForests));

        return derivation;
    }

    @Override public void addDerivation(AbstractParse<IncrementalParseForest, ?> parse, IncrementalParseNode parseNode,
        IncrementalDerivation derivation) {

        parse.observing.notify(observer -> observer.addDerivation(parseNode, derivation));

        parseNode.addDerivation(derivation);
    }

    @Override public IncrementalParseForest createCharacterNode(AbstractParse<IncrementalParseForest, ?> parse) {
        return createCharacterNode(parse, parse.currentChar);
    }

    public IncrementalParseForest createCharacterNode(AbstractParse<IncrementalParseForest, ?> parse, int currentChar) {
        IncrementalCharacterNode characterNode = new IncrementalCharacterNode(currentChar);

        parse.observing.notify(observer -> observer.createCharacterNode(characterNode, characterNode.character));

        return characterNode;
    }

    @Override public IncrementalParseForest[] parseForestsArray(int length) {
        return new IncrementalParseForest[length];
    }
}
