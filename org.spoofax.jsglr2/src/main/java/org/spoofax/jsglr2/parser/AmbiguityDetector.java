package org.spoofax.jsglr2.parser;

import java.util.Collection;

import org.spoofax.jsglr2.messages.Message;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parseforest.ParseNodeVisitor;

public class AmbiguityDetector
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>>
//@formatter:on
    implements ParseNodeVisitor<ParseForest, Derivation, ParseNode> {

    private final Collection<Message> messages;

    AmbiguityDetector(Collection<Message> messages) {
        this.messages = messages;
    }

    @Override public void postVisit(ParseNode parseNode, Position startPosition, Position endPosition) {
        if(parseNode.isAmbiguous()) {
            String message;

            switch(parseNode.production().concreteSyntaxContext()) {
                case Lexical:
                case Literal:
                    message = "Lexical ambiguity";
                    break;
                case Layout:
                    message = "Layout ambiguity";
                    break;
                case ContextFree:
                default:
                    message = "Ambiguity";
                    break;
            }

            messages.add(Message.warning(message, startPosition, endPosition));
        }
    }

}
