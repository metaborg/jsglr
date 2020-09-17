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

    private final Collection<Message> messages;

    private ParseFailureCause.Type failure = null;

    NonAssocDetector(Collection<Message> messages) {
        this.messages = messages;
    }

    @Override public boolean preVisit(ParseNode parseNode, Position startPosition) {
        if(hasNonAssoc(parseNode)) {
            // Because we return false here, the children of this parseNode are not visited,
            // and the postVisit method will be called on the same parseNode directly after this method returns.
            // Therefore, we can temporarily store the failure type in a local field
            // to avoid having to call the hasNonAssoc method twice. Ugly, but works! /shrug
            failure = ParseFailureCause.Type.NonAssoc;
            return false;
        } else if(hasNonNested(parseNode)) {
            failure = ParseFailureCause.Type.NonNested;
            return false;
        } else {
            return parseNode.production().isContextFree();
        }
    }


    @Override public void postVisit(ParseNode parseNode, Position startPosition, Position endPosition) {
        if(failure != null) {
            messages.add(Message.error(failure.message, startPosition, endPosition));
            failure = null;
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
