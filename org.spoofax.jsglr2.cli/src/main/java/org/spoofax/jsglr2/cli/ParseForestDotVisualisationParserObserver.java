package org.spoofax.jsglr2.cli;

import java.util.function.Consumer;

import org.metaborg.parsetable.productions.IProduction;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.IParseState;
import org.spoofax.jsglr2.stack.IStackNode;

class ParseForestDotVisualisationParserObserver
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends IStackNode,
    ParseState  extends IParseState<ParseForest, StackNode>>
//@formatter:on
    extends DotVisualisationParserObserver<ParseForest, StackNode, ParseState> {

    public ParseForestDotVisualisationParserObserver(Consumer<String> outputConsumer) {
        super(outputConsumer);
    }

    @Override public void parseStart(AbstractParse<ParseForest, StackNode, ParseState> parse) {
        super.parseStart(parse);
    }

    @Override public void createParseNode(ParseForest parseNode, IProduction production) {
        super.createParseNode(parseNode, production);

        dotStatement(idNode(parseNodeId(parseNode), id(parseNode), parseNode.descriptor()) + ";");
    }

    @Override public void createDerivation(IDerivation<ParseForest> derivation, IProduction production,
        ParseForest[] parseNodes) {
        super.createDerivation(derivation, production, parseNodes);
        dotStatement(idNode(derivationId(derivation), id(derivation), derivation.descriptor(), "white") + ";");
    }

    @Override public void createCharacterNode(ParseForest characterNode, int character) {
        super.createCharacterNode(characterNode, character);

        dotStatement(idNode(parseNodeId(characterNode), id(characterNode), characterNode.descriptor(), "white"));
    }

    @Override public void addDerivation(ParseForest parseNode, IDerivation<ParseForest> derivation) {
        dotStatement(derivationId(derivation) + ":p:n -> " + parseNodeId(parseNode) + ":p:s [arrowhead = \"none\"];");

        for(ParseForest parseForest : derivation.parseForests())
            dotStatement(parseNodeId(parseForest) + ":p:n -> " + derivationId(derivation) + ":p:s;");
    }

    String parseNodeId(ParseForest parseNode) {
        return parseNodeId(id(parseNode));
    }

    String parseNodeId(int id) {
        return "parseNode_" + id;
    }

    String derivationId(IDerivation<ParseForest> derivation) {
        return derivationId(id(derivation));
    }

    String derivationId(int id) {
        return "derivation_" + id;
    }

    void output() {
        String prefix = "digraph {\nrankdir = BT;\n";

        outputConsumer.accept(prefix + dotStatements + "}");
    }

}
