package org.spoofax.jsglr2.cli.output.dot;

import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserver;
import org.spoofax.jsglr2.parser.result.ParseFailure;
import org.spoofax.jsglr2.parser.result.ParseSuccess;
import org.spoofax.jsglr2.stack.IStackNode;

abstract class DotVisualisationParserObserver
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<?, StackNode>>
//@formatter:on
    extends ParserObserver<ParseForest, Derivation, ParseNode, StackNode, ParseState> {

    StringBuilder dotStatements;

    @Override public void parseStart(ParseState parseState) {
        super.parseStart(parseState);
        dotStatements = new StringBuilder();
    }

    String idNode(String name, int id, String label, String color) {
        String html = "<TABLE CELLSPACING=\"0\" CELLPADDING=\"0\" BORDER=\"0\" CELLBORDER=\"0\">"
            + "<TR><TD CELLPADDING=\"2\"><FONT POINT-SIZE=\"10\">" + id + "</FONT></TD><TD COLSPAN=\"1\"></TD></TR>"
            + "<TR ROWSPAN=\"1\"><TD></TD><TD COLSPAN=\"1\" BORDER=\"1\" CELLPADDING=\"5\" PORT=\"p\" BGCOLOR=\""
            + color + "\">" + escapeHtml(label) + "</TD></TR>" + "</TABLE>";

        return name + " [shape=plain,label=<" + html + ">]";
    }

    String idNode(String name, int id, String label) {
        return idNode(name, id, label, "gray");
    }

    void dotStatement(String string) {
        dotStatements.append(string).append('\n');
    }

    @Override public void success(ParseSuccess<ParseForest> success) {
        output();
    }

    @Override public void failure(ParseFailure<ParseForest> failure) {
        output();
    }

    public abstract String output();

    String escape(String string) {
        return string.replace("[", "\\[").replace("]", "\\]").replace("\"", "\\\"");
    }

    private String escapeHtml(String string) {
        return string.replace("<", "&lt;").replace(">", "&gt;");
    }

}
