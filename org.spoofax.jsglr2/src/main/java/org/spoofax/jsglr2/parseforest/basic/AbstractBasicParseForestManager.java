package org.spoofax.jsglr2.parseforest.basic;

import java.util.List;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.productions.ProductionType;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;

public abstract class AbstractBasicParseForestManager
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<?, StackNode>>
//@formatter:on
    extends ParseForestManager<ParseForest, Derivation, ParseNode, StackNode, ParseState> {

    public AbstractBasicParseForestManager(
        ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing) {
        super(observing);
    }

    protected abstract ParseNode constructParseNode(IProduction production);

    protected abstract Derivation constructDerivation(IProduction production, ProductionType productionType,
        ParseForest[] parseForests);

    protected abstract ParseForest constructCharacterNode(ParseState parseState);

    @Override public ParseNode createParseNode(ParseState parseState, IStackNode stack, IProduction production,
        Derivation firstDerivation) {
        ParseNode parseNode = constructParseNode(production);

        observing.notify(observer -> observer.createParseNode(parseNode, production));

        addDerivation(parseState, parseNode, firstDerivation);

        return parseNode;
    }

    @Override public Derivation createDerivation(ParseState parseState, IStackNode stack, IProduction production,
        ProductionType productionType, ParseForest[] parseForests) {
        Derivation derivation = constructDerivation(production, productionType, parseForests);

        observing.notify(observer -> observer.createDerivation(derivation, production, parseForests));

        return derivation;
    }

    @Override public void addDerivation(ParseState parseState, ParseNode parseNode, Derivation derivation) {
        observing.notify(observer -> observer.addDerivation(parseNode, derivation));

        parseNode.addDerivation(derivation);
    }

    @Override public ParseForest createCharacterNode(ParseState parseState) {
        ParseForest termNode = constructCharacterNode(parseState);

        observing.notify(observer -> observer.createCharacterNode(termNode, parseState.inputStack.getChar()));

        return termNode;
    }

    @Override protected ParseNode filteredTopParseNode(ParseNode parseNode, List<Derivation> derivations) {
        ParseNode topParseNode = constructParseNode(parseNode.production());

        for(Derivation derivation : derivations)
            topParseNode.addDerivation(derivation);

        return topParseNode;
    }

}
