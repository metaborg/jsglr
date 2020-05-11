package org.spoofax.jsglr2.recovery;

import org.spoofax.jsglr2.messages.Message;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parseforest.ParseForestVisitor;
import org.spoofax.jsglr2.parser.Position;

import java.util.ArrayList;
import java.util.Collection;

public class RecoveryMessagesParseForestVisitor
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>>
//@formatter:on
    implements ParseForestVisitor<ParseForest, Derivation, ParseNode> {

    public final Collection<Message> messages = new ArrayList<>();

    @Override public void visitParseNode(ParseNode parseNode, Position startPosition, Position endPosition) {
        if(parseNode.production().isRecovery())
            messages.add(RecoveryMessages.get(parseNode.production(), startPosition, endPosition));
    }

}
