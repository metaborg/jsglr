package org.spoofax.jsglr2.parser;

import java.util.Collection;

import org.spoofax.jsglr2.messages.Message;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parseforest.ParseNodeVisitor;
import org.spoofax.jsglr2.parser.result.ParseFailureCause;

public class NonAssocDetector
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>>
//@formatter:on
    implements ParseNodeVisitor<ParseForest, Derivation, ParseNode> {

    Collection<Message> messages;

    NonAssocDetector(Collection<Message> messages) {
        this.messages = messages;
    }

    @Override public boolean preVisit(ParseNode parseNode, Position startPosition) {
        if(hasNonAssoc(parseNode)) {
            return false;
        } else if(hasNonNested(parseNode)) {
            return false;
        } else {
            return parseNode.production().isContextFree();
        }
    }


    @Override public void postVisit(ParseNode parseNode, Position startPosition, Position endPosition) {
        if(hasNonAssoc(parseNode)) {
            messages.add(Message.error(ParseFailureCause.Type.NonAssoc.message, startPosition, endPosition));
        } else if(hasNonNested(parseNode)) {
            messages.add(Message.error(ParseFailureCause.Type.NonNested.message, startPosition, endPosition));
        }
    }

    private boolean hasNonAssoc(ParseNode parseNode) {
        for(Derivation derivation : parseNode.getDerivations()) {
            ParseForest[] children = derivation.parseForests();
            if(children.length == 0)
                continue;
            ParseForest firstChild = children[0];
            if(firstChild instanceof IParseNode
                && derivation.production().isNonAssocWith(((IParseNode<?, ?>) firstChild).production()))
                return true;
        }
        return false;
    }

    private boolean hasNonNested(ParseNode parseNode) {
        for(Derivation derivation : parseNode.getDerivations()) {
            ParseForest[] children = derivation.parseForests();
            if(children.length == 0)
                continue;
            ParseForest lastChild = children[children.length - 1];
            if(lastChild instanceof IParseNode
                && derivation.production().isNonNestedWith(((IParseNode<?, ?>) lastChild).production()))
                return true;
        }
        return false;
    }

}
