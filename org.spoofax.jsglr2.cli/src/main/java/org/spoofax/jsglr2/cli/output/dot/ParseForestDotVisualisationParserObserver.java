package org.spoofax.jsglr2.cli.output.dot;

import org.metaborg.parsetable.productions.IProduction;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.stack.IStackNode;

public class ParseForestDotVisualisationParserObserver
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<StackNode>>
//@formatter:on
    extends DotVisualisationParserObserver<ParseForest, Derivation, ParseNode, StackNode, ParseState> {

    @Override public void parseStart(ParseState parseState) {
        super.parseStart(parseState);
    }

    @Override public void createParseNode(ParseNode parseNode, IProduction production) {
        super.createParseNode(parseNode, production);

        dotStatement(
            idNode(parseNodeId((ParseForest) parseNode), id((ParseForest) parseNode), parseNode.descriptor()) + ";");
    }

    @Override public void createDerivation(Derivation derivation, IProduction production, ParseForest[] parseNodes) {
        super.createDerivation(derivation, production, parseNodes);
        dotStatement(idNode(derivationId(derivation), id(derivation), derivation.descriptor(), "white") + ";");
    }

    @Override public void createCharacterNode(ParseForest characterNode, int character) {
        super.createCharacterNode(characterNode, character);

        dotStatement(idNode(parseNodeId(characterNode), id(characterNode), characterNode.descriptor(), "white"));
    }

    @Override public void addDerivation(ParseNode parseNode, Derivation derivation) {
        dotStatement(derivationId(derivation) + ":p:n -> " + parseNodeId((ParseForest) parseNode)
            + ":p:s [arrowhead = \"none\"];");

        for(ParseForest parseForest : derivation.parseForests())
            dotStatement(parseNodeId(parseForest) + ":p:n -> " + derivationId(derivation) + ":p:s;");
    }

    private String parseNodeId(ParseForest parseNode) {
        return parseNodeId(id(parseNode));
    }

    private String parseNodeId(int id) {
        return "parseNode_" + id;
    }

    private String derivationId(IDerivation<ParseForest> derivation) {
        return derivationId(id(derivation));
    }

    private String derivationId(int id) {
        return "derivation_" + id;
    }

    public String output() {
        return "digraph {\nrankdir = BT;\n" + dotStatements + "}";
    }

}
